package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.Comment;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;
@Component
@Slf4j
public class EvidenceEnrichment {
    @Autowired
    private IdgenUtil idgenUtil;


    public void enrichEvidenceRegistration(EvidenceRequest evidenceRequest) {
        try {
                List<String> artifactRegistrationIdList = idgenUtil.getIdList(
                        evidenceRequest.getRequestInfo(),
                        evidenceRequest.getRequestInfo().getUserInfo().getTenantId(),
                        "artifact.artifact_number",
                        null,
                        1
                );

                AuditDetails auditDetails = AuditDetails.builder()
                        .createdBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid())
                        .createdTime(System.currentTimeMillis())
                        .lastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid())
                        .lastModifiedTime(System.currentTimeMillis())
                        .build();

                evidenceRequest.getArtifact().setAuditdetails(auditDetails);
                evidenceRequest.getArtifact().setId(UUID.randomUUID());

                for (Comment comment : evidenceRequest.getArtifact().getComments()) {
                    comment.setId(UUID.randomUUID());
                }

                evidenceRequest.getArtifact().setIsActive(false);
                evidenceRequest.getArtifact().setArtifactNumber(artifactRegistrationIdList.get(0));

                if (evidenceRequest.getArtifact().getFile() != null) {
                    evidenceRequest.getArtifact().getFile().setId(String.valueOf(UUID.randomUUID()));
                    evidenceRequest.getArtifact().getFile().setDocumentUid(evidenceRequest.getArtifact().getFile().getId());
                }

        } catch (CustomException e) {
            log.error("Custom Exception occurred while Enriching evidence: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error enriching evidence application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in evidence enrichment service: " + e.getMessage());
        }
    }

    String getIdgenByArtifactType(String artifactType) {
        switch (artifactType) {
            case "complainant":
                return "document.evidence_complainant";
            case "accused":
                return "document.evidence_accused";
            case "court":
                return "document.evidence_court";
            case "witness_complainant":
                return "document.witness_complainant";
            case "witness_accused":
                return "document.witness_accused";
            case "witness_court":
                return "document.witness_court";
            default:
                throw new CustomException(ENRICHMENT_EXCEPTION, "Invalid artifact type provided");
        }
    }
    public void enrichEvidenceNumberAndIsActiveStatus(EvidenceRequest evidenceRequest) {
        try {
            String idName = getIdgenByArtifactType(evidenceRequest.getArtifact().getArtifactType());
            List<String> evidenceNumberList = idgenUtil.getIdList(
                    evidenceRequest.getRequestInfo(),
                    evidenceRequest.getRequestInfo().getUserInfo().getTenantId(),
                    idName,
                    null,
                    1
            );
            evidenceRequest.getArtifact().setEvidenceNumber(evidenceNumberList.get(0));
            evidenceRequest.getArtifact().setIsActive(true);
        }
        catch (Exception e) {
            log.error("Error enriching evidence number and isActive status upon update: {}", e.getMessage());
            throw new CustomException("ENRICHMENT_EXCEPTION", "Error in enrichment service during evidence number and isActive status update process: " + e.getMessage());
        }
    }


    public void enrichEvidenceRegistrationUponUpdate(EvidenceRequest evidenceRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching evidence application upon update: {}", e.getMessage());
            throw new CustomException("ENRICHMENT_EXCEPTION", "Error in enrichment service during  update process: " + e.getMessage());
        }
    }
}
