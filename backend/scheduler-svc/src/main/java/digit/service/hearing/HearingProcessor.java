package digit.service.hearing;


import digit.config.Configuration;
import digit.kafka.producer.Producer;
import digit.mapper.CustomMapper;
import digit.service.HearingService;
import digit.util.DateUtil;
import digit.util.HearingUtil;
import digit.web.models.Pair;
import digit.web.models.ScheduleHearing;
import digit.web.models.ScheduleHearingRequest;
import digit.web.models.hearing.Hearing;
import digit.web.models.hearing.HearingRequest;
import digit.web.models.hearing.HearingUpdateBulkRequest;
import digit.web.models.hearing.PresidedBy;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class HearingProcessor {

    private final CustomMapper customMapper;
    private final HearingService hearingService;
    private final DateUtil dateUtil;
    private final HearingUtil hearingUtil;
    private final Producer producer;
    private final Configuration config;


    @Autowired
    public HearingProcessor(CustomMapper customMapper, HearingService hearingService, Producer producer, DateUtil dateUtil, HearingUtil hearingUtil, Configuration config) {
        this.customMapper = customMapper;
        this.hearingService = hearingService;
        this.dateUtil = dateUtil;
        this.hearingUtil = hearingUtil;
        this.producer = producer;
        this.config = config;
    }


    public void processCreateHearingRequest(HearingRequest hearingRequest) {

        Hearing hearing = hearingRequest.getHearing();
        RequestInfo requestInfo = hearingRequest.getRequestInfo();
        PresidedBy presidedBy = hearing.getPresidedBy();
        List<String> filling = hearing.getFilingNumber();

        Pair<Long, Long> startTimeAndEndTime = getStartTimeAndEndTime(hearing.getStartTime());


        ScheduleHearing scheduleHearing = customMapper.hearingToScheduleHearingConversion(hearing);
        scheduleHearing.setCaseId(filling.get(0));
        scheduleHearing.setStartTime(startTimeAndEndTime.getKey());
        scheduleHearing.setEndTime(startTimeAndEndTime.getValue());
        scheduleHearing.setHearingDate(startTimeAndEndTime.getKey());


        // currently one judge only
        scheduleHearing.setJudgeId(presidedBy.getJudgeID().get(0));
        scheduleHearing.setCourtId(presidedBy.getCourtID());
        scheduleHearing.setStatus("SCHEDULED");

        ScheduleHearingRequest request = ScheduleHearingRequest.builder().hearing(Collections.singletonList(scheduleHearing)).requestInfo(requestInfo).build();

        List<ScheduleHearing> scheduledHearings = hearingService.schedule(request);   // BLOCKED THE JUDGE CALENDAR
        ScheduleHearing scheduledHearing = scheduledHearings.get(0);

        hearing.setStartTime(scheduledHearing.getStartTime());
        hearing.setEndTime(scheduledHearing.getEndTime());

        hearingRequest.setHearing(hearing);

        HearingUpdateBulkRequest updateHearingRequest = HearingUpdateBulkRequest.builder()
                .requestInfo(requestInfo)
                .hearings(Collections.singletonList(hearing))
                .build();
        hearingUtil.callHearing(updateHearingRequest);

        producer.push(config.getScheduleHearingTopic(), scheduledHearings);

    }

    private Pair<Long, Long> getStartTimeAndEndTime(Long epochTime) {

        LocalDate startOfDay = dateUtil.getLocalDateFromEpoch(epochTime);
        LocalDate nextDay = startOfDay.plusDays(1);
        long startEpochMillis = dateUtil.getEPochFromLocalDate(startOfDay);
        long endEpochMillis = dateUtil.getEPochFromLocalDate(nextDay);

        return new Pair<>(startEpochMillis, endEpochMillis);
    }

}
