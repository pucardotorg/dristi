package digit.service;

import digit.config.Configuration;
import digit.enrichment.ADiaryEnrichment;
import digit.kafka.Producer;
import digit.repository.DiaryRepository;
import digit.util.CaseUtil;
import digit.util.FileStoreUtil;
import digit.util.PdfServiceUtil;
import digit.validators.ADiaryValidator;
import digit.web.models.*;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.util.*;

import static digit.config.ServiceConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

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

    @Mock
    private DiaryEntryService diaryEntryService;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private PdfServiceUtil pdfServiceUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private CaseUtil caseUtil;

    @InjectMocks
    private DiaryService diaryService;

    private CaseDiarySearchRequest searchRequest;
    private CaseDiaryRequest caseDiaryRequest;
    private CaseDiaryGenerateRequest generateRequest;

    @BeforeEach
    void setUp() {
        searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(CaseDiarySearchCriteria.builder()
                .tenantId("tenant1")
                .build());

        caseDiaryRequest = new CaseDiaryRequest();
        CaseDiary diary = new CaseDiary();
        diary.setId(UUID.randomUUID());
        diary.setTenantId("tenant1");
        diary.setDiaryType("ADiary");
        diary.setDiaryDate(2L);
        diary.setDocuments(new ArrayList<>());
        caseDiaryRequest.setDiary(diary);
        caseDiaryRequest.setRequestInfo(new RequestInfo());

        generateRequest = new CaseDiaryGenerateRequest();
        generateRequest.setDiary(diary);
        generateRequest.setRequestInfo(new RequestInfo());
    }

    @Test
    void searchCaseDiariesSuccess() {
        List<CaseDiaryListItem> mockResults = Collections.singletonList(new CaseDiaryListItem());
        when(diaryRepository.getCaseDiaries(searchRequest)).thenReturn(mockResults);

        List<CaseDiaryListItem> result = diaryService.searchCaseDiaries(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(diaryRepository).getCaseDiaries(searchRequest);
    }

    @Test
    void searchCaseDiariesNullRequest() {
        List<CaseDiaryListItem> result = diaryService.searchCaseDiaries(null);
        assertNull(result);
    }

    @Test
    void searchCaseDiariesRepositoryException() {
        when(diaryRepository.getCaseDiaries(searchRequest)).thenThrow(new CustomException(DIARY_SEARCH_EXCEPTION, "Error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryService.searchCaseDiaries(searchRequest);
        });

        assertEquals(DIARY_SEARCH_EXCEPTION, exception.getCode());
    }

    @Test
    void updateDiarySuccess() {
        Workflow workflow = Workflow.builder().action(SIGN_ACTION).build();
        caseDiaryRequest.getDiary().setWorkflow(workflow);

        when(configuration.getDiaryUpdateTopic()).thenReturn("diary-update-topic");

        CaseDiary result = diaryService.updateDiary(caseDiaryRequest);

        assertNotNull(result);
        verify(validator).validateUpdateDiary(caseDiaryRequest);
        verify(enrichment).enrichUpdateCaseDiary(caseDiaryRequest);
        verify(workflowService).updateWorkflowStatus(caseDiaryRequest);
        verify(producer).push(eq("diary-update-topic"), eq(caseDiaryRequest));
    }

    @Test
    void updateDiaryNullWorkflow() {
        CaseDiary diary = caseDiaryRequest.getDiary();
        diary.setWorkflow(null);

        when(configuration.getDiaryUpdateTopic()).thenReturn("diary-update-topic");

        CaseDiary result = diaryService.updateDiary(caseDiaryRequest);

        assertNotNull(result);
        assertNotNull(result.getWorkflow());
        assertEquals(SIGN_ACTION, result.getWorkflow().getAction());
    }

    @Test
    void generateDiarySuccess() {
        // Setup mock behaviors
        when(configuration.getCaseDiaryTopic()).thenReturn("case-diary-topic");
        when(configuration.getADiaryPdfTemplateKey()).thenReturn("adiary-template");
        when(configuration.getCourtName()).thenReturn("Test Court");

        ByteArrayResource mockByteResource = mock(ByteArrayResource.class);
        Document mockDocument = Document.builder()
                .fileStore("filestore-id")
                .documentType(UNSIGNED_DOCUMENT_TYPE)
                .build();

        CaseDiaryDocument caseDiaryDocument = CaseDiaryDocument.builder()
                .id(UUID.randomUUID())
                .build();

        generateRequest.getDiary().setDocuments(Collections.singletonList(caseDiaryDocument));

        List<CaseDiaryEntry> caseDiaryEntries = Collections.singletonList(CaseDiaryEntry.builder()
                        .hearingDate(2L)
                .build());

        when(diaryEntryService.searchDiaryEntries(any())).thenReturn(caseDiaryEntries);
        when(pdfServiceUtil.generatePdfFromPdfService(any(), any(), any())).thenReturn(mockByteResource);
        when(fileStoreUtil.saveDocumentToFileStore(any(), any())).thenReturn(mockDocument);

        String result = diaryService.generateDiary(generateRequest);

        assertNotNull(result);
        verify(validator).validateGenerateRequest(generateRequest);
        verify(enrichment).enrichGenerateRequestForDiary(generateRequest);
        verify(workflowService).updateWorkflowStatus(any());
        verify(producer).push(eq("case-diary-topic"), any());
    }

    @Test
    void generateDiaryValidationException() {
        doThrow(new CustomException(DIARY_GENERATE_EXCEPTION, "Validation Error"))
                .when(validator).validateGenerateRequest(generateRequest);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryService.generateDiary(generateRequest);
        });

        assertEquals(DIARY_GENERATE_EXCEPTION, exception.getCode());
    }

    @Test
    void searchCaseDiaryForJudgeSuccess() {
        List<CaseDiary> mockDiaries = new ArrayList<>();
        CaseDiary mockDiary = new CaseDiary();
        mockDiary.setDocuments(Collections.singletonList(
                CaseDiaryDocument.builder()
                        .documentType(SIGNED_DOCUMENT_TYPE)
                        .build()
        ));
        mockDiaries.add(mockDiary);

        when(diaryRepository.getCaseDiariesWithDocuments(any())).thenReturn(mockDiaries);

        CaseDiary result = diaryService.searchCaseDiaryForJudge(
                "tenant1", "judge1", "ADiary", System.currentTimeMillis(), UUID.randomUUID()
        );

        assertNotNull(result);
        assertNotNull(result.getDocuments());
        assertEquals(1, result.getDocuments().size());
    }

    @Test
    void searchCaseDiaryForJudgeNoDataOrCaseIdException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryService.searchCaseDiaryForJudge(
                    "tenant1", "judge1", "ADiary", null, null
            );
        });

        assertEquals(DIARY_SEARCH_EXCEPTION, exception.getCode());
    }

    @Test
    void searchCaseDiaryForJudgeMultipleDiariesException() {
        List<CaseDiary> mockDiaries = Arrays.asList(new CaseDiary(), new CaseDiary());
        when(diaryRepository.getCaseDiariesWithDocuments(any())).thenReturn(mockDiaries);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryService.searchCaseDiaryForJudge(
                    "tenant1", "judge1", "ADiary", System.currentTimeMillis(), UUID.randomUUID()
            );
        });

        assertEquals(DIARY_SEARCH_EXCEPTION, exception.getCode());
    }
}