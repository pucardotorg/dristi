package org.pucar.dristi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);
    }

    @Test
    void fetchResult_success() {
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("key", "value");

        when(restTemplate.postForObject(eq(uri.toString()), eq(request), eq(Map.class))).thenReturn(expectedResponse);

        Object response = serviceRequestRepository.fetchResult(uri, request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void fetchResult_httpClientErrorException() {
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();
        HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        when(restTemplate.postForObject(eq(uri.toString()), eq(request), eq(Map.class))).thenThrow(httpClientErrorException);
        when(httpClientErrorException.getResponseBodyAsString()).thenReturn("Error response body");

        ServiceCallException thrown = assertThrows(ServiceCallException.class, () -> {
            serviceRequestRepository.fetchResult(uri, request);
        });
        assertNull(thrown.getMessage());
    }

    @Test
    void fetchResult_generalException() {
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();

        when(restTemplate.postForObject(eq(uri.toString()), eq(request), eq(Map.class))).thenThrow(new RuntimeException("General exception"));

        Object response = serviceRequestRepository.fetchResult(uri, request);

        assertNull(response);
    }
}
