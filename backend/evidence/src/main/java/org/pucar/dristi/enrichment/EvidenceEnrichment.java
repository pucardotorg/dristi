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
            if(evidenceRequest.getRequestInfo().getUserInfo() != null) {
                List<String> aetifactRegistrationIdList = idgenUtil.getIdList(evidenceRequest.getRequestInfo(), evidenceRequest.getRequestInfo().getUserInfo().getTenantId(), "artifact.artifact_number", null, 1);
                int index = 0;
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                evidenceRequest.getArtifact().setAuditdetails(auditDetails);
                evidenceRequest.getArtifact().setId(UUID.randomUUID());
                    for (Comment comment : evidenceRequest.getArtifact().getComments()) {
                        comment.setId(UUID.randomUUID());
                    }
                evidenceRequest.getArtifact().setIsActive(false);
                evidenceRequest.getArtifact().setArtifactNumber(aetifactRegistrationIdList.get(index++));
                    if(evidenceRequest.getArtifact().getFile()!=null){
                        evidenceRequest.getArtifact().getFile().setId(String.valueOf(UUID.randomUUID()));
                        evidenceRequest.getArtifact().getFile().setDocumentUid(evidenceRequest.getArtifact().getFile().getId());
                    }
                }
            else{
                throw new CustomException(ENRICHMENT_EXCEPTION,"User info not found!!!");
            }
        } catch (CustomException e){
            log.error("Custom Exception occurred while Enriching evidence");
            throw e;
        } catch (Exception e) {
            log.error("Error enriching evidence application: {}", e.getMessage());
            // Handle the exception or throw a custom exception
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error evidence in enrichment service: "+ e.getMessage());
        }

    }
    public void enrichEvidenceRegistrationUponUpdate(EvidenceRequest evidenceRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            evidenceRequest.getArtifact().getAuditdetails().setLastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update: {}", e.getMessage());
            throw new CustomException("ENRICHMENT_EXCEPTION", "Error in order enrichment service during order update process: " + e.getMessage());
        }
    }
}
