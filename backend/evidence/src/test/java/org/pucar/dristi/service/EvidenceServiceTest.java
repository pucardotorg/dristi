package org.pucar.dristi.service;

import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    private EvidenceRepository repository;
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
    void testSearchEvidenceSuccessWithResults() {
        String id = "testId";
        String tenantId = "testTenantId";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        RequestInfo requestInfo = new RequestInfo();

        List<Artifact> artifactList = new ArrayList<>();
        Artifact artifact = new Artifact();
        artifact.setArtifactNumber("testArtifactNumber");
        artifactList.add(artifact);

        ProcessInstance processInstance = new ProcessInstance();
        Workflow workflow = new Workflow();

        when(repository.getArtifacts(id, caseId, application, hearing, order, sourceId, sourceName)).thenReturn(artifactList);
        when(workflowService.getCurrentWorkflow(requestInfo, tenantId, "testArtifactNumber")).thenReturn(processInstance);
        when(workflowService.getWorkflowFromProcessInstance(processInstance)).thenReturn(workflow);

        List<Artifact> result = evidenceService.searchEvidence(id, tenantId, caseId, application, hearing, order, sourceId, sourceName, requestInfo);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(workflow, result.get(0).getWorkflow());
    }

    @Test
    void testSearchEvidenceSuccessNoResults() {
        String id = "testId";
        String tenantId = "testTenantId";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        RequestInfo requestInfo = new RequestInfo();

        when(repository.getArtifacts(id, caseId, application, hearing, order, sourceId, sourceName)).thenReturn(new ArrayList<>());

        List<Artifact> result = evidenceService.searchEvidence(id, tenantId, caseId, application, hearing, order, sourceId, sourceName, requestInfo);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchEvidenceHandleException() {
        String id = "testId";
        String tenantId = "testTenantId";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        RequestInfo requestInfo = new RequestInfo();

        when(repository.getArtifacts(id, caseId, application, hearing, order, sourceId, sourceName)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                evidenceService.searchEvidence(id, tenantId, caseId, application, hearing, order, sourceId, sourceName, requestInfo));

        assertEquals("EVIDENCE_SEARCH_EXCEPTION", exception.getCode());
        assertEquals("Database error", exception.getMessage());
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
