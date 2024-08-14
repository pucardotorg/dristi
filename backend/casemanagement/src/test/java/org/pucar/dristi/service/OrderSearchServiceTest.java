package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class OrderSearchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private OrderSearchService orderSearchService;

    @Mock
    private ResponseEntity<Object> responseEntity;

    @BeforeEach
    void setUp() {
        lenient().when(configuration.getOrderSearchHost()).thenReturn("http://localhost:8080");
        lenient().when(configuration.getOrderSearchPath()).thenReturn("/order/search");
    }

    @Test
    void testSearchOrderSuccess() throws Exception {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        String responseBody = "{\"list\":[{\"cnrNumber\":\"CNR123\"}]}";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new Object());
        when(objectMapper.writeValueAsString(any())).thenReturn(responseBody);

        String cnrNumber = orderSearchService.searchOrder(referenceId, tenantId, requestInfo);

        assertNotNull(cnrNumber);
        assertEquals("CNR123", cnrNumber);

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
        verify(objectMapper, times(1)).writeValueAsString(any());
    }

    @Test
    void testSearchOrderRestTemplateException() {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new RuntimeException("RestTemplate Exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderSearchService.searchOrder(referenceId, tenantId, requestInfo)
        );

        assertEquals("ORDER_SEARCH_ERR", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while fetching the order details"));

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void testSearchOrderJsonParsingException() throws Exception {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new Object());
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Parsing Exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderSearchService.searchOrder(referenceId, tenantId, requestInfo)
        );

        assertEquals("JSON_PARSING_ERROR", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while extracting cnr number from the order response"));

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
        verify(objectMapper, times(1)).writeValueAsString(any());
    }

    @Test
    void testSearchOrderResponseNull() {
        String referenceId = "123";
        String tenantId = "tenant1";
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken("auth-token");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(null);  // Return null body

        CustomException exception = assertThrows(CustomException.class, () ->
                orderSearchService.searchOrder(referenceId, tenantId, requestInfo)
        );

        assertEquals("ORDER_SEARCH_ERR", exception.getCode());
        assertTrue(exception.getMessage().contains("Response body is null"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }
}
