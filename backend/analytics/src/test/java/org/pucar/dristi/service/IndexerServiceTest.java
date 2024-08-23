package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.PROCESS_INSTANCE_PATH;

public class IndexerServiceTest {

    @InjectMocks
    private IndexerService indexerService;

    @Mock
    private IndexerUtils indexerUtils;

    @Mock
    private Configuration config;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Util util;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEsIndexer_Success() throws Exception {
        // Setup mocks
        String topic = "test-topic";
        String kafkaJson = "{\"ProcessInstances\": [{\"id\": \"9fd498a3-0306-4626-960b-34b33ab82320\"}], \"RequestInfo\": {\"userInfo\": {\"roles\": []}}}";
        JSONArray kafkaJsonArray = new JSONArray();
        kafkaJsonArray.put(new JSONObject().put("id", "9fd498a3-0306-4626-960b-34b33ab82320"));
        JSONObject requestInfo = new JSONObject();
        requestInfo.put("userInfo", new JSONObject().put("roles", new JSONArray()));

        when(util.constructArray(kafkaJson, PROCESS_INSTANCE_PATH)).thenReturn(kafkaJsonArray);
        when(indexerUtils.buildString(any())).thenReturn("stringifiedObject");
        when(indexerUtils.buildPayload(anyString(), any())).thenReturn("payload");
        when(config.getEsHostUrl()).thenReturn("http://localhost:9200");
        when(config.getBulkPath()).thenReturn("/_bulk");

        // Invoke the method
        indexerService.esIndexer(topic, kafkaJson);

        // Verify interactions
        verify(indexerUtils, times(1)).buildString(any());
        verify(indexerUtils, times(1)).buildPayload(anyString(), any());
        verify(indexerUtils, times(1)).esPost(anyString(), anyString());
    }

    @Test
    void testEsIndexer_ExceptionDuringProcessing() throws Exception {
        // Setup mocks
        String topic = "test-topic";
        String kafkaJson = "{\"ProcessInstances\": [{}], \"RequestInfo\": {\"userInfo\": {\"roles\": []}}}";

        when(util.constructArray(kafkaJson, PROCESS_INSTANCE_PATH)).thenThrow(new RuntimeException("Test Exception"));

        // Invoke the method and check for logging of errors
        indexerService.esIndexer(topic, kafkaJson);

        // No interactions with indexerUtils or restTemplate
        verify(indexerUtils, times(0)).buildString(any());
        verify(indexerUtils, times(0)).buildPayload(anyString(), any());
        verify(restTemplate, times(0)).postForObject(anyString(), any(), eq(String.class));
    }

    @Test
    void testBuildBulkRequest() throws JSONException {
        // Setup mocks
        JSONArray kafkaJsonArray = new JSONArray();
        kafkaJsonArray.put(new JSONObject().put("key", "value"));
        JSONObject requestInfo = new JSONObject();

        when(indexerUtils.buildString(any())).thenReturn("stringifiedObject");
        when(indexerUtils.buildPayload(anyString(), any())).thenReturn("payload");

        // Invoke the method
        StringBuilder result = indexerService.buildBulkRequest(kafkaJsonArray, requestInfo);

        // Verify the constructed bulk request
        assertEquals("payload", result.toString());
    }

    @Test
    void testBuildBulkRequest_EmptyArray() {
        // Setup mocks
        JSONArray kafkaJsonArray = new JSONArray();
        JSONObject requestInfo = new JSONObject();

        // Invoke the method
        StringBuilder result = indexerService.buildBulkRequest(kafkaJsonArray, requestInfo);

        // Verify the constructed bulk request is empty
        assertEquals("", result.toString());
    }

    @Test
    void testProcessJsonObject() throws JSONException {
        // Setup mocks
        JSONObject jsonObject = new JSONObject().put("key", "value");
        JSONObject requestInfo = new JSONObject();
        StringBuilder bulkRequest = new StringBuilder();

        when(indexerUtils.buildString(jsonObject)).thenReturn("stringifiedObject");
        when(indexerUtils.buildPayload(anyString(), any())).thenReturn("payload");

        // Invoke the method
        indexerService.processJsonObject(jsonObject, bulkRequest, requestInfo);

        // Verify the constructed bulk request
        assertEquals("payload", bulkRequest.toString());
    }

    @Test
    void testProcessJsonObject_BuildPayloadReturnsNull() throws JSONException {
        // Setup mocks
        JSONObject jsonObject = new JSONObject().put("key", "value");
        JSONObject requestInfo = new JSONObject();
        StringBuilder bulkRequest = new StringBuilder();

        when(indexerUtils.buildString(jsonObject)).thenReturn("stringifiedObject");
        when(indexerUtils.buildPayload(anyString(), any())).thenReturn(null);

        // Invoke the method
        indexerService.processJsonObject(jsonObject, bulkRequest, requestInfo);

        // Verify the constructed bulk request is empty
        assertEquals("", bulkRequest.toString());
    }
}
