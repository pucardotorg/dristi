package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.junit.jupiter.api.BeforeEach;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.JsonNode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentRowMapperTest {


    private DocumentRowMapper documentRowMapper;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        documentRowMapper = new DocumentRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void testExtractData() throws Exception {
        when(resultSet.next()).thenReturn(true, false); // Mock the iteration
        when(resultSet.getString("id")).thenReturn("doc1");
        when(resultSet.getString("fileStore")).thenReturn("fileStore1");
        when(resultSet.getString("documentUid")).thenReturn("uid1");
        when(resultSet.getString("documentType")).thenReturn("type1");

        PGobject pGobject = new PGobject();
        pGobject.setType("json");
        pGobject.setValue("{\"key\":\"value\"}");
        when(resultSet.getObject("additionalDetails")).thenReturn(pGobject);

        Document document = documentRowMapper.extractData(resultSet);

        assertNotNull(document);
        assertEquals("doc1", document.getId());
        assertEquals("fileStore1", document.getFileStore());
        assertEquals("uid1", document.getDocumentUid());
        assertEquals("type1", document.getDocumentType());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode additionalDetails = objectMapper.readTree(pGobject.getValue());
        assertEquals("value", additionalDetails.get("key").asText());
    }

    @Test
    void testExtractDataWithException() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenThrow(new SQLException("Test SQL exception"));

        DocumentRowMapper rowMapper = new DocumentRowMapper();
        CustomException exception = assertThrows(CustomException.class, () -> rowMapper.extractData(rs));

        assertEquals("ROW_MAPPER_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Test SQL exception"));
    }

    @Test
    void testExtractDataWithNullAdditionalDetails() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn("doc1");
        when(rs.getString("fileStore")).thenReturn("fileStore1");
        when(rs.getString("documentUid")).thenReturn("docUid1");
        when(rs.getString("documentType")).thenReturn("type1");
        when(rs.getString("artifactId")).thenReturn("artifact1");
        when(rs.getObject("additionalDetails")).thenReturn(null);

        DocumentRowMapper rowMapper = new DocumentRowMapper();
        Document document = rowMapper.extractData(rs);

        assertNotNull(document);
        assertEquals("doc1", document.getId());
        assertEquals("fileStore1", document.getFileStore());
        assertEquals("docUid1", document.getDocumentUid());
        assertEquals("type1", document.getDocumentType());
        assertNull(document.getAdditionalDetails());
    }
}
