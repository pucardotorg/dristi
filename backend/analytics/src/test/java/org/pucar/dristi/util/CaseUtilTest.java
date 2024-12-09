package org.pucar.dristi.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @InjectMocks
    private CaseUtil caseUtil;

    @BeforeEach
    void setUp() {
        String caseHost = "http://localhost";
        String caseSearchPath = "/case-search";
        when(config.getCaseHost()).thenReturn(caseHost);
        when(config.getCaseSearchPath()).thenReturn(caseSearchPath);
    }

    @Test
    void testGetCase_AllParameters() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = "CNR123";
        String filingNumber = "FIL123";
        String caseId = "CASE123";

        String mockResponse = "{\"cases\": [{\"caseId\": \"CASE123\", \"details\": \"Case Details\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockCases = new JSONArray();
        mockCases.put(new JSONObject().put("caseId", caseId).put("details", "Case Details"));
        when(util.constructArray(anyString(), anyString())).thenReturn(mockCases);

        Object result = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("CASE123", ((JSONObject) result).getString("caseId"));
    }

    @Test
    void testGetCase_CnrNumberOnly() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = "CNR123";
        String filingNumber = null;
        String caseId = null;

        String mockResponse = "{\"cases\": [{\"caseId\": \"CASE123\", \"details\": \"Case Details\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockCases = new JSONArray();
        mockCases.put(new JSONObject().put("caseId", "CASE123").put("details", "Case Details"));
        when(util.constructArray(anyString(), anyString())).thenReturn(mockCases);

        Object result = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("CASE123", ((JSONObject) result).getString("caseId"));
    }

    @Test
    void testGetCase_FilingNumberOnly() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = null;
        String filingNumber = "FIL123";
        String caseId = null;

        String mockResponse = "{\"cases\": [{\"caseId\": \"CASE123\", \"details\": \"Case Details\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockCases = new JSONArray();
        mockCases.put(new JSONObject().put("caseId", "CASE123").put("details", "Case Details"));
        when(util.constructArray(anyString(), anyString())).thenReturn(mockCases);

        Object result = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("CASE123", ((JSONObject) result).getString("caseId"));
    }

    @Test
    void testGetCase_CaseIdOnly() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = null;
        String filingNumber = null;
        String caseId = "CASE123";

        String mockResponse = "{\"cases\": [{\"caseId\": \"CASE123\", \"details\": \"Case Details\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockCases = new JSONArray();
        mockCases.put(new JSONObject().put("caseId", caseId).put("details", "Case Details"));
        when(util.constructArray(anyString(), anyString())).thenReturn(mockCases);

        Object result = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("CASE123", ((JSONObject) result).getString("caseId"));
    }

    @Test
    void testGetCase_NoParameters() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = null;
        String filingNumber = null;
        String caseId = null;

        String mockResponse = "{\"cases\": []}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockCases = new JSONArray();
        when(util.constructArray(anyString(), anyString())).thenReturn(mockCases);

        Object result = caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId);

        assertNull(result);
    }

    @Test
    void testGetCase_Exception() {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String cnrNumber = "CNR123";
        String filingNumber = "FIL123";
        String caseId = "CASE123";

        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenThrow(new RuntimeException("Error fetching case"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> caseUtil.getCase(request, tenantId, cnrNumber, filingNumber, caseId));

        assertEquals("Error while processing case response", exception.getMessage());
    }
}
