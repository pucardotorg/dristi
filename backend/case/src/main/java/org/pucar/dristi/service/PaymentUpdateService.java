package org.pucar.dristi.service;


import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.web.models.Bill;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.PaymentDetail;
import org.pucar.dristi.web.models.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentUpdateService {

    private WorkflowService workflowService;

    private ObjectMapper mapper;

    private CaseRepository repository;

    private Producer producer;

    private Configuration configuration;

    @Autowired
    public PaymentUpdateService(WorkflowService workflowService, ObjectMapper mapper, CaseRepository repository, Producer producer, Configuration configuration) {
        this.workflowService = workflowService;
        this.mapper = mapper;
        this.repository = repository;
        this.producer = producer;
        this.configuration = configuration;
    }

    public void process(Map<String, Object> record) {

        try {

            PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
            RequestInfo requestInfo = paymentRequest.getRequestInfo();

            List<PaymentDetail> paymentDetails = paymentRequest.getPayment().getPaymentDetails();
            String tenantId = paymentRequest.getPayment().getTenantId();

            for (PaymentDetail paymentDetail : paymentDetails) {
                updateWorkflowForCasePayment(requestInfo, tenantId, paymentDetail);
            }
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
        }

    }

    private void updateWorkflowForCasePayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {

        Bill bill  = paymentDetail.getBill();

        CaseCriteria criteria = CaseCriteria.builder()
                .filingNumber(bill.getConsumerCode())
//                .tenantId(tenantId)
                .build();
        List<CaseCriteria> criterias = new ArrayList<>();
        criterias.add(criteria);
        List<CaseCriteria> caseCriterias = repository.getApplications(criterias, requestInfo);

        if (CollectionUtils.isEmpty(caseCriterias.get(0).getResponseList()))
            throw new CustomException("INVALID RECEIPT",
                    "No applications found for the consumerCode " + criteria.getFilingNumber());

        Role role = Role.builder().code("SYSTEM_ADMIN").tenantId(tenantId).build();
        requestInfo.getUserInfo().getRoles().add(role);

        caseCriterias.forEach(caseCriteria -> {

            CaseSearchRequest updateRequest = CaseSearchRequest.builder().requestInfo(requestInfo)
                    .criteria(Collections.singletonList(caseCriteria)).build();

            ProcessInstanceRequest wfRequest = workflowService.getProcessInstanceForCasePayment(updateRequest,tenantId);

            State state = workflowService.callWorkFlow(wfRequest);

            CourtCase courtCase = updateRequest.getCriteria().get(0).getResponseList().get(0);
            courtCase.setStatus(state.getState());
            AuditDetails auditDetails = courtCase.getAuditdetails();
            auditDetails.setLastModifiedBy(paymentDetail.getAuditDetails().getLastModifiedBy());
            auditDetails.setLastModifiedTime(paymentDetail.getAuditDetails().getLastModifiedTime());
            courtCase.setAuditdetails(auditDetails);
            producer.push(configuration.getCaseUpdateStatusTopic(),courtCase);
        });
    }

}