package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EvidenceServiceTest {

    @InjectMocks
    private EvidenceService evidenceService;

    @Mock
    private EvidenceValidator validator;

    @Mock
    private EvidenceEnrichment evidenceEnrichment;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private EvidenceRepository repository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    private EvidenceRequest evidenceRequest;
    private RequestInfo requestInfo;
    private EvidenceSearchCriteria evidenceSearchCriteria;
    private Artifact artifact;

    @BeforeEach
    public void setUp() {
        evidenceRequest = new EvidenceRequest();
        artifact = new Artifact();
        artifact.setArtifactNumber("ART123");
        artifact.setTenantId("default");
        evidenceRequest.setArtifact(artifact);
        evidenceSearchCriteria = new EvidenceSearchCriteria();
        requestInfo = new RequestInfo();
    }

    @Test
    public void testCreateEvidence_withWorkflow() {
        artifact.setHearing("Hearing123");

        when(config.getEvidenceCreateTopic()).thenReturn("evidence-create-topic");

        Artifact result = evidenceService.createEvidence(evidenceRequest);

        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistration(evidenceRequest);
        verify(workflowService).updateWorkflowStatus(evidenceRequest);
        verify(producer).push(config.getEvidenceCreateTopic(), evidenceRequest);
        assertEquals(artifact, result);
    }

    @Test
    public void testCreateEvidence_withoutWorkflow() {
        when(config.getEvidenceCreateWithoutWorkflowTopic()).thenReturn("evidence-create-without-workflow-topic");

        Artifact result = evidenceService.createEvidence(evidenceRequest);

        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistration(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceNumber(evidenceRequest);
        verify(producer).push(config.getEvidenceCreateWithoutWorkflowTopic(), evidenceRequest);
        assertEquals(artifact, result);
    }

    @Test
    public void testSearchEvidence() {
        when(repository.getArtifacts(evidenceSearchCriteria)).thenReturn(Collections.singletonList(artifact));
        when(workflowService.getCurrentWorkflow(requestInfo, artifact.getTenantId(), artifact.getArtifactNumber()))
                .thenReturn(null);
        when(workflowService.getWorkflowFromProcessInstance(null)).thenReturn(null);

        List<Artifact> result = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria);

        verify(repository).getArtifacts(evidenceSearchCriteria);
        assertFalse(result.isEmpty());
        assertEquals(artifact, result.get(0));
    }

    @Test
    public void testSearchEvidence_noResults() {
        when(repository.getArtifacts(evidenceSearchCriteria)).thenReturn(Collections.emptyList());

        List<Artifact> result = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria);

        verify(repository).getArtifacts(evidenceSearchCriteria);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateEvidence_withWorkflow() {
        artifact.setHearing("Hearing123");
        evidenceRequest.getArtifact().setStatus("PUBLISHED");

        when(config.getUpdateEvidenceKafkaTopic()).thenReturn("update-evidence-topic");

        Artifact existingArtifact = new Artifact();
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(existingArtifact);

        Artifact result = evidenceService.updateEvidence(evidenceRequest);

        verify(validator).validateApplicationExistence(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistrationUponUpdate(evidenceRequest);
        verify(workflowService).updateWorkflowStatus(evidenceRequest);
        verify(producer).push(config.getUpdateEvidenceKafkaTopic(), evidenceRequest);
        assertEquals(artifact, result);
    }

    @Test
    public void testUpdateEvidence_withoutWorkflow() {
        when(config.getUpdateEvidenceWithoutWorkflowKafkaTopic()).thenReturn("update-evidence-without-workflow-topic");

        Artifact existingArtifact = new Artifact();
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(existingArtifact);

        Artifact result = evidenceService.updateEvidence(evidenceRequest);

        verify(validator).validateApplicationExistence(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistrationUponUpdate(evidenceRequest);
        verify(producer).push(config.getUpdateEvidenceWithoutWorkflowKafkaTopic(), evidenceRequest);
        assertEquals(artifact, result);
    }

    @Test
    public void testValidateExistingApplication() {
        Artifact existingArtifact = new Artifact();
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(existingArtifact);

        Artifact result = evidenceService.validateExistingApplication(evidenceRequest);

        verify(validator).validateApplicationExistence(evidenceRequest);
        assertEquals(existingArtifact, result);
    }

    @Test
    public void testEnrichBasedOnStatus_published() {
        artifact.setStatus("PUBLISHED");

        evidenceService.enrichBasedOnStatus(evidenceRequest);

        verify(evidenceEnrichment).enrichEvidenceNumber(evidenceRequest);
    }

    @Test
    public void testEnrichBasedOnStatus_abated() {
        artifact.setStatus("ABATED");

        evidenceService.enrichBasedOnStatus(evidenceRequest);

        verify(evidenceEnrichment).enrichIsActive(evidenceRequest);
    }
}
