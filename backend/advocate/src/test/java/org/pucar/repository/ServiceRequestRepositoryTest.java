package org.pucar.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.repository.ServiceRequestRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

}
