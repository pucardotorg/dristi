package org.pucar.dristi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.web.models.AuditDetails;
import org.pucar.dristi.web.models.Bill;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Payment;
import org.pucar.dristi.web.models.PaymentDetail;
import org.pucar.dristi.web.models.PaymentRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PaymentUpdateServiceTest {

    @Mock
    private WorkflowService workflowService;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private CaseRepository repository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private PaymentUpdateService paymentUpdateService;

    @Test
    void process_handlesPaymentRequestSuccessfully() {
        HashMap<String, Object> record = new HashMap<>();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPayment(new Payment());
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        ;
        user.setRoles(new ArrayList<Role>(Collections.singletonList(new Role())));
        requestInfo.setUserInfo(user);
        paymentRequest.setRequestInfo(requestInfo);
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setTenantId("tenantId");
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("createdBy");
        auditDetails.setCreatedTime(123456789L);
        auditDetails.setLastModifiedBy("lastModifiedBy");
        auditDetails.setLastModifiedTime(123456789L);
        paymentDetail.setAuditDetails(auditDetails);
        paymentRequest.getPayment().setPaymentDetails(Collections.singletonList(paymentDetail));
        Bill bill = new Bill();
        bill.setTenantId("tenantId");
        bill.setConsumerCode("consumerCode-suffix");
        paymentDetail.setBill(bill);

        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("filingNumber");
        org.egov.common.contract.models.AuditDetails auditDetails1 = new org.egov.common.contract.models.AuditDetails();
        auditDetails1.setLastModifiedBy("lastModifiedBy");
        auditDetails1.setLastModifiedTime(123456789L);
        auditDetails1.setCreatedBy("createdBy");
        auditDetails1.setCreatedTime(123456789L);
        courtCase.setAuditdetails(auditDetails1);

        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));


        when(mapper.convertValue(record, PaymentRequest.class)).thenReturn(paymentRequest);
        when(repository.getCases(any(), any())).thenReturn(Collections.singletonList(caseCriteria));
        when(configuration.getCaseUpdateStatusTopic()).thenReturn("kafkaUpdateTopic");

        when(workflowService.callWorkFlow(any())).thenReturn(new State());
        doNothing().when(producer).push(any(), any());

        paymentUpdateService.process(record);

        verify(workflowService, times(1)).getProcessInstanceForCasePayment(any(), any());
        verify(workflowService, times(1)).callWorkFlow(any());
    }

    @Test
    void process_logsErrorWhenExceptionIsThrown() {
        HashMap<String, Object> record = new HashMap<>();

        when(mapper.convertValue(record, PaymentRequest.class)).thenThrow(new RuntimeException());

        assertDoesNotThrow(() -> paymentUpdateService.process(record));
    }
}