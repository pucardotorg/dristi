package digit.validator;

import digit.util.DateUtil;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReScheduleRequestValidatorTest {

    @InjectMocks
    private ReScheduleRequestValidator validator;

    @Mock
    private DateUtil dateUtil;

    private BulkReScheduleHearingRequest request;
    private BulkReschedulingOfHearings buklRescheduling;
    @BeforeEach
    void setUp() {
        request = new BulkReScheduleHearingRequest();
        buklRescheduling = new BulkReschedulingOfHearings();
        buklRescheduling.setJudgeId("judgeId");
        buklRescheduling.setStartTime(1722501060000L);
        buklRescheduling.setEndTime(1722501120000L);
        buklRescheduling.setTenantId("tenantId");
        buklRescheduling.setScheduleAfter(1722501060000L);

        request.setRequestInfo(new RequestInfo());
        request.setBulkRescheduling(buklRescheduling);
    }


    @Test
    public void validateBulkRescheduleRequest_Success() {
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getStartTime())).thenReturn(LocalDateTime.now().plusHours(2));
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getEndTime())).thenReturn(LocalDateTime.now().plusHours(4));
        when(dateUtil.getLocalDateFromEpoch(buklRescheduling.getScheduleAfter())).thenReturn(LocalDate.now().plusDays(1));

        validator.validateBulkRescheduleRequest(request);
    }

    @Test
    public void validateBulkRescheduleRequest_Exception() {
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getStartTime())).thenReturn(LocalDateTime.now().minusDays(2));

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateBulkRescheduleRequest(request);
        });

        assertEquals("DK_SH_APP_ERR", exception.getCode());
    }

    @Test
    public void validateBulkRescheduleRequest_Exception2() {
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getStartTime())).thenReturn(LocalDateTime.now().plusHours(2));
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getEndTime())).thenReturn(LocalDateTime.now().minusHours(2));

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateBulkRescheduleRequest(request);
        });

        assertEquals("DK_SH_APP_ERR", exception.getCode());
    }

    @Test
    public void validateBulkRescheduleRequest_Exception3() {
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getStartTime())).thenReturn(LocalDateTime.now().plusHours(2));
        when(dateUtil.getLocalDateTimeFromEpoch(buklRescheduling.getEndTime())).thenReturn(LocalDateTime.now().plusHours(4));
        when(dateUtil.getLocalDateFromEpoch(buklRescheduling.getScheduleAfter())).thenReturn(LocalDate.now().minusDays(1));

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateBulkRescheduleRequest(request);
        });

        assertEquals("DK_SH_APP_ERR", exception.getCode());
    }
}
