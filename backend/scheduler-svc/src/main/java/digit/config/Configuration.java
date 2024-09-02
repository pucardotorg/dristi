package digit.config;


import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    @Value("${drishti.scheduler.hearing}")
    private String scheduleHearingTopic;

    @Value("${drishti.scheduler.opt-out}")
    private String optOutTopic;

    @Value("${drishti.scheduler.opt-out.update}")
    private String optOutUpdateTopic;

    @Value("${drishti.scheduler.hearing.update}")
    private String scheduleHearingUpdateTopic;

    @Value("${drishti.scheduler.hearing.reschedule}")
    private String rescheduleRequestCreateTopic;

    @Value("${drishti.judge.calendar.update}")
    private String updateJudgeCalendarTopic;

    @Value("${drishti.causelist.insert}")
    private String causeListInsertTopic;

    @Value("${causelist.pdf.template.key}")
    private String causeListPdfTemplateKey;

    @Value("${async.submission.insert}")
    private String asyncSubmissionSaveTopic;

    @Value("${async.submission.update}")
    private String asyncSubmissionUpdateTopic;

    @Value("${async.reschedule.hearing}")
    private String asyncSubmissionReScheduleHearing;

    @Value("${min.async.submission.days}")
    private Integer minAsyncSubmissionDays;

    @Value("${min.async.response.days}")
    private Integer minAsyncResponseDays;
    @Value("${drishti.scheduler.hearing.reschedule.update}")
    private String updateRescheduleRequestTopic;

    //Tenant Id
    @Value("${egov-state-level-tenant-id}")
    private String egovStateTenantId;

    // User Config
    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;


    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;


    // id format
    @Value("${drishti.idgen.hearing.id.format}")
    private String hearingIdFormat;

    @Value("${drishti.idgen.reschedule.id.format}")
    private String rescheduleHearingIdFormat;

    // id format
    @Value("${drishti.idgen.async.submission.id.format}")
    private String asyncSubmissionIdFormat;

    //Workflow Config
    @Value("${egov.workflow.host}")
    private String wfHost;

    @Value("${egov.workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${egov.workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String wfProcessInstanceSearchPath;


    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;


    //Due date of hearing
    @Value("${drishti.opt-out.due.days}")
    private Long optOutDueDate;

    //date before reschedule request can be raised
    @Value("${drishti.reschedule.before.date}")
    private Long rescheduleRequestDueDate;

    //Pdf Services
    @Value("${egov.pdf.service.host}")
    private String pdfServiceHost;

    @Value("${egov.pdf.service.create.endpoint}")
    private String pdfServiceEndpoint;

    //CaseCriteria
    @Value("${drishti.case.host}")
    private String caseUrl;

    @Value("${drishti.case.endpoint}")
    private String caseEndpoint;

    @Value("${drishti.opt-out.selection.limit}")
    private Long optOutLimit;

    @Value("${drishti.analytics.host}")
    private String analyticsHost;

    @Value("${drishti.analytics.endpoint}")
    private String analyticsEndpoint;

    //Hearing config
    @Value("${dristhi.hearing.host}")
    private String HearingHost;

    @Value("${drishti.hearingupdate.endpoint}")
    private String HearingUpdateEndPoint;

    @Value("${drishti.hearing.search.endpoint}")
    private String hearingSearchEndPoint;

    @Value("${app.zone.id}")
    private String zoneId;

    @Value("${drishti.judge.pending.due.days}")
    private Long judgePendingSla;
}
