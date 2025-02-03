package digit.service;

import digit.config.Configuration;
import digit.enrichment.ADiaryEntryEnrichment;
import digit.kafka.Producer;
import digit.repository.DiaryEntryRepository;
import digit.validators.ADiaryValidator;
import digit.web.models.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiaryEntryServiceTest {

    @Mock
    private ADiaryValidator validator;

    @Mock
    private ADiaryEntryEnrichment enrichment;

    @Mock
    private Producer producer;

    @Mock
    private Configuration configuration;

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @InjectMocks
    private DiaryEntryService diaryEntryService;

    private CaseDiaryEntryRequest diaryEntryRequest;
    private CaseDiaryEntry diaryEntry;

    @BeforeEach
    void setUp() {
        diaryEntryRequest = new CaseDiaryEntryRequest();
        diaryEntry = new CaseDiaryEntry();
        diaryEntryRequest.setDiaryEntry(diaryEntry);
    }

    @Test
    void addDiaryEntrySuccess() {
        when(configuration.getDiaryEntryCreateTopic()).thenReturn("topic");

        CaseDiaryEntry result = diaryEntryService.addDiaryEntry(diaryEntryRequest);

        assertNotNull(result);
        verify(validator).validateSaveDiaryEntry(diaryEntryRequest);
        verify(enrichment).enrichCreateDiaryEntry(diaryEntryRequest);
        verify(producer).push(eq("topic"), eq(diaryEntryRequest));
    }

    @Test
    void addDiaryEntryValidationFailure() {
        doThrow(new CustomException("VALIDATION_ERROR", "Validation failed"))
                .when(validator).validateSaveDiaryEntry(diaryEntryRequest);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryEntryService.addDiaryEntry(diaryEntryRequest);
        });

        assertEquals("VALIDATION_ERROR", exception.getCode());
        verify(validator).validateSaveDiaryEntry(diaryEntryRequest);
        verifyNoInteractions(enrichment);
        verifyNoInteractions(producer);
    }

    @Test
    void updateDiaryEntrySuccess() {
        when(configuration.getDiaryEntryUpdateTopic()).thenReturn("topic");

        CaseDiaryEntry result = diaryEntryService.updateDiaryEntry(diaryEntryRequest);

        assertNotNull(result);
        verify(validator).validateUpdateDiaryEntry(diaryEntryRequest);
        verify(enrichment).enrichUpdateEntry(diaryEntryRequest);
        verify(producer).push(eq("topic"), eq(diaryEntryRequest));
    }

    @Test
    void updateDiaryEntryValidationFailure() {
        doThrow(new CustomException("VALIDATION_ERROR", "Validation failed"))
                .when(validator).validateUpdateDiaryEntry(diaryEntryRequest);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryEntryService.updateDiaryEntry(diaryEntryRequest);
        });

        assertEquals("VALIDATION_ERROR", exception.getCode());
        verify(validator).validateUpdateDiaryEntry(diaryEntryRequest);
        verifyNoInteractions(enrichment);
        verifyNoInteractions(producer);
    }

    @Test
    void searchDiaryEntriesSuccess() {
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(CaseDiarySearchCriteria.builder()
                .judgeId("judge1")
                .build());
        when(diaryEntryRepository.getCaseDiaryEntries(searchRequest))
                .thenReturn(Collections.singletonList(diaryEntry));

        List<CaseDiaryEntry> result = diaryEntryService.searchDiaryEntries(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(diaryEntryRepository).getCaseDiaryEntries(searchRequest);
    }

    @Test
    void searchDiaryEntriesEmptyCriteria() {
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(null);

        List<CaseDiaryEntry> result = diaryEntryService.searchDiaryEntries(searchRequest);

        assertNull(result);
        verifyNoInteractions(diaryEntryRepository);
    }

}
