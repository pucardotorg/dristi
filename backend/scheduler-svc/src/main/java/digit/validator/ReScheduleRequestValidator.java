package digit.validator;

import digit.util.DateUtil;
import digit.web.models.BulkRescheduleRequest;
import digit.web.models.BulkReschedule;
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


    public void validateBulkRescheduleRequest(BulkRescheduleRequest request) {


    }
}
