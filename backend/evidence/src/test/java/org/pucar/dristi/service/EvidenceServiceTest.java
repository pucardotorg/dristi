package org.pucar.dristi.service;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EvidenceServiceTest {

    @Mock
    private EvidenceValidator validator;
    @Mock
    private EvidenceEnrichment enrichmentUtil;
    @Mock
    private WorkflowService workflowService;
    @Mock
    private Producer producer;
    @Mock
    private Configuration config;

    @InjectMocks
    private EvidenceService evidenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(evidenceService, "config", config);
    }

    @Test
    public void testCreateEvidence() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(config.getEvidenceCreateTopic()).thenReturn("create_topic");

        // Execute method
        Artifact result = evidenceService.createEvidence(evidenceRequest);

        // Verify behavior
        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verify(workflowService).updateWorkflowStatus(evidenceRequest);
        verify(producer).push("create_topic", evidenceRequest);
    }
    @Test
    public void testCreateEvidenceWithCustomException() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(config.getEvidenceCreateTopic()).thenReturn("create_topic");
        // Mocking validation throwing CustomException
        doThrow(new CustomException()).when(validator).validateEvidenceRegistration(evidenceRequest);

        // Execute and assert
        assertThrows(CustomException.class, () -> evidenceService.createEvidence(evidenceRequest));
        // Verify behavior
        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verifyNoInteractions(workflowService, producer);
    }

    @Test
    public void testCreateEvidenceWithOtherException() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(config.getEvidenceCreateTopic()).thenReturn("create_topic");
        // Mocking validation throwing RuntimeException
        doThrow(new RuntimeException()).when(validator).validateEvidenceRegistration(evidenceRequest);

        // Execute and assert
        assertThrows(CustomException.class, () -> evidenceService.createEvidence(evidenceRequest));
        // Verify behavior
        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verifyNoInteractions(workflowService, producer);
    }
    @Test
    public void testUpdateEvidence() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(config.getUpdateEvidenceKafkaTopic()).thenReturn("update_topic");
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(new Artifact());

        // Execute method
        Artifact result = evidenceService.updateEvidence(evidenceRequest);

        // Verify behavior
        verify(validator).validateApplicationExistence(evidenceRequest);
        verify(workflowService).updateWorkflowStatus(evidenceRequest);
        verify(producer).push("update_topic", evidenceRequest);
    }
    @Test
    public void testUpdateEvidenceWithValidationException() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(validator.validateApplicationExistence(evidenceRequest)).thenThrow(new RuntimeException("Validation Exception"));

        // Execute and assert
        assertThrows(CustomException.class, () -> evidenceService.updateEvidence(evidenceRequest));

        // Verify behavior
        verify(validator).validateApplicationExistence(evidenceRequest);
        verifyNoInteractions(workflowService, producer);
    }

    @Test
    public void testUpdateEvidenceWithOtherException() {
        // Prepare data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(new Artifact());

        // Mock behavior
        when(validator.validateApplicationExistence(evidenceRequest)).thenThrow(new NullPointerException("Null Pointer Exception"));

        // Execute and assert
        assertThrows(CustomException.class, () -> evidenceService.updateEvidence(evidenceRequest));

        // Verify behavior
        verify(validator).validateApplicationExistence(evidenceRequest);
        verifyNoInteractions(workflowService, producer);
    }
}
