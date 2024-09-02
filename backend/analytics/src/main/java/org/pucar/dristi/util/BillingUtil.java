package org.pucar.dristi.util;


import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class BillingUtil {

    private final Configuration config;

    private final IndexerUtils indexerUtil;
    private final ServiceRequestRepository requestRepository;

    private final CaseUtil caseUtil;

    @Autowired
    public BillingUtil(Configuration config, IndexerUtils indexerUtil, ServiceRequestRepository requestRepository, CaseUtil caseUtil) {
        this.config = config;
        this.indexerUtil = indexerUtil;
        this.requestRepository = requestRepository;
        this.caseUtil = caseUtil;
    }


    public String buildPayload(String jsonItem, JSONObject requestInfo) {

        String id = JsonPath.read(jsonItem, ID_PATH);
        String businessService = JsonPath.read(jsonItem, BUSINESS_SERVICE_PATH);
        String consumerCode = JsonPath.read(jsonItem, CONSUMER_CODE_PATH);
        String status = JsonPath.read(jsonItem, STATUS_PATH);
        String tenantId = JsonPath.read(jsonItem, TENANT_ID_PATH);
//        String consumerType = JsonPath.read(jsonItem, CONSUMER_TYPE_PATH);

//        // Extract numeric fields
//        Long taxPeriodFrom = JsonPath.read(jsonItem, TAX_PERIOD_FROM_PATH);
//        Long taxPeriodTo = JsonPath.read(jsonItem, TAX_PERIOD_TO_PATH);
//        Double minimumAmountPayable = ((Integer) JsonPath.read(jsonItem, MINIMUM_AMOUNT_PAYABLE_PATH)).doubleValue();
//        Long billExpiryTime = JsonPath.read(jsonItem, BILL_EXPIRY_TIME_PATH);
//
//        // Extract complex objects
//        Object additionalDetails = JsonPath.read(jsonItem, ADDITIONAL_DETAILS_PATH);
//        Object fixedBillExpiryDate = JsonPath.read(jsonItem, FIXED_BILL_EXPIRY_DATE_PATH);
//        Object payer = JsonPath.read(jsonItem, PAYER_PATH);

        // Extract demandDetails array
        List<Map<String, Object>> demandDetails = JsonPath.read(jsonItem, DEMAND_DETAILS_PATH);

        // Extract audit details
        Map<String, Object> auditDetails = JsonPath.read(jsonItem, AUDIT_DETAILS_PATH);
        Gson gson = new Gson();
        String auditJsonString = gson.toJson(auditDetails);
        Double totalAmount = 0.0;
        for (Map<String, Object> demandDetail : demandDetails) {

            Double taxAmount = Double.parseDouble(demandDetail.get("taxAmount").toString());
            totalAmount += taxAmount;

        }

        log.info("Inside indexer utils build payload:: entityType: {}, referenceId: {}, status: {}, action: {}, tenantId: {}", businessService, consumerCode, status, "change-me", tenantId);
        JSONObject request = new JSONObject();
        request.put("RequestInfo", requestInfo);
        Map<String, String> details = indexerUtil.processEntityByType(businessService, request, consumerCode, null);

        String cnrNumber = details.get("cnrNumber");
        String filingNumber = details.get("filingNumber");

        Object caseObject = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, null);
        String caseTitle = JsonPath.read(caseObject.toString(), CASE_TITLE_PATH);
        String caseStage = JsonPath.read(caseObject.toString(), CASE_STAGE_PATH);

        return String.format(
                ES_INDEX_HEADER_FORMAT + ES_INDEX_BILLING_FORMAT,
                config.getBillingIndex(), id, id, tenantId, caseTitle, filingNumber, caseStage, "caseType", "paymentType", totalAmount, status, consumerCode, businessService, auditJsonString
        );
    }

    public String buildString(JSONObject jsonObject) {
        return indexerUtil.buildString(jsonObject);
    }


    public String getDemand(String tenantId, String demandId, JSONObject requestInfo) {

        String baseUrl = config.getDemandHost() + config.getDemandEndPoint();

        String url = String.format("%s?tenantId=%s&demandId=%s", baseUrl, tenantId, demandId);

        return requestRepository.fetchResult(new StringBuilder(url), requestInfo);
    }
}
