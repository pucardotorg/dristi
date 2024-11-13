package digit.enrichment;


import digit.config.Configuration;
import digit.models.coremodels.AuditDetails;
import digit.util.IdgenUtil;
import digit.web.models.ReScheduleHearing;
import digit.web.models.ReScheduleHearingRequest;
import digit.web.models.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static digit.config.ServiceConstants.ACTIVE;

@Component
@Slf4j
public class ReScheduleRequestEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private Configuration configuration;

    public void enrichRescheduleRequest(ReScheduleHearingRequest reScheduleHearingsRequest) {
        List<ReScheduleHearing> reScheduleHearing = reScheduleHearingsRequest.getReScheduleHearing();
        RequestInfo requestInfo = reScheduleHearingsRequest.getRequestInfo();
        log.info("starting update method for reschedule hearing enrichment");
        log.info("generating IDs for reschedule hearing enrichment using IdGenService");

        AuditDetails auditDetails = getAuditDetailsReScheduleHearing(requestInfo);

        for (ReScheduleHearing element : reScheduleHearing) {
            element.setRowVersion(1);
            element.setAuditDetails(auditDetails);
            element.setStatus(ACTIVE);
        }
    }

    private AuditDetails getAuditDetailsReScheduleHearing(RequestInfo requestInfo) {

        return AuditDetails.builder().createdBy(requestInfo.getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(requestInfo.getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();

    }
}
