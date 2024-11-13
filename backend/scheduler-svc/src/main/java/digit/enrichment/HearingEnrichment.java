package digit.enrichment;


import digit.models.coremodels.AuditDetails;
import digit.repository.HearingRepository;
import digit.util.DateUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
@Slf4j
public class HearingEnrichment {


    @Autowired
    private HearingRepository repository;

    @Autowired
    private DateUtil dateUtil;


    public void enrichScheduleHearing(ScheduleHearingRequest schedulingRequests, List<MdmsSlot> defaultSlots, Map<String, MdmsHearing> hearingTypeMap) {

        RequestInfo requestInfo = schedulingRequests.getRequestInfo();
        List<ScheduleHearing> hearingList = schedulingRequests.getHearing();

        AuditDetails auditDetails = getAuditDetailsScheduleHearing(requestInfo);
        for (ScheduleHearing hearing : hearingList) {
            hearing.setAuditDetails(auditDetails);
            hearing.setRowVersion(1);
            if (hearing.getStatus()!=null && hearing.getStatus().equals("BLOCKED")) {
                hearing.setHearingBookingId(UUID.randomUUID().toString());
            }
        }

        updateTimingInHearings(hearingList, hearingTypeMap, defaultSlots);

    }


    void updateTimingInHearings(List<ScheduleHearing> hearingList, Map<String, MdmsHearing> hearingTypeMap, List<MdmsSlot> defaultSlots) {

        List<String> statuses = new ArrayList<>();
        statuses.add("SCHEDULED");
        statuses.add("BLOCKED");
        HashMap<String, List<ScheduleHearing>> sameDayHearings = new HashMap<>();
        for (ScheduleHearing hearing : hearingList) {

            ScheduleHearingSearchCriteria searchCriteria = ScheduleHearingSearchCriteria.builder()
                    .judgeId(hearing.getJudgeId())
                    .startDateTime(hearing.getStartTime())
                    .endDateTime(hearing.getEndTime())
                    .status(statuses).build();

            List<ScheduleHearing> hearings;
            hearings = repository.getHearings(searchCriteria, null, null);
            Integer hearingTime = hearingTypeMap.get(hearing.getHearingType()).getHearingTime();
            updateHearingTime(hearing, defaultSlots, hearings, hearingTime);


        }

    }


    public void enrichUpdateScheduleHearing(RequestInfo requestInfo, List<ScheduleHearing> hearingList) {

        hearingList.forEach((hearing) -> {

            Long currentTime = System.currentTimeMillis();
            hearing.getAuditDetails().setLastModifiedTime(currentTime);
            hearing.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
            hearing.setRowVersion(hearing.getRowVersion() + 1);

        });

    }

    private AuditDetails getAuditDetailsScheduleHearing(RequestInfo requestInfo) {

        return AuditDetails.builder().createdBy(requestInfo.getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(requestInfo.getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();

    }

    void updateHearingTime(ScheduleHearing hearing, List<MdmsSlot> slots, List<ScheduleHearing> scheduledHearings, int hearingDuration) {
        long startTime = hearing.getStartTime();

        LocalDate date = dateUtil.getLocalDateFromEpoch(startTime);

        for (MdmsSlot slot : slots) {
            LocalTime currentStartTime = dateUtil.getLocalTime(slot.getSlotStartTime());

            boolean flag = true;
            while (!currentStartTime.isAfter(dateUtil.getLocalTime(slot.getSlotEndTime()))) {
                LocalTime currentEndTime = currentStartTime.plusMinutes(hearingDuration);
                hearing.setStartTime(dateUtil.getEpochFromLocalDateTime(LocalDateTime.of(date, currentStartTime)));
                hearing.setEndTime(dateUtil.getEpochFromLocalDateTime(LocalDateTime.of(date, currentEndTime)));

                if (canScheduleHearings(hearing, scheduledHearings, slots)) {
                    // Hearing scheduled successfully
                    flag = false;
                    break;
                }
                currentStartTime = currentStartTime.plusMinutes(1); // Move to the next time slot
            }
            if (!flag) break;
        }
    }


    boolean canScheduleHearings(ScheduleHearing newHearing, List<ScheduleHearing> scheduledHearings, List<MdmsSlot> slots) {
        // Check if new Hearings overlaps with existing Hearings and fits within any of the slots
        for (ScheduleHearing hearing : scheduledHearings) {
            if (newHearing.overlapsWith(hearing)) {
                return false;
            }

        }
        for (MdmsSlot slot : slots) {

            // later we can directly compare long
            LocalDateTime hearingEndTime = dateUtil.getLocalDateTimeFromEpoch(newHearing.getEndTime());
            LocalDateTime slotStart = dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(newHearing.getStartTime()), slot.getSlotStartTime());
            LocalDateTime slotEnd = dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(newHearing.getEndTime()), slot.getSlotEndTime());

            if (hearingEndTime.isAfter(slotStart) && hearingEndTime.isBefore(slotEnd)) {
                return true;
            }
        }
        return false;
    }


    public void enrichBulkReschedule(ScheduleHearingRequest request, List<MdmsSlot> defaultHearings, Map<String, MdmsHearing> hearingTypeMap) {

        List<ScheduleHearing> hearing = request.getHearing();

        hearing.forEach((element) -> {

            Long currentTime = System.currentTimeMillis();
            element.getAuditDetails().setLastModifiedTime(currentTime);
            element.getAuditDetails().setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
            element.setRowVersion(element.getRowVersion() + 1);

        });

        updateTimingInHearings(hearing, hearingTypeMap, defaultHearings);
    }


}
