package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.workflow.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class PaymentUpdateService {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CaseRepository repository;

    public void process(HashMap<String, Object> record) {

        try {

            PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
            RequestInfo requestInfo = paymentRequest.getRequestInfo();

            List<PaymentDetail> paymentDetails = paymentRequest.getPayment().getPaymentDetails();
            String tenantId = paymentRequest.getPayment().getTenantId();

            for (PaymentDetail paymentDetail : paymentDetails) {
                updateWorkflowForBirthRegistrationPayment(requestInfo, tenantId, paymentDetail);
            }
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
        }

    }

    private void updateWorkflowForBirthRegistrationPayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {

        Bill bill  = paymentDetail.getBill();

        CaseCriteria criteria = CaseCriteria.builder()
                .filingNumber(bill.getConsumerCode())
//                .tenantId(tenantId)
                .build();
        List<CaseCriteria> criterias = new ArrayList<>();
        criterias.add(criteria);
        List<CaseCriteria> caseCriterias = repository.getApplications(criterias);

        if (CollectionUtils.isEmpty(caseCriterias))
            throw new CustomException("INVALID RECEIPT",
                    "No applications found for the consumerCode " + criteria.getFilingNumber());

        Role role = Role.builder().code("SYSTEM_PAYMENT").tenantId(tenantId).build();
        requestInfo.getUserInfo().getRoles().add(role);

        caseCriterias.forEach(caseCriteria -> {

            CaseSearchRequest updateRequest = CaseSearchRequest.builder().requestInfo(requestInfo)
                    .criteria(Collections.singletonList(caseCriteria)).build();

            ProcessInstanceRequest wfRequest = workflowService.getProcessInstanceForCasePayment(updateRequest,tenantId);

            State state = workflowService.callWorkFlow(wfRequest);

        });
    }

}