package org.pucar.dristi.repository.rowmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;

@ExtendWith(MockitoExtension.class)
class RepresentativeDocumentRowMapperTest {

    @Mock
    private ResultSet rs;

    private RepresentiveDocumentRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new RepresentiveDocumentRowMapper();
    }

    @Test
    void testExtractData_Successful() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("representative_id")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(rs.getString("id")).thenReturn("doc001");
        when(rs.getString("documenttype")).thenReturn("TypeA");
        when(rs.getString("filestore")).thenReturn("StoreX");
        when(rs.getString("documentuid")).thenReturn("UID123");

        PGobject pgObject = mock(PGobject.class);
        when(pgObject.getValue()).thenReturn("{\"key\":\"value\"}");
        when(rs.getObject("docadditionaldetails")).thenReturn(pgObject);

        Map<UUID, List<Document>> result = rowMapper.extractData(rs);

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertEquals(1, result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).size());

        Document doc = result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).get(0);
        assertEquals("doc001", doc.getId());
        assertNotNull(doc.getAdditionalDetails());

        verify(rs, times(1)).getString("representative_id");
    }

    @Test
    void testExtractData_NullRepresentativeId() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("representative_id")).thenReturn(null);

        Map<UUID, List<Document>> result = rowMapper.extractData(rs);

        assertTrue(result.containsKey(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        assertEquals(1, result.get(UUID.fromString("00000000-0000-0000-0000-000000000000")).size());
    }

    @Test
    void testExceptionHandling() throws Exception {
        when(rs.next()).thenThrow(new RuntimeException("Database error"));
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void testExtractData_Exception() throws Exception {
        when(rs.next()).thenThrow(new SQLException("Database error"));

        assertThrows(Exception.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        when(rs.next()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }
}


