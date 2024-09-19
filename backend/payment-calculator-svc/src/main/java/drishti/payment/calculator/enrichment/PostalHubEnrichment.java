package drishti.payment.calculator.enrichment;


import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostalHubEnrichment {
    public void enrichPostalHubRequest(PostalHubRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();
        AuditDetails auditData = getAuditDetailsFromRequestInfo(requestInfo);
        request.getPostalHubs().forEach(hub -> {
            hub.setHubId(UUID.randomUUID().toString());
            hub.setRowVersion(1);
            hub.setAuditDetails(auditData);

        });
    }

    public void enrichExistingPostalHubRequest(PostalHubRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();

        request.getPostalHubs().forEach(application -> {
            Long currentTime = System.currentTimeMillis();
            application.getAuditDetails().setLastModifiedTime(currentTime);
            application.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
            application.setRowVersion(application.getRowVersion() + 1);
        });


    }

    private AuditDetails getAuditDetailsFromRequestInfo(RequestInfo requestInfo) {

        return AuditDetails.builder().createdBy(requestInfo.getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(requestInfo.getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();

    }
}
