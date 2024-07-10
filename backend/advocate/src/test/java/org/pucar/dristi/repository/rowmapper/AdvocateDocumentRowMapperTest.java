package org.pucar.dristi.repository.rowmapper;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AdvocateDocumentRowMapperTest {

    @Mock
    private ResultSet rs;

    private AdvocateDocumentRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new AdvocateDocumentRowMapper();
    }

    @Test
    void shouldExtractDataCorrectly() throws SQLException {
        // Setup
        lenient().when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        lenient().when(rs.getString("advocateid")).thenReturn("123e4567-e89b-12d3-a456-426614174000", "123e4567-e89b-12d3-a456-426614174000");
        lenient().when(rs.getString("aid")).thenReturn("doc1", "doc2");
        lenient().when(rs.getString("documenttype")).thenReturn("type1", "type2");
        lenient().when(rs.getString("filestore")).thenReturn("file1", "file2");
        lenient().when(rs.getString("documentuid")).thenReturn("uid1", "uid2");
        lenient().when(rs.getString("additionaldetails")).thenReturn("details1", "details2");

        // Execution
        Map<UUID, List<Document>> result = rowMapper.extractData(rs);

        // Verification
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertEquals(2, result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).size());
        assertEquals("doc1", result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).get(0).getId());
        assertEquals("type2", result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).get(1).getDocumentType());
    }

    @Test
    void shouldHandleException() throws SQLException {
        // Setup
        when(rs.next()).thenThrow(new SQLException("Database error"));

        // Execution & Verification
        Exception exception = assertThrows(Exception.class, () -> rowMapper.extractData(rs));
        assertTrue(exception.getMessage().contains("Exception occurred while processing document ResultSet: Database error"));
    }

    @Test
    void shouldHandle_CustomException() throws SQLException {
        // Setup
        when(rs.next()).thenThrow(new CustomException());

        // Execution & Verification
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }
}
