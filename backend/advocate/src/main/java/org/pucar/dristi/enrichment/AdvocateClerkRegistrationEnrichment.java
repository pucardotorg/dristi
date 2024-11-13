package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkRegistrationEnrichment {

    private final IdgenUtil idgenUtil;
    private final Configuration configuration;

    @Autowired
    public AdvocateClerkRegistrationEnrichment(IdgenUtil idgenUtil, Configuration configuration) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
    }
    /**
     * Enrich the advocate clerk application by setting values in different field
     *
     * @param advocateClerkRequest the advocate clerk registration request body
     */
    public void enrichAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) {
        try {
            String tenantId = advocateClerkRequest.getClerk().getTenantId();
            String idName = configuration.getClerkConfig();
            String idFormat = configuration.getClerkFormat();

            List<String> clerkApplicationNumbers = idgenUtil.getIdList(advocateClerkRequest.getRequestInfo(), tenantId, idName, idFormat, 1, true);
            log.info("Advocate Clerk Application Number :: {}",clerkApplicationNumbers);

            AdvocateClerk advocateClerk = advocateClerkRequest.getClerk();
            AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            advocateClerk.setAuditDetails(auditDetails);

            advocateClerk.setId(UUID.randomUUID());
            //setting false unless the application is approved
            advocateClerk.setIsActive(false);
            if (advocateClerk.getDocuments() != null)
                advocateClerk.getDocuments().forEach(document -> document.setId(String.valueOf(UUID.randomUUID())));;

            //setting generated application number
            advocateClerk.setApplicationNumber(clerkApplicationNumbers.get(0));
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error enriching advocate clerk application :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in clerk enrichment service: " + e.getMessage());
        }
    }

    /**
     * Enrich the advocate clerk application on update
     *
     * @param advocateClerkRequest the advocate registration request body
     */
    public void enrichAdvocateClerkApplicationUponUpdate(AdvocateClerkRequest advocateClerkRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            AdvocateClerk advocateClerk = advocateClerkRequest.getClerk();
            advocateClerk.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            advocateClerk.getAuditDetails().setLastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching advocate clerk  application upon update :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in clerk enrichment service during update: " + e.getMessage());
        }
    }
}