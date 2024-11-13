package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
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

    private IdgenUtil idgenUtil;
    private Configuration configuration;

    @Autowired
    public HearingRegistrationEnrichment(IdgenUtil idgenUtil, Configuration configuration) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
    }

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
            String tenantId = hearing.getFilingNumber().get(0).replace("-","");

            String idName = configuration.getHearingConfig();
            String idFormat = configuration.getHearingFormat();

            List<String> hearingIdList = idgenUtil.getIdList(hearingRequest.getRequestInfo(), tenantId, idName, idFormat, 1, false);
            hearing.setHearingId(hearing.getFilingNumber().get(0) +"-"+hearingIdList.get(0));

            if(null != hearing.getCourtCaseNumber())
                hearing.setCaseReferenceNumber(hearing.getCourtCaseNumber());
            else
                hearing.setCaseReferenceNumber(hearing.getCmpNumber());
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

            if(hearing.getDocuments()!=null){
                hearing.getDocuments().forEach(document -> {
                    if(document.getId()==null)
                     document.setId(String.valueOf(UUID.randomUUID()));
                });
            }
        } catch (Exception e) {
            log.error("Error enriching hearing application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in hearing enrichment service during hearing update process: " + e.getMessage());
        }
    }
}
