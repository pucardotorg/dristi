package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceUrlEntityRequestServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OrderSearchService orderSearchService;

    @Mock
    private TaskSearchService taskSearchService;

    @InjectMocks
    private ServiceUrlEntityRequestService serviceUrlEntityRequestService;

    private VcCredentialRequest vcCredentialRequest;
    private RequestInfo requestInfo;

    @BeforeEach
    void setup() {
        requestInfo = new RequestInfo();
        requestInfo.setAuthToken("testAuthToken");

        vcCredentialRequest = VcCredentialRequest.builder()
                .referenceId("testReferenceId")
                .tenantId("testTenantId")
                .requestInfo(requestInfo)
                .moduleName("testModule")
                .build();
    }

    @Test
    void testGetEntityDetails_success() throws Exception {
        String responseBodyString = "{ \"list\": [ { \"taskDetails\": \"{ \\\"caseDetails\\\": { \\\"courtName\\\": \\\"Test Court\\\" }, " +
                "\\\"summonDetails\\\": { \\\"summonId\\\": \\\"TestSummonId\\\", \\\"issueDate\\\": \\\"2023-01-01\\\" }, " +
                "\\\"respondentDetails\\\": { \\\"name\\\": \\\"Test Respondent\\\" } }\", \"orderId\": \"TestOrderId\" } ] }";
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(responseBodyString);

        when(taskSearchService.getTaskSearchResponse(anyString(), anyString(), any(RequestInfo.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any())).thenReturn(responseBodyString);
        JsonNode taskDetailsJson = new ObjectMapper().readTree("{ \"caseDetails\": { \"courtName\": \"Test Court\" }, \"summonDetails\": { \"summonId\": \"TestSummonId\", \"issueDate\": \"2023-01-01\" }, \"respondentDetails\": { \"name\": \"Test Respondent\" } }");
        when(objectMapper.readTree(anyString())).thenReturn(taskDetailsJson);
        when(orderSearchService.searchOrder(anyString(), anyString(), any(RequestInfo.class))).thenReturn("testCnrNumber");

        CredentialRequest credentialRequest = serviceUrlEntityRequestService.getEntityDetails("testSignDate", vcCredentialRequest);

        assertNotNull(credentialRequest);
        assertEquals("testCnrNumber", credentialRequest.getCnrNumber());
        assertEquals("Test Court", credentialRequest.getCourtName());
        assertEquals("2023-01-01", credentialRequest.getSummonsIssueDate());
        assertEquals("TestSummonId", credentialRequest.getSummonId());
        assertEquals("testSignDate", credentialRequest.getOrderPdfSignature());
        assertEquals("Test Respondent", credentialRequest.getRespondentName());
        assertEquals("testReferenceId", credentialRequest.getId());
        assertEquals("testModule", credentialRequest.getModule());
    }

    @Test
    void testGetEntityDetails_jsonParsingException() throws Exception {
        String responseBodyString = "{ \"list\": [ { \"taskDetails\": \"invalidJson\", \"orderId\": \"TestOrderId\" } ] }";
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(responseBodyString);

        when(taskSearchService.getTaskSearchResponse(anyString(), anyString(), any(RequestInfo.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any())).thenReturn(responseBodyString);
        when(objectMapper.readTree(anyString())).thenThrow(new RuntimeException("JSON parse error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                serviceUrlEntityRequestService.getEntityDetails("testSignDate", vcCredentialRequest));

        assertEquals("JSON_PARSING_ERROR", exception.getCode());
        assertTrue(exception.getMessage().contains("error while parsing task search response"));
    }

    @Test
    void testGetEntityDetails_orderSearchException() throws Exception {
        String responseBodyString = "{ \"list\": [ { \"taskDetails\": \"{ \\\"caseDetails\\\": { \\\"courtName\\\": \\\"Test Court\\\" }, " +
                "\\\"summonDetails\\\": { \\\"summonId\\\": \\\"TestSummonId\\\", \\\"issueDate\\\": \\\"2023-01-01\\\" }, " +
                "\\\"respondentDetails\\\": { \\\"name\\\": \\\"Test Respondent\\\" } }\", \"orderId\": \"TestOrderId\" } ] }";
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(responseBodyString);

        when(taskSearchService.getTaskSearchResponse(anyString(), anyString(), any(RequestInfo.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any())).thenReturn(responseBodyString);
        JsonNode taskDetailsJson = new ObjectMapper().readTree("{ \"caseDetails\": { \"courtName\": \"Test Court\" }, \"summonDetails\": { \"summonId\": \"TestSummonId\", \"issueDate\": \"2023-01-01\" }, \"respondentDetails\": { \"name\": \"Test Respondent\" } }");
        when(objectMapper.readTree(anyString())).thenReturn(taskDetailsJson);
        when(orderSearchService.searchOrder(anyString(), anyString(), any(RequestInfo.class))).thenThrow(new CustomException("ORDER_SEARCH_ERR", "Order search error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                serviceUrlEntityRequestService.getEntityDetails("testSignDate", vcCredentialRequest));

        assertEquals("ORDER_SEARCH_ERR", exception.getCode());
        assertTrue(exception.getMessage().contains("Order search error"));
    }
}
