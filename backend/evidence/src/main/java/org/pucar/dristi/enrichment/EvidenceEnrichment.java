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
                List<String> aetifactRegistrationIdList = idgenUtil.getIdList(evidenceRequest.getRequestInfo(), evidenceRequest.getRequestInfo().getUserInfo().getTenantId(), "artifact.artifact_number", null, evidenceRequest.getArtifacts().size());
                int index = 0;
                for (Artifact artifact : evidenceRequest.getArtifacts()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    artifact.setAuditdetails(auditDetails);
                    artifact.setId(UUID.randomUUID());
                    for (Comment comment : artifact.getComments()) {
                        comment.setId(UUID.randomUUID());
                    }
                    artifact.setIsActive(false);
                    artifact.setArtifactNumber(aetifactRegistrationIdList.get(index++));
                    if(artifact.getFile()!=null){
                        artifact.getFile().setId(String.valueOf(UUID.randomUUID()));
                        artifact.getFile().setDocumentUid(artifact.getFile().getId());
                    }
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
            evidenceRequest.getArtifacts().get(0).getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            evidenceRequest.getArtifacts().get(0).getAuditdetails().setLastModifiedBy(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update: {}", e.getMessage());
            throw new CustomException("ENRICHMENT_EXCEPTION", "Error in order enrichment service during order update process: " + e.getMessage());
        }
    }
}
