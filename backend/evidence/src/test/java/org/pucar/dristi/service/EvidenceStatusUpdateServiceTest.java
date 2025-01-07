package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvidenceStatusUpdateServiceTest {

    @InjectMocks
    private EvidenceStatusUpdateService evidenceStatusUpdateService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EvidenceEnrichment evidenceEnrichment;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Mock
    private EvidenceService evidenceService;


    @Test
    @DisplayName("do update evidence on Application Update")
    void doUpdateEvidenceOnApplicationUpdate() throws JsonProcessingException {
        // Arrange

        Map<String, Object> recordMap = new HashMap<>();
        Map<String, Object> applicationMap = new HashMap<>();
        applicationMap.put("applicationNumber", "12345");
        applicationMap.put("status", "Completed");

        recordMap.put("application", applicationMap);
        recordMap.put("RequestInfo", new HashMap<>());

        String recordJson = "{\"application\":{\"applicationNumber\":\"12345\",\"status\":\"Completed\"},\"RequestInfo\":{}}";
        when(objectMapper.writeValueAsString(any())).thenReturn(recordJson);

        RequestInfo requestInfo = new RequestInfo();
        LinkedHashMap<String, Object> requestInfoMap = new LinkedHashMap<>();
        when(objectMapper.convertValue(requestInfoMap, RequestInfo.class)).thenReturn(requestInfo);

        Artifact artifact = new Artifact();
        artifact.setStatus("OldStatus");
        List<Artifact> artifacts = Collections.singletonList(artifact);

        EvidenceSearchCriteria evidenceSearchCriteria = EvidenceSearchCriteria.builder().applicationNumber("12345").build();
        when(evidenceService.searchEvidence(any(RequestInfo.class), eq(evidenceSearchCriteria), any())).thenReturn(artifacts);
        when(config.getUpdateEvidenceWithoutWorkflowKafkaTopic()).thenReturn("test-topic");

        // Act
        evidenceStatusUpdateService.updateEvidenceStatus(recordMap);

        // Assert
        verify(evidenceEnrichment, times(1)).enrichEvidenceRegistrationUponUpdate(any(EvidenceRequest.class));
        verify(producer, times(1)).push(eq("test-topic"), any(EvidenceRequest.class));
        assertEquals("Completed", artifact.getStatus());
    }

    @Test
    @DisplayName("update evidence json processing exception")
    void updateEvidenceJsonProcessingException() throws JsonProcessingException {
        Map<String, Object> record = new HashMap<>();
        when(objectMapper.writeValueAsString(record)).thenThrow(new JsonProcessingException("Test Exception") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> evidenceStatusUpdateService.updateEvidenceStatus(record));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("update evidence general exception")
    void updateEvidenceGeneralException() {
        Map<String, Object> record = new HashMap<>();

        assertDoesNotThrow(() -> evidenceStatusUpdateService.updateEvidenceStatus(record));
    }
}
