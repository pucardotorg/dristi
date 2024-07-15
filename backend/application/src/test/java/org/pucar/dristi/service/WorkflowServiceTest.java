package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
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
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;


import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.pucar.dristi.config.ServiceConstants.*;

@ExtendWith(MockitoExtension.class)
public class WorkflowServiceTest {

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @Mock
    private Application mockApplication;
    @Mock
    private ObjectMapper mapper;

    @Test
    void updateWorkflowStatus_Success() {
        // Mock AdvocateRequest
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setRequestInfo(new RequestInfo());
        Application application = new Application();
        application.setApplicationNumber("APP001");
        application.setTenantId("tenant1");
        application.setWorkflow(Workflow.builder().action("APPROVE").build());
        applicationRequest.setApplication(application);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(applicationRequest));
    }

    @Test
    void updateWorkflowStatus_Exceptions() {
        // Mock EvidenceRequest
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setRequestInfo(new RequestInfo());
        Application application = new Application();
        application.setApplicationNumber("APP001");
        application.setTenantId("tenant1");
        application.setWorkflow(Workflow.builder().action("APPROVE").build());
        applicationRequest.setApplication(application);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        // Mock repository.fetchResult to throw CustomException
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException());

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.updateWorkflowStatus(applicationRequest));

        // Mock repository.fetchResult to throw generic Exception
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new RuntimeException("Generic Exception"));

        // Execute the method
        assertThrows(CustomException.class, () -> workflowService.updateWorkflowStatus(applicationRequest));
    }
    @Test
    public void testSetBusinessServiceAccordingToWorkflow_ReferenceIdIsNull() {
        // Arrange
        when(mockApplication.getReferenceId()).thenReturn(null);

        // Act
        String result = workflowService.getBusinessServiceFromAppplication(mockApplication);

        // Assert
        assertEquals(ASYNC_VOLUNTARY_SUBMISSION_SERVICES, result);
    }

    @Test
    public void testSetBusinessServiceAccordingToWorkflow_ResponseRequiredIsTrue() {
        // Arrange
        when(mockApplication.getReferenceId()).thenReturn(UUID.fromString("577392d1-b0f3-4ff8-aead-52cd58f61da2"));
        when(mockApplication.isResponseRequired()).thenReturn(true);

        // Act
        String result = workflowService.getBusinessServiceFromAppplication(mockApplication);

        // Assert
        assertEquals(ASYNSUBMISSIONWITHRESPONSE, result);
    }

    @Test
    public void testSetBusinessServiceAccordingToWorkflow_ResponseRequiredIsFalse() {
        // Arrange
        when(mockApplication.getReferenceId()).thenReturn(UUID.fromString("577392d1-b0f3-4ff8-aead-52cd58f61da2"));
        when(mockApplication.isResponseRequired()).thenReturn(false);

        // Act
        String result = workflowService.getBusinessServiceFromAppplication(mockApplication);

        // Assert
        assertEquals(ASYNCSUBMISSIONWITHOUTRESPONSE, result);
    }
    @Test
    void updateWorkflowStatus_Exception() {
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setRequestInfo(new RequestInfo());
        Application application = new Application();
        application.setApplicationNumber("APP001");
        application.setTenantId("tenant1");
        application.setWorkflow(Workflow.builder().action("APPROVE").build());
        applicationRequest.setApplication(application);

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new RuntimeException());

        // Execute the method
        assertThrows(Exception.class, () -> {workflowService.updateWorkflowStatus(applicationRequest);
        });
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


}