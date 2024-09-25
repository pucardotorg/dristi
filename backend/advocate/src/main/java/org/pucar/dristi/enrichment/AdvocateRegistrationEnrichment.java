package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class AdvocateRegistrationEnrichment {

    private final IdgenUtil idgenUtil;
    private final Configuration configuration;

    @Autowired
    public AdvocateRegistrationEnrichment(IdgenUtil idgenUtil, Configuration configuration) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
    }


    /**
     * Enrich the advocate application by setting values in different field
     *
     * @param advocateRequest the advocate registration request body
     */
    public void enrichAdvocateRegistration(AdvocateRequest advocateRequest) {
        try {
            String tenantId = advocateRequest.getAdvocate().getTenantId();
            String idName = configuration.getAdvConfig();
            String idFormat = configuration.getAdvFormat();

            List<String> advocateApplicationNumbers = idgenUtil.getIdList(advocateRequest.getRequestInfo(), tenantId, idName, idFormat, 1, true);
            log.info("Advocate Application Number :: {}",advocateApplicationNumbers);

            Advocate advocate =  advocateRequest.getAdvocate();
            AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            advocate.setAuditDetails(auditDetails);
            advocate.setId(UUID.randomUUID());
            //setting false unless the application is approved
            advocate.setIsActive(false);
            //setting generated application number
            advocate.setApplicationNumber(advocateApplicationNumbers.get(0));
            if (advocate.getDocuments() != null) {
                advocate.getDocuments().forEach(document -> document.setId(String.valueOf(UUID.randomUUID())));
            }
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e) {
            log.error("Error enriching advocate application :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error advocate in enrichment service: " + e.getMessage());
        }

    }

    /**
     * Enrich the advocate application on update
     *
     * @param advocateRequest the advocate registration request body
     */
    public void enrichAdvocateApplicationUponUpdate(AdvocateRequest advocateRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            Advocate advocate =  advocateRequest.getAdvocate();
            advocate.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            advocate.getAuditDetails().setLastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in advocate enrichment service during advocate update process: " + e.getMessage());
        }
    }
}
