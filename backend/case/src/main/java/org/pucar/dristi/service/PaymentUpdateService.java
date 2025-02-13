package org.pucar.dristi.service;


import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.EncryptionDecryptionUtil;
import org.pucar.dristi.web.models.*;
import org.pucar.dristi.web.models.task.TaskRequest;
import org.pucar.dristi.web.models.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.FSO_VALIDATED;

@Slf4j
@Service
public class PaymentUpdateService {

    private WorkflowService workflowService;

    private ObjectMapper mapper;

    private CaseRepository repository;

    private Producer producer;

    private Configuration configuration;

    private CacheService cacheService;

    private NotificationService notificationService;

    private CaseService caseService;

    private CaseRegistrationEnrichment enrichmentUtil;

    private EncryptionDecryptionUtil encryptionDecryptionUtil;

    @Autowired
    public PaymentUpdateService(WorkflowService workflowService, ObjectMapper mapper, CaseRepository repository,
                                Producer producer, Configuration configuration, CacheService cacheService, CaseService caseService, CaseRegistrationEnrichment enrichmentUtil,EncryptionDecryptionUtil encryptionDecryptionUtil) {
        this.workflowService = workflowService;
        this.mapper = mapper;
        this.repository = repository;
        this.producer = producer;
        this.configuration = configuration;
        this.cacheService = cacheService;
        this.caseService = caseService;
        this.enrichmentUtil = enrichmentUtil;
        this.encryptionDecryptionUtil = encryptionDecryptionUtil;
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

    public void updateJoinCaseDetails(Map<String, Object> record) {

        try {
            TaskRequest taskRequest = mapper.convertValue(record, TaskRequest.class);
            Object additionalDetails = taskRequest.getTask().getAdditionalDetails();
            ObjectMapper objectMapper = new ObjectMapper();
            JoinCaseRequest joinCaseRequest  = objectMapper.convertValue(additionalDetails, JoinCaseRequest.class);
            caseService.verifyJoinCaseRequest(joinCaseRequest,true);
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
        }

    }

    private void updateWorkflowForCasePayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {

        Bill bill  = paymentDetail.getBill();

        String consumerCode = bill.getConsumerCode();
        String[] consumerCodeSplitArray = consumerCode.split("_", 2);
        String fillingNumber=consumerCodeSplitArray[0];

        CaseCriteria criteria = CaseCriteria.builder()
                .filingNumber(fillingNumber)
//                .tenantId(tenantId)
                .build();
        List<CaseCriteria> criterias = new ArrayList<>();
        criterias.add(criteria);
        List<CaseCriteria> caseCriterias = repository.getCases(criterias, requestInfo);

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
            enrichmentUtil.enrichCaseRegistrationFillingDate(courtCase);
            AuditDetails auditDetails = courtCase.getAuditdetails();
            auditDetails.setLastModifiedBy(paymentDetail.getAuditDetails().getLastModifiedBy());
            auditDetails.setLastModifiedTime(paymentDetail.getAuditDetails().getLastModifiedTime());
            courtCase.setAuditdetails(auditDetails);
            CourtCase decryptedCourtCase = encryptionDecryptionUtil.decryptObject(courtCase, configuration.getCaseDecryptSelf(), CourtCase.class, requestInfo);

            CaseRequest caseRequest = new CaseRequest();
            caseRequest.setRequestInfo(requestInfo);
            caseRequest.setCases(decryptedCourtCase);
            if(UNDER_SCRUTINY.equalsIgnoreCase(courtCase.getStatus())) {
                caseService.callNotificationService(caseRequest, CASE_PAYMENT_COMPLETED);
            }
            enrichmentUtil.enrichAccessCode(caseRequest);
            log.info("In Payment Update, Encrypting: {}", caseRequest.getCases().getId());
            caseRequest.setCases(encryptionDecryptionUtil.encryptObject(caseRequest.getCases(), configuration.getCourtCaseEncrypt(), CourtCase.class));

            producer.push(configuration.getCaseUpdateStatusTopic(),caseRequest);
            cacheService.save(requestInfo.getUserInfo().getTenantId() + ":" + courtCase.getId().toString(), caseRequest.getCases());

        });
    }


}