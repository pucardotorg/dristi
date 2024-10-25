package digit.task;

import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.kafka.producer.Producer;
import digit.repository.ReScheduleRequestRepository;
import digit.repository.RescheduleRequestOptOutRepository;
import digit.service.UserService;
import digit.service.hearing.OptOutProcessor;
import digit.util.DateUtil;
import digit.util.MasterDataUtil;
import digit.util.PendingTaskUtil;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestOptOutScheduleTaskTest {

    @Mock
    private ReScheduleRequestRepository reScheduleRepository;

    @Mock
    private RescheduleRequestOptOutRepository requestOptOutRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Mock
    private MasterDataUtil mdmsUtil;

    @Mock
    private OptOutProcessor optOutProcessor;

    @Mock
    private DateUtil dateUtil;

    @Mock
    private PendingTaskUtil pendingTaskUtil;

    @Mock
    private ServiceConstants constants;

    @Mock
    private UserService userService;

    @InjectMocks
    private RequestOptOutScheduleTask requestOptOutScheduleTask;

    @Test
    public void updateAvailableDatesFromOptOuts_success() {
        SchedulerConfig schedulerConfig = new SchedulerConfig();
        schedulerConfig.setIdentifier("OPT_OUT_DUE");
        schedulerConfig.setUnit(1);
        Long dueDate = LocalDate.now().plusDays(1).toEpochDay();

        ReScheduleHearing reScheduleHearing = new ReScheduleHearing();
        reScheduleHearing.setRescheduledRequestId("rescheduledRequestId");
        reScheduleHearing.setJudgeId("judgeId");
        reScheduleHearing.setCaseId("caseId");
        reScheduleHearing.setTenantId("tenantId");
        reScheduleHearing.setSuggestedDates(List.of(1L, 2L, 3L));

        OptOut optOut = new OptOut();
        optOut.setTenantId("tenantId");
        optOut.setJudgeId("judgeId");
        optOut.setCaseId("caseId");
        optOut.setRescheduleRequestId("rescheduledRequestId");
        optOut.setOptoutDates(List.of(1L));

        PendingTaskRequest pendingTaskRequest = new PendingTaskRequest();
        pendingTaskRequest.setRequestInfo(new RequestInfo());
        pendingTaskRequest.setPendingTask(new PendingTask());

        when(mdmsUtil.getDataFromMDMS(any(), anyString(), anyString())).thenReturn(Collections.singletonList(schedulerConfig));
        when(dateUtil.getEpochFromLocalDateTime(any())).thenReturn(dueDate);
        when(config.getEgovStateTenantId()).thenReturn("tenantId");
        when(reScheduleRepository.getReScheduleRequest(any(), any(), any())).thenReturn(List.of(reScheduleHearing));
        when(requestOptOutRepository.getOptOut(any(), any(), any())).thenReturn(List.of(optOut));

        requestOptOutScheduleTask.updateAvailableDatesFromOptOuts();
    }

    @Test
    void updateAvailableDatesFromOptOuts_exception() {
        when(mdmsUtil.getDataFromMDMS(any(), anyString(), anyString())).thenReturn(null);
        CustomException exception = assertThrows(CustomException.class, () -> requestOptOutScheduleTask.updateAvailableDatesFromOptOuts());
        assertEquals("DK_SH_APP_ERR", exception.getCode());
        assertEquals("Error in setting available dates.", exception.getMessage());

    }
}
