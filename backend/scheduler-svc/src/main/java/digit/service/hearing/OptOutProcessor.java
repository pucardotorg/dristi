package digit.service.hearing;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.kafka.producer.Producer;
import digit.repository.ReScheduleRequestRepository;
import digit.service.HearingService;
import digit.service.RescheduleRequestOptOutService;
import digit.service.UserService;
import digit.util.PendingTaskUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static digit.config.ServiceConstants.ACTIVE;
import static digit.config.ServiceConstants.INACTIVE;

@Service
@Slf4j
public class OptOutProcessor {

    private final Producer producer;
    private final ReScheduleRequestRepository repository;
    private final Configuration configuration;
    private final ObjectMapper mapper;
    private final HearingService hearingService;
    private final RescheduleRequestOptOutService optOutService;
    private final PendingTaskUtil pendingTaskUtil;
    private final UserService userService;

    @Autowired
    public OptOutProcessor(Producer producer, ReScheduleRequestRepository repository, Configuration configuration, ObjectMapper mapper, HearingService hearingService, RescheduleRequestOptOutService optOutService, PendingTaskUtil pendingTaskUtil, UserService userService) {
        this.producer = producer;
        this.repository = repository;
        this.configuration = configuration;
        this.mapper = mapper;
        this.hearingService = hearingService;
        this.optOutService = optOutService;
        this.pendingTaskUtil = pendingTaskUtil;
        this.userService = userService;
    }


    public void checkAndScheduleHearingForOptOut(HashMap<String, Object> record) {
        try {
            log.info("operation = checkAndScheduleHearingForOptOut, result = IN_PROGRESS, record = {}", record);
            OptOutRequest optOutRequest = mapper.convertValue(record, OptOutRequest.class);
            OptOut optOut = optOutRequest.getOptOut();

            String rescheduleRequestId = optOut.getRescheduleRequestId();

            List<ReScheduleHearing> reScheduleRequest = repository.getReScheduleRequest(ReScheduleHearingReqSearchCriteria.builder()
                    .rescheduledRequestId(Collections.singletonList(rescheduleRequestId)).build(), null, null);

            ReScheduleHearing reScheduleHearing = reScheduleRequest.get(0);

            log.info("RescheduleHearingId for which opt out is made: {}", reScheduleHearing.getRescheduledRequestId());
            log.info("HearingBookingId for which opt out is made: {}", reScheduleHearing.getHearingBookingId());

            boolean isLastOptOut = isLastOptOut(optOut,reScheduleHearing);
            // todo: audit details part
            if (isLastOptOut) {  // second condition is for lag if this data is already persisted into the db,it should be second only
                // this is last opt out, need to close the request. open the pending task for judge
                log.info("this is the last opt out, need to close the request. open the pending task for judge");
                PendingTask pendingTask = pendingTaskUtil.createPendingTask(reScheduleHearing);
                PendingTaskRequest request = PendingTaskRequest.builder()
                        .pendingTask(pendingTask)
                        .requestInfo(new RequestInfo()).build();
                pendingTaskUtil.callAnalytics(request);
                reScheduleHearing.setStatus(INACTIVE);
                //unblock the calendar for judge (suggested days -available days)
                unblockJudgeCalendarForSuggestedDays(reScheduleHearing);

            } else {
                //update the request and reduce available dates
                reScheduleHearing.setStatus(ACTIVE);
            }

            RequestInfo requestInfo = createInternalRequestInfo();
            ReScheduleHearingRequest reScheduleHearingRequest = ReScheduleHearingRequest.builder().requestInfo(requestInfo)
                    .reScheduleHearing(reScheduleRequest).build();
            producer.push(configuration.getUpdateRescheduleRequestTopic(), reScheduleHearingRequest);

            log.info("operation = checkAndScheduleHearingForOptOut, result = SUCCESS");

        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
            log.info("operation = checkAndScheduleHearingForOptOut, result = FAILURE, message = {}", e.getMessage());

        }
    }

    private boolean isLastOptOut(OptOut optOut,ReScheduleHearing reScheduleHearing) {
        String rescheduleRequestId = optOut.getRescheduleRequestId();
        List<Long> optoutDates = optOut.getOptoutDates();
        OptOutSearchRequest searchRequest = OptOutSearchRequest.builder()
                .requestInfo(new RequestInfo())
                .criteria(OptOutSearchCriteria.builder()
                        .rescheduleRequestId(rescheduleRequestId)
                        .build()).build();

        int totalOptOutCanBeMade = reScheduleHearing.getLitigants().size() + reScheduleHearing.getRepresentatives().size();
        List<Long> suggestedDates = reScheduleHearing.getSuggestedDates();
        List<Long> availableDates = reScheduleHearing.getAvailableDates()==null?new ArrayList<>():reScheduleHearing.getAvailableDates();
        Set<Long> suggestedDatesSet = availableDates.isEmpty() ? new HashSet<>(suggestedDates) : new HashSet<>(availableDates);

        List<OptOut> optOuts = optOutService.search(searchRequest, null, null);
        int optOutAlreadyMade = optOuts.size();
        optoutDates.forEach(suggestedDatesSet::remove);
        reScheduleHearing.setAvailableDates(new ArrayList<>(suggestedDatesSet));

        boolean isOptOutSaved = optOuts.stream().anyMatch(optOut1 -> optOut1.getIndividualId().equals(optOut.getIndividualId()));

        return (totalOptOutCanBeMade - optOutAlreadyMade == 0 && isOptOutSaved) || (totalOptOutCanBeMade - optOutAlreadyMade == 1 && !isOptOutSaved);
    }

    public void unblockJudgeCalendarForSuggestedDays(ReScheduleHearing reScheduleHearing ) {
        try {
            log.info("operation = unblockJudgeCalendarForSuggestedDays, result = IN_PROGRESS, request = {}",reScheduleHearing);
            List<Long> suggestedDays = reScheduleHearing.getSuggestedDates();
            List<Long> availableDays = reScheduleHearing.getAvailableDates();
            Set<Long> suggestedDaysSet = new HashSet<>(suggestedDays);
            availableDays.forEach(suggestedDaysSet::remove);
            List<ScheduleHearing> scheduleHearings = hearingService.search(HearingSearchRequest.builder()
                    .criteria(ScheduleHearingSearchCriteria.builder()
                            .rescheduleId(reScheduleHearing.getRescheduledRequestId())
                            .tenantId(reScheduleHearing.getTenantId())
                            .build())
                    .build(), null, null);

            List<ScheduleHearing> newHearings = new ArrayList<>();
            for (ScheduleHearing scheduleHearing : scheduleHearings) {
                Long hearingDate = scheduleHearing.getHearingDate();
                if(suggestedDaysSet.contains(hearingDate)){
                    scheduleHearing.setStatus(INACTIVE);
                    newHearings.add(scheduleHearing);
                }
            }

            RequestInfo requestInfo = createInternalRequestInfo();
            ScheduleHearingRequest scheduleHearingRequest = ScheduleHearingRequest.builder().requestInfo(requestInfo)
                    .hearing(newHearings).build();
            producer.push(configuration.getScheduleHearingUpdateTopic(), scheduleHearingRequest);
            log.info("operation = unblockJudgeCalendarForSuggestedDays, result = SUCCESS");
        } catch (Exception e) {
            log.error("Error unblocking calendar: {}", e.getMessage());
            log.info("operation = unblockJudgeCalendarForSuggestedDays, result = FAILURE, message = {}", e.getMessage());
        }
    }

    private RequestInfo createInternalRequestInfo() {
        User userInfo = new User();
        userInfo.setUuid(userService.internalMicroserviceRoleUuid);
        return RequestInfo.builder().userInfo(userInfo).build();
    }
}
