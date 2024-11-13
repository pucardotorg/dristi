package digit.service;

import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.enrichment.HearingEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.HearingRepository;
import digit.util.HearingUtil;
import digit.util.MasterDataUtil;
import digit.web.models.*;
import digit.web.models.hearing.Hearing;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingServiceTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceConstants serviceConstants;

    @Mock
    private Producer producer;

    @Mock
    private HearingEnrichment enrichment;

    @Mock
    private MasterDataUtil helper;

    @Mock
    private HearingSearchRequest hearingSearchRequest;

    @Mock
    private HearingRepository hearingRepository;

    @Mock
    private HearingUtil hearingUtil;

    @InjectMocks
    private HearingService hearingService;

    @Test
    void testSchedule() {
        ScheduleHearingRequest schedulingRequests = new ScheduleHearingRequest();
        ScheduleHearing hearing = new ScheduleHearing();
        schedulingRequests.setHearing(List.of(hearing));

        MdmsSlot slot = new MdmsSlot();
        slot.setSlotDuration(120); // in minutes
        List<MdmsSlot> defaultSlots = List.of(slot);

        MdmsHearing hearingData = new MdmsHearing();
        hearingData.setHearingType("default");
        List<MdmsHearing> defaultHearings = List.of(hearingData);
        Map<String, MdmsHearing> hearingTypeMap = defaultHearings.stream().collect(Collectors.toMap(
                MdmsHearing::getHearingType,
                obj -> obj
        ));

        when(helper.getDataFromMDMS(MdmsSlot.class, serviceConstants.DEFAULT_SLOTTING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME)).thenReturn(defaultSlots);
        when(helper.getDataFromMDMS(MdmsHearing.class, serviceConstants.DEFAULT_HEARING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME)).thenReturn(defaultHearings);

        List<ScheduleHearing> hearingList = hearingService.schedule(schedulingRequests);

        double totalHrs = defaultSlots.stream().reduce(0.0, (total, slotData) -> total + slotData.getSlotDuration() / 60.0, Double::sum);

        verify(enrichment, times(1)).enrichScheduleHearing(schedulingRequests, defaultSlots, hearingTypeMap);

        assertEquals(schedulingRequests.getHearing(), hearingList);
    }

    @Test
    void testUpdate() {
        ScheduleHearingRequest scheduleHearingRequest = new ScheduleHearingRequest();
        RequestInfo requestInfo = new RequestInfo();
        ScheduleHearing hearing = new ScheduleHearing();
        scheduleHearingRequest.setRequestInfo(requestInfo);
        scheduleHearingRequest.setHearing(List.of(hearing));

        when(config.getScheduleHearingUpdateTopic()).thenReturn("scheduleHearingUpdateTopic");

        List<ScheduleHearing> result = hearingService.update(scheduleHearingRequest);

        verify(enrichment, times(1)).enrichUpdateScheduleHearing(requestInfo, scheduleHearingRequest.getHearing());
        verify(producer, times(1)).push(eq("scheduleHearingUpdateTopic"), any());

        assertEquals(1, result.size());
        assertEquals(hearing, result.get(0));
    }

    @Test
    void testSearch() {
        List<ScheduleHearing> result = hearingService.search(hearingSearchRequest,0,10);
        assertNotNull(result);
    }

    @Test
    void testUpdateBulk() {
        ScheduleHearingRequest request = new ScheduleHearingRequest();
        ScheduleHearing hearing = new ScheduleHearing();
        request.setHearing(List.of(hearing));

        MdmsSlot slot = new MdmsSlot();
        List<MdmsSlot> defaultSlot = List.of(slot);

        MdmsHearing hearingData = new MdmsHearing();
        Map<String, MdmsHearing> hearingTypeMap = Map.of("default", hearingData);

        when(config.getScheduleHearingUpdateTopic()).thenReturn("scheduleHearingUpdateTopic");

        List<ScheduleHearing> result = hearingService.updateBulk(request, defaultSlot, hearingTypeMap);

        verify(enrichment, times(1)).enrichBulkReschedule(request, defaultSlot, hearingTypeMap);
        verify(producer, times(1)).push(eq("scheduleHearingUpdateTopic"), any());

        assertEquals(1, result.size());
        assertEquals(hearing, result.get(0));
    }

    @Test
    void testUpdateHearing() {
        UpdateHearingRequest request = new UpdateHearingRequest();
        ScheduleHearing hearing = new ScheduleHearing();
        hearing.setHearingBookingId("123");
        hearing.setRescheduleRequestId("reschedule123");
        request.setHearing(hearing);

        ScheduleHearing existingHearing = new ScheduleHearing();
        existingHearing.setHearingBookingId("123");

        when(hearingRepository.getHearings(any(), any(), any())).thenReturn(List.of(existingHearing));
        when(config.getScheduleHearingUpdateTopic()).thenReturn("scheduleHearingUpdateTopic");
        when(hearingUtil.fetchHearing(any())).thenReturn(List.of(new Hearing()));

        ScheduleHearing result = hearingService.updateHearing(request);

        verify(producer, times(2)).push(eq("scheduleHearingUpdateTopic"), any());

        assertNotNull(result);
        assertEquals(existingHearing.getHearingBookingId(), result.getHearingBookingId());
    }

    @Test
    void testGetAvailableDateForHearing() {
        ScheduleHearingSearchCriteria criteria = new ScheduleHearingSearchCriteria();
        List<AvailabilityDTO> expectedDates = List.of(new AvailabilityDTO());

        when(hearingRepository.getAvailableDatesOfJudges(criteria)).thenReturn(expectedDates);

        List<AvailabilityDTO> result = hearingService.getAvailableDateForHearing(criteria);

        assertNotNull(result);
        assertEquals(expectedDates, result);
        verify(hearingRepository, times(1)).getAvailableDatesOfJudges(criteria);
    }


}
