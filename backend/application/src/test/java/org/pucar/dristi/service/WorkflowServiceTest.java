//package org.pucar.dristi.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.egov.common.contract.models.Workflow;
//import org.egov.common.contract.request.RequestInfo;
//import org.egov.common.contract.workflow.ProcessInstance;
//import org.egov.common.contract.workflow.ProcessInstanceRequest;
//import org.egov.common.contract.workflow.ProcessInstanceResponse;
//import org.egov.common.contract.workflow.State;
//import org.egov.tracer.model.CustomException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.pucar.dristi.config.Configuration;
//import org.pucar.dristi.repository.ServiceRequestRepository;
//import org.pucar.dristi.web.models.Application;
//import org.pucar.dristi.web.models.ApplicationRequest;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class WorkflowServiceTest {
//
//    @InjectMocks
//    private WorkflowService workflowService;
//
//    @Mock
//    private ObjectMapper mapper;
//
//    @Mock
//    private ServiceRequestRepository repository;
//
//    @Mock
//    private Configuration config;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize any required objects here if necessary
//    }
//
//    @Test
//    public void testUpdateWorkflowStatus_Success() {
//        // Prepare test data
//        ApplicationRequest applicationRequest = mock(ApplicationRequest.class);
//        Application application = mock(Application.class);
//        RequestInfo requestInfo = mock(RequestInfo.class);
//        ProcessInstance processInstance = mock(ProcessInstance.class);
//        ProcessInstanceRequest workflowRequest = mock(ProcessInstanceRequest.class);
//        ProcessInstanceResponse processInstanceResponse = mock(ProcessInstanceResponse.class);
//        State state = mock(State.class);
//
//        when(applicationRequest.getApplication()).thenReturn(application);
//        when(applicationRequest.getRequestInfo()).thenReturn(requestInfo);
//        when(application.getApplicationNumber()).thenReturn("appNumber");
//        when(application.getWorkflow()).thenReturn(new Workflow());
//        when(application.getTenantId()).thenReturn("tenantId");
//        when(config.getWfHost()).thenReturn("http://localhost");
//        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");
//        when(repository.fetchResult(any(), any())).thenReturn(Collections.singletonMap("key", "value"));
//        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(processInstanceResponse);
//        when(processInstanceResponse.getProcessInstances()).thenReturn(Collections.singletonList(processInstance));
//        when(processInstance.getState()).thenReturn(state);
//        when(state.getApplicationStatus()).thenReturn("Approved");
//
//        // Call method under test
//        workflowService.updateWorkflowStatus(applicationRequest);
//
//        // Verify interactions and state
//        verify(application).setStatus("Approved");
//        verify(repository).fetchResult(any(), any());
//    }
//
//    @Test
//    public void testUpdateWorkflowStatus_ThrowsCustomException() {
//        // Prepare test data
//        ApplicationRequest applicationRequest = mock(ApplicationRequest.class);
//        Application application = mock(Application.class);
//
//        when(applicationRequest.getApplication()).thenReturn(application);
//        when(application.getWorkflow()).thenReturn(new Workflow());
//
//        doThrow(new CustomException("WORKFLOW_SERVICE_EXCEPTION", "Error calling workflow")).when(repository).fetchResult(any(), any());
//
//        // Verify exception is thrown
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            workflowService.updateWorkflowStatus(applicationRequest);
//        });
//
//        assertEquals("WORKFLOW_SERVICE_EXCEPTION", exception.getCode());
//    }
//
//    @Test
//    public void testCallWorkFlow_Success() {
//        // Prepare test data
//        ProcessInstanceRequest workflowRequest = mock(ProcessInstanceRequest.class);
//        ProcessInstanceResponse processInstanceResponse = mock(ProcessInstanceResponse.class);
//        State state = mock(State.class);
//
//        when(config.getWfHost()).thenReturn("http://localhost");
//        when(config.getWfTransitionPath()).thenReturn("/workflow/transition");
//        when(repository.fetchResult(any(), any())).thenReturn(Collections.singletonMap("key", "value"));
//        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(processInstanceResponse);
//        when(processInstanceResponse.getProcessInstances()).thenReturn(Collections.singletonList(new ProcessInstance(state)));
//
//        // Call method under test
//        State resultState = workflowService.callWorkFlow(workflowRequest);
//
//        // Verify interactions and state
//        assertNotNull(resultState);
//        verify(repository).fetchResult(any(), any());
//    }
////
////    @Test
////    public void testGetProcessInstance_Success() {
////        // Prepare test data
////        Application application = mock(Application.class);
////        RequestInfo requestInfo = mock(RequestInfo.class);
////        Workflow workflow = mock(Workflow.class);
////
////        when(application.getApplicationNumber()).thenReturn("appNumber");
////        when(application.getTenantId()).thenReturn("tenantId");
////        when(application.getWorkflow()).thenReturn(workflow);
////        when(workflow.getAction()).thenReturn("action");
////        when(workflow.getDocuments()).thenReturn(Collections.emptyList());
////        when(workflow.getComments()).thenReturn("comments");
////        when(workflow.getAssignes()).thenReturn(Collections.emptyList());
////
////        // Call method under test
////        ProcessInstance processInstance = workflowService.getProcessInstance(application, requestInfo);
////
////        // Verify interactions and state
////        assertNotNull(processInstance);
////        assertEquals("appNumber", processInstance.getBusinessId());
////        assertEquals("action", processInstance.getAction());
////        assertEquals("pucar", processInstance.getModuleName());
////        assertEquals("tenantId", processInstance.getTenantId());
////        assertEquals("application", processInstance.getBusinessService());
////        assertEquals("comments", processInstance.getComment());
////        assertTrue(CollectionUtils.isEmpty(processInstance.getAssignes()));
////    }
//}
