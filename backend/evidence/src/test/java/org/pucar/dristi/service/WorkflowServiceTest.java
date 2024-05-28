package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    void updateWorkflowStatus_Exceptions() {
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

        // Mock repository.fetchResult to throw CustomException
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException());

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.updateWorkflowStatus(evidenceRequest));

        // Mock repository.fetchResult to throw generic Exception
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new RuntimeException("Generic Exception"));

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.updateWorkflowStatus(evidenceRequest));
    }

    @Test
    void callWorkFlow_CustomException() {
        // Mock ProcessInstanceRequest
        ProcessInstanceRequest workflowReq = new ProcessInstanceRequest();
        workflowReq.setRequestInfo(new RequestInfo());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));

        // Mock repository.fetchResult to throw CustomException
        when(repository.fetchResult(url, workflowReq)).thenThrow(new CustomException());

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.callWorkFlow(workflowReq));
    }

    @Test
    void callWorkFlow_Exception() {
        // Mock ProcessInstanceRequest
        ProcessInstanceRequest workflowReq = new ProcessInstanceRequest();
        workflowReq.setRequestInfo(new RequestInfo());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));

        // Mock repository.fetchResult to throw generic Exception
        when(repository.fetchResult(url, workflowReq)).thenThrow(new RuntimeException("Generic Exception"));

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.callWorkFlow(workflowReq));
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
    @Test
    void getProcessInstanceForArtifact_WithAssignees() {
        // Mock EvidenceRequest
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setRequestInfo(new RequestInfo());
        Artifact artifact = new Artifact();
        artifact.setArtifactNumber("ART001");
        artifact.setTenantId("tenant1");
        Workflow workflow = new Workflow();
        workflow.setAction("create");
        List<String> assignees = Arrays.asList("assignee1", "assignee2");
        workflow.setAssignes(assignees);
        artifact.setWorkflow(workflow);
        evidenceRequest.setArtifact(artifact);

        // Execute the method
        ProcessInstance processInstance = workflowService.getProcessInstanceForArtifact(artifact, evidenceRequest.getRequestInfo());

        // Assertions
        assertNotNull(processInstance);
        assertEquals("ART001", processInstance.getBusinessId());
        assertEquals("tenant1", processInstance.getTenantId());

        // Assert assignees are set properly
        List<User> assigneesList = processInstance.getAssignes();
        assertNotNull(assigneesList);
        assertEquals(assignees.size(), assigneesList.size());
        for (int i = 0; i < assignees.size(); i++) {
            assertEquals(assignees.get(i), assigneesList.get(i).getUuid());
        }
    }

}
