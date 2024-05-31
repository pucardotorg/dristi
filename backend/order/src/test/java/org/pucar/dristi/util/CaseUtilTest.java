package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class CaseUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private CaseUtil caseUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchOrderDetails_Success() {
        // Arrange
        String cnrNumber = "CNR123";
        RequestInfo requestInfo = new RequestInfo();

        CaseResponse mockCaseResponse = new CaseResponse();
        mockCaseResponse.setCases(Collections.singletonList(new CourtCase()));
        Map<String, Object> mockResponse = new HashMap<>();
        doReturn(mockResponse).when(restTemplate).postForObject(any(String.class), any(CaseSearchRequest.class), eq(Map.class));
        doReturn(mockCaseResponse).when(mapper).convertValue(mockResponse, CaseResponse.class);
        doReturn("http://case-host").when(configs).getCaseHost();
        doReturn("/case-path").when(configs).getCasePath();

        // Act
        Boolean result = caseUtil.fetchOrderDetails(requestInfo, cnrNumber);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testFetchOrderDetails_NoCasesFound() {
        // Arrange
        String cnrNumber = "CNR123";
        RequestInfo requestInfo = new RequestInfo();

        CaseResponse mockCaseResponse = new CaseResponse();
        mockCaseResponse.setCases(Collections.emptyList());

        Map<String, Object> mockResponse = new HashMap<>();
        doReturn(mockResponse).when(restTemplate).postForObject(any(String.class), any(CaseSearchRequest.class), eq(Map.class));
        doReturn(mockCaseResponse).when(mapper).convertValue(mockResponse, CaseResponse.class);
        doReturn("http://case-host").when(configs).getCaseHost();
        doReturn("/case-path").when(configs).getCasePath();

        // Act
        Boolean result = caseUtil.fetchOrderDetails(requestInfo, cnrNumber);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFetchOrderDetails_Exception() {
        // Arrange
        String cnrNumber = "CNR123";
        RequestInfo requestInfo = new RequestInfo();

        doThrow(new RuntimeException("Error")).when(restTemplate).postForObject(any(String.class), any(CaseSearchRequest.class), eq(Map.class));
        doReturn("http://case-host").when(configs).getCaseHost();
        doReturn("/case-path").when(configs).getCasePath();

        // Act
        Boolean result = caseUtil.fetchOrderDetails(requestInfo, cnrNumber);

        // Assert
        assertFalse(result);
    }
}
