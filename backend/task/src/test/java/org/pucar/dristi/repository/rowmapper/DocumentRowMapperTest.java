package org.pucar.dristi.repository.rowmapper;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentRowMapperTest {

    private DocumentRowMapper documentRowMapper;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() {
        documentRowMapper = new DocumentRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void testExtractData_withValidData() throws Exception {
        UUID taskId1 = UUID.randomUUID();
        UUID taskId2 = UUID.randomUUID();

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("task_id")).thenReturn(taskId1.toString(), taskId2.toString());
        when(resultSet.getString("id")).thenReturn("doc1", "doc2");
        when(resultSet.getString("documenttype")).thenReturn("type1", "type2");
        when(resultSet.getString("filestore")).thenReturn("store1", "store2");
        when(resultSet.getString("documentuid")).thenReturn("uid1", "uid2");
        when(resultSet.getObject("additionaldetails")).thenReturn(null, null);

        Map<UUID, List<Document>> result = documentRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(taskId1));
        assertTrue(result.containsKey(taskId2));
        assertEquals(1, result.get(taskId1).size());
        assertEquals(1, result.get(taskId2).size());
        assertEquals("doc1", result.get(taskId1).get(0).getId());
        assertEquals("doc2", result.get(taskId2).get(0).getId());
    }


    @Test
    public void testExtractData_CustomException() throws Exception {
        // Simulate CustomException being thrown
        when(resultSet.next()).thenThrow(new CustomException("ERROR_CODE", "Error Message"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            documentRowMapper.extractData(resultSet);
        });

        assertEquals("ERROR_CODE", exception.getCode());
        assertEquals("Error Message", exception.getMessage());
    }

    @Test
    public void testExtractData_withAdditionalDetails() throws Exception {
        UUID taskId = UUID.randomUUID();
        PGobject pgObject = new PGobject();
        pgObject.setValue("{\"key\": \"value\"}");

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("task_id")).thenReturn(taskId.toString());
        when(resultSet.getString("id")).thenReturn("doc1");
        when(resultSet.getString("documenttype")).thenReturn("type1");
        when(resultSet.getString("filestore")).thenReturn("store1");
        when(resultSet.getString("documentuid")).thenReturn("uid1");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        Map<UUID, List<Document>> result = documentRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(taskId));
        assertEquals(1, result.get(taskId).size());
        assertEquals("doc1", result.get(taskId).get(0).getId());
        assertNotNull(result.get(taskId).get(0).getAdditionalDetails());
    }

    @Test
    public void testExtractData_withEmptyResultSet() throws Exception {
        when(resultSet.next()).thenReturn(false);

        Map<UUID, List<Document>> result = documentRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testExtractData_withException() throws Exception {
        when(resultSet.next()).thenThrow(new RuntimeException("Forced exception"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            documentRowMapper.extractData(resultSet);
        });

        assertEquals("Error occurred while processing document ResultSet: Forced exception", exception.getMessage());
    }
}
