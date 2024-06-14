package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.CaseExists;
import org.springframework.web.client.RestTemplate;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.egov.tracer.model.CustomException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaseUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private CaseUtil caseUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchCaseDetails_Success() {
        // Arrange
        CaseExistsRequest request = new CaseExistsRequest();
        CaseExistsResponse expectedResponse = new CaseExistsResponse();
        expectedResponse.setCriteria(Collections.singletonList(new CaseExists()));

        String host = "http://example.com";
        String path = "/caseExists";
        String fullUri = host + path;

        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("exists", true);

        when(configs.getCaseHost()).thenReturn(host);
        when(configs.getCaseExistsPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenReturn(responseObject);
        when(mapper.convertValue(responseObject, CaseExistsResponse.class)).thenReturn(expectedResponse);

        // Act
        CaseExistsResponse actualResponse = caseUtil.fetchCaseDetails(request);

        // Assert
        assertNotNull(actualResponse);
        assertFalse(actualResponse.getCriteria().isEmpty());    }

    @Test
    void testFetchCaseDetails_Exception() {
        // Arrange
        CaseExistsRequest request = new CaseExistsRequest();
        String errorMessage = "Error while fetching case details";

        String host = "http://example.com";
        String path = "/caseExists";
        String fullUri = host + path;

        when(configs.getCaseHost()).thenReturn(host);
        when(configs.getCaseExistsPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> caseUtil.fetchCaseDetails(request));
        assertEquals(errorMessage, exception.getMessage());
    }
}
