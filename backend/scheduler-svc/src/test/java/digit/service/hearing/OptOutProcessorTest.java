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
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static digit.config.ServiceConstants.ACTIVE;
import static digit.config.ServiceConstants.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptOutProcessorTest {

    @Mock
    private Producer producer;

    @Mock
    private ReScheduleRequestRepository repository;

    @Mock
    private Configuration configuration;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private HearingService hearingService;

    @Mock
    private RescheduleRequestOptOutService optOutService;

    @Mock
    private PendingTaskUtil pendingTaskUtil;

    @InjectMocks
    private OptOutProcessor optOutProcessor;

    @Mock
    private UserService userService;

    @Test
    public void testCheckAndScheduleHearingForOptOut_Success_LastOptOut() {
        // Arrange
        HashMap<String, Object> record = new HashMap<>();
        OptOut optOut = createOptOut();
        OptOutRequest optOutRequest = OptOutRequest.builder().optOut(optOut).build();
        when(mapper.convertValue(record, OptOutRequest.class)).thenReturn(optOutRequest);

        OptOutSearchRequest searchRequest = createOptOutSearchRequest(optOut);
        when(optOutService.search(any(OptOutSearchRequest.class), any(), any())).thenReturn(Collections.singletonList(optOut));

        ReScheduleHearing reScheduleHearing = createReScheduleHearing();
        when(repository.getReScheduleRequest(any(ReScheduleHearingReqSearchCriteria.class), any(), any()))
                .thenReturn(Collections.singletonList(reScheduleHearing));

        String updateTopic = "updateRescheduleRequestTopic";
        when(configuration.getUpdateRescheduleRequestTopic()).thenReturn(updateTopic);

        // Act
        optOutProcessor.checkAndScheduleHearingForOptOut(record);

        // Assert
        assertEquals(ACTIVE, reScheduleHearing.getStatus());
        verify(producer).push(eq(updateTopic), any(ReScheduleHearingRequest.class));
    }

    @Test
    public void testCheckAndScheduleHearingForOptOut_Success_MultipleOptOutsRemaining() {
        // Arrange
        HashMap<String, Object> record = new HashMap<>();
        OptOut optOut = createOptOut();
        OptOutRequest optOutRequest = OptOutRequest.builder().optOut(optOut).build();
        when(mapper.convertValue(record, OptOutRequest.class)).thenReturn(optOutRequest);

        OptOutSearchRequest searchRequest = createOptOutSearchRequest(optOut);
        when(optOutService.search(any(OptOutSearchRequest.class), any(), any())).thenReturn(Collections.emptyList());

        ReScheduleHearing reScheduleHearing = createReScheduleHearing();
        when(repository.getReScheduleRequest(any(ReScheduleHearingReqSearchCriteria.class), any(), any()))
                .thenReturn(Collections.singletonList(reScheduleHearing));

        String updateTopic = "updateRescheduleRequestTopic";
        when(configuration.getUpdateRescheduleRequestTopic()).thenReturn(updateTopic);

        // Act
        optOutProcessor.checkAndScheduleHearingForOptOut(record);

        // Assert
        assertEquals(ACTIVE, reScheduleHearing.getStatus());
        verify(producer).push(eq(updateTopic), any(ReScheduleHearingRequest.class));
    }

    @Test
    public void testCheckAndScheduleHearingForOptOut_Failure() {
        // Arrange
        HashMap<String, Object> record = new HashMap<>();
        when(mapper.convertValue(record, OptOut.class)).thenThrow(new RuntimeException("Conversion failed"));

        // Act & Assert
        optOutProcessor.checkAndScheduleHearingForOptOut(record);
        verify(producer, never()).push(anyString(), anyList());

    }

    @Test
    public void testUnblockJudgeCalendarForSuggestedDays_Success() {
        // Arrange
        ReScheduleHearing reScheduleHearing = createReScheduleHearing();
        List<ScheduleHearing> scheduleHearings = createScheduleHearings();

        when(hearingService.search(any(HearingSearchRequest.class), any(), any())).thenReturn(scheduleHearings);

        String scheduleUpdateTopic = "scheduleHearingUpdateTopic";
        when(configuration.getScheduleHearingUpdateTopic()).thenReturn(scheduleUpdateTopic);

        // Act
        optOutProcessor.unblockJudgeCalendarForSuggestedDays(reScheduleHearing);

        // Assert
        assertEquals(INACTIVE, scheduleHearings.get(0).getStatus());
        verify(producer).push(eq(scheduleUpdateTopic), any(ScheduleHearingRequest.class));
    }

    @Test
    public void testUnblockJudgeCalendarForSuggestedDays_Failure() {
        // Arrange
        ReScheduleHearing reScheduleHearing = createReScheduleHearing();
        when(hearingService.search(any(HearingSearchRequest.class), any(), any())).thenThrow(new RuntimeException("Search failed"));

        // Act & Assert
        optOutProcessor.unblockJudgeCalendarForSuggestedDays(reScheduleHearing);
    }

    private OptOut createOptOut() {
        return OptOut.builder()
                .optoutDates(Collections.singletonList(1625126400000L))
                .rescheduleRequestId("test-reschedule-id")
                .individualId("test-individual-id")
                .build();
    }

    private OptOutSearchRequest createOptOutSearchRequest(OptOut optOut) {
        return OptOutSearchRequest.builder()
                .requestInfo(new RequestInfo())
                .criteria(OptOutSearchCriteria.builder()
                        .rescheduleRequestId(optOut.getRescheduleRequestId())
                        .build()).build();
    }

    private ReScheduleHearing createReScheduleHearing() {
        return ReScheduleHearing.builder()
                .rescheduledRequestId("test-reschedule-id")
                .tenantId("test-tenant-id")
                .suggestedDates(Arrays.asList(1625126400000L, 1625212800000L))
                .availableDates(Arrays.asList(1625212800000L))
                .litigants(Set.of("litigant-1"))
                .representatives(Set.of("representative-1"))
                .build();
    }

    private PendingTask createPendingTask() {
        return PendingTask.builder().build();
    }

    private List<ScheduleHearing> createScheduleHearings() {
        ScheduleHearing scheduleHearing = ScheduleHearing.builder()
                .hearingDate(1625126400000L)
                .status(ACTIVE)
                .build();

        return Collections.singletonList(scheduleHearing);
    }
}
