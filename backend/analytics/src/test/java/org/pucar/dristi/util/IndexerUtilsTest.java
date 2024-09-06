package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.MdmsDataConfig;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskType;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.*;

public class IndexerUtilsTest {

    @InjectMocks
    private IndexerUtils indexerUtils;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private HearingUtil hearingUtil;

    @Mock
    private EvidenceUtil evidenceUtil;

    @Mock
    private TaskUtil taskUtil;

    @Mock
    private ApplicationUtil applicationUtil;

    @Mock
    private OrderUtil orderUtil;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MdmsDataConfig mdmsDataConfig;

    @Mock
    private CaseOverallStatusUtil caseOverallStatusUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(indexerUtils, "mdmsDataConfig", mdmsDataConfig);

    }

    private static PendingTask getPendingTask() {
        PendingTask pendingTask = new PendingTask();
        pendingTask.setId("id");
        pendingTask.setName("name");
        pendingTask.setEntityType("entityType");
        pendingTask.setReferenceId("referenceId");
        pendingTask.setStatus("status");
        pendingTask.setStateSla(123L);
        pendingTask.setBusinessServiceSla(456L);
        pendingTask.setAssignedTo(List.of(new User()));
        pendingTask.setAssignedRole(List.of("role"));
        pendingTask.setIsCompleted(true);
        pendingTask.setCnrNumber("cnrNumber");
        pendingTask.setFilingNumber("filingNumber");
        pendingTask.setAdditionalDetails(Map.of("key", "value"));
        return pendingTask;
    }

    @Test
    public void testIsNullOrEmpty() {
        assertTrue(IndexerUtils.isNullOrEmpty(null));
        assertTrue(IndexerUtils.isNullOrEmpty(""));
        assertTrue(IndexerUtils.isNullOrEmpty(" "));
        assertFalse(IndexerUtils.isNullOrEmpty("test"));
    }

    @Test
    public void testGetESEncodedCredentials() {
        when(config.getEsUsername()).thenReturn("user");
        when(config.getEsPassword()).thenReturn("pass");

        String credentials = "user:pass";
        byte[] credentialsBytes = credentials.getBytes();
        byte[] base64CredentialsBytes = Base64.getEncoder().encode(credentialsBytes);
        String expected = "Basic " + new String(base64CredentialsBytes);

        String result = indexerUtils.getESEncodedCredentials();
        assertEquals(expected, result);
    }

    @Test
    public void testBuildString_NullException() {
        // Act
        assertThrows(NullPointerException.class, () -> indexerUtils.buildString(null));
    }

    @Test
    public void testBuildString_ValidObject() {
        // Arrange
        Object obj = "key1:value1,key2:value2";

        // Act
        String result = indexerUtils.buildString(obj);

        // Assert
        assertEquals("key1:value1,key2:value2", result);
    }

    @Test
    public void testEsPost_Success() {
        // Arrange
        String uri = "http://localhost:9200/_bulk";
        String request = "{\"index\":{}}";

        // Act
        indexerUtils.esPost(uri, request);

        // Assert
        verify(restTemplate, times(1)).postForObject(eq(uri), any(HttpEntity.class), eq(String.class));
    }

    @Test
    public void testEsPostManual_Success() throws Exception {
        // Arrange
        String uri = "http://localhost:9200/_bulk";
        String request = "{\"index\":{\"test\":\"test\"}}";
        String response = "{\"index\":{\"test\":\"test\"},\"errors\":\"false\"}";

        when(restTemplate.postForObject(eq(uri), any(HttpEntity.class), eq(String.class))).thenReturn(response);

        // Act
        indexerUtils.esPostManual(uri, request);

        // Assert
        verify(restTemplate, times(1)).postForObject(eq(uri), any(HttpEntity.class), eq(String.class));
    }

    @Test()
    public void testEsPostManual_Failure() {
        // Arrange
        String uri = "http://localhost:9200/_bulk";
        String request = "{\"index\":{}}";
        when(restTemplate.postForObject(anyString(), any(), any())).thenThrow(new RuntimeException());

        // Act
        assertThrows(RuntimeException.class, () -> indexerUtils.esPostManual(uri, request));

    }


    @Test
    public void testBuildPayloadWithPendingTask() throws Exception {
        PendingTask pendingTask = getPendingTask();

        when(mapper.writeValueAsString(any())).thenReturn("{\"key\":\"value\"}");

        String expected = String.format(
                ES_INDEX_HEADER_FORMAT + ES_INDEX_DOCUMENT_FORMAT,
                "index", "referenceId", "id", "name", "entityType", "referenceId", "status", "[null]", "[\"role\"]", "cnrNumber", "filingNumber", true, 123L, 456L, "{\"key\":\"value\"}"
        );

        when(config.getIndex()).thenReturn("index");

        String result = indexerUtils.buildPayload(pendingTask);
        assertEquals(expected, result);
    }

    @Test
    public void testBuildPayloadWithJsonString() throws Exception {
        String jsonItem = "{"
                + "\"id\": \"id\","
                + "\"businessService\": \"entityType\","
                + "\"businessId\": \"referenceId\","
                + "\"state\": {\"state\":\"status\", \"actions\":[{\"roles\" : [\"role\"]}]},"
                + "\"stateSla\": 123,"
                + "\"businesssServiceSla\": 456,"
                + "\"assignes\": [\"user1\"],"
                + "\"assignedRoles\": [\"role\"],"
                + "\"tenantId\": \"tenantId\","
                + "\"action\": \"action\","
                + "}";
        JSONObject requestInfo = new JSONObject();

        when(config.getIndex()).thenReturn("index");
        when(caseOverallStatusUtil.checkCaseOverAllStatus(anyString(), anyString(), anyString(), anyString(), anyString(), any()))
                .thenReturn(new Object());
        when(mapper.writeValueAsString(any())).thenReturn("{\"key\":\"value\"}");

        String expected = String.format(
                ES_INDEX_HEADER_FORMAT + ES_INDEX_DOCUMENT_FORMAT,
                "index", "referenceId", "id", "name", "entityType", "referenceId", "status", "[\"user1\"]", "[\"role\"]", "null", "null", false, 123L, 456L, "{\"key\":\"value\"}"
        );

        PendingTaskType pendingTaskType = PendingTaskType.builder().pendingTask("name").state("status").triggerAction(List.of("action")).build();
        Map<String,List<PendingTaskType>> map = new HashMap<>();
        map.put("entityType",List.of(pendingTaskType));
        when(mdmsDataConfig.getPendingTaskTypeMap()).thenReturn(map);

        String result = indexerUtils.buildPayload(jsonItem, requestInfo);
        assertEquals(expected, result);
    }

    @Test
    public void testEsPost_ResourceAccessException() {
        // Arrange
        String uri = "http://localhost:9200/_bulk";
        String request = "{\"index\":{}}";
        when(restTemplate.postForObject(eq(uri), any(), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        // We need to mock the orchestrateListenerOnESHealth method to avoid actually running it
        IndexerUtils spyIndexerUtils = spy(indexerUtils);
        doNothing().when(spyIndexerUtils).orchestrateListenerOnESHealth();

        // Act
        spyIndexerUtils.esPost(uri, request);

        // Assert
        verify(restTemplate, times(1)).postForObject(eq(uri), any(), eq(String.class));
        verify(spyIndexerUtils, times(1)).orchestrateListenerOnESHealth();
    }

    @Test
    public void testEsPost_Success_1() {
        // Arrange
        String uri = "http://localhost:9200/_bulk";
        String request = "{\"index\":{}}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Authorization", "Basic bnVsbDpudWxs");
        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        when(restTemplate.postForObject(uri,entity,String.class)).thenReturn("{\"errors\":true}");

        // Act
        indexerUtils.esPost(uri, request);

        // Assert
        verify(restTemplate, times(1)).postForObject(eq(uri), any(HttpEntity.class), eq(String.class));
    }
}
