package org.pucar.dristi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchResult_Success() {
        // Prepare test data
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("result", "success");

        // Mock behavior
        when(restTemplate.postForObject(eq(uri.toString()), any(), eq(Map.class))).thenReturn(expectedResult);

        // Invoke method under test
        Object response = serviceRequestRepository.fetchResult(uri, request);

        // Verify behavior
        assertEquals(expectedResult, response);
        // Remove the verification of mapper.configure
    }


    @Test
    public void testFetchResult_HttpClientErrorException() {
        // Prepare test data
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");

        // Creating HttpClientErrorException with a valid status code (e.g., 404)
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);

        // Mock behavior
        when(restTemplate.postForObject(eq(uri.toString()), any(), eq(Map.class))).thenThrow(exception);

        // Mocking ObjectMapper to prevent unwanted method invocations
        ObjectMapper mapperMock = mock(ObjectMapper.class);

        // Verify behavior
        assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult(uri, request));
        verify(mapperMock, never()).configure(any(SerializationFeature.class), anyBoolean());
    }

}
