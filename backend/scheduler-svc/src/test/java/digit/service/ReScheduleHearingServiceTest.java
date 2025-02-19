package digit.service;

import com.fasterxml.jackson.databind.JsonNode;
import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.enrichment.ReScheduleRequestEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.ReScheduleRequestRepository;
import digit.util.CaseUtil;
import digit.util.DateUtil;
import digit.util.MasterDataUtil;
import digit.validator.ReScheduleRequestValidator;
import digit.web.models.*;
import digit.web.models.cases.SearchCaseRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReScheduleHearingServiceTest {

    @InjectMocks
    private ReScheduleHearingService reScheduleHearingService;


    @Mock
    private ReScheduleRequestRepository repository;

    @Mock
    private ReScheduleRequestValidator validator;

    @Mock
    private ReScheduleRequestEnrichment enrichment;

    @Mock
    private Producer producer;

    @Mock
    private HearingService hearingService;

    @Mock
    private CalendarService calendarService;

    @Mock
    private ServiceConstants serviceConstants;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private MasterDataUtil helper;

    @Mock
    private DateUtil dateUtil;

    @Mock
    private Configuration config;

    private ReScheduleHearingRequest reScheduleHearingRequest;

    @BeforeEach
    void setUp() {
        RequestInfo requestInfo = new RequestInfo();
        User user = User.builder().uuid("user-uuid").tenantId("tenant-id").build();
        requestInfo.setUserInfo(user);

        ReScheduleHearing reScheduleHearing = ReScheduleHearing.builder()
                .hearingBookingId("hearingBookingId")
                .judgeId("judgeId")
                .caseId("caseId")
                .tenantId("tenantId")
                .requesterId("requesterId")
                .reason("reason")
                .build();

        reScheduleHearingRequest = ReScheduleHearingRequest.builder()
                .reScheduleHearing(List.of(reScheduleHearing))
                .requestInfo(requestInfo)
                .build();


    }

    @Test
    public void testCreate() {
        doNothing().when(enrichment).enrichRescheduleRequest(any());
        doNothing().when(producer).push(null, reScheduleHearingRequest);
        RequestInfo requestInfo = new RequestInfo();
        List<ReScheduleHearing> result = reScheduleHearingService.create(reScheduleHearingRequest);

        assertEquals(1, result.size());
        verify(enrichment, times(1)).enrichRescheduleRequest(any());
    }

    @Test
    public void testSearch() {
        ReScheduleHearingReqSearchRequest searchRequest = ReScheduleHearingReqSearchRequest.builder()
                .criteria(ReScheduleHearingReqSearchCriteria.builder().build())
                .build();

        when(repository.getReScheduleRequest(any(), any(), any())).thenReturn(List.of(reScheduleHearingRequest.getReScheduleHearing().get(0)));

        List<ReScheduleHearing> result = reScheduleHearingService.search(searchRequest, 10, 0);

        assertEquals(1, result.size());
        verify(repository, times(1)).getReScheduleRequest(any(), any(), any());
    }

    @Test
    void testCreateSuccess() throws Exception {
        // Prepare data for the test
        ReScheduleHearingRequest request = new ReScheduleHearingRequest();
        List<ReScheduleHearing> hearingList = new ArrayList<>();
        ReScheduleHearing hearing = new ReScheduleHearing();
        hearing.setTenantId("tenant1");
        hearing.setCaseId("case1");
        hearing.setJudgeId("judge1");
        hearing.setAvailableAfter(1L);
        hearing.setHearingBookingId("booking1");
        hearingList.add(hearing);
        request.setReScheduleHearing(hearingList);
        request.setRequestInfo(new RequestInfo());

        // Mock enrichment
        doNothing().when(enrichment).enrichRescheduleRequest(any());

        // Mock caseUtil methods
        JsonNode mockCases = mock(JsonNode.class);
        when(caseUtil.getCases(any(SearchCaseRequest.class))).thenReturn(mockCases);
        when(caseUtil.getLitigants(mockCases)).thenReturn(mock(JsonNode.class));
        when(caseUtil.getRepresentatives(mockCases)).thenReturn(mock(JsonNode.class));

        // Mock helper method
        List<SchedulerConfig> mockSchedulerConfig = new ArrayList<>();
        SchedulerConfig schedulerConfig = new SchedulerConfig();
        schedulerConfig.setUnit(2);
        schedulerConfig.setIdentifier("OPT_OUT_SELECTION_LIMIT");
        mockSchedulerConfig.add(schedulerConfig);
        when(helper.getDataFromMDMS(eq(SchedulerConfig.class), anyString(), anyString())).thenReturn(mockSchedulerConfig);

        // Mock calendar service
        List<AvailabilityDTO> availabilityDTOs = new ArrayList<>();
        AvailabilityDTO availabilityDTO = new AvailabilityDTO();
        availabilityDTO.setDate("2023-10-10");
        availabilityDTOs.add(availabilityDTO);
        when(calendarService.getJudgeAvailability(any())).thenReturn(availabilityDTOs);
        when(dateUtil.getLocalDateFromEpoch(anyLong())).thenReturn(LocalDate.of(2023, 9, 5));

        when(dateUtil.getEPochFromLocalDate(any())).thenReturn(1693800000000L);

        // Mock hearing search
        ScheduleHearing scheduleHearing = new ScheduleHearing();
        scheduleHearing.setHearingBookingId("booking1");

        // Mock producer push

        // Call the create method
        List<ReScheduleHearing> result = reScheduleHearingService.create(request);

        // Assertions
        assertNotNull(result);
        verify(enrichment, times(1)).enrichRescheduleRequest(any());
        verify(calendarService, times(1)).getJudgeAvailability(any());
    }

    @Test
    void testCreateWithException() {
        // Prepare data for the test
        ReScheduleHearingRequest request = new ReScheduleHearingRequest();
        List<ReScheduleHearing> hearingList = new ArrayList<>();
        ReScheduleHearing hearing = new ReScheduleHearing();
        hearing.setTenantId("tenant1");
        hearing.setCaseId("case1");
        hearing.setJudgeId("judge1");
        hearingList.add(hearing);
        request.setReScheduleHearing(hearingList);
        request.setRequestInfo(new RequestInfo());

        // Mock enrichment
        doNothing().when(enrichment).enrichRescheduleRequest(any());

        // Mock caseUtil methods to throw an exception
        when(caseUtil.getCases(any())).thenThrow(new RuntimeException("Test Exception"));

        // Call the create method and catch the exception
        assertDoesNotThrow(() -> reScheduleHearingService.create(request));

    }


}
