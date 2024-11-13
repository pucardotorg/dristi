package digit.enrichment;

import digit.config.Configuration;
import digit.models.coremodels.AuditDetails;
import digit.repository.HearingRepository;
import digit.util.DateUtil;
import digit.util.IdgenUtil;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingEnrichmentTest {

    @InjectMocks
    private HearingEnrichment hearingEnrichment;


    @Mock
    private HearingRepository repository;

    @Mock
    private Configuration configuration;

    @Mock
    private DateUtil dateUtil;

    @Test
    void testEnrichScheduleHearing() {
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setUuid("test-uuid");
        requestInfo.setUserInfo(user);

        ScheduleHearing hearing1 = new ScheduleHearing();
        hearing1.setTenantId("tenantId1");
        hearing1.setHearingBookingId("hearingId1");
        hearing1.setHearingDate(LocalDate.now().toEpochDay());
        hearing1.setJudgeId("judge1");
        hearing1.setHearingType("ADMISSION");

        ScheduleHearing hearing2 = new ScheduleHearing();
        hearing2.setTenantId("tenantId1");
        hearing2.setHearingBookingId("hearingId2");
        hearing2.setHearingDate(LocalDate.now().toEpochDay());
        hearing2.setJudgeId("judge1");
        hearing2.setHearingType("ADMISSION");

        List<ScheduleHearing> hearingList = Arrays.asList(hearing1, hearing2);

        ScheduleHearingRequest schedulingRequests = new ScheduleHearingRequest();
        schedulingRequests.setRequestInfo(requestInfo);
        schedulingRequests.setHearing(hearingList);

        List<MdmsSlot> defaultSlots = new ArrayList<>();
        Map<String, MdmsHearing> hearingTypeMap = new HashMap<>();
        MdmsHearing mdmsHearing = new MdmsHearing();
        mdmsHearing.setHearingTime(30);
        hearingTypeMap.put("ADMISSION", mdmsHearing);

        hearingEnrichment.enrichScheduleHearing(schedulingRequests, defaultSlots, hearingTypeMap);

        assertNotNull(hearing1.getAuditDetails());
        assertNotNull(hearing2.getAuditDetails());
        assertEquals("hearingId1", hearing1.getHearingBookingId());
        assertEquals("hearingId2", hearing2.getHearingBookingId());
        assertEquals(1, hearing1.getRowVersion());
        assertEquals(1, hearing2.getRowVersion());
    }

    @Test
    void testUpdateTimingInHearings() {
        ScheduleHearing hearing1 = new ScheduleHearing();
        hearing1.setHearingDate(LocalDate.now().toEpochDay());
        hearing1.setJudgeId("judge1");
        hearing1.setHearingType("ADMISSION");

        List<ScheduleHearing> hearingList = Collections.singletonList(hearing1);

        List<MdmsSlot> defaultSlots = new ArrayList<>();
        MdmsSlot slot = new MdmsSlot();
        slot.setSlotStartTime("09:00:00");
        slot.setSlotEndTime("17:00:00");
        defaultSlots.add(slot);

        Map<String, MdmsHearing> hearingTypeMap = new HashMap<>();
        MdmsHearing mdmsHearing = new MdmsHearing();
        mdmsHearing.setHearingTime(30);
        hearingTypeMap.put("ADMISSION", mdmsHearing);

        when(repository.getHearings(any(), any(), any())).thenReturn(new ArrayList<>());
        when(dateUtil.getLocalDateTimeFromEpoch(anyLong())).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateFromEpoch(anyLong())).thenReturn(LocalDate.now());
        when(dateUtil.getLocalTime(anyString())).thenReturn(LocalTime.of(10, 0));
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getStartTime()), "09:00:00")).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getEndTime()), "17:00:00")).thenReturn(LocalDateTime.now());

        hearingEnrichment.updateTimingInHearings(hearingList, hearingTypeMap, defaultSlots);

    }

    @Test
    void testEnrichUpdateScheduleHearing() {
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setUuid("test-uuid");
        requestInfo.setUserInfo(user);

        ScheduleHearing hearing1 = new ScheduleHearing();
        AuditDetails auditDetails = new AuditDetails();
        hearing1.setAuditDetails(auditDetails);
        hearing1.setRowVersion(1);

        List<ScheduleHearing> hearingList = Collections.singletonList(hearing1);

        hearingEnrichment.enrichUpdateScheduleHearing(requestInfo, hearingList);

        assertEquals(2, hearing1.getRowVersion());
        assertEquals("test-uuid", hearing1.getAuditDetails().getLastModifiedBy());
        assertNotNull(hearing1.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void testUpdateHearingTime() {
        ScheduleHearing hearing = new ScheduleHearing();
        hearing.setHearingDate(LocalDate.now().toEpochDay());
        hearing.setHearingType("ADMISSION");

        List<MdmsSlot> slots = new ArrayList<>();
        MdmsSlot slot = new MdmsSlot();
        slot.setSlotStartTime("09:00:00");
        slot.setSlotEndTime("17:00:00");
        slots.add(slot);

        List<ScheduleHearing> scheduledHearings = new ArrayList<>();

        when(dateUtil.getLocalDateFromEpoch(anyLong())).thenReturn(LocalDate.now());
        when(dateUtil.getLocalTime(anyString())).thenReturn(LocalTime.of(10, 0));
        when(dateUtil.getEpochFromLocalDateTime(any())).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        when(dateUtil.getLocalDateTimeFromEpoch(anyLong())).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing.getStartTime()), "09:00:00")).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing.getEndTime()), "17:00:00")).thenReturn(LocalDateTime.now());
        hearingEnrichment.updateHearingTime(hearing, slots, scheduledHearings, 30);

        assertNotNull(hearing.getStartTime());
        assertNotNull(hearing.getEndTime());
    }

    @Test
    void testCanScheduleHearings() {
        ScheduleHearing hearing1 = new ScheduleHearing();
        hearing1.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)).toEpochSecond(ZoneOffset.UTC));
        hearing1.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 30)).toEpochSecond(ZoneOffset.UTC));

        ScheduleHearing hearing2 = new ScheduleHearing();
        hearing2.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)).toEpochSecond(ZoneOffset.UTC));
        hearing2.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30)).toEpochSecond(ZoneOffset.UTC));

        List<ScheduleHearing> scheduledHearings = Collections.singletonList(hearing2);

        List<MdmsSlot> slots = new ArrayList<>();
        MdmsSlot slot = new MdmsSlot();
        slot.setSlotStartTime("09:00:00");
        slot.setSlotEndTime("17:00:00");
        slots.add(slot);
        when(dateUtil.getLocalDateTimeFromEpoch(anyLong())).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getStartTime()), "09:00:00")).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getEndTime()), "17:00:00")).thenReturn(LocalDateTime.now());

        boolean canSchedule = hearingEnrichment.canScheduleHearings(hearing1, scheduledHearings, slots);

        assertFalse(canSchedule);
    }

    @Test
    void testEnrichBulkReschedule() {
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setUuid("test-uuid");
        requestInfo.setUserInfo(user);

        ScheduleHearing hearing1 = new ScheduleHearing();
        AuditDetails auditDetails = new AuditDetails();
        hearing1.setAuditDetails(auditDetails);
        hearing1.setRowVersion(1);
        hearing1.setHearingDate(LocalDate.now().toEpochDay());
        hearing1.setJudgeId("judge1");
        hearing1.setHearingType("ADMISSION");

        List<ScheduleHearing> hearingList = Collections.singletonList(hearing1);

        ScheduleHearingRequest request = new ScheduleHearingRequest();
        request.setRequestInfo(requestInfo);
        request.setHearing(hearingList);

        List<MdmsSlot> defaultSlots = new ArrayList<>();
        MdmsSlot slot = new MdmsSlot();
        slot.setSlotStartTime("09:00:00");
        slot.setSlotEndTime("17:00:00");
        defaultSlots.add(slot);

        Map<String, MdmsHearing> hearingTypeMap = new HashMap<>();
        MdmsHearing mdmsHearing = new MdmsHearing();
        mdmsHearing.setHearingTime(30);
        hearingTypeMap.put("ADMISSION", mdmsHearing);

        when(repository.getHearings(any(), any(), any())).thenReturn(new ArrayList<>());
        when(dateUtil.getLocalTime(anyString())).thenReturn(LocalTime.of(10, 0));
        when(dateUtil.getLocalDateFromEpoch(anyLong())).thenReturn(LocalDate.now());
        when(dateUtil.getLocalDateTimeFromEpoch(anyLong())).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getStartTime()), "09:00:00")).thenReturn(LocalDateTime.now());
        when(dateUtil.getLocalDateTime(dateUtil.getLocalDateTimeFromEpoch(hearing1.getEndTime()), "17:00:00")).thenReturn(LocalDateTime.now());

        hearingEnrichment.enrichBulkReschedule(request, defaultSlots, hearingTypeMap);

        assertEquals(2, hearing1.getRowVersion());
        assertEquals("test-uuid", hearing1.getAuditDetails().getLastModifiedBy());
        assertNotNull(hearing1.getAuditDetails().getLastModifiedTime());
        assertNotNull(hearing1.getStartTime());
        assertNotNull(hearing1.getEndTime());
    }
}

