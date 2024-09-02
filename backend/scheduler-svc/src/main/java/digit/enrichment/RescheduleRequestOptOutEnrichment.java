package digit.enrichment;


import digit.models.coremodels.AuditDetails;
import digit.web.models.OptOutRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Component;

@Component
public class RescheduleRequestOptOutEnrichment {

    public void enrichCreateRequest(OptOutRequest request) {

        AuditDetails auditDetails = getAuditDetailsScheduleHearing(request.getRequestInfo());
        request.getOptOut().setAuditDetails(auditDetails);
        request.getOptOut().setRowVersion(1);

    }


    private AuditDetails getAuditDetailsScheduleHearing(RequestInfo requestInfo) {

        return AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo().getUuid())
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy(requestInfo.getUserInfo().getUuid())
                .lastModifiedTime(System.currentTimeMillis())
                .build();

    }
}
