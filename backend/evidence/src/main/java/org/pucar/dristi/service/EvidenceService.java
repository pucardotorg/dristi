package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.*;
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
            if (body.getArtifact().getArtifactType() != null && body.getArtifact().getArtifactType().equals(DEPOSITION)) {
                workflowService.updateWorkflowStatus(body);
                producer.push(config.getEvidenceCreateTopic(), body);
            } else {
                if (body.getArtifact().getIsEvidence().equals(true)) {
                    evidenceEnrichment.enrichEvidenceNumber(body);
                }
                producer.push(config.getEvidenceCreateWithoutWorkflowTopic(), body);
            }
            return body.getArtifact();
        } catch (CustomException e) {
            log.error("Custom Exception occurred while creating evidence");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating evidence");
            throw new CustomException(EVIDENCE_CREATE_EXCEPTION, e.toString());
        }
    }

    public List<Artifact> searchEvidence(RequestInfo requestInfo, EvidenceSearchCriteria evidenceSearchCriteria, Pagination pagination) {
        try {
            // Fetch applications from database according to the given search criteria
            List<Artifact> artifacts = repository.getArtifacts(evidenceSearchCriteria, pagination);

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(artifacts))
                return new ArrayList<>();
            return artifacts;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while searching");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results");
            throw new CustomException(EVIDENCE_SEARCH_EXCEPTION, e.toString());
        }
    }

    public Artifact updateEvidence(EvidenceRequest evidenceRequest) {
        try {
            Artifact existingApplication = validateExistingEvidence(evidenceRequest);

            // Update workflow
            existingApplication.setWorkflow(evidenceRequest.getArtifact().getWorkflow());

            // Enrich application upon update
            evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

            if (evidenceRequest.getArtifact().getArtifactType() != null && evidenceRequest.getArtifact().getArtifactType().equals(DEPOSITION)) {
                workflowService.updateWorkflowStatus(evidenceRequest);
                enrichBasedOnStatus(evidenceRequest);
                producer.push(config.getUpdateEvidenceKafkaTopic(), evidenceRequest);
            } else {
                if (evidenceRequest.getArtifact().getIsEvidence().equals(true) && evidenceRequest.getArtifact().getEvidenceNumber() == null) {
                    evidenceEnrichment.enrichEvidenceNumber(evidenceRequest);
                }
                producer.push(config.getUpdateEvidenceWithoutWorkflowKafkaTopic(), evidenceRequest);
            }
            return evidenceRequest.getArtifact();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating evidence", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating evidence", e);
            throw new CustomException(EVIDENCE_UPDATE_EXCEPTION, "Error occurred while updating evidence: " + e.toString());
        }
    }

    Artifact validateExistingEvidence(EvidenceRequest evidenceRequest) {
        try {
            return validator.validateEvidenceExistence(evidenceRequest);
        } catch (Exception e) {
            log.error("Error validating existing application", e);
            throw new CustomException(EVIDENCE_UPDATE_EXCEPTION, "Error validating existing application: " + e.toString());
        }
    }

    void enrichBasedOnStatus(EvidenceRequest evidenceRequest) {
        String status = evidenceRequest.getArtifact().getStatus();
        if (PUBLISHED_STATE.equalsIgnoreCase(status)) {
            evidenceEnrichment.enrichEvidenceNumber(evidenceRequest);
        } else if (ABATED_STATE.equalsIgnoreCase(status)) {
            evidenceEnrichment.enrichIsActive(evidenceRequest);
        }
    }

    public void addComments(EvidenceAddCommentRequest evidenceAddCommentRequest) {
        try {
            EvidenceAddComment evidenceAddComment = evidenceAddCommentRequest.getEvidenceAddComment();
            List<Artifact> applicationList = repository.getArtifacts(EvidenceSearchCriteria.builder().artifactNumber(evidenceAddComment.getArtifactNumber()).build(), null);
            if (CollectionUtils.isEmpty(applicationList)) {
                throw new CustomException(COMMENT_ADD_ERR, "Evidence not found");
            }
            AuditDetails auditDetails = createAuditDetails(evidenceAddCommentRequest.getRequestInfo());
            evidenceAddComment.getComment().forEach(comment -> evidenceEnrichment.enrichCommentUponCreate(comment, auditDetails));
            Artifact artifactToUpdate = applicationList.get(0);
            List<Comment> updatedComments = new ArrayList<>(artifactToUpdate.getComments());
            updatedComments.addAll(evidenceAddComment.getComment());
            artifactToUpdate.setComments(updatedComments);
            evidenceAddComment.setComment(updatedComments);

            AuditDetails applicationAuditDetails = artifactToUpdate.getAuditdetails();
            applicationAuditDetails.setLastModifiedBy(auditDetails.getLastModifiedBy());
            applicationAuditDetails.setLastModifiedTime(auditDetails.getLastModifiedTime());
            producer.push(config.getEvidenceUpdateCommentsTopic(), artifactToUpdate);
        } catch (CustomException e) {
            log.error("Custom exception while adding comments {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error while adding comments {}", e.toString());
            throw new CustomException(COMMENT_ADD_ERR, e.getMessage());
        }
    }

    private AuditDetails createAuditDetails(RequestInfo requestInfo) {
        return AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo().getUuid())
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy(requestInfo.getUserInfo().getUuid())
                .lastModifiedTime(System.currentTimeMillis())
                .build();
    }

}
