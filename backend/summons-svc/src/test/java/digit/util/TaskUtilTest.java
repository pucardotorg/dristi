package digit.util;

import digit.config.Configuration;
import digit.web.models.TaskListResponse;
import digit.web.models.TaskRequest;
import digit.web.models.TaskResponse;
import digit.web.models.TaskSearchRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TaskUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @Mock
    private TaskSearchRequest searchRequest;

    @InjectMocks
    private TaskUtil taskUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void callUpdateTask_returnsExpectedResult_whenInputIsValid() {
        TaskRequest taskRequest = new TaskRequest();
        TaskResponse expectedResponse = new TaskResponse();
        String uri = "http://localhost/update-endpoint";

        when(config.getTaskServiceHost()).thenReturn("http://localhost");
        when(config.getTaskServiceUpdateEndpoint()).thenReturn("/update-endpoint");
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(TaskResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        TaskResponse result = taskUtil.callUpdateTask(taskRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void callUpdateTask_throwsCustomException_whenRestTemplateThrowsException() {
        TaskRequest taskRequest = new TaskRequest();
        String uri = "http://localhost/update-endpoint";

        when(config.getTaskServiceHost()).thenReturn("http://localhost");
        when(config.getTaskServiceUpdateEndpoint()).thenReturn("/update-endpoint");
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(TaskResponse.class)))
                .thenThrow(new RuntimeException("Service error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskUtil.callUpdateTask(taskRequest));

        assertEquals("TASK_UPDATE_ERROR", exception.getCode());
        assertEquals("Error getting response from task Service", exception.getMessage());
    }

    @Test
    void callSearchTask_returnsExpectedResult_whenInputIsValid() {
        TaskListResponse expectedResponse = new TaskListResponse();
        String uri = "http://localhost/search-endpoint";

        when(config.getTaskServiceHost()).thenReturn("http://localhost");
        when(config.getTaskServiceSearchEndpoint()).thenReturn("/search-endpoint");
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(TaskListResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        TaskListResponse result = taskUtil.callSearchTask(searchRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void callSearchTask_throwsCustomException_whenRestTemplateThrowsException() {
        String uri = "http://localhost/search-endpoint";

        when(config.getTaskServiceHost()).thenReturn("http://localhost");
        when(config.getTaskServiceUpdateEndpoint()).thenReturn("/search-endpoint");
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(TaskListResponse.class)))
                .thenThrow(new RuntimeException("Service error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskUtil.callSearchTask(searchRequest));

        assertEquals("TASK_SEARCH_ERROR", exception.getCode());
        assertEquals("Error getting response from task Service", exception.getMessage());
    }
}