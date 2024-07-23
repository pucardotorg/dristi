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
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;

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