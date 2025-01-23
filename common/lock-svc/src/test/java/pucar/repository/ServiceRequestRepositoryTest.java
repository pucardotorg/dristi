package pucar.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.ServiceCallException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Test
    void testFetchResult_Success() {
        StringBuilder uri = new StringBuilder("http://test-service/api/resource");
        Object request = new Object();
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("key", "value");

        when(restTemplate.postForObject(uri.toString(), request, Map.class)).thenReturn(mockResponse);

        Object result = serviceRequestRepository.fetchResult(uri, request);

        assertNotNull(result);
        assertInstanceOf(Map.class, result);
        assertEquals("value", ((Map<?, ?>) result).get("key"));
        verify(restTemplate, times(1)).postForObject(uri.toString(), request, Map.class);
    }

    @Test
    void testFetchResult_HttpClientErrorException() {
        StringBuilder uri = new StringBuilder("http://test-service/api/resource");
        Object request = new Object();
        String errorResponse = null;

        HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);
        when(httpClientErrorException.getResponseBodyAsString()).thenReturn(errorResponse);
        doThrow(httpClientErrorException).when(restTemplate).postForObject(uri.toString(), request, Map.class);

        ServiceCallException exception = assertThrows(ServiceCallException.class, () ->
                serviceRequestRepository.fetchResult(uri, request));

        assertEquals(errorResponse, exception.getMessage());
        verify(restTemplate, times(1)).postForObject(uri.toString(), request, Map.class);
    }

    @Test
    void testFetchResult_OtherException() {
        StringBuilder uri = new StringBuilder("http://test-service/api/resource");
        Object request = new Object();

        doThrow(new RuntimeException("Unexpected error"))
                .when(restTemplate).postForObject(uri.toString(), request, Map.class);

        Object result = serviceRequestRepository.fetchResult(uri, request);

        assertNull(result);
        verify(restTemplate, times(1)).postForObject(uri.toString(), request, Map.class);
    }
}
