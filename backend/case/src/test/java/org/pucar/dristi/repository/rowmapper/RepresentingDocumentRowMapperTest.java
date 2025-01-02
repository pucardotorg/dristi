package org.pucar.dristi.repository.rowmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.pucar.dristi.web.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RepresentingDocumentRowMapperTest {

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private RepresentingDocumentRowMapper rowMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExtractData_Success() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("representing_id")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(mockResultSet.getString("id")).thenReturn("doc1");
        when(mockResultSet.getString("documenttype")).thenReturn("Type1");
        when(mockResultSet.getString("filestore")).thenReturn("FileStore1");
        when(mockResultSet.getString("documentuid")).thenReturn("UID123");

        Map<UUID, List<Document>> result = rowMapper.extractData(mockResultSet);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertEquals(1, result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).size());
    }

    @Test
    public void testExtractData_SQLException() throws Exception {
        when(mockResultSet.next()).thenThrow(new SQLException("Database error"));

        assertThrows(CustomException.class, () -> rowMapper.extractData(mockResultSet));
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        when(mockResultSet.next()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> rowMapper.extractData(mockResultSet));
    }
}


