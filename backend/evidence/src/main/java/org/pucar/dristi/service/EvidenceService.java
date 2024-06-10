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
    @Autowired
    private EvidenceValidator validator;
    @Autowired
    private EvidenceEnrichment enrichmentUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private EvidenceRepository repository;
    @Autowired
    private Producer producer;
    @Autowired
    private Configuration config;
    public Artifact createEvidence(EvidenceRequest body) {
        try {

            // Validate applications
            validator.validateEvidenceRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichEvidenceRegistration(body);

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
            throw new CustomException(EVIDENCE_CREATE_EXCEPTION,e.getMessage());
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
            throw new CustomException("EVIDENCE_SEARCH_EXCEPTION",e.getMessage());
        }
    }

    public Artifact updateEvidence(EvidenceRequest evidenceRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Artifact existingApplication;
            try {
                existingApplication = validator.validateApplicationExistence(evidenceRequest);
            } catch (Exception e) {
                log.error("Error validating existing application");
                throw new CustomException("EVIDENCE_UPDATE_EXCEPTION", "Error validating existing application: " + e.getMessage());
            }
            existingApplication.setWorkflow(evidenceRequest.getArtifact().getWorkflow());

            // Enrich application upon update
            enrichmentUtil.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

            workflowService.updateWorkflowStatus(evidenceRequest);
            if (ACTIVE_STATUS.equalsIgnoreCase(evidenceRequest.getArtifact().getStatus())) {
                evidenceRequest.getArtifact().setIsActive(true);
            }
            producer.push(config.getUpdateEvidenceKafkaTopic(), evidenceRequest);

            return evidenceRequest.getArtifact();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating evidence");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating evidence");
            throw new CustomException("EVIDENCE_UPDATE_EXCEPTION", "Error occurred while updating evidence: " + e.getMessage());
        }

    }
}
