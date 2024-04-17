//package org.pucar.service;
//
//import org.egov.common.contract.request.RequestInfo;
//import org.egov.common.contract.workflow.ProcessInstanceRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.pucar.repository.ServiceRequestRepository;
//import org.pucar.web.models.Advocate;
//import org.pucar.web.models.AdvocateRequest;
//import java.util.Collections;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class WorkflowServiceTest {
//
//    @InjectMocks
//    private WorkflowService workflowService;
//
//    @Mock
//    private ServiceRequestRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void updateWorkflowStatus_Success() {
//        // Mock AdvocateRequest
//        Advocate advocate = new Advocate();
//        advocate.setApplicationNumber("APP001");
//        advocate.setTenantId("tenant1");
//
//        AdvocateRequest advocateRequest = new AdvocateRequest();
//        advocateRequest.setAdvocates(Collections.singletonList(advocate));
//
//        // Mock repository.fetchResult
//        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(new Object());
//
//        // Execute the method
//        assertDoesNotThrow(() -> workflowService.updateWorkflowStatus(advocateRequest));
//    }
//
//    @Test
//    void callWorkFlow_Success() {
//        // Mock ProcessInstanceRequest
//        ProcessInstanceRequest request = new ProcessInstanceRequest();
//        request.setRequestInfo(new RequestInfo());
//
//        // Mock repository.fetchResult
//        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(new Object());
//
//        // Execute the method
//        assertDoesNotThrow(() -> workflowService.callWorkFlow(request));
//    }
//
//    @Test
//    void getProcessInstanceForAdvocateRegistrationPayment_Success() {
//        // Mock AdvocateRequest
//        Advocate advocate = new Advocate();
//        advocate.setApplicationNumber("APP001");
//        advocate.setTenantId("tenant1");
//
//        AdvocateRequest updateRequest = new AdvocateRequest();
//        updateRequest.setRequestInfo(new RequestInfo());
//        updateRequest.setAdvocates(Collections.singletonList(advocate));
//
//        // Execute the method
//        ProcessInstanceRequest processInstanceRequest = workflowService.getProcessInstanceForAdvocateRegistrationPayment(updateRequest);
//
//        // Assertions
//        assertNotNull(processInstanceRequest);
//        assertEquals(1, processInstanceRequest.getProcessInstances().size());
//        assertEquals("ADV", processInstanceRequest.getProcessInstances().get(0).getBusinessService());
//    }
//}
