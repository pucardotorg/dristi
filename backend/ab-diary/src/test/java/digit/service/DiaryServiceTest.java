package digit.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import digit.config.Configuration;
import digit.enrichment.ADiaryEnrichment;
import digit.kafka.Producer;
import digit.repository.DiaryRepository;
import digit.util.ADiaryUtil;
import digit.validators.ADiaryValidator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.egov.common.contract.models.Document;
import digit.web.models.*;
import digit.util.FileStoreUtil;
import digit.util.PdfServiceUtil;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

    @InjectMocks
    private DiaryService diaryService;

    @Mock
    private Producer producer;

    @Mock
    private Configuration configuration;

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private ADiaryValidator validator;

    @Mock
    private ADiaryEnrichment enrichment;

    @InjectMocks
    private DiaryEntryService diaryEntryService;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private ADiaryUtil aDiaryUtil;

    @Mock
    private PdfServiceUtil pdfServiceUtil;

    @Mock
    private WorkflowService workflowService;

    private CaseDiary caseDiary;
    private CaseDiaryRequest caseDiaryRequest;

    @Test
    public void testSearchCaseDiaries_success() {
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(CaseDiarySearchCriteria.builder()
                .judgeId("judge1")
                .build());

        when(diaryRepository.getCaseDiaries(searchRequest)).thenReturn(Collections.singletonList(new CaseDiaryListItem()));

        List<CaseDiaryListItem> result = diaryService.searchCaseDiaries(searchRequest);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test(expected = CustomException.class)
    public void testSearchCaseDiaries_exception() {
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(CaseDiarySearchCriteria.builder()
                .judgeId("judge1")
                .build());

        when(diaryRepository.getCaseDiaries(searchRequest)).thenThrow(new RuntimeException("Service exception"));

        diaryService.searchCaseDiaries(searchRequest);
    }

    @Test
    public void testUpdateDiary_success() {
        CaseDiaryRequest caseDiaryRequest = mock(CaseDiaryRequest.class);
        when(caseDiaryRequest.getDiary()).thenReturn(mock(CaseDiary.class));

        doNothing().when(workflowService).updateWorkflowStatus(caseDiaryRequest);

        CaseDiary result = diaryService.updateDiary(caseDiaryRequest);
        assertNotNull(result);
    }

    @Test(expected = CustomException.class)
    public void testGenerateDiary_exception() {
        CaseDiaryGenerateRequest generateRequest = mock(CaseDiaryGenerateRequest.class);
        CaseDiary caseDiary = mock(CaseDiary.class);
        when(generateRequest.getDiary()).thenReturn(caseDiary);
        when(generateRequest.getRequestInfo()).thenReturn(mock(RequestInfo.class));

        List<CaseDiaryEntry> mockCaseDiaryEntries = new ArrayList<>();
        CaseDiaryEntry entry = new CaseDiaryEntry();
        entry.setHearingDate(System.currentTimeMillis());
        mockCaseDiaryEntries.add(entry);

        when(diaryEntryService.searchDiaryEntries(any())).thenThrow(new RuntimeException("Service exception"));

        diaryService.generateDiary(generateRequest);
    }

    @Test
    public void testBuildCaseDiarySearchRequest() {
        CaseDiaryGenerateRequest generateRequest = new CaseDiaryGenerateRequest();
        CaseDiary caseDiary = new CaseDiary();
        caseDiary.setTenantId("tenantId");
        caseDiary.setDiaryDate(System.currentTimeMillis());
        generateRequest.setDiary(caseDiary);

        CaseDiarySearchRequest result = diaryService.buildCaseDiarySearchRequest(generateRequest,"cmpNumber");
        assertNotNull(result);
        assertNotNull(result.getCriteria());
        assertEquals("tenantId", result.getCriteria().getTenantId());
        assertEquals(caseDiary.getDiaryDate(), result.getCriteria().getDate());
    }

    @Test
    public void testGenerateCaseDiary() {
        CaseDiary caseDiary = new CaseDiary();
        caseDiary.setTenantId("tenantId");
        caseDiary.setDiaryType("aDiary");
        caseDiary.setDiaryDate(System.currentTimeMillis());
        RequestInfo requestInfo = new RequestInfo();
        CaseDiaryGenerateRequest generateRequest = new CaseDiaryGenerateRequest();
        generateRequest.setDiary(caseDiary);
        generateRequest.setRequestInfo(requestInfo);

        List<CaseDiaryEntry> mockCaseDiaryEntries = new ArrayList<>();
        CaseDiaryEntry entry = new CaseDiaryEntry();
        entry.setHearingDate(System.currentTimeMillis());
        mockCaseDiaryEntries.add(entry);

        when(diaryEntryService.searchDiaryEntries(any())).thenReturn(mockCaseDiaryEntries);
        when(fileStoreUtil.saveDocumentToFileStore(any(), any())).thenReturn(new Document());

        diaryService.generateCaseDiary(caseDiary, requestInfo);
    }

    @Test
    public void getCaseDiaryDocument() {
        CaseDiaryGenerateRequest generateRequest = new CaseDiaryGenerateRequest();
        CaseDiary caseDiary = new CaseDiary();
        caseDiary.setTenantId("tenantId");
        caseDiary.setDiaryDate(System.currentTimeMillis());
        generateRequest.setDiary(caseDiary);
        generateRequest.setRequestInfo(new RequestInfo());

        Document document = new Document();
        when(fileStoreUtil.saveDocumentToFileStore(any(), any())).thenReturn(document);

        CaseDiaryDocument caseDiaryDocument = diaryService.getCaseDiaryDocument(generateRequest, document);
        assertNotNull(caseDiaryDocument);
        assertEquals(document.getFileStore(), caseDiaryDocument.getFileStoreId());
    }

    @Test
    public void testGenerateDiary_success() throws Exception {
        CaseDiaryGenerateRequest generateRequest = mock(CaseDiaryGenerateRequest.class);
        RequestInfo requestInfo = mock(RequestInfo.class);
        CaseDiary caseDiary = mock(CaseDiary.class);
        Document document = mock(Document.class);
        ByteArrayResource byteArrayResource = mock(ByteArrayResource.class);

        when(generateRequest.getRequestInfo()).thenReturn(requestInfo);
        when(generateRequest.getDiary()).thenReturn(caseDiary);
        when(caseDiary.getDiaryDate()).thenReturn(System.currentTimeMillis());
        when(caseDiary.getCaseNumber()).thenReturn("12345");
        when(caseDiary.getTenantId()).thenReturn("tenantId");
        when(caseDiary.getDiaryType()).thenReturn("adiary");
        when(caseDiary.getJudgeId()).thenReturn("judgeId");

        List<CaseDiaryEntry> caseDiaryEntries = new ArrayList<>();
        CaseDiaryEntry entry = new CaseDiaryEntry();
        entry.setHearingDate(System.currentTimeMillis());
        caseDiaryEntries.add(entry);

        when(diaryEntryService.searchDiaryEntries(any())).thenReturn(caseDiaryEntries);
        when(fileStoreUtil.saveDocumentToFileStore(byteArrayResource, "tenantId")).thenReturn(document);
        when(document.getFileStore()).thenReturn("fileStoreId");

        when(pdfServiceUtil.generatePdfFromPdfService(any(), eq("tenantId"), any())).thenReturn(byteArrayResource);

        CaseDiaryRequest caseDiaryRequest1 = CaseDiaryRequest.builder().requestInfo(generateRequest.getRequestInfo()).diary(generateRequest.getDiary()).build();
        doNothing().when(workflowService).updateWorkflowStatus(caseDiaryRequest);

        String result = diaryService.generateDiary(generateRequest);

        assertNotNull(result);
        assertEquals("fileStoreId", result);

        verify(diaryEntryService, times(1)).searchDiaryEntries(any());
        verify(fileStoreUtil, times(1)).saveDocumentToFileStore(byteArrayResource, "tenantId");
        verify(producer, times(1)).push(eq(configuration.getCaseDiaryTopic()), any(CaseDiaryRequest.class));
    }
}
