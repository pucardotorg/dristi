package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class HearingRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    /**
     * Enrich the hearing application by setting values in different field
     *
     * @param hearingRequest the hearing registration request body
     */
    public void enrichHearingRegistration(HearingRequest hearingRequest) {
        try {
            Hearing hearing = hearingRequest.getHearing();
            AuditDetails auditDetails = AuditDetails.builder().createdBy(hearingRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(hearingRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            hearing.setAuditDetails(auditDetails);
            hearing.setId(UUID.randomUUID());
            //setting false unless the application is approved
            hearing.setIsActive(false);
            if(hearing.getDocuments()!=null){
                hearing.getDocuments().forEach(document -> {
                    document.setId(String.valueOf(UUID.randomUUID()));
                    document.setDocumentUid(document.getId());
                });
            }
            List<String> hearingIdList = idgenUtil.getIdList(hearingRequest.getRequestInfo(), hearingRequest.getRequestInfo().getUserInfo().getTenantId(), "hearing.id", null, 1);
            hearing.setHearingId(hearingIdList.get(0));
        } catch (CustomException e) {
            log.error("Custom Exception occurred while Enriching hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error enriching hearing application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error hearing in enrichment service: " + e.getMessage());
        }

    }

    /**
     * Enrich the hearing application on update
     *
     * @param hearingRequest the hearing registration request body
     */
    public void enrichHearingApplicationUponUpdate(HearingRequest hearingRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            Hearing hearing = hearingRequest.getHearing();
            hearing.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            hearing.getAuditDetails().setLastModifiedBy(hearingRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching hearing application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in hearing enrichment service during hearing update process: " + e.getMessage());
        }
    }
}
