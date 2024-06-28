package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.EvidenceSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Service
public class EvidenceService {
    private final EvidenceValidator validator;
    private final EvidenceEnrichment evidenceEnrichment;
    private final WorkflowService workflowService;
    private final EvidenceRepository repository;
    private final Producer producer;
    private final Configuration config;
    @Autowired
    public EvidenceService(EvidenceValidator validator, EvidenceEnrichment evidenceEnrichment,
                           WorkflowService workflowService, EvidenceRepository repository,
                           Producer producer, Configuration config) {
        this.validator = validator;
        this.evidenceEnrichment = evidenceEnrichment;
        this.workflowService = workflowService;
        this.repository = repository;
        this.producer = producer;
        this.config = config;
    }
    public Artifact createEvidence(EvidenceRequest body) {
        try {

            // Validate applications
            validator.validateEvidenceRegistration(body);

            // Enrich applications
            evidenceEnrichment.enrichEvidenceRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getEvidenceCreateTopic(), body);

            return body.getArtifact();
        } catch (CustomException e){
            log.error("Custom Exception occurred while creating evidence");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating evidence");
            throw new CustomException(EVIDENCE_CREATE_EXCEPTION,e.toString());
        }
    }
    public List<Artifact> searchEvidence(RequestInfo requestInfo, EvidenceSearchCriteria evidenceSearchCriteria) {
        try {
                // Fetch applications from database according to the given search criteria
            List<Artifact> artifacts = repository.getArtifacts(evidenceSearchCriteria);

            // If no applications are found matching the given criteria, return an empty list
            if(CollectionUtils.isEmpty(artifacts))
                return new ArrayList<>();
            artifacts.forEach(artifact -> artifact.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, artifact.getTenantId(), artifact.getArtifactNumber()))));
            return artifacts;
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException("EVIDENCE_SEARCH_EXCEPTION",e.toString());
        }
    }

    public Artifact updateEvidence(EvidenceRequest evidenceRequest) {
        try {
            Artifact existingApplication = validateExistingApplication(evidenceRequest);

            // Update workflow
            existingApplication.setWorkflow(evidenceRequest.getArtifact().getWorkflow());

            // Enrich application upon update
            evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

            // Update workflow status
            workflowService.updateWorkflowStatus(evidenceRequest);

            // Enrich based on artifact status
            enrichBasedOnStatus(evidenceRequest);

            // Push to Kafka
            producer.push(config.getUpdateEvidenceKafkaTopic(), evidenceRequest);

            return evidenceRequest.getArtifact();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating evidence", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating evidence", e);
            throw new CustomException("EVIDENCE_UPDATE_EXCEPTION", "Error occurred while updating evidence: " + e.toString());
        }
    }

    private Artifact validateExistingApplication(EvidenceRequest evidenceRequest) {
        try {
            return validator.validateApplicationExistence(evidenceRequest);
        } catch (Exception e) {
            log.error("Error validating existing application", e);
            throw new CustomException("EVIDENCE_UPDATE_EXCEPTION", "Error validating existing application: " + e.toString());
        }
    }

    private void enrichBasedOnStatus(EvidenceRequest evidenceRequest) {
        String status = evidenceRequest.getArtifact().getStatus();

        if (PUBLISHED_STATE.equalsIgnoreCase(status)) {
            evidenceEnrichment.enrichEvidenceNumber(evidenceRequest);
        } else if (ABATED_STATE.equalsIgnoreCase(status)) {
            evidenceEnrichment.enrichIsActive(evidenceRequest);
        }
    }

}
