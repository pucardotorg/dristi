package drishti.payment.calculator.enrichment;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostalServiceEnrichment {

    public void enrichPostalServiceRequest(PostalServiceRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();
        AuditDetails auditData = getAuditDetailsFromRqstInfo(requestInfo);
        request.getPostalServices().forEach(postal -> {
            postal.setPostalServiceId(UUID.randomUUID().toString());
            postal.setRowVersion(1);

            postal.setAuditDetails(auditData);

        });
    }

    public void enrichExistingPostalServiceRequest(PostalServiceRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();

        request.getPostalServices().forEach(application -> {
            Long currentTime = System.currentTimeMillis();
            application.getAuditDetails().setLastModifiedTime(currentTime);
            application.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
            application.setRowVersion(application.getRowVersion() + 1);
        });

    }

    private AuditDetails getAuditDetailsFromRqstInfo(RequestInfo requestInfo) {

        return AuditDetails.builder().createdBy(requestInfo.getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(requestInfo.getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();

    }
}
