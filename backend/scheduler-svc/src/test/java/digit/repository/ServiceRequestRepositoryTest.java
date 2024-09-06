package digit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.repository.ServiceRequestRepository;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    private StringBuilder uri;
    private Object requestObject;

    @BeforeEach
    public void setUp() {
        uri = new StringBuilder("http://example.com/api");
        requestObject = new Object();
    }

    @Test
    public void testPostMethod_Success() {
        String expectedResponse = "response";
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(eq(uri.toString()), eq(HttpMethod.POST), eq(requestEntity), eq(String.class))).thenReturn(responseEntity);

        Object response = serviceRequestRepository.postMethod(uri, requestObject);

        assertEquals(expectedResponse, response);
        verify(mapper, times(1)).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(restTemplate, times(1)).exchange(eq(uri.toString()), eq(HttpMethod.POST), eq(requestEntity), eq(String.class));
    }

    @Test
    public void testPostMethod_Exception() {
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);
        when(restTemplate.exchange(eq(uri.toString()), eq(HttpMethod.POST), eq(requestEntity), eq(String.class))).thenThrow(new RuntimeException("Exception"));

        Object response = serviceRequestRepository.postMethod(uri, requestObject);

        assertNull(response);
        verify(mapper, times(1)).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(restTemplate, times(1)).exchange(eq(uri.toString()), eq(HttpMethod.POST), eq(requestEntity), eq(String.class));
    }

    @Test
    public void testFetchResult_Success() {
        Map<String, Object> expectedResponse = new HashMap<>();
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);
        when(restTemplate.postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class))).thenReturn(expectedResponse);

        Object response = serviceRequestRepository.fetchResult(uri, requestObject);

        assertEquals(expectedResponse, response);
        verify(mapper, times(1)).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(restTemplate, times(1)).postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class));
    }

    @Test
    public void testFetchResult_HttpClientErrorException() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getResponseBodyAsString()).thenReturn("Error message");
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);
        when(restTemplate.postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class))).thenThrow(exception);

        ServiceCallException thrown = assertThrows(ServiceCallException.class, () -> {
            serviceRequestRepository.fetchResult(uri, requestObject);
        });

        verify(mapper, times(1)).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(restTemplate, times(1)).postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class));
    }

    @Test
    public void testFetchResult_Exception() {
        when(mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(mapper);
        when(restTemplate.postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class))).thenThrow(new RuntimeException("Exception"));

        Object response = serviceRequestRepository.fetchResult(uri, requestObject);

        assertNull(response);
        verify(mapper, times(1)).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(restTemplate, times(1)).postForObject(eq(uri.toString()), eq(requestObject), eq(Map.class));
    }
}