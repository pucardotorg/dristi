package org.pucar.dristi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.ServiceCallException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRequestRepositoryTest {

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    private StringBuilder uri;
    private JSONObject request;

    @BeforeEach
    void setUp() throws JSONException {
        uri = new StringBuilder("http://example.com/api");
        request = new JSONObject();
        request.put("key", "value");
    }

    @Test
    void testFetchResultSuccess() {
        String expectedResponse = "{\"response\":\"success\"}";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String response = serviceRequestRepository.fetchResult(uri, request);

        assertEquals(expectedResponse, response);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testFetchResultHttpClientErrorException() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getResponseBodyAsString()).thenReturn("error body");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(exception);

        ServiceCallException thrown = assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult(uri, request));

        assertEquals("error body", thrown.getError());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testFetchResultOtherException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Runtime exception"));

        String response = serviceRequestRepository.fetchResult(uri, request);

        assertNull(response);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }
}
