package digit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestRepositoryTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;


    @Test
    void fetchResult_returnsResponseSuccessfully() {
        String uri = "http://localhost:8080/test";
        Object request = new Object();
        Map<String, Object> expectedResponse = new HashMap<>();

        when(restTemplate.postForObject(uri, request, Map.class)).thenReturn(expectedResponse);

        Object actualResponse = serviceRequestRepository.fetchResult(new StringBuilder(uri), request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void fetchResult_throwsServiceCallException_whenHttpClientErrorExceptionOccurs() {
        String uri = "http://localhost:8080/test";
        Object request = new Object();

        when(restTemplate.postForObject(uri, request, Map.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult(new StringBuilder(uri), request));
    }

    @Test
    void fetchResult_returnsNull_whenExceptionOccurs() {
        String uri = "http://localhost:8080/test";
        Object request = new Object();

        when(restTemplate.postForObject(uri, request, Map.class)).thenThrow(new RuntimeException());

        Object actualResponse = serviceRequestRepository.fetchResult(new StringBuilder(uri), request);

        assertNull(actualResponse);
    }
}