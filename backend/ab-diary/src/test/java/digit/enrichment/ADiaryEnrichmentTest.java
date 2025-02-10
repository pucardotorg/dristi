package digit.enrichment;

import digit.repository.DiaryRepository;
import digit.util.ADiaryUtil;
import digit.web.models.*;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ADiaryEnrichmentTest {

    @Mock
    private ADiaryUtil aDiaryUtil;

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private ADiaryEnrichment aDiaryEnrichment;

    @Mock
    private CaseDiaryRequest caseDiaryRequest;

    @Mock
    private RequestInfo requestInfo;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().uuid("user-123").build();
        requestInfo = RequestInfo.builder().userInfo(user).build();
        caseDiaryRequest = new CaseDiaryRequest();
        caseDiaryRequest.setRequestInfo(requestInfo);
        caseDiaryRequest.setDiary(new CaseDiary());
    }

    @Test
    void testEnrichUpdateCaseDiarySuccess() {
        CaseDiary diary = new CaseDiary();
        CaseDiaryDocument caseDiaryDocument = mock(CaseDiaryDocument.class);
        diary.setDocuments(Collections.singletonList(caseDiaryDocument));
        caseDiaryRequest.setDiary(diary);

        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenReturn(12345L);

        aDiaryEnrichment.enrichUpdateCaseDiary(caseDiaryRequest);

        assertNotNull(caseDiaryRequest.getDiary().getAuditDetails());
    }

    @Test
    void testEnrichUpdateCaseDiaryWithDocuments() {
        CaseDiaryDocument document = new CaseDiaryDocument();
        caseDiaryRequest.getDiary().setDocuments(Collections.singletonList(document));

        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenReturn(12345L);
        UUID uuid = UUID.randomUUID();
        when(aDiaryUtil.generateUUID()).thenReturn(uuid);

        aDiaryEnrichment.enrichUpdateCaseDiary(caseDiaryRequest);

        assertEquals(uuid, caseDiaryRequest.getDiary().getDocuments().get(0).getId());
        assertEquals("casediary.signed", caseDiaryRequest.getDiary().getDocuments().get(0).getDocumentType());
    }

    @Test
    void testEnrichUpdateCaseDiaryThrowsException() {
        caseDiaryRequest.setDiary(mock(CaseDiary.class));
        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenThrow(RuntimeException.class);

        CustomException exception = assertThrows(CustomException.class, () ->
                aDiaryEnrichment.enrichUpdateCaseDiary(caseDiaryRequest)
        );

        assertEquals("Error during enriching diary", exception.getMessage());
    }

    @Test
    void testEnrichDiaryDocument() {
        CaseDiaryDocument document = new CaseDiaryDocument();
        caseDiaryRequest.getDiary().setDocuments(Collections.singletonList(document));

        UUID uuid = UUID.randomUUID();

        when(aDiaryUtil.generateUUID()).thenReturn(uuid);
        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenReturn(12345L);

        aDiaryEnrichment.enrichDiaryDocument(caseDiaryRequest);

        CaseDiaryDocument enrichedDocument = caseDiaryRequest.getDiary().getDocuments().get(0);
        assertEquals(uuid, enrichedDocument.getId());
        assertEquals("casediary.signed", enrichedDocument.getDocumentType());
        assertTrue(enrichedDocument.isActive());
    }

    @Test
    void testEnrichGenerateRequestForDiaryWithExistingDiary() {
        CaseDiary diary = new CaseDiary();
        diary.setJudgeId("judge-1");
        diary.setDiaryDate(1L);
        diary.setDiaryType("A");
        diary.setTenantId("tenant-1");
        CaseDiaryGenerateRequest generateRequest = new CaseDiaryGenerateRequest();
        generateRequest.setDiary(diary);
        generateRequest.setRequestInfo(requestInfo);

        CaseDiary existingDiary = new CaseDiary();
        UUID uuid = UUID.randomUUID();
        existingDiary.setId(uuid);
        existingDiary.setAuditDetails(AuditDetails.builder().build());
        existingDiary.setDocuments(Collections.singletonList(CaseDiaryDocument.builder().id(uuid).auditDetails(AuditDetails.builder().build()).build()));

        when(diaryRepository.getCaseDiariesWithDocuments(any())).thenReturn(Collections.singletonList(existingDiary));
        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenReturn(12345L);

        aDiaryEnrichment.enrichGenerateRequestForDiary(generateRequest);

        assertEquals(uuid, generateRequest.getDiary().getId());
        assertEquals(uuid, generateRequest.getDiary().getDocuments().get(0).getId());
    }

    @Test
    void testEnrichGenerateRequestForDiaryWithNewDiary() {
        CaseDiary diary = new CaseDiary();
        diary.setJudgeId("judge-2");
        UUID uuid = UUID.randomUUID();
        diary.setDiaryDate(1L);
        diary.setDiaryType("B");
        diary.setTenantId("tenant-2");
        CaseDiaryGenerateRequest generateRequest = new CaseDiaryGenerateRequest();
        generateRequest.setDiary(diary);
        generateRequest.setRequestInfo(requestInfo);

        when(diaryRepository.getCaseDiariesWithDocuments(any())).thenReturn(Collections.emptyList());
        when(aDiaryUtil.generateUUID()).thenReturn(uuid);
        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenReturn(12345L);

        aDiaryEnrichment.enrichGenerateRequestForDiary(generateRequest);

        assertEquals(uuid, generateRequest.getDiary().getId());
        assertEquals(uuid, generateRequest.getDiary().getDocuments().get(0).getId());
    }

}
