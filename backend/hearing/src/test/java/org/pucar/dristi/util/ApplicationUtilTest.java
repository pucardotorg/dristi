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
    void fetchApplicationDetails_Success() {
        // Arrange
        ApplicationExistsRequest request = new ApplicationExistsRequest();
        ApplicationExistsResponse expectedResponse = new ApplicationExistsResponse();
        expectedResponse.setApplicationExists(Collections.singletonList(new ApplicationExists()));

        String host = "http://example.com";
        String path = "/applicationExists";
        String fullUri = host + path;

        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("exists", true);

        when(configs.getApplicationHost()).thenReturn(host);
        when(configs.getApplicationExistsPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenReturn(responseObject);
        when(mapper.convertValue(responseObject, ApplicationExistsResponse.class)).thenReturn(expectedResponse);

        // Act
        ApplicationExistsResponse actualResponse = applicationUtil.fetchApplicationDetails(request);

        // Assert
        assertNotNull(actualResponse);
        assertFalse(actualResponse.getApplicationExists().isEmpty());
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
