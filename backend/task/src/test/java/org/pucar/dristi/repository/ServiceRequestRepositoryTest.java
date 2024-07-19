package org.pucar.dristi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestRepositoryTest {

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    private StringBuilder uri;
    private Object request;

    @BeforeEach
    public void setUp() {
        uri = new StringBuilder("http://example.com");
        request = new Object();
    }

    @Test
    public void testFetchResult_Success() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(response);

        // Act
        Object result = serviceRequestRepository.fetchResult(uri, request);

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }

    @Test
    public void testFetchResult_HttpClientErrorException() {
        // Arrange
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getResponseBodyAsString()).thenReturn("error response");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenThrow(exception);

        // Act & Assert
        assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult(uri, request));

        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }

    @Test
    public void testFetchResult_GeneralException() {
        // Arrange
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("General exception"));

        // Act
        Object result = serviceRequestRepository.fetchResult(uri, request);

        // Assert
        assertNull(result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }
}
