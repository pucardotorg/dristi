package org.pucar.dristi.util;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingUtilTest {

    @Mock
    private Configuration config;
    @Mock
    private IndexerUtils indexerUtil;
    @Mock
    private ServiceRequestRepository requestRepository;
    @Mock
    private CaseUtil caseUtil;

    private BillingUtil billingUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingUtil = new BillingUtil(config, indexerUtil, requestRepository, caseUtil);
    }

    @Test
    void testBuildPayload() throws JSONException {
        // Prepare test data
        String jsonItem = "{\"id\":\"123\",\"businessService\":\"test-service\",\"consumerCode\":\"CC-001\",\"status\":\"ACTIVE\",\"tenantId\":\"test-tenant\",\"demandDetails\":[{\"taxAmount\":100.0},{\"taxAmount\":50.0}],\"auditDetails\":{\"createdBy\":\"user1\",\"lastModifiedBy\":\"user2\"}}";
        JSONObject requestInfo = new JSONObject();
        requestInfo.put("userInfo", new JSONObject().put("name", "Test User"));

        // Mock dependencies
        when(config.getBillingIndex()).thenReturn("billing-index");
        when(indexerUtil.processEntityByType(anyString(), any(), anyString(), isNull()))
                .thenReturn(Map.of("cnrNumber", "CNR001", "filingNumber", "FN001"));
        when(caseUtil.getCase(any(), anyString(), anyString(), anyString(), isNull()))
                .thenReturn("{\"caseTitle\":\"Test Case\",\"stage\":\"HEARING\"}");

        // Execute the method
        String result = billingUtil.buildPayload(jsonItem, requestInfo);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.contains("\"index\":{\"_index\":\"billing-index\",\"_id\":\"123\"}"));
        assertTrue(result.contains("\"_id\":\"123\""));
        assertTrue(result.contains("\"tenantId\": \"test-tenant\""));
        assertTrue(result.contains("\"caseTitleFilingNumber\": \"Test Case,FN001\""));
        assertTrue(result.contains("\"stage\": \"HEARING\""));
    }

    @Test
    void testBuildString() throws JSONException {
        JSONObject jsonObject = new JSONObject().put("key", "value");
        when(indexerUtil.buildString(any(JSONObject.class))).thenReturn("mocked-string");

        String result = billingUtil.buildString(jsonObject);

        assertEquals("mocked-string", result);
        verify(indexerUtil).buildString(jsonObject);
    }

    @Test
    void testGetDemand() throws JSONException {
        String tenantId = "test-tenant";
        String demandId = "demand-001";
        JSONObject requestInfo = new JSONObject().put("userInfo", new JSONObject().put("name", "Test User"));

        when(config.getDemandHost()).thenReturn("http://demand-host");
        when(config.getDemandEndPoint()).thenReturn("/demand");
        when(requestRepository.fetchResult(any(StringBuilder.class), any(JSONObject.class)))
                .thenReturn("mocked-demand-response");

        String result = billingUtil.getDemand(tenantId, demandId, requestInfo);

        assertEquals("mocked-demand-response", result);
        verify(requestRepository).fetchResult(
                argThat(sb -> sb.toString().equals("http://demand-host/demand?tenantId=test-tenant&demandId=demand-001")),
                eq(requestInfo)
        );
    }
}