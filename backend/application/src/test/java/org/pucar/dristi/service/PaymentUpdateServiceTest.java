package org.pucar.dristi.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.web.models.*;

public class PaymentUpdateServiceTest {

    @InjectMocks
    private PaymentUpdateService paymentUpdateService;

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

    @Mock
    private ApplicationSearchRequest applicationSearchRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessSuccessful() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        RequestInfo requestInfo = new RequestInfo();
        paymentRequest.setRequestInfo(requestInfo);

        PaymentDetail paymentDetail = new PaymentDetail();
        Bill bill = new Bill();
        bill.setConsumerCode("12345");
        paymentDetail.setBill(bill);

        paymentRequest.setPayment(new Payment());
        paymentRequest.getPayment().setPaymentDetails(Collections.singletonList(paymentDetail));
        paymentRequest.getPayment().setTenantId("tenant1");

        Application application = new Application();
        application.setStatus("Pending");
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setLastModifiedBy("admin");
        auditDetails.setLastModifiedTime(System.currentTimeMillis());
        application.setAuditDetails(auditDetails);

        ApplicationCriteria criteria = ApplicationCriteria.builder()
                .filingNumber("12345")
                .build();

        List<Application> applications = Arrays.asList(application);

        when(mapper.convertValue(any(Map.class), eq(PaymentRequest.class))).thenReturn(paymentRequest);
        when(configuration.getAsyncOrderSubBusinessServiceName()).thenReturn("order");
        when(configuration.getAsyncOrderSubWithResponseBusinessServiceName()).thenReturn("orderResponse");
        when(configuration.getAsyncVoluntarySubBusinessServiceName()).thenReturn("voluntary");
        when(repository.getApplications(any(ApplicationSearchRequest.class))).thenReturn(applications);

        ProcessInstanceRequest wfRequest = new ProcessInstanceRequest();
        when(workflowService.getProcessInstanceForApplicationPayment(any(ApplicationSearchRequest.class), anyString(), anyString()))
                .thenReturn(wfRequest);

        State state = new State();
        state.setState("Approved");
        when(workflowService.callWorkFlow(any(ProcessInstanceRequest.class))).thenReturn(state);

        // Act
        paymentUpdateService.process(Collections.emptyMap());

    }
    @Test
    void testUpdateWorkflowForApplicationPaymentException() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        PaymentDetail paymentDetail = new PaymentDetail();
        Bill bill = new Bill();
        bill.setConsumerCode("12345");
        paymentDetail.setBill(bill);

        ApplicationCriteria criteria = ApplicationCriteria.builder()
                .filingNumber("12345")
                .build();

        when(repository.getApplications(any(ApplicationSearchRequest.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        paymentUpdateService.updateWorkflowForApplicationPayment(requestInfo, "tenant1", paymentDetail);

    }
}

