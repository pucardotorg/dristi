package org.pucar.dristi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.TEST_EXCEPTION;

class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    private ServiceRequestRepository serviceRequestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        serviceRequestRepository = new ServiceRequestRepository(objectMapper, restTemplate);
    }

    @Test
    void fetchResult_SuccessfulServiceCall_ReturnsResponse() {
        StringBuilder uri = new StringBuilder("http://example.com");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("result", "success");

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(expectedResponse);

        Object response = serviceRequestRepository.fetchResult(uri, request);

        assertEquals(expectedResponse, response);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }
    @Test()
     void testFetchResult_GenericException() {
        StringBuilder uri = new StringBuilder("https://google.com");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");
        when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenThrow(new CustomException(TEST_EXCEPTION, "Mocked Error"));
        try{
            serviceRequestRepository.fetchResult(uri, request);
        }
        catch (Exception e){
            assertTrue(e instanceof CustomException);
            assertEquals("Mocked Error", e.getMessage());
        }
    }
    @Test()
     void testFetchResult_HttpClientErrorException() {
        StringBuilder uri = new StringBuilder("http://mock-service/api/endpoint");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value"); // Replace with your request object class
        when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Mocked Error"));
        try{
            serviceRequestRepository.fetchResult(uri, request);
        }
        catch (Exception e){
            assertTrue(e instanceof ServiceCallException);
        }
    }
}
