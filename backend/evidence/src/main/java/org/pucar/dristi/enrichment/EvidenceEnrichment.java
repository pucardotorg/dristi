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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                evidenceRequest.getArtifact().setCreatedDate(System.currentTimeMillis());
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

    private static final Map<String, String> artifactSourceMap = new HashMap<>();

    static {
        artifactSourceMap.put("DOCUMENTARY_COMPLAINANT", "document.evidence_complainant");
        artifactSourceMap.put("DOCUMENTARY_ACCUSED", "document.evidence_accused");
        artifactSourceMap.put("DOCUMENTARY_COURT", "document.evidence_court");
        artifactSourceMap.put("AFFIDAVIT_COMPLAINANT", "document.evidence_complainant");
        artifactSourceMap.put("AFFIDAVIT_ACCUSED", "document.evidence_accused");
        artifactSourceMap.put("AFFIDAVIT_COURT", "document.evidence_court");
        artifactSourceMap.put("DEPOSITION_COMPLAINANT", "document.witness_complainant");
        artifactSourceMap.put("DEPOSITION_ACCUSED", "document.witness_accused");
        artifactSourceMap.put("DEPOSITION_COURT", "document.witness_court");
    }

    public String getIdgenByArtifactTypeAndSourceType(String artifactType, String sourceType) {
        String key = artifactType + "_" + sourceType;
        String result = artifactSourceMap.get(key);
        if (result != null) {
            return result;
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

    public void enrichCommentUponCreate(Comment comment, AuditDetails auditDetails) {
        try {
            comment.setId(UUID.randomUUID());
            comment.setAuditdetails(auditDetails);
        } catch (Exception e) {
            log.error("Error enriching comment upon create: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error enriching comment upon create: " + e.getMessage());
        }
    }
}
