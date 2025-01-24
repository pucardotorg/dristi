package digit.validators;

import digit.repository.DiaryEntryRepository;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ADiaryValidatorTest {

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @InjectMocks
    private ADiaryValidator validator;

    private CaseDiaryEntryRequest validRequest;
    private RequestInfo validRequestInfo;
    private CaseDiaryEntry validDiaryEntry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        when(diaryEntryRepository.getCaseDiaryEntries(any())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("diary entry does not exists", exception.getMessage());
    }

    @Test
    void validateExistingDiaryEntry_MultipleEntriesFound_ThrowsException() {
        CaseDiaryEntry duplicateEntry = CaseDiaryEntry.builder()
                .id(UUID.randomUUID())
                .tenantId("default")
                .hearingDate(1L)
                .entryDate(1L)
                .build();

        when(diaryEntryRepository.getCaseDiaryEntries(any()))
                .thenReturn(Arrays.asList(validDiaryEntry, validDiaryEntry));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("diary entry does not exists", exception.getMessage());
    }

    @Test
    void validateExistingDiaryEntry_NoMatchingEntry_ThrowsException() {
        CaseDiaryEntry differentEntry = CaseDiaryEntry.builder()
                .id(UUID.randomUUID())
                .tenantId("default")
                .hearingDate(1L)
                .entryDate(1L)
                .build();

        when(diaryEntryRepository.getCaseDiaryEntries(any()))
                .thenReturn(Collections.singletonList(differentEntry));

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateUpdateDiaryEntry(validRequest));
        assertEquals("diary entry does not exists", exception.getMessage());
    }
}