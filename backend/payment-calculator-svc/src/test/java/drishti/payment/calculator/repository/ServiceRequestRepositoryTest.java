package drishti.payment.calculator.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Test
    public void testFetchResult_Success() {
        StringBuilder uri = new StringBuilder("http://localhost:8080/test");
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(response);

        Object result = serviceRequestRepository.fetchResult(uri, request);

        assertEquals(response, result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }

}
