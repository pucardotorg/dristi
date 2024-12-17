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
import org.pucar.dristi.util.EvidenceUtil;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvidenceUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @InjectMocks
    private EvidenceUtil evidenceUtil;

    @BeforeEach
    void setUp() {
        String evidenceHost = "http://localhost";
        String evidenceSearchPath = "/evidence-search";
        when(config.getEvidenceHost()).thenReturn(evidenceHost);
        when(config.getEvidenceSearchPath()).thenReturn(evidenceSearchPath);
    }

    @Test
    void testGetEvidence_WithArtifactNumber() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String artifactNumber = "ART123";

        String mockResponse = "{\"artifacts\": [{\"artifactNumber\": \"ART123\", \"details\": \"Artifact Details\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockArtifacts = new JSONArray();
        mockArtifacts.put(new JSONObject().put("artifactNumber", artifactNumber).put("details", "Artifact Details"));
        when(util.constructArray(anyString(), anyString())).thenReturn(mockArtifacts);

        Object result = evidenceUtil.getEvidence(request, tenantId, artifactNumber);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("ART123", ((JSONObject) result).getString("artifactNumber"));
    }

    @Test
    void testGetEvidence_WithNullArtifactNumber() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String artifactNumber = null;

        String mockResponse = "{\"artifacts\": []}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(mockResponse);

        JSONArray mockArtifacts = new JSONArray();
        when(util.constructArray(anyString(), anyString())).thenReturn(mockArtifacts);

        Object result = evidenceUtil.getEvidence(request, tenantId, artifactNumber);

        assertNull(result);
    }

    @Test
    void testGetEvidence_Exception() throws Exception {
        JSONObject request = new JSONObject();
        String tenantId = "tenant1";
        String artifactNumber = "ART123";

        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenThrow(new RuntimeException("Error fetching evidence"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            evidenceUtil.getEvidence(request, tenantId, artifactNumber);
        });

        assertEquals("Error while fetching or processing the evidence response", exception.getMessage());
    }
}
