package org.pucar.dristi.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.Criteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
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
    void setUp() {
        when(configs.getCaseHost()).thenReturn("http://localhost:8080");
        when(configs.getCaseExistsPath()).thenReturn("/caseExists");
    }

    @Test
    void testFetchCaseDetailsSuccess(){
        CaseExistsRequest request = new CaseExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", true)));

        CaseExistsResponse caseExistsResponse = CaseExistsResponse.builder()
                .criteria(List.of(CaseExists.builder().exists(true).build()))
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, CaseExistsResponse.class))
                .thenReturn(caseExistsResponse);

        Boolean result = caseUtil.fetchCaseDetails(request);
        assertTrue(result);
    }

    @Test
    void testFetchCaseDetailsDoesNotExist(){
        CaseExistsRequest request = new CaseExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", false)));

        CaseExistsResponse caseExistsResponse = CaseExistsResponse.builder()
                .criteria(List.of(CaseExists.builder().exists(false).build()))
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, CaseExistsResponse.class))
                .thenReturn(caseExistsResponse);

        Boolean result = caseUtil.fetchCaseDetails(request);
        assertFalse(result);
    }


    @Test
    void testFetchCaseDetailsException() {
        CaseExistsRequest request = new CaseExistsRequest();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> {
            caseUtil.fetchCaseDetails(request);
        });
    }
}
