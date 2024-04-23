package org.pucar.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.util.IdgenUtil;
import org.pucar.util.UserUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.config.ServiceConstants.ENRICHMENT_EXCEPTION;
import static org.pucar.config.ServiceConstants.IDGEN_ERROR;

@Component
@Slf4j
public class AdvocateRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserUtil userUtils;

    public void enrichAdvocateRegistration(AdvocateRequest advocateRequest) {
        try {
            if(advocateRequest.getRequestInfo().getUserInfo() != null) {
                List<String> advocateRegistrationIdList = idgenUtil.getIdList(advocateRequest.getRequestInfo(), advocateRequest.getRequestInfo().getUserInfo().getTenantId(), "advocate.id", null, advocateRequest.getAdvocates().size());
                Integer index = 0;
                for (Advocate advocate : advocateRequest.getAdvocates()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    advocate.setAuditDetails(auditDetails);

                    advocate.setId(UUID.randomUUID());

                    advocate.setApplicationNumber(advocateRegistrationIdList.get(index++));
                    if(advocate.getDocuments()!=null)
                    advocate.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });
                }
            }
        } catch (CustomException e){
            log.error("Custom Exception occurred while Enriching advocate");
            throw e;
        } catch (Exception e) {
            log.error("Error enriching advocate application: {}", e.getMessage());
            // Handle the exception or throw a custom exception
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in enrichment service");
        }
    }

    public void enrichAdvocateApplicationUponUpdate(AdvocateRequest advocateRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            for (Advocate advocate : advocateRequest.getAdvocates()) {
                advocate.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                advocate.getAuditDetails().setLastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid());
            }
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update: {}", e.getMessage());
            // Handle the exception or throw a custom exception
            throw new CustomException(ENRICHMENT_EXCEPTION,e.getMessage());
        }
    }
}
