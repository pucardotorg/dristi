package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.ApplicationExists;
import org.springframework.web.client.RestTemplate;

import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.ApplicationExistsRequest;
import org.pucar.dristi.web.models.ApplicationExistsResponse;
import org.egov.tracer.model.CustomException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private ApplicationUtil applicationUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchApplicationDetailsSuccess() {
        ApplicationExistsRequest request = new ApplicationExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", true)));

        ApplicationExistsResponse applicationExistsResponse = ApplicationExistsResponse.builder()
                .applicationExists(List.of(ApplicationExists.builder().exists(true).build()))
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, ApplicationExistsResponse.class))
                .thenReturn(applicationExistsResponse);

        Boolean result = applicationUtil.fetchApplicationDetails(request);
        assertTrue(result);
    }



    @Test
    void fetchApplicationDetails_ExceptionThrown() {
        // Arrange
        // Arrange
        ApplicationExistsRequest request = new ApplicationExistsRequest();
        String errorMessage = "Error while fetching application details";

        String host = "http://example.com";
        String path = "/applicationExists";
        String fullUri = host + path;

        when(configs.getApplicationHost()).thenReturn(host);
        when(configs.getApplicationExistsPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> applicationUtil.fetchApplicationDetails(request));
        assertEquals(errorMessage, exception.getMessage());
    }
}
