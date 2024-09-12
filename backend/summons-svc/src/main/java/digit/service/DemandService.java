package digit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.util.MdmsUtil;
import digit.util.TaskUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jayway.jsonpath.JsonPath;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static digit.config.ServiceConstants.*;

@Service
@Slf4j
public class DemandService {

    private final Configuration config;

    private final ObjectMapper mapper;

    private final ServiceRequestRepository repository;

    private final MdmsUtil mdmsUtil;

    private final TaskUtil taskUtil;

    @Autowired
    public DemandService(Configuration config, ObjectMapper mapper, ServiceRequestRepository repository, MdmsUtil mdmsUtil, TaskUtil taskUtil) {
        this.config = config;
        this.mapper = mapper;
        this.repository = repository;
        this.mdmsUtil = mdmsUtil;
        this.taskUtil = taskUtil;
    }
    public final Map<String, String> masterCodePayemntTyprMap= new HashMap<String,String>(){
        {
            put("TASK_SUMMON_ADVANCE_CARRYFORWARD_COURT_FEES", "Summons Post Court Fee");
            put("TASK_SUMMON_ADVANCE_CARRYFORWARD_I_POST", "Summons Post Process Fee");
            put("TASK_SUMMON_ADVANCE_CARRYFORWARD_GST", "Summons Post Process Fee");
        }
    };

    public BillResponse fetchPaymentDetailsAndGenerateDemandAndBill(TaskRequest taskRequest) {
        Task task = taskRequest.getTask();
        String channelName = taskRequest.getTask().getTaskDetails().getDeliveryChannel().getChannelName();
        if(channelName.equalsIgnoreCase("POST")) {
            List<Calculation> calculationList = generatePaymentDetails(taskRequest.getRequestInfo(), task);
            Set<String> consumerCodeList = generateDemands(taskRequest.getRequestInfo(), calculationList, task);
            return getBillWithMultipleConsumerCode(taskRequest.getRequestInfo(), consumerCodeList, task);
        }
        else {
            updateTaskStatus(taskRequest);
            return null;
        }
    }

    public List<Calculation> generatePaymentDetails(RequestInfo requestInfo, Task task) {
        SummonCalculationCriteria criteria = SummonCalculationCriteria.builder()
                .channelId(ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).toString())
                .receiverPincode(task.getTaskDetails().getRespondentDetails().getAddress().getPinCode())
                .tenantId(task.getTenantId()).summonId(task.getTaskNumber()).build();

        StringBuilder url = new StringBuilder().append(config.getPaymentCalculatorHost())
                .append(config.getPaymentCalculatorCalculateEndpoint());

        log.info("Requesting Payment Calculator : {}", criteria.toString());

        SummonCalculationRequest calculationRequest = SummonCalculationRequest.builder()
                .requestInfo(requestInfo).calculationCriteria(Collections.singletonList(criteria)).build();

        Object response = repository.fetchResult(url, calculationRequest);

        CalculationResponse calculationResponse = mapper.convertValue(response, CalculationResponse.class);
        return calculationResponse.getCalculation();
    }

    public Set<String> generateDemands(RequestInfo requestInfo, List<Calculation> calculations, Task task) {        List<Demand> demands = new ArrayList<>();
        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo,
                config.getEgovStateTenantId(), config.getPaymentBusinessServiceName(), createMasterDetails()
        );
        for (Calculation calculation : calculations) {
            List<DemandDetail> demandDetailList = createDemandDetails(calculation, task, mdmsData);
            demands.addAll(createDemandList(task, demandDetailList, calculation.getTenantId(), mdmsData));
        }
        return callBillServiceAndCreateDemand(requestInfo, demands, task);
    }

    private List<DemandDetail> createDemandDetails(Calculation calculation, Task task, Map<String, Map<String, JSONArray>> mdmsData) {
        List<DemandDetail> demandDetailList = new ArrayList<>();

        if (config.isTest()) {
            demandDetailList.addAll(createTestDemandDetails(calculation.getTenantId(), task));
        } else {
            Map<String, String> masterCodes = getTaxHeadMasterCodes(mdmsData, config.getTaskBusinessService());
            for (BreakDown breakDown : calculation.getBreakDown()) {
                demandDetailList.add(createDemandDetail(calculation.getTenantId(), breakDown, masterCodes));
            }
        }
        return demandDetailList;
    }

    private List<DemandDetail> createTestDemandDetails(String tenantId, Task task) {
        List<DemandDetail> demandDetailList = new ArrayList<>();
        String channelName = ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).name();

        if (channelName.equals("POST")) {
            DemandDetail courtDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(config.getTaskTaxHeadCourtMasterCode())
                    .build();

            DemandDetail ePostDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(config.getTaskTaxHeadEPostMasterCode())
                    .build();

            demandDetailList.add(courtDetail);
            demandDetailList.add(ePostDetail);
        } else {
            DemandDetail basicDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(config.getTaskTaxHeadMasterCode())
                    .build();

            demandDetailList.add(basicDetail);
        }

        return demandDetailList;
    }

    private DemandDetail createDemandDetail(String tenantId, BreakDown breakDown, Map<String, String> masterCodes) {
        return DemandDetail.builder()
                .tenantId(tenantId)
                .taxAmount(BigDecimal.valueOf(breakDown.getAmount()))
                .taxHeadMasterCode(masterCodes.getOrDefault(breakDown.getType(), ""))
                .build();
    }

    private Set<String> callBillServiceAndCreateDemand(RequestInfo requestInfo, List<Demand> demands, Task task) {
        StringBuilder url = new StringBuilder().append(config.getBillingServiceHost())
                .append(config.getDemandCreateEndpoint());
        DemandRequest demandRequest = DemandRequest.builder().requestInfo(requestInfo).demands(demands).build();
        repository.fetchResult(url, demandRequest);
        Set<String> consumerCode = new HashSet<>();
        for(Demand demand : demands){
            consumerCode.add(demand.getConsumerCode());
        }
        return consumerCode;
    }

    private List<Demand> createDemandList(Task task, List<DemandDetail> demandDetailList, String tenantId, Map<String, Map<String, JSONArray>> mdmsData) {
        List<Demand> demandList = new ArrayList<>();
        String channelName = ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).name();
        String taskNumber = task.getTaskNumber();
        Map<String, String> paymentTypeData = getPaymentType(mdmsData,channelName);
        for (DemandDetail detail : demandDetailList) {
            String taxHeadMasterCode = detail.getTaxHeadMasterCode();
            String paymentType = masterCodePayemntTyprMap.get(taxHeadMasterCode);
            String paymentTypeSuffix = paymentTypeData.get(paymentType);
            String consumerCode = taskNumber + "_" + paymentTypeSuffix;
            demandList.add(createDemandObject(Collections.singletonList(detail), tenantId, consumerCode));
        }

        return demandList;
    }

    private Demand createDemandObject(List<DemandDetail> demandDetailList, String tenantId, String consumerCode) {
        Demand demand = Demand.builder()
                .tenantId(tenantId)
                .consumerCode(consumerCode)
                .consumerType(config.getTaxConsumerType())
                .businessService(config.getTaskBusinessService())
                .taxPeriodFrom(config.getTaxPeriodFrom()).taxPeriodTo(config.getTaxPeriodTo())
                .demandDetails(demandDetailList)
                .build();
        return demand;
    }

    private Map<String, String> getTaxHeadMasterCodes(Map<String, Map<String, JSONArray>> mdmsData, String taskBusinessService) {
        if (mdmsData != null && mdmsData.containsKey("payment") && mdmsData.get(config.getPaymentBusinessServiceName()).containsKey(PAYMENTMASTERCODE)) {
            JSONArray masterCode = mdmsData.get(config.getPaymentBusinessServiceName()).get(PAYMENTMASTERCODE);
            Map<String, String> result = new HashMap<>();
            for (Object masterCodeObj : masterCode) {
                Map<String, String> subType = (Map<String, String>) masterCodeObj;
                if (taskBusinessService.equals(subType.get("businessService"))) {
                    result.put(subType.get("type"), subType.get("masterCode"));
                }
            }
            return result;
        }
        return Collections.emptyMap();
    }

    private Map<String, String> getPaymentType(Map<String, Map<String, JSONArray>> mdmsData, String channelName) {
        if (mdmsData != null && mdmsData.containsKey("payment") && mdmsData.get(config.getPaymentBusinessServiceName()).containsKey(PAYMENTTYPE)) {
            JSONArray masterCode = mdmsData.get(config.getPaymentBusinessServiceName()).get(PAYMENTTYPE);

            String filterStringDeliveryChannel = String.format(FILTER_PAYMENT_TYPE_DELIVERY_CHANNEL, channelName, config.getTaskBusinessService());

            JSONArray paymentTypeData = JsonPath.read(masterCode, filterStringDeliveryChannel);
            Map<String, String> result = new HashMap<>();
            for (Object data : paymentTypeData) {
                    JsonNode jsonNode = mapper.convertValue(data, JsonNode.class);
                    String suffix = jsonNode.get("suffix").asText();
                    String paymentType = jsonNode.get("paymentType").asText();
                    result.put(paymentType, suffix);

            }
            return result;
        }
        return null;
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add(PAYMENTMASTERCODE);
        masterList.add(PAYMENTTYPE);
        return masterList;
    }

    public BillResponse getBill(RequestInfo requestInfo, Task task) {
        String uri = buildFetchBillURI(task.getTenantId(), Collections.singleton(task.getTaskNumber()), config.getTaskBusinessService());

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        Object response = repository.fetchResult(new StringBuilder(uri), requestInfoWrapper);

        return mapper.convertValue(response, BillResponse.class);
    }

    public BillResponse getBillWithMultipleConsumerCode(RequestInfo requestInfo, Set<String> consumerCodes, Task task) {
        String uri = buildFetchBillURI(task.getTenantId(), consumerCodes, config.getTaskBusinessService());

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        Object response = repository.fetchResult(new StringBuilder(uri), requestInfoWrapper);

        return mapper.convertValue(response, BillResponse.class);
    }

    private String buildFetchBillURI(String tenantId, Set<String> applicationNumbers, String businessService) {
        try {
            String encodedTenantId = URLEncoder.encode(tenantId, StandardCharsets.UTF_8);
            String encodedBusinessService = URLEncoder.encode(businessService, StandardCharsets.UTF_8);
            String applicationNumbersParam = applicationNumbers.stream()
                    .map(num -> URLEncoder.encode(num, StandardCharsets.UTF_8))
                    .collect(Collectors.joining(","));

            return URI.create(String.format("%s%s?tenantId=%s&consumerCode=%s&businessService=%s",
                    config.getBillingServiceHost(),
                    config.getFetchBillEndpoint(),
                    encodedTenantId,
                    applicationNumbersParam,
                    encodedBusinessService)).toString();
        } catch (Exception e) {
            log.error("Error occurred when creating bill URI with search params", e);
            throw new CustomException("GENERATE_BILL_ERROR", "Error occurred when generating bill");
        }
    }

    public void updateTaskStatus(TaskRequest request) {

        Task task = request.getTask();
        Workflow workflow = null;
        if (request.getTask().getStatus().equalsIgnoreCase("PAYMENT_PENDING")) {
            workflow = Workflow.builder().action("MAKE PAYMENT").build();
        }

        task.setWorkflow(workflow);
        TaskRequest taskRequest = TaskRequest.builder()
                .requestInfo(request.getRequestInfo()).task(task).build();
        taskUtil.callUpdateTask(taskRequest);
    }
}