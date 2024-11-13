package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.egov.tracer.model.CustomException;
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
import org.pucar.dristi.web.models.CaseSearchRequest;
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

    @Test
    void testFetchCaseDetailsSuccess(){
        CaseExistsRequest request = new CaseExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", true)));
        when(configs.getCaseHost()).thenReturn("http://localhost:8080");
        when(configs.getCaseExistsPath()).thenReturn("/caseExists");
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
        when(configs.getCaseHost()).thenReturn("http://localhost:8080");
        when(configs.getCaseExistsPath()).thenReturn("/caseExists");
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
        when(configs.getCaseHost()).thenReturn("http://localhost:8080");
        when(configs.getCaseExistsPath()).thenReturn("/caseExists");
        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> {
            caseUtil.fetchCaseDetails(request);
        });
    }

    @Test
    void testSearchCaseDetails_Success() throws JsonProcessingException {
        // Arrange
        CaseSearchRequest request = new CaseSearchRequest();
        JsonNode expectedResponse = new ObjectMapper().readTree("{\"criteria\":[{\"responseList\":[{\"caseId\":\"123\"}]}]}");

        String host = "http://example.com";
        String path = "/caseSearch";
        String fullUri = host + path;

        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("criteria", Collections.singletonList(new CaseExists()));

        when(configs.getCaseHost()).thenReturn(host);
        when(configs.getCaseSearchPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenReturn(responseObject);
        when(mapper.readTree(mapper.writeValueAsString(responseObject))).thenReturn(expectedResponse);

        // Act
        JsonNode actualResponse = caseUtil.searchCaseDetails(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("123", actualResponse.get("caseId").asText());
    }

    @Test
    void testSearchCaseDetails_Exception() throws JsonProcessingException {
        // Arrange
        CaseSearchRequest request = new CaseSearchRequest();
        String errorMessage = "Error while fetching case details";

        String host = "http://example.com";
        String path = "/caseSearch";
        String fullUri = host + path;

        when(configs.getCaseHost()).thenReturn(host);
        when(configs.getCaseSearchPath()).thenReturn(path);
        when(restTemplate.postForObject(eq(fullUri), eq(request), eq(Map.class))).thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> caseUtil.searchCaseDetails(request));
        assertEquals(errorMessage, exception.getMessage());
    }
}
