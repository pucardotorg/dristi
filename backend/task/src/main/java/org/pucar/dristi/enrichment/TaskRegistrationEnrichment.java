package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class TaskRegistrationEnrichment {

    private final IdgenUtil idgenUtil;
    private final Configuration config;

    @Autowired
    public TaskRegistrationEnrichment(IdgenUtil idgenUtil, Configuration config) {
        this.idgenUtil = idgenUtil;
        this.config = config;
    }

    public void enrichTaskRegistration(TaskRequest taskRequest) {
        try {
            Task task = taskRequest.getTask();

            List<String> taskRegistrationIdList = idgenUtil.getIdList(taskRequest.getRequestInfo(), task.getTenantId(), config.getTaskNumber(), null, 1);
            log.info("Task Registration Id List :: {}", taskRegistrationIdList);
            AuditDetails auditDetails = AuditDetails.builder().createdBy(taskRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(taskRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            task.setAuditDetails(auditDetails);

            task.setId(UUID.randomUUID());

            if (task.getDocuments() != null) {
                task.getDocuments().forEach(document -> {
                    document.setId(String.valueOf(UUID.randomUUID()));
                    document.setDocumentUid(document.getId());
                });
            }
            task.getAmount().setId(UUID.randomUUID());
            task.setCreatedDate(System.currentTimeMillis());
            task.setTaskNumber(taskRegistrationIdList.get(0));

        } catch (Exception e) {
            log.error("Error enriching task application :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichCaseApplicationUponUpdate(TaskRequest taskRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            Task task = taskRequest.getTask();
            task.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            task.getAuditDetails().setLastModifiedBy(taskRequest.getRequestInfo().getUserInfo().getUuid());
            if (task.getDocuments() != null) {
                task.getDocuments().removeIf(document -> document.getId() != null);
                task.getDocuments().forEach(document -> {
                    if (document.getId() == null) {
                        document.setId(UUID.randomUUID().toString());
                        document.setDocumentUid(document.getId());
                    }
                });
            }

        } catch (Exception e) {
            log.error("Error enriching task application upon update :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Exception in task enrichment service during task update process: " + e.getMessage());
        }
    }
}