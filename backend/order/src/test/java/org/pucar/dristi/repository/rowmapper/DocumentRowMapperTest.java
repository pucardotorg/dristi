package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentRowMapperTest {

    private DocumentRowMapper documentRowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documentRowMapper = new DocumentRowMapper();
    }

    @Test
    void testExtractData_TypicalResultSet() throws Exception {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("order_id")).thenReturn("550e8400-e29b-41d4-a716-446655440000");
        when(resultSet.getString("id")).thenReturn("doc1");
        when(resultSet.getString("documenttype")).thenReturn("type1");
        when(resultSet.getString("filestore")).thenReturn("store1");
        when(resultSet.getString("documentuid")).thenReturn("uid1");

        PGobject pgObject = new PGobject();
        pgObject.setValue("{\"key\": \"value\"}");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        Map<UUID, List<Document>> documentMap = documentRowMapper.extractData(resultSet);

        UUID expectedUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        Document expectedDocument = Document.builder()
                .id("doc1")
                .documentType("type1")
                .fileStore("store1")
                .documentUid("uid1")
                .additionalDetails(new ObjectMapper().readTree("{\"key\": \"value\"}"))
                .build();

        assertTrue(documentMap.containsKey(expectedUuid));
        assertEquals(1, documentMap.get(expectedUuid).size());
        assertEquals(expectedDocument.getId(), documentMap.get(expectedUuid).get(0).getId());
    }

    @Test
    void testExtractData_NullAdditionalDetails() throws Exception {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("order_id")).thenReturn("550e8400-e29b-41d4-a716-446655440000");
        when(resultSet.getString("id")).thenReturn("doc1");
        when(resultSet.getString("documenttype")).thenReturn("type1");
        when(resultSet.getString("filestore")).thenReturn("store1");
        when(resultSet.getString("documentuid")).thenReturn("uid1");
        when(resultSet.getObject("additionaldetails")).thenReturn(null);

        Map<UUID, List<Document>> documentMap = documentRowMapper.extractData(resultSet);

        UUID expectedUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        Document expectedDocument = Document.builder()
                .id("doc1")
                .documentType("type1")
                .fileStore("store1")
                .documentUid("uid1")
                .build();

        assertTrue(documentMap.containsKey(expectedUuid));
        assertEquals(1, documentMap.get(expectedUuid).size());
        assertEquals(expectedDocument.getId(), documentMap.get(expectedUuid).get(0).getId());
    }

    @Test
    void testExtractData_MultipleDocumentsSameOrderId() throws Exception {
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("order_id")).thenReturn("550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440000");
        when(resultSet.getString("id")).thenReturn("doc1", "doc2");
        when(resultSet.getString("documenttype")).thenReturn("type1", "type2");
        when(resultSet.getString("filestore")).thenReturn("store1", "store2");
        when(resultSet.getString("documentuid")).thenReturn("uid1", "uid2");

        PGobject pgObject = new PGobject();
        pgObject.setValue("{\"key\": \"value\"}");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject, pgObject);

        Map<UUID, List<Document>> documentMap = documentRowMapper.extractData(resultSet);

        UUID expectedUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        Document expectedDocument1 = Document.builder()
                .id("doc1")
                .documentType("type1")
                .fileStore("store1")
                .documentUid("uid1")
                .additionalDetails(new ObjectMapper().readTree("{\"key\": \"value\"}"))
                .build();
        Document expectedDocument2 = Document.builder()
                .id("doc2")
                .documentType("type2")
                .fileStore("store2")
                .documentUid("uid2")
                .additionalDetails(new ObjectMapper().readTree("{\"key\": \"value\"}"))
                .build();

        assertTrue(documentMap.containsKey(expectedUuid));
        assertEquals(2, documentMap.get(expectedUuid).size());
        assertEquals(expectedDocument1.getId(), documentMap.get(expectedUuid).get(0).getId());
        assertEquals(expectedDocument2.getId(), documentMap.get(expectedUuid).get(1).getId());
    }

    @Test
    void testExtractData_ExceptionThrown() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Test exception"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            documentRowMapper.extractData(resultSet);
        });

        assertEquals("ROW_MAPPER_EXCEPTION", thrown.getCode());
        assertTrue(thrown.getMessage().contains("Error occurred while processing document ResultSet: Test exception"));
    }
}
