package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class BillingService {

    private final BillingUtil billingUtil;
    private final Util util;
    private final Configuration config;
    private final IndexerUtils indexerUtils;
    private final MdmsUtil mdmsUtil;

    @Autowired
    public BillingService(BillingUtil billingUtil, Util util, Configuration config, IndexerUtils indexerUtils, MdmsUtil mdmsUtil) {
        this.billingUtil = billingUtil;
        this.util = util;
        this.config = config;
        this.indexerUtils = indexerUtils;
        this.mdmsUtil = mdmsUtil;
    }

    public void process(String topic, String kafkaJson) {

        log.info("Inside billing service:: Topic: {}, kafkaJson: {}", topic, kafkaJson);
        String demandGenerateTopic = config.getDemandGenerateTopic();
        String paymentCollectTopic = config.getPaymentCollectTopic();


        if (topic.equals(demandGenerateTopic)) {
            processDemand(kafkaJson);
        } else if (topic.equals(paymentCollectTopic)) {
            processPayment(kafkaJson);
        } else {
            throw new CustomException(UNKNOWN_TOPIC_EXCEPTION, "Unexpected topic: " + topic);
        }

    }


    private void processPayment(String payment) {
        try {
            JSONArray paymentDetailsArray = util.constructArray(payment, PAYMENT_PAYMENT_DETAILS_PATH);
            LinkedHashMap<String, Object> requestInfoMap = JsonPath.read(payment, REQUEST_INFO_PATH);
            JSONObject requestInfo = new JSONObject();
            requestInfo.put(REQUEST_INFO, requestInfoMap);
            Set<String> demandSet = extractDemandIds(paymentDetailsArray);
            String tenantId = config.getStateLevelTenantId();
            updateDemandStatus(demandSet, tenantId, requestInfo);
        } catch (Exception e) {
            log.error("Error processing payment", e);
        }
    }

    private Set<String> extractDemandIds(JSONArray paymentDetailsArray) throws JSONException {
        Set<String> demandSet = new HashSet<>();
        for (int i = 0; i < paymentDetailsArray.length(); i++) {
            JSONObject paymentDetails = paymentDetailsArray.getJSONObject(i);
            JSONArray billDetailsArray = null;
            try {
                billDetailsArray = util.constructArray(paymentDetails.toString(), PAYMENT_PAYMENT_BILL_DETAILS_PATH);
            } catch (Exception e) {
                log.error("Error processing bill details array", e);
                throw new CustomException(EXTRACT_DEMAND_ID_ERROR, e.getMessage());
            }
            for (int j = 0; j < billDetailsArray.length(); j++) {
                JSONObject billDetails = billDetailsArray.getJSONObject(i);
                String demandId = billDetails.getString(DEMAND_ID);
                demandSet.add(demandId);
            }
        }
        return demandSet;
    }

    private void updateDemandStatus(Set<String> demandSet, String tenantId, JSONObject requestInfo) throws JSONException {
        for (String demandId : demandSet) {
            String demand = billingUtil.getDemand(tenantId, demandId, requestInfo);
            JSONArray demandArray = null;
            try {
                demandArray = util.constructArray(demand, DEMAND_PATH);
            } catch (Exception e) {

                log.error("Error processing bill details array", e);
                throw new CustomException(UPDATE_DEMAND_ERROR, e.getMessage());
            }
            for (int i = 0; i < demandArray.length(); i++) {
                JSONObject demandObject = demandArray.getJSONObject(i);
                demandObject.put(STATUS_KEY, STATUS_PAID);
            }
            JSONObject demandRequest = new JSONObject();
            demandRequest.put(REQUEST_INFO, requestInfo);
            demandRequest.put(DEMANDS, demandArray);
            processDemand(demandRequest.toString());
        }

    }


    private void processDemand(String demands) {
        try {
            JSONArray kafkaJsonArray = util.constructArray(demands, DEMAND_PATH);

            LinkedHashMap<String, Object> requestInfoMap = JsonPath.read(demands, REQUEST_INFO_PATH);
            JSONObject requestInfo = new JSONObject(requestInfoMap);

            StringBuilder bulkRequest = buildBulkRequest(kafkaJsonArray, requestInfo);

            if (!bulkRequest.isEmpty()) {
                String uri = config.getEsHostUrl() + config.getBulkPath();
                indexerUtils.esPost(uri, bulkRequest.toString());
            }
        } catch (Exception e) {

        }
    }

    StringBuilder buildBulkRequest(JSONArray kafkaJsonArray, JSONObject requestInfo) {
        StringBuilder bulkRequest = new StringBuilder();
        try {
            for (int i = 0; i < kafkaJsonArray.length(); i++) {
                JSONObject jsonObject = kafkaJsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    processJsonObject(jsonObject, bulkRequest, requestInfo);
                }
            }
        } catch (JSONException e) {
            log.error("Error processing JSON array", e);
        }

        return bulkRequest;
    }

    void processJsonObject(JSONObject jsonObject, StringBuilder bulkRequest, JSONObject requestInfo) {
        try {
            String stringifiedObject = billingUtil.buildString(jsonObject);
            String consumerCode = JsonPath.read(stringifiedObject, CONSUMER_CODE_PATH);
            String[] consumerCodeSplitArray = consumerCode.split("_", 2);

            if (isOfflinePaymentAvailable(consumerCodeSplitArray[1])) {
                String payload = billingUtil.buildPayload(stringifiedObject, requestInfo);
                if (payload != null && !payload.isEmpty())
                    bulkRequest.append(payload);
            } else {
                throw new CustomException(OFFLINE_PAYMENT_ERROR, OFFLINE_PAYMENT_ERROR_MESSAGE);
            }

        } catch (Exception e) {
            log.error("Error while processing JSON object: {}", jsonObject, e);
        }
    }

    private boolean isOfflinePaymentAvailable(String suffix) {

        RequestInfo requestInfo = RequestInfo.builder().build();
        net.minidev.json.JSONArray paymentMode = mdmsUtil.fetchMdmsData(requestInfo, config.getStateLevelTenantId(), PAYMENT_MODULE_NAME, List.of(PAYMENT_MODE_MASTER_NAME))
                .get(PAYMENT_MODULE_NAME).get(PAYMENT_MODE_MASTER_NAME);

        String filterString = String.format(FILTER_PAYMENT_MODE, suffix, OFFLINE);

        net.minidev.json.JSONArray payment = JsonPath.read(paymentMode, filterString);
        return !payment.isEmpty();

    }
}
