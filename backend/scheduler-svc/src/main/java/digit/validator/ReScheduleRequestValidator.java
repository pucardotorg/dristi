package digit.validator;

import digit.util.DateUtil;
import digit.web.models.BulkReScheduleHearingRequest;
import digit.web.models.BulkReschedulingOfHearings;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
@Slf4j
public class ReScheduleRequestValidator {

    private final DateUtil dateUtil;

    @Autowired
    public ReScheduleRequestValidator(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }


    public void validateBulkRescheduleRequest(BulkReScheduleHearingRequest request) {

        log.info("operation = validateBulkRescheduleRequest, result = IN_PROGRESS");
        BulkReschedulingOfHearings bulkRescheduling = request.getBulkRescheduling();

        LocalDateTime currentDateTime = LocalDateTime.now();

        Long startTime = bulkRescheduling.getStartTime();
        Long endTime = bulkRescheduling.getEndTime();
        if (startTime != null && endTime != null) {

            LocalDateTime startLocalDateTime = dateUtil.getLocalDateTimeFromEpoch(startTime);
            if (startLocalDateTime.isBefore(currentDateTime)) {
                throw new CustomException("DK_SH_APP_ERR", "Can not reschedule for past date hearings");
            }


            LocalDateTime endLocalDateTime = dateUtil.getLocalDateTimeFromEpoch(endTime);

            if (endLocalDateTime.isBefore(startLocalDateTime)) {
                throw new CustomException("DK_SH_APP_ERR", "end time is before start time");
            }

        }


        Long scheduleAfter = bulkRescheduling.getScheduleAfter();

        LocalDate scheduleAfterDate = dateUtil.getLocalDateFromEpoch(scheduleAfter);
        if (scheduleAfterDate.isBefore(LocalDate.now())) {
            throw new CustomException("DK_SH_APP_ERR", "can not reschedule for past dates");
        }
        log.info("operation = validateBulkRescheduleRequest, result = SUCCESS");

    }
}
