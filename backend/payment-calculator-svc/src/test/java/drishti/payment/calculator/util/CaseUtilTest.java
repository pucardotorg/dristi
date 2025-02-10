package drishti.payment.calculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.helper.CaseSearchRequestTestBuilder;
import drishti.payment.calculator.web.models.CaseSearchRequest;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseUtilTest {


    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @InjectMocks
    private CaseUtil caseUtil;

    @Test
    @DisplayName("test search case details")
    public void testSearchCaseDetails() throws JsonProcessingException {
        CaseSearchRequest caseSearchRequest = CaseSearchRequestTestBuilder.builder()
                .withCriteriaAndRequestInfo("123", "KL-123").build();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("criteria", List.of(Map.of("responseList", List.of(Map.of("caseId", "123")))));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(responseMap);
        when(mapper.writeValueAsString(any())).thenAnswer(invocation -> new ObjectMapper().writeValueAsString(invocation.getArgument(0)));
        when(mapper.readTree(anyString())).thenAnswer(invocation -> new ObjectMapper().readTree(invocation.getArgument(0).toString()));
        JsonNode jsonNode = caseUtil.searchCaseDetails(caseSearchRequest);

        assertEquals("123", jsonNode.get("caseId").asText());
    }

    @Test
    @DisplayName("test get advocate for litigant")
    public void testGetAdvocateForLitigant() throws JsonProcessingException {
        RequestInfo requestInfo = new RequestInfo();
        String filingNumber = "KL-123";
        String tenantId = "kl";
        Map<String, Object> responseMap = new HashMap<>();

        Map<String, Object> response = new HashMap<>();
        response.put("caseId", "123");
        response.put("litigants", List.of(Map.of("individualId", "1")));
        response.put("representatives", List.of(Map.of("isActive",true,"representing", List.of(Map.of("individualId", "1","isActive",true)))));

        responseMap.put("criteria", List.of(Map.of("responseList", List.of(response))));

        when(configs.getCaseHost()).thenReturn("https://casehost.com");
        when(configs.getCaseSearchPath()).thenReturn("/case/v1/_search");
        when(mapper.writeValueAsString(any())).thenAnswer(invocation -> new ObjectMapper().writeValueAsString(invocation.getArgument(0)));
        when(mapper.readTree(anyString())).thenAnswer(invocation -> new ObjectMapper().readTree(invocation.getArgument(0).toString()));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(responseMap);
        Map<String, List<JsonNode>> result = caseUtil.getAdvocateForLitigant(requestInfo, filingNumber, tenantId);
        assertEquals(1, result.size());
    }

}
