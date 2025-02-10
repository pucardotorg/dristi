package digit.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.CaseDiary;
import digit.web.models.CaseDiaryDocument;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static digit.config.ServiceConstants.ROW_MAPPER_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaryWithDocumentRowMapperTest {

    private DiaryWithDocumentRowMapper diaryWithDocumentRowMapper;
    private ResultSet resultSet;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        diaryWithDocumentRowMapper = new DiaryWithDocumentRowMapper(objectMapper);
        resultSet = mock(ResultSet.class);
    }

    @Test
    void extractData_Success() throws Exception {
        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true, true, false);

        // Mock CaseDiary fields
        UUID diaryId = UUID.randomUUID();
        when(resultSet.getString("id")).thenReturn(diaryId.toString());
        when(resultSet.getString("tenantId")).thenReturn("tenant-123");
        when(resultSet.getString("caseNumber")).thenReturn("case-456");
        when(resultSet.getLong("diaryDate")).thenReturn(1672348800000L);
        when(resultSet.getString("diaryType")).thenReturn("TypeA");
        when(resultSet.getString("judgeId")).thenReturn("judge-789");
        when(resultSet.getString("diaryCreateBy")).thenReturn("user1");
        when(resultSet.getLong("diaryCreatedTime")).thenReturn(1672348800001L);
        when(resultSet.getString("diaryLastModifiedBy")).thenReturn("user2");
        when(resultSet.getLong("diaryLastModifiedTime")).thenReturn(1672348800002L);

        // Mock additional details
        when(resultSet.getObject("additionalDetails")).thenReturn(null);

        // Mock CaseDiaryDocument fields
        UUID documentId = UUID.randomUUID();
        when(resultSet.getString("documentId")).thenReturn(documentId.toString());
        when(resultSet.getString("fileStoreId")).thenReturn("file-store-123");
        when(resultSet.getString("documentUid")).thenReturn("doc-uid-456");
        when(resultSet.getString("documentName")).thenReturn("Sample Document");
        when(resultSet.getString("documentType")).thenReturn("PDF");
        when(resultSet.getString("caseDiaryId")).thenReturn(diaryId.toString());
        when(resultSet.getBoolean("documentIsActive")).thenReturn(true);
        when(resultSet.getString("documentCreatedBy")).thenReturn("docUser1");
        when(resultSet.getLong("documentCreatedTime")).thenReturn(1672348800003L);
        when(resultSet.getString("documentLastModifiedBy")).thenReturn("docUser2");
        when(resultSet.getLong("documentLastModifiedTime")).thenReturn(1672348800004L);

        // Execute the method
        List<CaseDiary> result = diaryWithDocumentRowMapper.extractData(resultSet);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        CaseDiary caseDiary = result.get(0);
        assertEquals(diaryId, caseDiary.getId());
        assertEquals("tenant-123", caseDiary.getTenantId());
        assertEquals("case-456", caseDiary.getCaseNumber());
        assertEquals(1672348800000L, caseDiary.getDiaryDate());
        assertEquals("TypeA", caseDiary.getDiaryType());

        // Verify documents
        List<CaseDiaryDocument> documents = caseDiary.getDocuments();
        assertEquals(2, documents.size());
        CaseDiaryDocument document = documents.get(0);
        assertEquals(documentId, document.getId());
        assertEquals("file-store-123", document.getFileStoreId());
        assertEquals("doc-uid-456", document.getDocumentUid());
        assertEquals("Sample Document", document.getDocumentName());
        assertEquals("PDF", document.getDocumentType());
        assertEquals(diaryId.toString(), document.getCaseDiaryId());
        assertTrue(document.isActive());
    }

    @Test
    void extractData_EmptyResultSet() throws Exception {
        when(resultSet.next()).thenReturn(false);

        List<CaseDiary> result = diaryWithDocumentRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void extractData_ExceptionHandling() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> diaryWithDocumentRowMapper.extractData(resultSet));

        assertEquals(ROW_MAPPER_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Database error"));
    }

    @Test
    void extractData_AdditionalDetails() throws Exception {
        when(resultSet.next()).thenReturn(true, false);
        UUID diaryId = UUID.randomUUID();
        when(resultSet.getString("id")).thenReturn(diaryId.toString());
        when(resultSet.getString("documentId")).thenReturn(diaryId.toString());

        // Mock additionalDetails JSON data
        String jsonString = "{\"key1\":\"value1\",\"key2\":2}";
        PGobject pgObject = new PGobject();
        pgObject.setValue(jsonString);
        when(resultSet.getObject("additionalDetails")).thenReturn(pgObject);

        List<CaseDiary> result = diaryWithDocumentRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(jsonString, result.get(0).getAdditionalDetails().toString());
    }
}
