package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static digit.config.ServiceConstants.ERROR_FROM_CASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseUtilTest {

    @Mock
    private Configuration configuration;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CaseUtil caseUtil;

    private CaseDiaryGenerateRequest generateRequest;
    private String mockCaseHost;
    private String mockCaseSearchPath;

    @BeforeEach
    void setUp() {
        // Initialize test data
        generateRequest = new CaseDiaryGenerateRequest();
        CaseDiary diary = new CaseDiary();
        diary.setCaseNumber("CASE123");
        generateRequest.setDiary(diary);
        generateRequest.setRequestInfo(new RequestInfo());

        mockCaseHost = "http://case-service";
        mockCaseSearchPath = "/case/v1/_search";

        // Configure mock responses
        when(configuration.getCaseHost()).thenReturn(mockCaseHost);
        when(configuration.getCaseSearchPath()).thenReturn(mockCaseSearchPath);
    }

    @Test
    void getCaseDetailsSuccess() {
        // Prepare mock response
        Map<String, Object> mockResponse = new HashMap<>();
        CaseListResponse mockCaseListResponse = new CaseListResponse();
        List<CaseCriteria> criteriaList = new ArrayList<>();
        CaseCriteria searchCriteria = new CaseCriteria();
        List<CourtCase> courtCases = new ArrayList<>();
        courtCases.add(new CourtCase());
        searchCriteria.setResponseList(courtCases);
        criteriaList.add(searchCriteria);
        mockCaseListResponse.setCriteria(criteriaList);

        // Mock REST call and object mapping
        when(restTemplate.postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        )).thenReturn(mockResponse);

        when(objectMapper.convertValue(mockResponse, CaseListResponse.class))
                .thenReturn(mockCaseListResponse);

        // Execute test
        List<CourtCase> result = caseUtil.getCaseDetails(generateRequest);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate).postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        );
        verify(objectMapper).convertValue(mockResponse, CaseListResponse.class);
    }

    @Test
    void getCaseDetailsNullResponse() {
        // Mock REST call returning null
        when(restTemplate.postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        )).thenReturn(null);

        when(objectMapper.convertValue(null, CaseListResponse.class))
                .thenReturn(null);

        // Execute test
        List<CourtCase> result = caseUtil.getCaseDetails(generateRequest);

        // Verify results
        assertNull(result);
        verify(restTemplate).postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        );
    }

    @Test
    void getCaseDetailsRestTemplateException() {
        // Mock REST call throwing exception
        when(restTemplate.postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        )).thenThrow(new RuntimeException("REST call failed"));

        // Execute test and verify exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            caseUtil.getCaseDetails(generateRequest);
        });

        assertEquals(ERROR_FROM_CASE, exception.getCode());
        verify(restTemplate).postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        );
    }

    @Test
    void getCaseDetailsNullCriteria() {
        // Prepare mock response with null criteria
        Map<String, Object> mockResponse = new HashMap<>();
        CaseListResponse mockCaseListResponse = new CaseListResponse();
        mockCaseListResponse.setCriteria(null);

        // Mock REST call and object mapping
        when(restTemplate.postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        )).thenReturn(mockResponse);

        when(objectMapper.convertValue(mockResponse, CaseListResponse.class))
                .thenReturn(mockCaseListResponse);

        // Execute test
        List<CourtCase> result = caseUtil.getCaseDetails(generateRequest);

        // Verify results
        assertNull(result);
        verify(restTemplate).postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        );
        verify(objectMapper).convertValue(mockResponse, CaseListResponse.class);
    }

    @Test
    void getCaseDetailsEmptyCriteriaList() {
        // Prepare mock response with empty criteria list
        Map<String, Object> mockResponse = new HashMap<>();
        CaseListResponse mockCaseListResponse = new CaseListResponse();
        mockCaseListResponse.setCriteria(new ArrayList<>());

        // Mock REST call and object mapping
        when(restTemplate.postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        )).thenReturn(mockResponse);

        when(objectMapper.convertValue(mockResponse, CaseListResponse.class))
                .thenReturn(mockCaseListResponse);

        // Execute test
        List<CourtCase> result = caseUtil.getCaseDetails(generateRequest);

        // Verify results
        assertNull(result);
        verify(restTemplate).postForObject(
                eq(mockCaseHost + mockCaseSearchPath),
                any(CaseSearchRequest.class),
                eq(Map.class)
        );
        verify(objectMapper).convertValue(mockResponse, CaseListResponse.class);
    }
}