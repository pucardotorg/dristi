package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DemandService {

    private final Configuration config;

    private final ObjectMapper mapper;

    private final ServiceRequestRepository repository;

    @Autowired
    public DemandService(Configuration config, ObjectMapper mapper, ServiceRequestRepository repository) {
        this.config = config;
        this.mapper = mapper;
        this.repository = repository;
    }

    public BillResponse fetchPaymentDetailsAndGenerateDemandAndBill(TaskRequest taskRequest) {
        Task task = taskRequest.getTask();
        List<Calculation> calculationList = generatePaymentDetails(taskRequest.getRequestInfo(), task);
        generateDemands(taskRequest.getRequestInfo(), calculationList, task);
        return getBill(taskRequest.getRequestInfo(), task);
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

    public List<Demand> generateDemands(RequestInfo requestInfo, List<Calculation> calculations, Task task) {
        List<Demand> demands = new ArrayList<>();

        for (Calculation calculation : calculations) {
            DemandDetail demandDetail = DemandDetail.builder()
                    .tenantId(calculation.getTenantId())
                    //.taxAmount(BigDecimal.valueOf(calculation.getTotalAmount()))
                    .taxAmount(BigDecimal.valueOf(4))
                    .taxHeadMasterCode(config.getTaskTaxHeadMasterCode()).build();

            //TODO- should create separate demand details based on break down
            Demand demand = Demand.builder()
                    .tenantId(calculation.getTenantId())
                    .consumerCode(task.getTaskNumber())
                    .consumerType(config.getTaxConsumerType())
                    .businessService(config.getTaskModuleCode())
                    .taxPeriodFrom(config.getTaxPeriodFrom()).taxPeriodTo(config.getTaxPeriodTo())
                    .demandDetails(Collections.singletonList(demandDetail))
                    .build();

            demands.add(demand);
        }
        StringBuilder url = new StringBuilder().append(config.getBillingServiceHost())
                .append(config.getDemandCreateEndpoint());

        DemandRequest demandRequest = DemandRequest.builder().requestInfo(requestInfo).demands(demands).build();

        Object response = repository.fetchResult(url, demandRequest);

        DemandResponse demandResponse = mapper.convertValue(response, DemandResponse.class);
        return demandResponse.getDemands();
    }

    public BillResponse getBill(RequestInfo requestInfo, Task task) {
        String uri = buildFetchBillURI(task.getTenantId(), task.getTaskNumber(), config.getTaskBusinessService());

        Object response = repository.fetchResult(new StringBuilder(uri), requestInfo);

        return mapper.convertValue(response, BillResponse.class);
    }

    private String buildFetchBillURI(String tenantId, String applicationNumber, String businessService) {
        try {
            String encodedTenantId = URLEncoder.encode(tenantId, StandardCharsets.UTF_8);
            String encodedApplicationNumber = URLEncoder.encode(applicationNumber, StandardCharsets.UTF_8);
            String encodedBusinessService = URLEncoder.encode(businessService, StandardCharsets.UTF_8);

            return URI.create(String.format("%s%s?tenantId=%s&consumerCode=%s&businessService=%s",
                    config.getBillingServiceHost(),
                    config.getFetchBillEndpoint(),
                    encodedTenantId,
                    encodedApplicationNumber,
                    encodedBusinessService)).toString();
        } catch (Exception e) {
            log.error("Error occurred when creating bill uri with search params", e);
            throw new CustomException("GENERATE_BILL_ERROR", "Error Occurred when  generating bill");
        }
    }
}