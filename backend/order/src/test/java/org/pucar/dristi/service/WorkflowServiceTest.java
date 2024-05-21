package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class WorkflowServiceTest {

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateWorkflowStatus_Success() {
        Order order = new Order();
        order.setOrderNumber("ORDER123");
        Workflow workflow = new Workflow();
        workflow.setAction("ACTION");
        order.setWorkflow(workflow);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);
        orderRequest.setRequestInfo(new RequestInfo());

        when(config.getWfHost()).thenReturn("http://localhost:8080");
        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(new State());
        ProcessInstanceResponse workflowRequest = new ProcessInstanceResponse(new ResponseInfo(), Collections.singletonList(processInstance));

        // Mock repository.fetchResult
        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(workflowRequest);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(workflowRequest);

        // Execute the method
        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(orderRequest));
    }

    @Test
    public void testCallWorkFlow_success() {
        ProcessInstanceRequest workflowReq = new ProcessInstanceRequest();
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        State state = new State();
        state.setState("APPROVED");
        processInstance.setState(state);
        processInstanceResponse.setProcessInstances(Collections.singletonList(processInstance));

        when(config.getWfHost()).thenReturn("http://localhost");
        when(config.getWfTransitionPath()).thenReturn("/workflow/process/_transition");
        when(repository.fetchResult(any(), any())).thenReturn(processInstanceResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(processInstanceResponse);

        State result = workflowService.callWorkFlow(workflowReq);

        assertNotNull(result);
        assertEquals("APPROVED", result.getState());
        verify(repository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testGetCurrentWorkflow_success() {
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String businessId = "businessId";
        ProcessInstance processInstance = new ProcessInstance();
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        processInstanceResponse.setProcessInstances(Collections.singletonList(processInstance));

        when(config.getWfHost()).thenReturn("http://localhost");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/workflow/process/_search");
        when(repository.fetchResult(any(), any())).thenReturn(processInstanceResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(processInstanceResponse);

        ProcessInstance result = workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);

        assertNotNull(result);
        verify(repository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testGetWorkflowFromProcessInstance_success() {
        ProcessInstance processInstance = new ProcessInstance();
        State state = new State();
        state.setState("APPROVED");
        processInstance.setState(state);
        processInstance.setComment("This is a comment");

        Workflow result = workflowService.getWorkflowFromProcessInstance(processInstance);

        assertNotNull(result);
        assertEquals("APPROVED", result.getAction());
        assertEquals("This is a comment", result.getComments());
    }
}
