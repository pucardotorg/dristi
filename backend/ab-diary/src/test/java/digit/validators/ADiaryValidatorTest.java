package digit.validators;

import digit.repository.DiaryEntryRepository;
import digit.repository.DiaryRepository;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ADiaryValidatorTest {

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private ADiaryValidator validator;

    private CaseDiaryEntryRequest validRequest;
    private RequestInfo validRequestInfo;
    private CaseDiaryEntry validDiaryEntry;
    private CaseDiaryRequest validDiaryRequest;
    private CaseDiary validDiary;

    @BeforeEach
    void setUp() {

        validRequestInfo = new RequestInfo();
        validRequestInfo.setUserInfo(new User());

        validDiaryEntry = CaseDiaryEntry.builder()
                .id(UUID.randomUUID())
                .tenantId("default")
                .hearingDate(1L)
                .entryDate(1L)
                .build();

        validRequest = CaseDiaryEntryRequest.builder()
                .requestInfo(validRequestInfo)
                .diaryEntry(validDiaryEntry)
                .build();

        validDiary = CaseDiary.builder()
                .tenantId("default")
                .diaryDate(1L)
                .judgeId("JUDGE123")
                .build();

        validDiaryRequest = CaseDiaryRequest.builder()
                .requestInfo(validRequestInfo)
                .diary(validDiary)
                .build();
    }

    // Save Diary Entry Tests
    @Test
    void validateSaveDiaryEntry_ValidRequest_NoExceptionThrown() {
        assertDoesNotThrow(() -> validator.validateSaveDiaryEntry(validRequest));
    }

    @Test
    void validateSaveDiaryEntry_NullDiaryEntry_ThrowsException() {
        CaseDiaryEntryRequest request = CaseDiaryEntryRequest.builder()
                .requestInfo(validRequestInfo)
                .diaryEntry(null)
                .build();

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateSaveDiaryEntry(request));
        assertEquals("case diary entry is mandatory to create an entry", exception.getMessage());
    }

    @Test
    void validateSaveDiaryEntry_NullRequestInfo_ThrowsException() {
        validRequest.setRequestInfo(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateSaveDiaryEntry(validRequest));
        assertEquals("request Info or user info can not be null", exception.getMessage());
    }

    @Test
    void validateSaveDiaryEntry_NullUserInfo_ThrowsException() {
        validRequest.getRequestInfo().setUserInfo(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateSaveDiaryEntry(validRequest));
        assertEquals("request Info or user info can not be null", exception.getMessage());
    }

    // Update Diary Entry Tests
    @Test
    void validateUpdateDiaryEntry_ValidRequest_NoExceptionThrown() {
        when(diaryEntryRepository.getExistingDiaryEntry(any()))
                .thenReturn(Collections.singletonList(validDiaryEntry));

        assertDoesNotThrow(() -> validator.validateUpdateDiaryEntry(validRequest));
    }

    @Test
    void validateUpdateDiaryEntry_NullDiaryEntry_ThrowsException() {
        CaseDiaryEntryRequest request = CaseDiaryEntryRequest.builder()
                .requestInfo(validRequestInfo)
                .diaryEntry(null)
                .build();

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(request));
        assertEquals("case diary entry is mandatory to update", exception.getMessage());
    }

    @Test
    void validateUpdateDiaryEntry_NullId_ThrowsException() {
        validDiaryEntry.setId(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("Id is mandatory to update entry", exception.getMessage());
    }

    @Test
    void validateUpdateDiaryEntry_NullRequestInfo_ThrowsException() {
        validRequest.setRequestInfo(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("request info or user info is mandatory", exception.getMessage());
    }

    @Test
    void validateUpdateDiaryEntry_NullUserInfo_ThrowsException() {
        validRequest.getRequestInfo().setUserInfo(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("request info or user info is mandatory", exception.getMessage());
    }

    // Existing Diary Entry Validation Tests
    @Test
    void validateExistingDiaryEntry_EntryNotFound_ThrowsException() {
        when(diaryEntryRepository.getExistingDiaryEntry(any())).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("diary entry does not exists", exception.getMessage());
    }

    @Test
    void validateExistingDiaryEntry_MultipleEntriesFound_ThrowsException() {
        when(diaryEntryRepository.getExistingDiaryEntry(any()))
                .thenReturn(Arrays.asList(validDiaryEntry, validDiaryEntry));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("multiple diary entries found with id", exception.getMessage());
    }

    // Update Diary Tests
    @Test
    void validateUpdateDiary_ValidRequest_NoExceptionThrown() {
        when(diaryRepository.getCaseDiariesWithDocuments(any()))
                .thenReturn(Collections.singletonList(validDiary));

        CaseDiaryDocument diaryDocument = CaseDiaryDocument.builder()
                .fileStoreId("fileStoreId")
                .build();

        validDiaryRequest.getDiary().setDocuments(Collections.singletonList(diaryDocument));

        assertDoesNotThrow(() -> validator.validateUpdateDiary(validDiaryRequest));
    }

    @Test
    void validateUpdateDiary_NullDiary_ThrowsException() {
        validDiaryRequest.setDiary(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiary(validDiaryRequest));
        assertEquals("case diary is mandatory to create/update an entry", exception.getMessage());
    }

    @Test
    void validateUpdateDiary_NullRequestInfo_ThrowsException() {
        validDiaryRequest.setRequestInfo(null);

        CaseDiaryDocument diaryDocument = CaseDiaryDocument.builder()
                .fileStoreId("fileStoreId")
                .build();

        validDiaryRequest.getDiary().setDocuments(Collections.singletonList(diaryDocument));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiary(validDiaryRequest));
        assertEquals("request Info or user info can not be null", exception.getMessage());
    }

    @Test
    void validateUpdateDiary_NonExistingDiary_ThrowsException() {
        when(diaryRepository.getCaseDiariesWithDocuments(any()))
                .thenReturn(Collections.emptyList());

        CaseDiaryDocument caseDiaryDocument = CaseDiaryDocument.builder().fileStoreId("filestore").build();

        validDiaryRequest.getDiary().setDocuments(Collections.singletonList(caseDiaryDocument));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiary(validDiaryRequest));
        assertEquals("diary does not exist", exception.getMessage());
    }

    @Test
    void validateUpdateDiary_NoDiaryDocument_ThrowsException() {

        CaseDiaryDocument caseDiaryDocument = CaseDiaryDocument.builder().build();

        validDiaryRequest.getDiary().setDocuments(Collections.singletonList(caseDiaryDocument));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiary(validDiaryRequest));
        assertEquals("diary document with file store id is mandatory to update", exception.getMessage());
    }

    @Test
    void validateUpdateDiary_MultipleDiariesFound_ThrowsException() {

        CaseDiaryDocument diaryDocument = CaseDiaryDocument.builder()
                .fileStoreId("fileStoreId")
                .build();

        validDiaryRequest.getDiary().setDocuments(Collections.singletonList(diaryDocument));

        when(diaryRepository.getCaseDiariesWithDocuments(any()))
                .thenReturn(Arrays.asList(validDiary, validDiary));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiary(validDiaryRequest));
        assertEquals("multiple entries found with same id", exception.getMessage());
    }

    // Generate Request Tests
    @Test
    void validateGenerateRequest_FutureDiaryDate_ThrowsException() {
        CaseDiary diary = CaseDiary.builder().diaryDate(System.currentTimeMillis() + 100000L).build();
        CaseDiaryGenerateRequest generateRequest = CaseDiaryGenerateRequest.builder().diary(diary).build();

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateGenerateRequest(generateRequest));
        assertEquals("diary date can not be in future", exception.getMessage());
    }
}