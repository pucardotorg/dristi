package digit.service;

import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.kafka.producer.Producer;
import digit.repository.CauseListRepository;
import digit.repository.HearingRepository;
import digit.util.MdmsUtil;
import digit.util.PdfServiceUtil;
import digit.web.models.*;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ByteArrayResource;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CauseListServiceTest {

    @InjectMocks
    private CauseListService causeListService;

    @Mock
    private HearingRepository hearingRepository;

    @Mock
    private CauseListRepository causeListRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Mock
    private PdfServiceUtil pdfServiceUtil;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ServiceConstants serviceConstants;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateCauseListForTomorrow_noHearings() {
        // Mocking
        when(hearingRepository.getHearings(any(), any(), any())).thenReturn(Collections.emptyList());

        // Test
        causeListService.updateCauseListForTomorrow();

        // Verify
        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void testUpdateCauseListForTomorrow_withHearings() {
        // Mocking
        List<ScheduleHearing> hearings = new ArrayList<>();
        hearings.add(ScheduleHearing.builder().judgeId("judge001").hearingType("ADMISSION").build());
        when(hearingRepository.getHearings(any(), any(), any())).thenReturn(hearings);
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(getMockMdmsData());
        when(config.getCauseListInsertTopic()).thenReturn("causeListInsertTopic");

        // Test
        causeListService.updateCauseListForTomorrow();

    }

    @Test
    void testViewCauseListForTomorrow_validDate() {
        // Mocking
        CauseListSearchCriteria criteria = CauseListSearchCriteria.builder().searchDate(LocalDate.now().plusDays(1)).build();
        CauseListSearchRequest request = CauseListSearchRequest.builder().causeListSearchCriteria(criteria).build();
        List<CauseList> expectedCauseLists = new ArrayList<>();
        when(causeListRepository.getCauseLists(any())).thenReturn(expectedCauseLists);

        // Test
        List<CauseList> causeLists = causeListService.viewCauseListForTomorrow(request);

        // Verify
        assertEquals(expectedCauseLists, causeLists);
    }

    @Test
    void testViewCauseListForTomorrow_invalidDate() {
        // Mocking
        CauseListSearchCriteria criteria = CauseListSearchCriteria.builder().searchDate(LocalDate.now().plusDays(2)).build();
        CauseListSearchRequest request = CauseListSearchRequest.builder().causeListSearchCriteria(criteria).build();

        // Test & Verify
        assertThrows(CustomException.class, () -> causeListService.viewCauseListForTomorrow(request));
    }

    @Test
    void testDownloadCauseListForTomorrow() {
        // Mocking
        CauseListSearchCriteria criteria = CauseListSearchCriteria.builder().searchDate(LocalDate.now().plusDays(1)).build();
        CauseListSearchRequest request = CauseListSearchRequest.builder().causeListSearchCriteria(criteria).requestInfo(RequestInfo.builder().userInfo(new User()).build()).build();
        List<CauseList> causeLists = new ArrayList<>();
        when(causeListRepository.getCauseLists(any())).thenReturn(causeLists);
        when(pdfServiceUtil.generatePdfFromPdfService(any(), any(), any())).thenReturn(new ByteArrayResource(new byte[0]));
        when(config.getCauseListPdfTemplateKey()).thenReturn("causeListPdfTemplateKey");

        // Test
        ByteArrayResource resource = causeListService.downloadCauseListForTomorrow(request);

        // Verify
        assertNotNull(resource);
    }

//    @Test
//    void testGenerateCauseListForJudge_withHearings() {
//        // Mocking
//        List<ScheduleHearing> hearings = new ArrayList<>();
//        hearings.add(ScheduleHearing.builder().judgeId("judge001").eventType(new EventType("event1")).build());
//        when(hearingRepository.getHearings(any(), any(), any())).thenReturn(hearings);
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(getMockMdmsData());
//
//        // Test
//        List<CauseList> causeLists = new ArrayList<>();
//        causeListService.generateCauseListForJudge("judge001", causeLists);
//
//        // Verify
//        assertFalse(CollectionUtils.isEmpty(causeLists));
//    }

    private Map<String, Map<String, JSONArray>> getMockMdmsData() {
        Map<String, Map<String, JSONArray>> data = new HashMap<>();
        Map<String, JSONArray> innerMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new HashMap<>());
        innerMap.put("hearings", jsonArray);
        data.put("court", innerMap);
        return data;
    }
}

