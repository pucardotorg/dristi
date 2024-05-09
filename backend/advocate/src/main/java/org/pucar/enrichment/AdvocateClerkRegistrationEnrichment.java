package org.pucar.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.util.IdgenUtil;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;
    @Autowired
    private Configuration configuration;

    public void enrichAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) {
        try {
            List<String> advocateClerkRegistrationIdList = idgenUtil.getIdList(advocateClerkRequest.getRequestInfo(), advocateClerkRequest.getRequestInfo().getUserInfo().getTenantId(), configuration.getAdvocateClerkIdgenIdName(), null, advocateClerkRequest.getClerks().size());
            int index = 0;
            for (AdvocateClerk advocateClerk : advocateClerkRequest.getClerks()) {
                AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                advocateClerk.setAuditDetails(auditDetails);

                advocateClerk.setId(UUID.randomUUID());
                advocateClerk.setIsActive(false);//setting false unless the application is approved
                if (advocateClerk.getDocuments() != null)
                    advocateClerk.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });

                advocateClerk.setApplicationNumber(advocateClerkRegistrationIdList.get(index++));
            }
        } catch (Exception e) {
            log.error("Error enriching advocate clerk application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in clerk enrichment service: " + e.getMessage());
        }
    }

    public void enrichAdvocateClerkApplicationUponUpdate(AdvocateClerkRequest advocateClerkRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            for (AdvocateClerk advocateClerk : advocateClerkRequest.getClerks()) {
                advocateClerk.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                advocateClerk.getAuditDetails().setLastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid());
            }
        } catch (Exception e) {
            log.error("Error enriching advocate clerk  application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in clerk enrichment service during update: " + e.getMessage());
        }
    }
}