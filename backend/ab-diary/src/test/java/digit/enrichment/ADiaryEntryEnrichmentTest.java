package digit.enrichment;

import digit.util.ADiaryUtil;
import digit.web.models.CaseDiaryEntry;
import digit.web.models.CaseDiaryEntryRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ADiaryEntryEnrichmentTest {

    @Mock
    private ADiaryUtil aDiaryUtil;

    @InjectMocks
    private ADiaryEntryEnrichment enrichment;

    private CaseDiaryEntryRequest request;
    private CaseDiaryEntry diaryEntry;
    private static final String uuid = "a0a1836a-6653-4d94-bad2-5de2355bce4c";
    private static final long CURRENT_TIME = 1234567890L;

    @BeforeEach
    void setUp() {
        // Initialize user
        User user = new User();
        user.setUuid(uuid);

        // Initialize RequestInfo
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);

        // Initialize CaseDiaryEntry
        diaryEntry = CaseDiaryEntry.builder().build();

        // Initialize CaseDiaryEntryRequest
        request = CaseDiaryEntryRequest.builder()
                .requestInfo(requestInfo)
                .diaryEntry(diaryEntry)
                .build();

    }

    @Test
    void enrichCreateDiaryEntry_Success() {
        // Act
        enrichment.enrichCreateDiaryEntry(request);

        // Assert
        assertNotNull(diaryEntry.getAuditDetails());
        assertEquals(uuid, diaryEntry.getAuditDetails().getCreatedBy());
        assertEquals(uuid, diaryEntry.getAuditDetails().getLastModifiedBy());

        // Verify
        verify(aDiaryUtil).generateUUID();
        verify(aDiaryUtil, times(2)).getCurrentTimeInMilliSec();
    }

    @Test
    void enrichCreateDiaryEntry_NullUserInfo_ThrowsException() {
        // Arrange
        request.getRequestInfo().setUserInfo(null);

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> enrichment.enrichCreateDiaryEntry(request));
        assertEquals("Cannot invoke \"org.egov.common.contract.request.User.getUuid()\" because \"user\" is null", exception.getMessage());
    }

    @Test
    void enrichUpdateEntry_Success() {
        // Arrange
        AuditDetails existingAuditDetails = AuditDetails.builder()
                .createdBy("original-creator")
                .createdTime(1000L)
                .lastModifiedBy("original-modifier")
                .lastModifiedTime(2000L)
                .build();
        diaryEntry.setAuditDetails(existingAuditDetails);

        // Act
        enrichment.enrichUpdateEntry(request);

        // Assert
        assertEquals("original-creator", diaryEntry.getAuditDetails().getCreatedBy());
        assertEquals(1000L, diaryEntry.getAuditDetails().getCreatedTime());
        assertEquals(uuid, diaryEntry.getAuditDetails().getLastModifiedBy());

        // Verify
        verify(aDiaryUtil).getCurrentTimeInMilliSec();
    }

    @Test
    void enrichUpdateEntry_NullAuditDetails_ThrowsException() {
        // Arrange
        diaryEntry.setAuditDetails(null);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> enrichment.enrichUpdateEntry(request));
        assertEquals("Error during enriching diary entry", exception.getMessage());
    }

    @Test
    void enrichUpdateEntry_NullRequestInfo_ThrowsException() {
        // Arrange
        request.setRequestInfo(null);
        diaryEntry.setAuditDetails(AuditDetails.builder().build());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> enrichment.enrichUpdateEntry(request));
        assertEquals("Error during enriching diary entry", exception.getMessage());
    }

    @Test
    void enrichUpdateEntry_NullUserInfo_ThrowsException() {
        // Arrange
        request.getRequestInfo().setUserInfo(null);
        diaryEntry.setAuditDetails(AuditDetails.builder().build());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> enrichment.enrichUpdateEntry(request));
        assertEquals("Error during enriching diary entry", exception.getMessage());
    }

    @Test
    void enrichUpdateEntry_UtilException_ThrowsException() {
        // Arrange
        diaryEntry.setAuditDetails(AuditDetails.builder().build());
        when(aDiaryUtil.getCurrentTimeInMilliSec()).thenThrow(new RuntimeException("Time retrieval failed"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> enrichment.enrichUpdateEntry(request));
        assertEquals("Error during enriching diary entry", exception.getMessage());
    }
}