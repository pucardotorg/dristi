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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HearingProcessorTest {

    @Mock
    private CustomMapper customMapper;

    @Mock
    private HearingService hearingService;

    @Mock
    private DateUtil dateUtil;

    @Mock
    private HearingUtil hearingUtil;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @InjectMocks
    private HearingProcessor hearingProcessor;

    @Test
    void testProcessCreateHearingRequest() {
        // Arrange
        Hearing hearing = mock(Hearing.class);
        PresidedBy presidedBy = mock(PresidedBy.class);
        RequestInfo requestInfo = mock(RequestInfo.class);

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setHearing(hearing);
        hearingRequest.setRequestInfo(requestInfo);

        when(hearing.getPresidedBy()).thenReturn(presidedBy);
        when(hearing.getFilingNumber()).thenReturn(Collections.singletonList("case1"));
        when(presidedBy.getJudgeID()).thenReturn(Collections.singletonList("judge1"));
        when(presidedBy.getCourtID()).thenReturn("court1");
        when(config.getScheduleHearingTopic()).thenReturn("topic1");
        Pair<Long, Long> startTimeAndEndTime = new Pair<>(12345L, 67890L);
        when(hearing.getStartTime()).thenReturn(12345L);
        when(customMapper.hearingToScheduleHearingConversion(any())).thenReturn(new ScheduleHearing());
        when(dateUtil.getLocalDateFromEpoch(anyLong())).thenReturn(LocalDate.now());
        when(dateUtil.getEPochFromLocalDate(any())).thenReturn(12345L, 67890L);
        when(hearingService.schedule(any())).thenReturn(Collections.singletonList(new ScheduleHearing()));

        // Act
        hearingProcessor.processCreateHearingRequest(hearingRequest);

        // Assert
        verify(hearingService, times(1)).schedule(any(ScheduleHearingRequest.class));
        verify(hearingUtil, times(1)).callHearing(any(HearingUpdateBulkRequest.class));
        verify(producer, times(1)).push(anyString(), any(Object.class));

        ArgumentCaptor<ScheduleHearingRequest> requestCaptor = ArgumentCaptor.forClass(ScheduleHearingRequest.class);
        verify(hearingService).schedule(requestCaptor.capture());
        ScheduleHearingRequest capturedRequest = requestCaptor.getValue();
        assertEquals("case1", capturedRequest.getHearing().get(0).getCaseId());
        assertEquals("judge1", capturedRequest.getHearing().get(0).getJudgeId());
        assertEquals("court1", capturedRequest.getHearing().get(0).getCourtId());
    }

//    @Test
//    void testGetStartTimeAndEndTime() {
//        // Arrange
//        long epochTime = 12345L;
//        LocalDate localDate = LocalDate.now();
//        when(dateUtil.getLocalDateFromEpoch(epochTime)).thenReturn(localDate);
//        when(dateUtil.getEPochFromLocalDate(localDate)).thenReturn(12345L);
//        when(dateUtil.getEPochFromLocalDate(localDate.plusDays(1))).thenReturn(67890L);
//
//        // Act
//        Pair<Long, Long> result = hearingProcessor.getStartTimeAndEndTime(epochTime);
//
//        // Assert
//        assertEquals(12345L, result.getKey());
//        assertEquals(67890L, result.getValue());
//    }
}
