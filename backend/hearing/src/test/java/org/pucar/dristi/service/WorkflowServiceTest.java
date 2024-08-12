package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class WorkflowServiceTest {

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateWorkflowStatus_Success() {
        // Mock AdvocateRequest
        HearingRequest hearingRequest = createHearingRequest();

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(State.builder().state("CREATE").build());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(hearingRequest));
        // Assert
        assertNotNull(hearingRequest.getHearing().getStatus());
        assertEquals("CREATE",hearingRequest.getHearing().getStatus());
    }


    @Test
    void updateWorkflowStatus_Exception() {
        // Arrange
        HearingRequest hearingRequest = createHearingRequest();

        // Act & Assert
        assertThrows(Exception.class, () -> workflowService.updateWorkflowStatus(hearingRequest));
    }
    @Test
    void updateWorkflowStatus_CustomException() {
        // Mock AdvocateRequest
        HearingRequest hearingRequest = createHearingRequest();

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenThrow(new CustomException());

        // Execute the method
        assertThrows(CustomException.class, () -> {workflowService.updateWorkflowStatus(hearingRequest);
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

    // Helper method to create a HearingRequest for testing
    private HearingRequest createHearingRequest() {
        Hearing hearing = new Hearing();
        hearing.setHearingId("Hearing001");
        hearing.setTenantId("tenant1");
        List<String> list = new ArrayList<>();
        list.add("assigne1");
        hearing.setWorkflow(Workflow.builder().action("CREATE").assignes(list).build());
        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(new RequestInfo());
        hearingRequest.setHearing(hearing);
        return hearingRequest;
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
