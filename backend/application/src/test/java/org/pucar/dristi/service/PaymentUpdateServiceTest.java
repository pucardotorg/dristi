package org.pucar.dristi.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.web.models.*;

import java.util.Collections;
import java.util.Map;

class PaymentUpdateServiceTest {

    @Mock
    private WorkflowService workflowService;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ApplicationRepository repository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private PaymentUpdateService paymentUpdateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock configurations
        when(configuration.getAsyncOrderSubBusinessServiceName()).thenReturn("async-order-submission-managelifecycle");
        when(configuration.getAsyncOrderSubWithResponseBusinessServiceName()).thenReturn("async-submission-with-response-managelifecycle");
        when(configuration.getAsyncVoluntarySubBusinessServiceName()).thenReturn("async-voluntary-submission-managelifecycle");

        when(configuration.getAsyncOrderSubBusinessName()).thenReturn("async-order-submission");
        when(configuration.getAsyncOrderSubWithResponseBusinessName()).thenReturn("async-order-submission-with-response");
        when(configuration.getAsyncVoluntarySubBusinessName()).thenReturn("async-voluntary-submission");
        when(configuration.getApplicationUpdateStatusTopic()).thenReturn("update-application-status-application");
    }

    @Test
    void testProcess_ValidBusinessService_Success() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest();
        RequestInfo requestInfo = new RequestInfo();
        PaymentDetail paymentDetail = new PaymentDetail();
        Bill bill = new Bill();
        bill.setConsumerCode("12345_suffix");
        paymentDetail.setBill(bill);
        paymentDetail.setBusinessService("async-order-submission-managelifecycle");
        paymentRequest.setRequestInfo(requestInfo);
        paymentRequest.setPayment(new Payment());
        paymentRequest.getPayment().setPaymentDetails(Collections.singletonList(paymentDetail));
        paymentRequest.getPayment().setTenantId("tenantId");

        Application application = new Application();
        application.setStatus("PENDING");
        application.setApplicationNumber("12345");

        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(requestInfo);
        applicationSearchRequest.setCriteria(ApplicationCriteria.builder()
                .applicationNumber("12345")
                .build());

        // Mock behavior
        when(mapper.convertValue(any(Map.class), eq(PaymentRequest.class))).thenReturn(paymentRequest);
        when(repository.getApplications(applicationSearchRequest)).thenReturn(Collections.singletonList(application));

        ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        when(workflowService.getProcessInstanceForApplicationPayment(any(ApplicationSearchRequest.class), anyString(), anyString()))
                .thenReturn(processInstanceRequest);

        State state = new State();
        state.setState("APPROVED");
        when(workflowService.callWorkFlow(processInstanceRequest)).thenReturn(state);

        // When
        paymentUpdateService.process(Collections.emptyMap());

        // Then
        // Verify that producer.push is not called with specific parameters
        verify(producer, never()).push(eq("update-application-status-application"), any(Application.class));

        // Ensure no more interactions with mocks
        verifyNoMoreInteractions(repository, workflowService, producer);
    }






    @Test
    void testProcess_InvalidBusinessService() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest();
        RequestInfo requestInfo = new RequestInfo();
        PaymentDetail paymentDetail = new PaymentDetail();
        Bill bill = new Bill();
        bill.setConsumerCode("12345_suffix");
        paymentDetail.setBill(bill);
        paymentDetail.setBusinessService("invalid-business-service");
        paymentRequest.setRequestInfo(requestInfo);
        paymentRequest.setPayment(new Payment());
        paymentRequest.getPayment().setPaymentDetails(Collections.singletonList(paymentDetail));
        paymentRequest.getPayment().setTenantId("tenantId");

        when(mapper.convertValue(any(Map.class), eq(PaymentRequest.class))).thenReturn(paymentRequest);

        // When
        paymentUpdateService.process(Collections.emptyMap());

        // Then
        verifyNoInteractions(repository, workflowService, producer);
    }


    @Test
    void testUpdateWorkflowForApplicationPayment_Exception() {
        // Given
        RequestInfo requestInfo = new RequestInfo();
        PaymentDetail paymentDetail = new PaymentDetail();
        Bill bill = new Bill();
        bill.setConsumerCode("12345_suffix");
        paymentDetail.setBill(bill);
        paymentDetail.setBusinessService("async-order-submission-managelifecycle");
        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(requestInfo);
        applicationSearchRequest.setCriteria(ApplicationCriteria.builder()
                .applicationNumber("12345")
                .build());

        when(repository.getApplications(applicationSearchRequest)).thenThrow(new RuntimeException("DB error"));

        // When
        paymentUpdateService.updateWorkflowForApplicationPayment(requestInfo, "tenantId", paymentDetail);

        // Then
        verifyNoInteractions(workflowService, producer);
    }
}
