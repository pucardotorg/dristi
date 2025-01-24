package digit.repository.rowmapper;

import digit.web.models.CaseDiaryEntry;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryEntryRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private DiaryEntryRowMapper rowMapper;

    private final String TEST_UUID = "123e4567-e89b-12d3-a456-426614174000";
    private final String TENANT_ID = "default";
    private final Long ENTRY_DATE = 1234567890L;
    private final String CASE_NUMBER = "CASE-123";
    private final String JUDGE_ID = "JUDGE-123";
    private final String BUSINESS_OF_DAY = "Regular hearing";
    private final String REFERENCE_ID = "REF-123";
    private final String REFERENCE_TYPE = "HEARING";
    private final Long HEARING_DATE = 1234567899L;
    private final Long CREATED_TIME = 1234567800L;
    private final String CREATED_BY = "CREATOR-123";
    private final Long MODIFIED_TIME = 1234567899L;
    private final String MODIFIED_BY = "MODIFIER-123";

    @BeforeEach
    void setUp() throws SQLException {
        // Default behavior for resultSet.next()
        when(resultSet.next()).thenReturn(true, false);
    }

    @Test
    void extractData_Success() throws SQLException, DataAccessException {
        // Arrange
        setupValidResultSet();

        // Act
        List<CaseDiaryEntry> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        CaseDiaryEntry entry = result.get(0);
        assertEquals(UUID.fromString(TEST_UUID), entry.getId());
        assertEquals(TENANT_ID, entry.getTenantId());
        assertEquals(ENTRY_DATE, entry.getEntryDate());
        assertEquals(CASE_NUMBER, entry.getCaseNumber());
        assertEquals(JUDGE_ID, entry.getJudgeId());
        assertEquals(BUSINESS_OF_DAY, entry.getBusinessOfDay());
        assertEquals(REFERENCE_ID, entry.getReferenceId());
        assertEquals(REFERENCE_TYPE, entry.getReferenceType());
        assertEquals(HEARING_DATE, entry.getHearingDate());

        AuditDetails auditDetails = entry.getAuditDetails();
        assertNotNull(auditDetails);
        assertEquals(CREATED_TIME, auditDetails.getCreatedTime());
        assertEquals(CREATED_BY, auditDetails.getCreatedBy());
        assertEquals(MODIFIED_TIME, auditDetails.getLastModifiedTime());
        assertEquals(MODIFIED_BY, auditDetails.getLastModifiedBy());

        // Verify
        verify(resultSet, times(2)).next();
        verifyResultSetReads();
    }

    @Test
    void DataWithEmptyHearingDate_Success() throws SQLException {
        setupValidResultSet();
        when(resultSet.getString("hearingDate")).thenReturn(null);

        // Act
        List<CaseDiaryEntry> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        CaseDiaryEntry entry = result.get(0);
        assertEquals(UUID.fromString(TEST_UUID), entry.getId());
        assertEquals(TENANT_ID, entry.getTenantId());
        assertEquals(ENTRY_DATE, entry.getEntryDate());
        assertEquals(CASE_NUMBER, entry.getCaseNumber());
        assertEquals(JUDGE_ID, entry.getJudgeId());
        assertEquals(BUSINESS_OF_DAY, entry.getBusinessOfDay());
        assertEquals(REFERENCE_ID, entry.getReferenceId());
        assertEquals(REFERENCE_TYPE, entry.getReferenceType());
        assertNull(entry.getHearingDate());

        AuditDetails auditDetails = entry.getAuditDetails();
        assertNotNull(auditDetails);
        assertEquals(CREATED_TIME, auditDetails.getCreatedTime());
        assertEquals(CREATED_BY, auditDetails.getCreatedBy());
        assertEquals(MODIFIED_TIME, auditDetails.getLastModifiedTime());
        assertEquals(MODIFIED_BY, auditDetails.getLastModifiedBy());

        // Verify
        verify(resultSet, times(2)).next();
        verifyResultSetReads();
    }

    @Test
    void extractData_EmptyResultSet_ReturnsEmptyList() throws SQLException, DataAccessException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        List<CaseDiaryEntry> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(resultSet).next();
    }

    @Test
    void extractData_SQLExceptionThrown_ThrowsCustomException() throws SQLException {
        // Arrange
        when(resultSet.next()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> rowMapper.extractData(resultSet));
        assertTrue(exception.getMessage()
                .contains("Error occurred while processing document ResultSet"));

        // Verify
        verify(resultSet).next();
    }

    @Test
    void extractData_DataAccessExceptionThrown_ThrowsCustomException() throws SQLException {
        // Arrange
        when(resultSet.next()).thenThrow(new DataAccessException("Data access error") {
        });

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> rowMapper.extractData(resultSet));
        assertTrue(exception.getMessage()
                .contains("Error occurred while processing document ResultSet"));

        // Verify
        verify(resultSet).next();
    }

    private void setupValidResultSet() throws SQLException {
        when(resultSet.getString("id")).thenReturn(TEST_UUID);
        when(resultSet.getString("tenantId")).thenReturn(TENANT_ID);
        when(resultSet.getLong("entryDate")).thenReturn(ENTRY_DATE);
        when(resultSet.getString("caseNumber")).thenReturn(CASE_NUMBER);
        when(resultSet.getString("judgeId")).thenReturn(JUDGE_ID);
        when(resultSet.getString("businessOfDay")).thenReturn(BUSINESS_OF_DAY);
        when(resultSet.getString("referenceId")).thenReturn(REFERENCE_ID);
        when(resultSet.getString("referenceType")).thenReturn(REFERENCE_TYPE);
        when(resultSet.getString("hearingDate")).thenReturn(HEARING_DATE.toString());
        when(resultSet.getLong("createdTime")).thenReturn(CREATED_TIME);
        when(resultSet.getString("createdBy")).thenReturn(CREATED_BY);
        when(resultSet.getLong("lastModifiedTime")).thenReturn(MODIFIED_TIME);
        when(resultSet.getString("lastModifiedBy")).thenReturn(MODIFIED_BY);
    }

    private void verifyResultSetReads() throws SQLException {
        verify(resultSet).getString("id");
        verify(resultSet).getString("tenantId");
        verify(resultSet).getLong("entryDate");
        verify(resultSet).getString("caseNumber");
        verify(resultSet).getString("judgeId");
        verify(resultSet).getString("businessOfDay");
        verify(resultSet).getString("referenceId");
        verify(resultSet).getString("referenceType");
        verify(resultSet).getString("hearingDate");
        verify(resultSet).getLong("createdTime");
        verify(resultSet).getString("createdBy");
        verify(resultSet).getLong("lastModifiedTime");
        verify(resultSet).getString("lastModifiedBy");
    }
}