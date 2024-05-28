package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.service.WorkflowService;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkflowServiceTest {

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @Mock
    private ObjectMapper mapper;

    @Test
    void updateWorkflowStatus_Success() {
        // Mock EvidenceRequest
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setRequestInfo(new RequestInfo());
        Artifact artifact = new Artifact();
        artifact.setArtifactNumber("ART001");
        artifact.setTenantId("tenant1");
        artifact.setWorkflow(Workflow.builder().action("APPROVE").build());
        evidenceRequest.setArtifact(artifact);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowResponse = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowResponse);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(evidenceRequest));
    }

    @Test
    void getProcessInstanceForArtifact_Success() {
        // Mock EvidenceRequest
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setRequestInfo(new RequestInfo());
        Artifact artifact = new Artifact();
        artifact.setArtifactNumber("ART001");
        artifact.setTenantId("tenant1");
        Workflow workflow = new Workflow();
        workflow.setAction("create");
        artifact.setWorkflow(workflow);
        evidenceRequest.setArtifact(artifact);

        // Execute the method
        ProcessInstance processInstance = workflowService.getProcessInstanceForArtifact(artifact, evidenceRequest.getRequestInfo());

        // Assertions
        assertNotNull(processInstance);
        assertEquals("ART001", processInstance.getBusinessId());
        assertEquals("tenant1", processInstance.getTenantId());
    }
}
