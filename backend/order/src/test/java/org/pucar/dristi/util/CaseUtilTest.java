package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseUtilTest {

    @InjectMocks
    private CaseUtil caseUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    private RequestInfo requestInfo;
    private String cnrNumber;
    private String filingNumber;
    private String caseHost;
    private String casePath;
    private CaseExistsResponse caseExistsResponse;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        cnrNumber = "CNR123";
        filingNumber = "FILING123";
        caseHost = "http://localhost:8080/case";
        casePath = "/case/_exists";

        caseExistsResponse = new CaseExistsResponse();
        caseExistsResponse.setCriteria(Collections.singletonList(new CaseExists()));
        when(configs.getCaseHost()).thenReturn(caseHost);
        when(configs.getCasePath()).thenReturn(casePath);
    }

    @Test
    void testFetchCaseDetailsSuccess() throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> caseDetails = new HashMap<>();
        caseDetails.put("exists", true);
        responseMap.put("criteria", Collections.singletonList(caseDetails));

        CaseExistsResponse caseExistsResponse = new CaseExistsResponse();
        CaseExists caseExists = new CaseExists();
        caseExists.setExists(true);
        caseExistsResponse.setCriteria(Collections.singletonList(caseExists));

        when(restTemplate.postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class))).thenReturn(responseMap);
        when(mapper.convertValue(responseMap, CaseExistsResponse.class)).thenReturn(caseExistsResponse);

        Boolean exists = caseUtil.fetchCaseDetails(requestInfo, cnrNumber, filingNumber);

        assertTrue(exists);

        verify(configs).getCaseHost();
        verify(configs).getCasePath();
        verify(restTemplate).postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class));
        verify(mapper).convertValue(responseMap, CaseExistsResponse.class);
    }

//    @Test
//    void testFetchCaseDetailsNullResponse() throws Exception {
//        when(restTemplate.postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class))).thenReturn(null);
//
//        Boolean exists = caseUtil.fetchCaseDetails(requestInfo, cnrNumber, filingNumber);
//
//        assertFalse(exists);
//
//        verify(configs).getCaseHost();
//        verify(configs).getCasePath();
//        verify(restTemplate).postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class));
//    }
//
//    @Test
//    void testFetchCaseDetailsEmptyCriteria() throws Exception {
//        Map<String, Object> responseMap = new HashMap<>();
//
//        when(restTemplate.postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class))).thenReturn(responseMap);
//        when(mapper.convertValue(responseMap, CaseExistsResponse.class)).thenReturn(caseExistsResponse);
//
//        Boolean exists = caseUtil.fetchCaseDetails(requestInfo, cnrNumber, filingNumber);
//
//        assertFalse(exists);
//
//        verify(configs).getCaseHost();
//        verify(configs).getCasePath();
//        verify(restTemplate).postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class));
//        verify(mapper).convertValue(responseMap, CaseExistsResponse.class);
//    }
//
//    @Test
//    void testFetchCaseDetailsException() throws Exception {
//        when(restTemplate.postForObject(anyString(), any(CaseExistsRequest.class), eq(Map.class))).thenReturn(null);
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            caseUtil.fetchCaseDetails(requestInfo, cnrNumber, filingNumber);
//        });
//
//        assertEquals("java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0", exception);
//    }
}
