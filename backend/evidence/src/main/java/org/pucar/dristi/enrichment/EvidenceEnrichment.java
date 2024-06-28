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

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class EvidenceEnrichment {
    private final IdgenUtil idgenUtil;

    @Autowired
    public EvidenceEnrichment(IdgenUtil idgenUtil) {
        this.idgenUtil = idgenUtil;
    }

    public void enrichEvidenceRegistration(EvidenceRequest evidenceRequest) {
        try {
                List<String> artifactRegistrationIdList = idgenUtil.getIdList(
                        evidenceRequest.getRequestInfo(),
                        evidenceRequest.getRequestInfo().getUserInfo().getTenantId(),
                        ARTIFACT_ID_NAME,
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

                evidenceRequest.getArtifact().setIsActive(true);
                evidenceRequest.getArtifact().setArtifactNumber(artifactRegistrationIdList.get(0));

                if (evidenceRequest.getArtifact().getFile() != null) {
                    evidenceRequest.getArtifact().getFile().setId(String.valueOf(UUID.randomUUID()));
                    evidenceRequest.getArtifact().getFile().setDocumentUid(evidenceRequest.getArtifact().getFile().getId());
                }

        } catch (CustomException e) {
            log.error("Custom Exception occurred while Enriching evidence: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error enriching evidence application: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in evidence enrichment service: " + e.toString());
        }
    }

    String getIdgenByArtifactTypeAndSourceType(String artifactType, String sourceType) {
        if (("DOCUMENTARY".equals(artifactType) || AFFIDAVIT.equals(artifactType)) && COMPLAINANT.equals(sourceType)) {
            return "document.evidence_complainant";
        } else if ((DOCUMENTARY.equals(artifactType) || AFFIDAVIT.equals(artifactType)) && ACCUSED.equals(sourceType)) {
            return "document.evidence_accused";
        } else if ((DOCUMENTARY.equals(artifactType) || AFFIDAVIT.equals(artifactType)) && COURT.equals(sourceType)) {
            return "document.evidence_court";
        } else if (DEPOSITION.equals(artifactType) && COMPLAINANT.equals(sourceType)) {
            return "document.witness_complainant";
        } else if (DEPOSITION.equals(artifactType) && ACCUSED.equals(sourceType)) {
            return "document.witness_accused";
        } else if (DEPOSITION.equals(artifactType) && COURT.equals(sourceType)) {
            return "document.witness_court";
        } else {
            throw new CustomException(ENRICHMENT_EXCEPTION, "Invalid artifact type or source type provided");
        }
    }

    public void enrichEvidenceNumber(EvidenceRequest evidenceRequest) {
        try {
            String idName = getIdgenByArtifactTypeAndSourceType(evidenceRequest.getArtifact().getArtifactType(), evidenceRequest.getArtifact().getSourceType());
            List<String> evidenceNumberList = idgenUtil.getIdList(
                    evidenceRequest.getRequestInfo(),
                    evidenceRequest.getRequestInfo().getUserInfo().getTenantId(),
                    idName,
                    null,
                    1
            );
            evidenceRequest.getArtifact().setEvidenceNumber(evidenceNumberList.get(0));
            evidenceRequest.getArtifact().setIsEvidence(true);
        } catch (Exception e) {
            log.error("Error enriching evidence number upon update: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Failed to generate evidence number for " + evidenceRequest.getArtifact().getId() + ": " + e.toString());
        }
    }
        public void enrichIsActive(EvidenceRequest evidenceRequest) {
            try {
                evidenceRequest.getArtifact().setIsActive(false);
            }
            catch (Exception e) {
                log.error("Error enriching isActive status upon update: {}", e.toString());
                throw new CustomException(ENRICHMENT_EXCEPTION, "Error in enrichment service during isActive status update process: " + e.toString());
            }
    }


    public void enrichEvidenceRegistrationUponUpdate(EvidenceRequest evidenceRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching evidence application upon update: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in enrichment service during  update process: " + e.toString());
        }
    }
}
