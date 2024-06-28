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
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ABATED_STATE;
import static org.pucar.dristi.config.ServiceConstants.PUBLISHED_STATE;

public class EvidenceServiceTest {

    @Mock
    private EvidenceValidator validator;
    @Mock
    private EvidenceEnrichment evidenceEnrichment;
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
    void testSearchEvidence_SuccessWithResults() {
        // Set up test data
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        Artifact artifact = new Artifact();
        artifact.setTenantId("testTenant");
        artifact.setArtifactNumber("testArtifactNumber");
        List<Artifact> artifacts = Collections.singletonList(artifact);

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenReturn(artifacts);
        // Mock workflow service response
        when(workflowService.getCurrentWorkflow(any(RequestInfo.class), anyString(), anyString())).thenReturn(new ProcessInstance());
        when(workflowService.getWorkflowFromProcessInstance(any(ProcessInstance.class))).thenReturn(new Workflow());

        // Execute the method under test
        List<Artifact> result = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria);

        // Verify and assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
        verify(workflowService, times(1)).getCurrentWorkflow(any(RequestInfo.class), anyString(), anyString());
        verify(workflowService, times(1)).getWorkflowFromProcessInstance(any(ProcessInstance.class));
    }

    @Test
    void testSearchEvidence_SuccessNoResults() {
        // Set up test data
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenReturn(new ArrayList<>());

        // Execute the method under test
        List<Artifact> result = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria);

        // Verify and assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
        verify(workflowService, never()).getCurrentWorkflow(any(RequestInfo.class), anyString(), anyString());
        verify(workflowService, never()).getWorkflowFromProcessInstance(any(ProcessInstance.class));
    }

    @Test
    void testSearchEvidence_HandleCustomException() {
        // Set up test data
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenThrow(new CustomException("EVIDENCE_SEARCH_EXCEPTION", "Custom exception"));

        // Execute the method under test and assert exception
        CustomException exception = assertThrows(CustomException.class, () ->
                evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria));

        assertEquals("EVIDENCE_SEARCH_EXCEPTION", exception.getCode());
        assertEquals("Custom exception", exception.getMessage());
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
    }

    @Test
    void testSearchEvidence_HandleGeneralException() {
        // Set up test data
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenThrow(new RuntimeException("Database error"));

        // Execute the method under test and assert exception
        CustomException exception = assertThrows(CustomException.class, () ->
                evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria));

        assertEquals("EVIDENCE_SEARCH_EXCEPTION", exception.getCode());
        assertEquals("java.lang.RuntimeException: Database error", exception.getMessage());
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
    }

    @Test
    void testSearchEvidenceSuccessNoResults() {
        String id = "testId";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";

        // Create RequestInfo object
        RequestInfo requestInfo = new RequestInfo();

        // Create EvidenceSearchCriteria object and set the parameters
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        evidenceSearchCriteria.setId(id);
        evidenceSearchCriteria.setCaseId(caseId);
        evidenceSearchCriteria.setApplicationId(application);
        evidenceSearchCriteria.setHearing(hearing);
        evidenceSearchCriteria.setOrder(order);
        evidenceSearchCriteria.setSourceId(sourceId);
        evidenceSearchCriteria.setSourceName(sourceName);

        // Mock the repository's getArtifacts method
        when(repository.getArtifacts(evidenceSearchCriteria)).thenReturn(new ArrayList<>());

        // Call the evidenceService's searchEvidence method
        List<Artifact> result = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria);

        // Validate the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchEvidenceHandleException() {
        String id = "testId";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        RequestInfo requestInfo = new RequestInfo();

        // Create EvidenceSearchCriteria object and set the parameters
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        evidenceSearchCriteria.setId(id);
        evidenceSearchCriteria.setCaseId(caseId);
        evidenceSearchCriteria.setApplicationId(application);
        evidenceSearchCriteria.setHearing(hearing);
        evidenceSearchCriteria.setOrder(order);
        evidenceSearchCriteria.setSourceId(sourceId);
        evidenceSearchCriteria.setSourceName(sourceName);

        // Mock the repository's getArtifacts method to throw an exception
        when(repository.getArtifacts(evidenceSearchCriteria)).thenThrow(new RuntimeException("Database error"));

        // Validate the exception thrown by the evidenceService's searchEvidence method
        CustomException exception = assertThrows(CustomException.class, () ->
                evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria));

        assertEquals("EVIDENCE_SEARCH_EXCEPTION", exception.getCode());
        assertEquals("java.lang.RuntimeException: Database error", exception.getMessage());
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

    @Test
    public void testUpdateEvidenceWithPublishedState() throws Exception {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact=new Artifact();
        artifact.setStatus(PUBLISHED_STATE);
        evidenceRequest.setArtifact(artifact);
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(artifact);

        // Act
        evidenceService.updateEvidence(evidenceRequest);

        // Assert
        verify(evidenceEnrichment).enrichEvidenceNumber(evidenceRequest);
        verify(evidenceEnrichment, never()).enrichIsActive(evidenceRequest);
    }

    @Test
    public void testUpdateEvidenceWithAbatedState() throws Exception {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact=new Artifact();
        artifact.setStatus(ABATED_STATE);
        evidenceRequest.setArtifact(artifact);
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(artifact);

        // Act
        evidenceService.updateEvidence(evidenceRequest);

        // Assert
        verify(evidenceEnrichment, never()).enrichEvidenceNumber(evidenceRequest);
        verify(evidenceEnrichment).enrichIsActive(evidenceRequest);
    }

    @Test
    public void testUpdateEvidenceWithOtherState() throws Exception {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact=new Artifact();
        artifact.setStatus("OTHER_STATE");
        evidenceRequest.setArtifact(artifact);
        when(validator.validateApplicationExistence(evidenceRequest)).thenReturn(artifact);

        // Act
        evidenceService.updateEvidence(evidenceRequest);

        // Assert
        verify(evidenceEnrichment, never()).enrichEvidenceNumber(evidenceRequest);
        verify(evidenceEnrichment, never()).enrichIsActive(evidenceRequest);
    }
}
