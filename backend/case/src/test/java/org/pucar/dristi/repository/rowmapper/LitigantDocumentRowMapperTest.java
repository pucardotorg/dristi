package org.pucar.dristi.repository.rowmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
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
class LitigantDocumentRowMapperTest {

    @Mock
    private ResultSet resultSet;

    private LitigantDocumentRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new LitigantDocumentRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("litigant_id")).thenReturn("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(resultSet.getString("id")).thenReturn("doc001");
        when(resultSet.getString("documenttype")).thenReturn("TypeA");
        when(resultSet.getString("filestore")).thenReturn("StoreX");
        when(resultSet.getString("documentuid")).thenReturn("UID123");

        PGobject pgObject = new PGobject();
        pgObject.setValue("{\"key\":\"value\"}");
        when(resultSet.getObject("docadditionaldetails")).thenReturn(pgObject);

        Map<UUID, List<Document>> documents = rowMapper.extractData(resultSet);

        assertFalse(documents.isEmpty());
        assertTrue(documents.containsKey(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")));
        assertEquals(1, documents.get(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")).size());

        Document doc = documents.get(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")).get(0);
        assertEquals("doc001", doc.getId());
        assertEquals("TypeA", doc.getDocumentType());
        assertNotNull(doc.getAdditionalDetails());

        verify(resultSet, atLeastOnce()).getString("litigant_id");
    }

    @Test
    void testHandleNullLitigantId() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("litigant_id")).thenReturn(null);

        Map<UUID, List<Document>> documents = rowMapper.extractData(resultSet);
        assertTrue(documents.containsKey(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        assertFalse(documents.get(UUID.fromString("00000000-0000-0000-0000-000000000000")).isEmpty());
    }

    @Test
    void testExtractData_Exception() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Database error"));

        assertThrows(Exception.class, () -> rowMapper.extractData(resultSet));
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        when(resultSet.next()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> rowMapper.extractData(resultSet));
    }

}


