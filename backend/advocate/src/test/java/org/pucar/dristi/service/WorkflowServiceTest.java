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
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;

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
        // Mock AdvocateRequest
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");
        advocate.setWorkflow(Workflow.builder().action("APPROVE").build());

        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocates(Collections.singletonList(advocate));

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
    void getProcessInstanceForAdvocateRegistrationPayment_Success() {
        // Mock AdvocateRequest
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("APP001");
        advocate.setTenantId("tenant1");

        AdvocateRequest updateRequest = new AdvocateRequest();
        updateRequest.setRequestInfo(new RequestInfo());
        updateRequest.setAdvocates(Collections.singletonList(advocate));

        // Execute the method
        ProcessInstanceRequest processInstanceRequest = workflowService.getProcessInstanceForAdvocateRegistrationPayment(updateRequest);

        // Assertions
        assertNotNull(processInstanceRequest);
        assertEquals(1, processInstanceRequest.getProcessInstances().size());
        assertEquals("ADV", processInstanceRequest.getProcessInstances().get(0).getBusinessService());
    }
}
