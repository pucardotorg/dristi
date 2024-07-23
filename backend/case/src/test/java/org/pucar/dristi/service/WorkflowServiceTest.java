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
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;

import java.util.ArrayList;
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
    void updateWorkflowStatus_Success() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        courtCase.setWorkflow(Workflow.builder().action("APPROVE").assignes(list).build());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(caseRequest));
    }

    @Test
    void updateWorkflowStatus_CustomException() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        courtCase.setWorkflow(Workflow.builder().action("APPROVE").build());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException("Custom error", ""));

        // Execute the method
        assertThrows(CustomException.class, () -> {workflowService.updateWorkflowStatus(caseRequest);
        });
    }

    @Test
    void updateWorkflowStatus_Exception() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        courtCase.setWorkflow(Workflow.builder().action("APPROVE").build());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new RuntimeException());

        // Execute the method
        assertThrows(Exception.class, () -> {workflowService.updateWorkflowStatus(caseRequest);
        });
    }

    @Test
    void updateWorkflowStatus_ExceptionBis() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(null);
        courtCase.setWorkflow(Workflow.builder().action("APPROVE").build());

        // Execute the method
        assertThrows(Exception.class, () -> {workflowService.updateWorkflowStatus(caseRequest);
        });
    }

    @Test
    void getProcessInstanceForAdvocateRegistrationPayment_Success() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest updateRequest = new CaseRequest();
        updateRequest.setRequestInfo(new RequestInfo());
        updateRequest.setCases(courtCase);

        // Execute the method
        ProcessInstanceRequest processInstanceRequest = workflowService.getProcessInstanceRegistrationPayment(updateRequest);

        // Assertions
        assertNotNull(processInstanceRequest);
        assertEquals(1, processInstanceRequest.getProcessInstances().size());
        assertEquals("ADV", processInstanceRequest.getProcessInstances().get(0).getBusinessService());
    }

    @Test
    void getProcessInstanceForAdvocateRegistrationPayment_Exception() {
        // Mock AdvocateRequest
        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("APP001");
        courtCase.setTenantId("tenant1");

        CaseRequest updateRequest = new CaseRequest();
        updateRequest.setRequestInfo(new RequestInfo());
        updateRequest.setCases(null);

        assertThrows(Exception.class, () -> {workflowService.getProcessInstanceRegistrationPayment(updateRequest);
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