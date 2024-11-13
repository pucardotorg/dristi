package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentUpdateService {

    private final WorkflowService workflowService;
    private final ObjectMapper mapper;
    private final ApplicationRepository repository;
    private final Producer producer;
    private final Configuration configuration;
    private final List<String> allowedBusinessServices;
    @Autowired
    public PaymentUpdateService(WorkflowService workflowService, ObjectMapper mapper, ApplicationRepository repository, Producer producer, Configuration configuration) {
        this.workflowService = workflowService;
        this.mapper = mapper;
        this.repository = repository;
        this.producer = producer;
        this.configuration = configuration;
       this.allowedBusinessServices= Arrays.asList(
                configuration.getAsyncOrderSubBusinessServiceName(),
                configuration.getAsyncOrderSubWithResponseBusinessServiceName(),
                configuration.getAsyncVoluntarySubBusinessServiceName()
        );
    }

    public void process(Map<String, Object> record) {
        try {
            PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
            RequestInfo requestInfo = paymentRequest.getRequestInfo();
            List<PaymentDetail> paymentDetails = paymentRequest.getPayment().getPaymentDetails();
            String tenantId = paymentRequest.getPayment().getTenantId();

            for (PaymentDetail paymentDetail : paymentDetails) {
                if (allowedBusinessServices.contains(paymentDetail.getBusinessService())) {
                    updateWorkflowForApplicationPayment(requestInfo, tenantId, paymentDetail);
                }
            }
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR: {}", e.getMessage(), e);
        }
    }

    public void updateWorkflowForApplicationPayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {
        try {
            Bill bill = paymentDetail.getBill();
            String consumerCode = bill.getConsumerCode();
            String[] consumerCodeSplitArray = consumerCode.split("_", 2);
            String applicationNumber = consumerCodeSplitArray[0];
            ApplicationCriteria criteria = ApplicationCriteria.builder()
                    .applicationNumber(applicationNumber)
                    .build();
            ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
            applicationSearchRequest.setRequestInfo(requestInfo);
            applicationSearchRequest.setCriteria(criteria);

            List<Application> applications = repository.getApplications(applicationSearchRequest);

            if (CollectionUtils.isEmpty(applications)) {
                throw new CustomException("INVALID_RECEIPT", "No applications found for the consumerCode " + criteria.getFilingNumber());
            }

            Role role = Role.builder().code("SYSTEM_ADMIN").tenantId(tenantId).build();
            requestInfo.getUserInfo().getRoles().add(role);

            for (Application application : applications) {
                ApplicationSearchRequest updateRequest = ApplicationSearchRequest.builder()
                        .requestInfo(requestInfo)
                        .criteria(criteria)
                        .build();

                ProcessInstanceRequest wfRequest = workflowService.getProcessInstanceForApplicationPayment(updateRequest, tenantId, paymentDetail.getBusinessService());

                State state = workflowService.callWorkFlow(wfRequest);

                application.setStatus(state.getState());

                AuditDetails auditDetails = application.getAuditDetails();
                auditDetails.setLastModifiedBy(paymentDetail.getAuditDetails().getLastModifiedBy());
                auditDetails.setLastModifiedTime(paymentDetail.getAuditDetails().getLastModifiedTime());
                application.setAuditDetails(auditDetails);

                ApplicationRequest applicationRequest = new ApplicationRequest();
                applicationRequest.setApplication(application);
                applicationRequest.setRequestInfo(requestInfo);

                producer.push(configuration.getApplicationUpdateStatusTopic(), applicationRequest);
            }
        } catch (Exception e) {
            log.error("Error updating workflow for application payment: {}", e.getMessage(), e);
        }
    }

}
