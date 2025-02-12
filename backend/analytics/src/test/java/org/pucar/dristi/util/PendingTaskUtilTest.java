package org.pucar.dristi.util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.PendingTask;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PendingTaskUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PendingTaskUtil pendingTaskUtil;

    @Mock
    private IndexerUtils indexerUtils;

    @BeforeEach
    void setUp() {
        // Set up mocks for dependencies
        when(config.getEsHostUrl()).thenReturn("http://localhost:9200");
        lenient().when(config.getPendingTaskIndexEndpoint()).thenReturn("/tasks");;
        lenient().when(config.getPendingTaskSearchPath()).thenReturn("/search");
        lenient().when(config.getEsUsername()).thenReturn("elastic");
        lenient().when(config.getEsPassword()).thenReturn("password");
    }

    @Test
    void testCallPendingTask_success() throws Exception {
        String esQuery = "{ \"query\": { \"bool\": { \"must\": [ { \"match\": { \"Data.filingNumber.keyword\": \"12345\" } }, { \"term\": { \"Data.isCompleted\": false } } ] } } } }";
        String filingNumber = "12345";
        String url = "http://localhost:9200/tasks/search";
        String responseJson = "{\"hits\": { \"total\": 1, \"hits\": [] }}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString("elastic:password".getBytes()));
        HttpEntity<String> entity = new HttpEntity<>(esQuery, headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);
        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(responseJson)).thenReturn(mockJsonNode);

        JsonNode result = pendingTaskUtil.callPendingTask(filingNumber);

        assertNotNull(result);
        verify(restTemplate).postForEntity(eq(url), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testCallPendingTask_failure() {
        String filingNumber = "12345";
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("RestTemplate exception"));

        CustomException exception = assertThrows(CustomException.class, () -> pendingTaskUtil.callPendingTask(filingNumber));
        assertEquals("RestTemplate exception", exception.getMessage());
    }

    @Test
    void testUpdatePendingTask_success() throws Exception {
        JsonNode task = mock(JsonNode.class);
        JsonNode sourceNode = mock(JsonNode.class);
        JsonNode dataNode = mock(JsonNode.class);
        when(task.get("_source")).thenReturn(sourceNode);
        when(sourceNode.get("Data")).thenReturn(dataNode);
        PendingTask pendingTask = new PendingTask();
        when(objectMapper.convertValue(dataNode, PendingTask.class)).thenReturn(pendingTask);

        when(indexerUtils.buildPayload(any(PendingTask.class))).thenReturn("payload");
        doNothing().when(indexerUtils).esPostManual(anyString(), anyString());

        pendingTaskUtil.updatePendingTask(List.of(task));

        verify(indexerUtils, times(1)).esPostManual(anyString(), anyString());
    }

    @Test
    void testUpdatePendingTask_failure() throws Exception {
        JsonNode task = mock(JsonNode.class);
        JsonNode sourceNode = mock(JsonNode.class);
        JsonNode dataNode = mock(JsonNode.class);
        when(task.get("_source")).thenReturn(sourceNode);
        when(sourceNode.get("Data")).thenReturn(dataNode);
        PendingTask pendingTask = new PendingTask();
        when(objectMapper.convertValue(dataNode, PendingTask.class)).thenReturn(pendingTask);

        when(indexerUtils.buildPayload(any(PendingTask.class))).thenReturn("payload");
        doThrow(new RuntimeException("IndexerUtils exception"))
                .when(indexerUtils).esPostManual(anyString(), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pendingTaskUtil.updatePendingTask(List.of(task)));
        assertEquals("IndexerUtils exception", exception.getMessage());
    }
}

