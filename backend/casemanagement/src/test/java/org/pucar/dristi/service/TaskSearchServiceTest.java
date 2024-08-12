package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskSearchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private TaskSearchService taskSearchService;

    @Mock
    private ResponseEntity<Object> responseEntity;

    @BeforeEach
    void setUp() {
        lenient().when(configuration.getTaskSearchHost()).thenReturn("http://localhost:8080");
        lenient().when(configuration.getTaskSearchPath()).thenReturn("/task/v1/search");
    }

    @Test
    void testGetTaskSearchResponseSuccess() {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = taskSearchService.getTaskSearchResponse(referenceId, tenantId, requestInfo);

        assertNotNull(response);
        assertEquals(responseEntity, response);

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void testGetTaskSearchResponseThrowsCustomException() {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new RuntimeException("RestTemplate Exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                taskSearchService.getTaskSearchResponse(referenceId, tenantId, requestInfo)
        );

        assertEquals("TASK_SEARCH_ERR", exception.getCode());
        assertTrue(exception.getMessage().contains("error while fetching the task details"));

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void testGetTaskSearchResponseNullResponse() {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(null); // Return null response

        ResponseEntity<Object> response = taskSearchService.getTaskSearchResponse(referenceId, tenantId, requestInfo);

        assertNull(response);

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }
}
