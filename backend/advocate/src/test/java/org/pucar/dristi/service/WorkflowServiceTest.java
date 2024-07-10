package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.ProcessInstance;
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
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class WorkflowServiceTest {

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
        // Mock AdvocateRequest
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").assignes(list).build());

        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocate(advocate);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(advocateRequest));
    }

    @Test
    void updateWorkflowStatus_CustomException() {
        // Mock AdvocateRequest
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").build());

        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocate(advocate);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException());

        // Execute the method
        assertThrows(CustomException.class, () -> {workflowService.updateWorkflowStatus(advocateRequest);
        });
    }

    @Test
    void updateWorkflowStatus_Exception() {
        // Mock AdvocateRequest
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").build());

        AdvocateRequest advocateRequest = null;
        // Execute the method
        assertThrows(Exception.class, () -> {workflowService.updateWorkflowStatus(advocateRequest);
        });
    }

    @Test
    void updateWorkflowStatusClerk_Success() {
        // Mock AdvocateRequest
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").assignes(list).build());

        AdvocateClerkRequest advocateRequest = new AdvocateClerkRequest();
        advocateRequest.setClerk(advocate);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(advocateRequest));
    }

    @Test
    void updateWorkflowStatusClerk_CustomException() {
        // Mock AdvocateRequest
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").assignes(list).build());

        AdvocateClerkRequest advocateRequest = new AdvocateClerkRequest();
        advocateRequest.setClerk(advocate);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException());

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            workflowService.updateWorkflowStatus(advocateRequest);
        });
    }

    @Test
    void updateWorkflowStatusClerk_Exception() {
        // Mock AdvocateRequest
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").assignes(list).build());

        AdvocateClerkRequest advocateRequest = new AdvocateClerkRequest();
        advocateRequest.setClerk(null);

        assertThrows(Exception.class, () -> {
            workflowService.updateWorkflowStatus(advocateRequest);
        });
    }

    @Test
    void getCurrentWorkflow_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String businessId = "business1";
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        processInstanceResponse.setProcessInstances(Collections.singletonList(processInstance));
        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/workflow/transition");

        when(repository.fetchResult(any(), any())).thenReturn(processInstanceResponse);
        when(mapper.convertValue(processInstanceResponse, ProcessInstanceResponse.class)).thenReturn(processInstanceResponse);

        // Act
        ProcessInstance result = workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);

        // Assert
        assertNotNull(result);
        assertEquals(processInstance, result);
    }

    @Test
    void getCurrentWorkflow_NoProcessInstance() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String businessId = "business1";
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        processInstanceResponse.setProcessInstances(Collections.singletonList(processInstance));
        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/workflow/transition");

        when(repository.fetchResult(any(), any())).thenReturn(processInstanceResponse);

        // Act
        ProcessInstance result = workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentWorkflow_ProcessInstanceException() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String businessId = "business1";
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        processInstanceResponse.setProcessInstances(Collections.singletonList(processInstance));
        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/workflow/transition");

        when(repository.fetchResult(any(), any())).thenThrow(new RuntimeException("Internal error"));

        // Act and Assert
        assertThrows(Exception.class, () -> {
            workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);
        });
    }
}
