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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.service.WorkflowService;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.RequestInfoWrapper;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;

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
    void getWorkflowFromProcessInstance_NullProcessInstance() {
        // Test when processInstance is null
        ProcessInstance processInstance = null;
        Workflow result = workflowService.getWorkflowFromProcessInstance(processInstance);
        assertNull(result);
    }
    @Test
    void getSearchURLForProcessInstanceWithParams_ValidParameters() {
        // Mock configuration values
        when(config.getWfHost()).thenReturn("http://example.com");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/workflow/processInstance");

        // Test input parameters
        String tenantId = "tenant1";
        String businessService = "businessService1";

        // Expected URL construction
        String expectedURL = "http://example.com/workflow/processInstance?tenantId=tenant1&businessIds=businessService1";

        // Call the method to test
        StringBuilder result = workflowService.getSearchURLForProcessInstanceWithParams(tenantId, businessService);

        // Verify that the URL construction is correct
        assertEquals(expectedURL, result.toString());
    }
    @Test
    void getWorkflowFromProcessInstance_ValidProcessInstance() {
        // Test when processInstance has valid data
        State state = new State();
        state.setState("APPROVED");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(state);
        processInstance.setComment("Test comment");

        Workflow result = workflowService.getWorkflowFromProcessInstance(processInstance);

        assertNotNull(result);
        assertEquals("APPROVED", result.getAction());
        assertEquals("Test comment", result.getComments());
    }
    @Test
    void getCurrentWorkflow_ValidResponse() {
        // Mock inputs
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String businessId = "businessId1";

        // Mock config behavior
        when(config.getWfHost()).thenReturn("http://example.com");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/searchPath");

        // Mock response
        ProcessInstance processInstance = new ProcessInstance();
        ProcessInstanceResponse response = new ProcessInstanceResponse();
        response.setProcessInstances(Collections.singletonList(processInstance));

        // Mock repository response
        when(repository.fetchResult(any(StringBuilder.class), any(RequestInfoWrapper.class))).thenReturn(new Object());
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(response);

        // Execute the method
        ProcessInstance result = workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);

        // Verify the result
        assertNotNull(result);
        assertEquals(processInstance, result);
    }

    @Test
    void getWorkflowFromProcessInstance_ProcessInstanceWithNullComment() {
        // Test when processInstance has null comment
        State state = new State();
        state.setState("APPROVED");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(state);
        processInstance.setComment(null);

        Workflow result = workflowService.getWorkflowFromProcessInstance(processInstance);

        assertNotNull(result);
        assertEquals("APPROVED", result.getAction());
        assertNull(result.getComments()); // Comment is null, so comments should be null
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
        ProcessInstance processInstance = workflowService.getProcessInstanceForArtifact(artifact);

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
        ProcessInstance processInstance = workflowService.getProcessInstanceForArtifact(artifact);

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
    @Test
    public void testGetProcessInstanceForArtifact_CustomException() {
        // Mock Artifact and Workflow
        Artifact artifact = mock(Artifact.class);
        Workflow workflow = mock(Workflow.class);
        when(artifact.getWorkflow()).thenReturn(workflow);
        when(workflow.getAction()).thenThrow(new CustomException("CUSTOM_ERROR", "Custom exception occurred"));

        // Invoke the method and verify the exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            workflowService.getProcessInstanceForArtifact(artifact);
        });

        assertEquals("CUSTOM_ERROR", exception.getCode());
        assertEquals("Custom exception occurred", exception.getMessage());
    }

    @Test
    public void testGetProcessInstanceForArtifact_GeneralException() {
        // Mock Artifact and Workflow
        Artifact artifact = mock(Artifact.class);
        Workflow workflow = mock(Workflow.class);
        when(artifact.getWorkflow()).thenReturn(workflow);
        when(workflow.getAction()).thenThrow(new RuntimeException("General exception occurred"));

        // Invoke the method and verify the exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            workflowService.getProcessInstanceForArtifact(artifact);
        });

        assertEquals("WORKFLOW_SERVICE_EXCEPTION", exception.getCode());
        assertEquals("java.lang.RuntimeException: General exception occurred", exception.getMessage());
    }
}
