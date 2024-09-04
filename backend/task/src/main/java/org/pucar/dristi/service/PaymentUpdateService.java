package org.pucar.dristi.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.WorkflowUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Service
public class PaymentUpdateService {

    private final WorkflowUtil workflowUtil;

    private ObjectMapper mapper;

    private final TaskRepository repository;

    private Producer producer;

    private Configuration config;

    @Autowired
    public PaymentUpdateService(WorkflowUtil workflowUtil, ObjectMapper mapper, TaskRepository repository, Producer producer, Configuration config) {
        this.workflowUtil = workflowUtil;
        this.mapper = mapper;
        this.repository = repository;
        this.producer = producer;
        this.config = config;
    }

    public void process(Map<String, Object> record) {

        try {

            PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
            RequestInfo requestInfo = paymentRequest.getRequestInfo();

            List<PaymentDetail> paymentDetails = paymentRequest.getPayment().getPaymentDetails();
            String tenantId = paymentRequest.getPayment().getTenantId();

            for (PaymentDetail paymentDetail : paymentDetails) {
                if (paymentDetail.getBusinessService().equalsIgnoreCase(config.getTaskSummonBusinessServiceName())) {
                    updateWorkflowForCasePayment(requestInfo, tenantId, paymentDetail);
                }
            }
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
        }

    }

    private void updateWorkflowForCasePayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {

        try {
            Bill bill = paymentDetail.getBill();

            TaskCriteria criteria = TaskCriteria.builder()
                    .taskNumber(bill.getConsumerCode())
                    .build();

            List<Task> tasks = repository.getApplications(criteria ,null);

            if (CollectionUtils.isEmpty(tasks)) {
                throw new CustomException("INVALID_RECEIPT", "No Tasks found for the consumerCode " + criteria.getTaskNumber());
            }

            Role role = Role.builder().code("SYSTEM_ADMIN").tenantId(tenantId).build();
            requestInfo.getUserInfo().getRoles().add(role);

            for (Task task : tasks) {
                log.info("Updating pending payment status for task: {}", task);
                if (task.getTaskType().equals(SUMMON)) {
                    Workflow workflow = new Workflow();
                    workflow.setAction("MAKE PAYMENT");
                    task.setWorkflow(workflow);
                    String status = workflowUtil.updateWorkflowStatus(requestInfo, tenantId, task.getTaskNumber(),
                            config.getTaskSummonBusinessServiceName(), workflow, config.getTaskSummonBusinessName());
                    task.setStatus(status);

                    TaskRequest taskRequest = TaskRequest.builder().requestInfo(requestInfo).task(task).build();
                    if (ISSUESUMMON.equalsIgnoreCase(status))
                        producer.push(config.getTaskIssueSummonTopic(), taskRequest);

                    producer.push(config.getTaskUpdateTopic(), taskRequest);
                }
            }
        } catch (Exception e) {
            log.error("Error updating workflow for task payment: {}", e.getMessage(), e);
        }
    }

}