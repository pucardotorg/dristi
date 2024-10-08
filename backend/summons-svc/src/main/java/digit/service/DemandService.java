package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.util.MdmsUtil;
import digit.util.TaskUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.models.RequestInfoWrapper;
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
     Map<String, String> masterCodePayemntTypeMap= new HashMap<String,String>();

    public BillResponse fetchPaymentDetailsAndGenerateDemandAndBill(TaskRequest taskRequest) {
        Task task = taskRequest.getTask();
        String businessService = getBusinessService(task.getTaskType());
        List<Calculation> calculationList = generatePaymentDetails(taskRequest.getRequestInfo(), task);
        if(calculationList == null || calculationList.isEmpty()){
            throw new CustomException(PAYMENT_CALCULATOR_ERROR, "Getting empty or null data from payment-calculator");
        }
        Set<String> consumerCodeList = generateDemands(taskRequest.getRequestInfo(), calculationList, task, businessService);
        return getBillWithMultipleConsumerCode(taskRequest.getRequestInfo(), consumerCodeList, task, businessService);

    }

    public List<Calculation> generatePaymentDetails(RequestInfo requestInfo, Task task) {
        TaskPaymentCriteria criteria = TaskPaymentCriteria.builder()
                .channelId(ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).toString())
                .receiverPincode(task.getTaskDetails().getRespondentDetails().getAddress().getPinCode())
                .tenantId(task.getTenantId())
                .taskType(task.getTaskType())
                .id(task.getTaskNumber()).build();

        StringBuilder url = new StringBuilder().append(config.getPaymentCalculatorHost())
                .append(config.getPaymentCalculatorCalculateEndpoint());

        log.info("Requesting Payment Calculator : {}", criteria.toString());

        TaskPaymentRequest calculationRequest = TaskPaymentRequest.builder()
                .requestInfo(requestInfo).calculationCriteria(Collections.singletonList(criteria)).build();

        Object response = repository.fetchResult(url, calculationRequest);

        CalculationResponse calculationResponse = mapper.convertValue(response, CalculationResponse.class);
        return calculationResponse.getCalculation();
    }

    public Set<String> generateDemands(RequestInfo requestInfo, List<Calculation> calculations, Task task, String businessService) {
        List<Demand> demands = new ArrayList<>();
        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo,
                config.getEgovStateTenantId(), config.getPaymentBusinessServiceName(), createMasterDetails()
        );
        for (Calculation calculation : calculations) {
            List<DemandDetail> demandDetailList = createDemandDetails(calculation, task, mdmsData, businessService);
            demands.addAll(createDemandList(task, demandDetailList, calculation.getTenantId(), mdmsData, businessService));
        }
        return callBillServiceAndCreateDemand(requestInfo, demands, task);
    }

    private List<DemandDetail> createDemandDetails(Calculation calculation, Task task, Map<String,
            Map<String, JSONArray>> mdmsData, String businessService) {
        List<DemandDetail> demandDetailList = new ArrayList<>();
        String deliveryChannel = ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).name();
        Map<String, String> masterCodes = getTaxHeadMasterCodes(mdmsData, businessService, deliveryChannel);

        if (config.isTest()) {
            demandDetailList.addAll(createTestDemandDetails(calculation.getTenantId(), task, businessService));
        } else {
            for (BreakDown breakDown : calculation.getBreakDown()) {
                demandDetailList.add(createDemandDetail(calculation.getTenantId(), breakDown, masterCodes));
            }
        }
        return demandDetailList;
    }

    private List<DemandDetail> createTestDemandDetails(String tenantId, Task task, String businessService) {
        List<DemandDetail> demandDetailList = new ArrayList<>();
        String channelName = ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).name();

        if (channelName.equals("EPOST")) {
            DemandDetail courtDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestCourtTaxHeadMasterCode(businessService))
                    .build();

            DemandDetail ePostDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestPostTaxHeadMasterCode(businessService))
                    .build();

            demandDetailList.add(courtDetail);
            demandDetailList.add(ePostDetail);
        }
        else if(channelName.equals("POLICE")){
            DemandDetail basicDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestPoliceTaxHeadMasterCode(businessService))
                    .build();

            demandDetailList.add(basicDetail);
        }
        else if(channelName.equals("EMAIL")){
            DemandDetail basicDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestTaxEmailHeadMasterCode(businessService))
                    .build();

            demandDetailList.add(basicDetail);
        }
        else if(channelName.equals("SMS")){
            DemandDetail basicDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestTaxSmsHeadMasterCode(businessService))
                    .build();

            demandDetailList.add(basicDetail);
        }
        else if(channelName.equals("RPAD")){
            DemandDetail basicDetail = DemandDetail.builder()
                    .tenantId(tenantId)
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(getTestRpadTaxHeadMasterCode(businessService))
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

    private List<Demand> createDemandList(Task task, List<DemandDetail> demandDetailList, String tenantId, Map<String,
            Map<String, JSONArray>> mdmsData, String businessService) {
        List<Demand> demandList = new ArrayList<>();
        String channelName = ChannelName.fromString(task.getTaskDetails().getDeliveryChannel().getChannelName()).name();
        String taskNumber = task.getTaskNumber();
        Map<String, String> paymentTypeData = getPaymentType(mdmsData, channelName, businessService);
        for (DemandDetail detail : demandDetailList) {
            String taxHeadMasterCode = detail.getTaxHeadMasterCode();
            String paymentType = masterCodePayemntTypeMap.get(taxHeadMasterCode);
            String paymentTypeSuffix = paymentTypeData.get(paymentType);
            String consumerCode = taskNumber + "_" + paymentTypeSuffix;
            demandList.add(createDemandObject(Collections.singletonList(detail), tenantId, consumerCode, businessService));
        }

        return demandList;
    }

    private Demand createDemandObject(List<DemandDetail> demandDetailList, String tenantId, String consumerCode, String businessService) {
        Demand demand = Demand.builder()
                .tenantId(tenantId)
                .consumerCode(consumerCode)
                .consumerType(config.getTaxConsumerType())
                .businessService(businessService)
                .taxPeriodFrom(config.getTaxPeriodFrom()).taxPeriodTo(config.getTaxPeriodTo())
                .demandDetails(demandDetailList)
                .build();
        return demand;
    }

    private Map<String, String> getTaxHeadMasterCodes(Map<String, Map<String, JSONArray>> mdmsData, String taskBusinessService, String deliveryChannel) {
        if (mdmsData != null && mdmsData.containsKey("payment") && mdmsData.get(config.getPaymentBusinessServiceName()).containsKey(PAYMENTMASTERCODE)) {
            JSONArray masterCode = mdmsData.get(config.getPaymentBusinessServiceName()).get(PAYMENTMASTERCODE);
            Map<String, String> result = new HashMap<>();
            for (Object masterCodeObj : masterCode) {
                Map<String, String> subType = (Map<String, String>) masterCodeObj;
                if (taskBusinessService.equals(subType.get("businessService")) && deliveryChannel.equalsIgnoreCase(subType.get("deliveryChannel"))) {
                    result.put(subType.get("type"), subType.get("masterCode"));
                    masterCodePayemntTypeMap.put(subType.get("masterCode"), subType.get("paymentType"));
                }
            }
            return result;
        }
        return Collections.emptyMap();
    }

    private Map<String, String> getPaymentType(Map<String, Map<String, JSONArray>> mdmsData, String channelName, String businessService) {
        if (mdmsData != null && mdmsData.containsKey("payment") && mdmsData.get(config.getPaymentBusinessServiceName()).containsKey(PAYMENTTYPE)) {
            JSONArray masterCode = mdmsData.get(config.getPaymentBusinessServiceName()).get(PAYMENTTYPE);

            String filterStringDeliveryChannel = String.format(
                    FILTER_PAYMENT_TYPE_DELIVERY_CHANNEL, channelName
            );

            JSONArray paymentTypeData = JsonPath.read(masterCode, filterStringDeliveryChannel);
            Map<String, String> result = new HashMap<>();
            for (Object masterCodeObj : paymentTypeData) {
                Map<String, Object> subType = (Map<String, Object>) masterCodeObj;

                if (isMatchingBusinessService(subType, businessService, channelName)) {
                    String suffix = (String) subType.get("suffix");
                    String paymentType = (String) subType.get("paymentType");
                    result.put(paymentType, suffix);
                }
            }
            return result;
        }
        return null;
    }


    private boolean isMatchingBusinessService(Map<String, Object> subType, String taskBusinessService, String deliveryChannel) {
        List<Map<String, Object>> businessServices = (List<Map<String, Object>>) subType.get("businessService");

        for (Map<String, Object> service : businessServices) {
            if (taskBusinessService.equals(service.get("businessCode"))
                    && deliveryChannel.equalsIgnoreCase((String) subType.get("deliveryChannel"))) {
                return true; // Found a match
            }
        }
        return false; // No match found
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add(PAYMENTMASTERCODE);
        masterList.add(PAYMENTTYPE);
        return masterList;
    }

    public BillResponse getBill(RequestInfo requestInfo, Task task) {
        String businessService = getBusinessService(task.getTaskType());
        String uri = buildFetchBillURI(task.getTenantId(), Collections.singleton(task.getTaskNumber()), businessService);

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        Object response = repository.fetchResult(new StringBuilder(uri), requestInfoWrapper);

        return mapper.convertValue(response, BillResponse.class);
    }

    public BillResponse getBillWithMultipleConsumerCode(RequestInfo requestInfo, Set<String> consumerCodes, Task task, String businessService) {
        String uri = buildFetchBillURI(task.getTenantId(), consumerCodes, businessService);

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

    private String getBusinessService(String taskType) {
        return switch (taskType.toUpperCase()) {
            case SUMMON -> config.getTaskSummonBusinessService();
            case WARRANT -> config.getTaskWarrantBusinessService();
            case NOTICE -> config.getTaskNoticeBusinessService();
            default -> throw new IllegalArgumentException("Unsupported task type: " + taskType);
        };
    }

    private String getTestCourtTaxHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskNoticeBusinessService())) {
            return config.getTaskNoticeTaxHeadCourtMasterCode();
        } else {
            return config.getTaskSummonTaxHeadCourtMasterCode();
        }
    }
    private String getTestRpadTaxHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskNoticeBusinessService())) {
            return config.getTaskNoticeTaxHeadRpadCourtMasterCode();
        } else {
            return config.getTaskSummonTaxHeadRpadCourtMasterCode();
        }
    }

    private String getTestPostTaxHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskNoticeBusinessService())) {
            return config.getTaskNoticeTaxHeadEPostMasterCode();
        } else {
            return config.getTaskSummonTaxHeadEPostMasterCode();
        }
    }

    private String getTestPoliceTaxHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskWarrantBusinessService())) {
            return config.getTaskWarrantPoliceTaxHeadMasterCode();
        } else {
            return config.getTaskSummonPoliceTaxHeadMasterCode();
        }
    }

    private String getTestTaxEmailHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskNoticeBusinessService())) {
            return config.getTaskNoticeEmailTaxHeadMasterCode();
        } else {
            return config.getTaskSummonEmailTaxHeadMasterCode();
        }
    }
    private String getTestTaxSmsHeadMasterCode(String businessService) {
        if (businessService.equalsIgnoreCase(config.getTaskNoticeBusinessService())) {
            return config.getTaskNoticeSmsTaxHeadMasterCode();
        } else {
            return config.getTaskSummonSmsTaxHeadMasterCode();
        }
    }

}