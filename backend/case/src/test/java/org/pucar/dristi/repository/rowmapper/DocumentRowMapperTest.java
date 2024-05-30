package org.pucar.dristi.repository.rowmapper;

import org.egov.common.contract.models.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentRowMapperTest {

    @Mock
    private ResultSet rs;

    private DocumentRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rowMapper = new DocumentRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("case_id")).thenReturn("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(rs.getString("id")).thenReturn("doc123");
        when(rs.getString("documenttype")).thenReturn("Type1");
        when(rs.getString("filestore")).thenReturn("FileStore1");
        when(rs.getString("documentuid")).thenReturn("DocUID1");

        PGobject pgObject = mock(PGobject.class);
        when(rs.getObject("docadditionaldetails")).thenReturn(pgObject);
        when(pgObject.getValue()).thenReturn("{\"key\":\"value\"}");

        Map<UUID, List<Document>> documents = rowMapper.extractData(rs);

        assertNotNull(documents);
        assertTrue(documents.containsKey(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")));
        assertEquals(1, documents.get(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")).size());
        Document doc = documents.get(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")).get(0);
        assertEquals("doc123", doc.getId());
        assertNotNull(doc.getAdditionalDetails());

        verify(rs, times(1)).getString("case_id");
        verify(pgObject, times(1)).getValue();
    }

    @Test
    void testExtractData_Exception() throws Exception {
        when(rs.next()).thenThrow(new SQLException("Database error"));

        assertThrows(Exception.class, () -> rowMapper.extractData(rs));
    }
}

