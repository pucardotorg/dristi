package org.pucar.enrichment;


        import lombok.extern.slf4j.Slf4j;
        import org.egov.common.contract.models.AuditDetails;
        import org.pucar.util.IdgenUtil;
        import org.pucar.util.UserUtil;
        import org.pucar.web.models.AdvocateClerk;
        import org.pucar.web.models.AdvocateClerkRequest;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

        import java.util.List;
        import java.util.UUID;

@Component
@Slf4j
public class AdvocateClerkRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserUtil userUtils;

    public void enrichAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) {
        try {
            if(advocateClerkRequest.getRequestInfo().getUserInfo() != null) {
                List<String> advocateClerkRegistrationIdList = idgenUtil.getIdList(advocateClerkRequest.getRequestInfo(), advocateClerkRequest.getClerks().get(0).getTenantId(), "clerk.id", null, advocateClerkRequest.getClerks().size());
                Integer index = 0;
                for (AdvocateClerk advocateClerk : advocateClerkRequest.getClerks()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    advocateClerk.setAuditDetails(auditDetails);

                    advocateClerk.setId(UUID.randomUUID());
                    advocateClerk.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(String.valueOf(advocateClerk.getId()));
                    });



                    advocateClerk.setApplicationNumber(advocateClerkRegistrationIdList.get(index++));
                }
            }
        } catch (Exception e) {
            log.error("Error enriching advocate application: {}", e.getMessage());
        }
    }
}