package org.pucar.dristi.repository.rowMapper;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_ROW_MAPPER_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

public class DocumentRowMapperTest {

    @InjectMocks
    private DocumentRowMapper documentRowMapper;

    @Mock
    private ResultSet resultSet;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documentRowMapper = new DocumentRowMapper();
    }
    @Test
    public void testExtractData() throws Exception {
//        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Sample ResultSet data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("application_id")).thenReturn("319fbdeb-cb82-409f-ad45-5085fe3142b9");
        when(resultSet.getString("id")).thenReturn("doc_id_1");
        when(resultSet.getString("documenttype")).thenReturn("doc_type_1");
        when(resultSet.getString("filestore")).thenReturn("file_store_path_1");
        when(resultSet.getString("documentuid")).thenReturn("doc_uid_1");

        PGobject pgObject = new PGobject();
        pgObject.setType("jsonb");
        pgObject.setValue("{\"key\": \"value\"}");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        // Call the method to test
        Map<UUID, List<Document>> resultMap = documentRowMapper.extractData(resultSet);

        // Assertions
        assertEquals(1, resultMap.size());
        UUID expectedUuid = UUID.fromString("319fbdeb-cb82-409f-ad45-5085fe3142b9");
        assertEquals(expectedUuid, resultMap.keySet().iterator().next());
        List<Document> documents = resultMap.get(expectedUuid);
        assertEquals(1, documents.size());
        Document document = documents.get(0);
        assertEquals("doc_id_1", document.getId());
        assertEquals("doc_type_1", document.getDocumentType());
        assertEquals("file_store_path_1", document.getFileStore());
        assertEquals("doc_uid_1", document.getDocumentUid());
    }

    @Test
    void testExtractData_WhenSQLExceptionThrown_ShouldThrowCustomException() throws Exception {
        // Arrange
        when(resultSet.next()).thenThrow(new SQLException("Test SQL Exception"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            documentRowMapper.extractData(resultSet);
        });

        // Assert
        assertEquals(DOCUMENT_ROW_MAPPER_EXCEPTION, thrown.getCode());
        assertEquals("Error occurred while processing document ResultSet: Test SQL Exception", thrown.getMessage());
    }

    @Test
    void testExtractData_WhenOtherExceptionThrown_ShouldThrowCustomException() throws Exception {
        // Arrange
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(anyString())).thenThrow(new RuntimeException("Test Runtime Exception"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            documentRowMapper.extractData(resultSet);
        });

        // Assert
        assertEquals(DOCUMENT_ROW_MAPPER_EXCEPTION, thrown.getCode());
        assertEquals("Error occurred while processing document ResultSet: Test Runtime Exception", thrown.getMessage());
    }
}
