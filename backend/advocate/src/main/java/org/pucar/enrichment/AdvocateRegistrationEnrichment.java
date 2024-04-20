package org.pucar.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.pucar.util.IdgenUtil;
import org.pucar.util.UserUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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
                List<String> advocateClerkRegistrationIdList = idgenUtil.getIdList(advocateRequest.getRequestInfo(), advocateRequest.getAdvocates().get(0).getTenantId(), "advocate.id", null, advocateRequest.getAdvocates().size());
                Integer index = 0;
                for (Advocate advocate : advocateRequest.getAdvocates()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    advocate.setAuditDetails(auditDetails);

                    advocate.setId(UUID.randomUUID());

                    advocate.setApplicationNumber(advocateClerkRegistrationIdList.get(index++));

                    advocate.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });
                }
            }
        } catch (Exception e) {
            log.error("Error enriching birth application: {}", e.getMessage());
            // Handle the exception or throw a custom exception
        }
    }

    public void enrichBirthApplicationUponUpdate(AdvocateRequest advocateRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            advocateRequest.getAdvocates().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            advocateRequest.getAdvocates().get(0).getAuditDetails().setLastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching birth application upon update: {}", e.getMessage());
            // Handle the exception or throw a custom exception
        }
    }
}
