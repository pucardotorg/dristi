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
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

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

    @BeforeEach
    void setUp() {
        // Set up mocks for dependencies
        when(config.getEsHostUrl()).thenReturn("http://localhost:9200");
        when(config.getPendingTaskIndexEndpoint()).thenReturn("/tasks");
        lenient().when(config.getPendingTaskSearchPath()).thenReturn("/search");
        when(config.getEsUsername()).thenReturn("elastic");
        when(config.getEsPassword()).thenReturn("password");
    }

    @Test
    void testCallPendingTask_success() throws Exception {
        String filingNumber = "12345";
        String esQuery = "{ \"query\": { \"bool\": { \"must\": [ { \"match\": { \"Data.filingNumber\": \"12345\" } }, { \"term\": { \"Data.isCompleted\": false } } ] } } } }";
        String responseJson = "{\"hits\": { \"total\": 1, \"hits\": [] }}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(responseJson)).thenReturn(mockJsonNode);

        JsonNode result = pendingTaskUtil.callPendingTask(filingNumber);

        assertNotNull(result);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testCallPendingTask_failure() throws Exception {
        String filingNumber = "12345";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("RestTemplate exception"));

        CustomException exception = assertThrows(CustomException.class, () -> pendingTaskUtil.callPendingTask(filingNumber));
        assertEquals("RestTemplate exception", exception.getMessage());
    }

    @Test
    void testUpdatePendingTask_success() {
        JsonNode task1 = mock(JsonNode.class);
        JsonNode task2 = mock(JsonNode.class);
        when(task1.get("referenceId")).thenReturn(new TextNode("1"));
        when(task2.get("referenceId")).thenReturn(new TextNode("2"));

        HttpHeaders headers = new HttpHeaders();
        String url = "http://localhost:9200/tasks/bulk";
        HttpEntity<String> entity1 = new HttpEntity<>("{ \"update\": { \"_index\": \"pending-tasks-index\", \"_id\": \"1\" } }\n{ \"doc\": { ... }}", headers);

        pendingTaskUtil.updatePendingTask(Arrays.asList(task1, task2));
        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testUpdatePendingTask_failure() {
        JsonNode task = mock(JsonNode.class);
        when(task.get("referenceId")).thenReturn(new TextNode("1"));

        HttpHeaders headers = new HttpHeaders();
        String url = "http://localhost:9200/tasks/bulk";
        HttpEntity<String> entity = new HttpEntity<>("{ \"update\": { \"_index\": \"pending-tasks-index\", \"_id\": \"1\" } }\n{ \"doc\": { ... }}", headers);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("RestTemplate exception"));

        CustomException exception = assertThrows(CustomException.class, () -> pendingTaskUtil.updatePendingTask(Arrays.asList(task)));
        assertEquals("RestTemplate exception", exception.getMessage());
    }
}

