package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class BillingService {

    private final BillingUtil billingUtil;

    private final Util util;

    private final Configuration config;

    private final IndexerUtils indexerUtils;

    @Autowired
    public BillingService(BillingUtil billingUtil, Util util, Configuration config, IndexerUtils indexerUtils) {
        this.billingUtil = billingUtil;
        this.util = util;
        this.config = config;
        this.indexerUtils = indexerUtils;
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
            throw new CustomException("UNKNOWN_TOPIC_EXCEPTION", "Unexpected topic: " + topic);
        }

    }

    private void processPayment(String payment) {

        try {
            JSONArray paymentDetailsArray = util.constructArray(payment, PAYMENT_PAYMENT_DETAILS_PATH);

            LinkedHashMap<String, Object> requestInfoMap = JsonPath.read(payment, REQUEST_INFO_PATH);
            JSONObject requestInfo = new JSONObject();
            requestInfo.put("RequestInfo",requestInfoMap);
            Set<String> demandSet = new HashSet<>();

            String tenantId = config.getStateLevelTenantId(); // check how to set stateleveltenantid
            for (int i = 0; i < paymentDetailsArray.length(); i++) {

                JSONObject paymentDetails = paymentDetailsArray.getJSONObject(i);
                JSONArray billDetailsArray = util.constructArray(paymentDetails.toString(), PAYMENT_PAYMENT_BILL_DETAILS_PATH);

                for (int j = 0; j < billDetailsArray.length(); j++) {

                    JSONObject billDetails = billDetailsArray.getJSONObject(i);

                    String demandId = billDetails.getString("demandId");
                    demandSet.add(demandId);

                }

            }

            //  todo : two approach either call demand then make payload and create or  fetch es and update
            //  fetch demand based approach
            for (String demandId : demandSet) {
                String demand = billingUtil.getDemand(tenantId, demandId, requestInfo);
                JSONArray demandArray = util.constructArray(demand, DEMAND_PATH);

                for (int i = 0; i < demandArray.length(); i++) {
                    JSONObject demandObject = demandArray.getJSONObject(i);
                    demandObject.put("status", "PAID");
                }

                JSONObject demandRequest = new JSONObject(requestInfo);
                demandRequest.put("Demands", demandArray);
                demandRequest.put("RequestInfo",requestInfoMap);
                processDemand(demandRequest.toString());

            }

        } catch (Exception e) {

            log.error("ERROR");

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
            String payload = billingUtil.buildPayload(stringifiedObject, requestInfo);
            if (payload != null && !payload.isEmpty())
                bulkRequest.append(payload);
        } catch (Exception e) {
            log.error("Error while processing JSON object: {}", jsonObject, e);
        }
    }
}
