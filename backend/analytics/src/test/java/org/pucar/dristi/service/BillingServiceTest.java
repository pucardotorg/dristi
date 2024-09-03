package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.REQUEST_INFO_PATH;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillingUtil billingUtil;

    @Mock
    private Util util;

    @Mock
    private Configuration config;

    @Mock
    private IndexerUtils indexerUtils;

    @InjectMocks
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        // Common setup for all tests
    }

    @Test
    void testProcessWithDemandGenerateTopic() throws Exception {
        when(config.getDemandGenerateTopic()).thenReturn("demand-generate");
        String kafkaJson = "{\"demands\": [{\"id\": \"1\"}]}";
        billingService.process("demand-generate", kafkaJson);

        verify(util).constructArray(eq(kafkaJson), anyString());
    }

    @Test
    void testProcessWithPaymentCollectTopic() throws Exception {
        when(config.getPaymentCollectTopic()).thenReturn("payment-collect");

        String kafkaJson = "{\"Payment\": {\"paymentDetails\": [{\"billDetails\": [{\"demandId\": \"1\"}]}]}}";
        billingService.process("payment-collect", kafkaJson);

        verify(util).constructArray(eq(kafkaJson), anyString());
    }

    @Test
    void testProcessWithUnknownTopic() {
        assertThrows(CustomException.class, () ->
                billingService.process("unknown-topic", "{}"));
    }

    @Test
    void testProcessDemand() throws Exception {
        when(util.constructArray(anyString(), anyString())).thenReturn(new JSONArray("[{\"id\": \"1\"}]"));
        when(config.getDemandGenerateTopic()).thenReturn("demand-generate");
        when(config.getPaymentCollectTopic()).thenReturn("payment-collect");
        when(billingUtil.buildString(any())).thenReturn("{\"id\": \"1\"}");
        when(billingUtil.buildPayload(anyString(), any())).thenReturn("payload");
        String demands = "{\"demands\": [{\"id\": \"1\"}], \"RequestInfo\": {}}";
        billingService.process("demand-generate", demands);
    }

    @Test
    void testBuildBulkRequest() throws Exception {
        JSONArray jsonArray = new JSONArray("[{\"id\": \"1\"}]");
        JSONObject requestInfo = new JSONObject("{\"userInfo\": {\"id\": 1}}");
        when(billingUtil.buildString(any())).thenReturn("{\"id\": \"1\"}");
        when(billingUtil.buildPayload(anyString(), any())).thenReturn("payload");

        StringBuilder result = billingService.buildBulkRequest(jsonArray, requestInfo);

        assertEquals("payload", result.toString());
    }

    @Test
    void testProcessJsonObject() throws Exception {
        JSONObject jsonObject = new JSONObject("{\"id\": \"1\"}");
        JSONObject requestInfo = new JSONObject("{\"userInfo\": {\"id\": 1}}");
        StringBuilder bulkRequest = new StringBuilder();
        when(billingUtil.buildString(any())).thenReturn("{\"id\": \"1\"}");
        when(billingUtil.buildPayload(anyString(), any())).thenReturn("payload");

        billingService.processJsonObject(jsonObject, bulkRequest, requestInfo);

        assertEquals("payload", bulkRequest.toString());
    }

    @Test
    void testProcessJsonObjectWithNullPayload() throws Exception {
        JSONObject jsonObject = new JSONObject("{\"id\": \"1\"}");
        JSONObject requestInfo = new JSONObject("{\"userInfo\": {\"id\": 1}}");
        StringBuilder bulkRequest = new StringBuilder();
        when(billingUtil.buildString(any())).thenReturn("{\"id\": \"1\"}");
        when(billingUtil.buildPayload(anyString(), any())).thenReturn(null);

        billingService.processJsonObject(jsonObject, bulkRequest, requestInfo);

        assertEquals("", bulkRequest.toString());
    }

    @Test
    void testProcessJsonObjectFailure() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        StringBuilder bulkRequest = new StringBuilder();
        JSONObject requestInfo = new JSONObject();

        when(billingUtil.buildString(any(JSONObject.class))).thenThrow(new RuntimeException("Test exception"));

        billingService.processJsonObject(jsonObject, bulkRequest, requestInfo);

        assertEquals("", bulkRequest.toString());

        verify(billingUtil, never()).buildPayload(anyString(), any(JSONObject.class));
    }

}