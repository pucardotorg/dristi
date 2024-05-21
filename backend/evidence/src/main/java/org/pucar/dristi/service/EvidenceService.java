package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.EVIDENCE_CREATE_EXCEPTION;
@Slf4j
@Service
public class EvidenceService {
    @Autowired
    private EvidenceValidator validator;
    @Autowired
    private EvidenceEnrichment enrichmentUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private Producer producer;
    @Autowired
    private Configuration config;
    public List<Artifact> createEvidence(EvidenceRequest body) {
        try {

            // Validate applications
            validator.validateEvidenceRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichEvidenceRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getEvidenceCreateTopic(), body);

            return body.getArtifacts();
        } catch (CustomException e){
            log.error("Custom Exception occurred while creating evidence");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating evidence");
            throw new CustomException(EVIDENCE_CREATE_EXCEPTION,e.getMessage());
        }
    }
}
