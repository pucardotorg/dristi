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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class HearingDocumentRowMapperTest {

    @Mock
    private ResultSet resultSet;

    private HearingDocumentRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new HearingDocumentRowMapper();
    }

    @Test
    void shouldExtractDataSuccessfully() throws SQLException {
        // Arrange
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("hearingid")).thenReturn(uuid1.toString()).thenReturn(uuid1.toString()).thenReturn(uuid2.toString());
        when(resultSet.getString("id")).thenReturn("doc1").thenReturn("doc2");
        when(resultSet.getString("documenttype")).thenReturn("type1").thenReturn("type2");
        when(resultSet.getString("filestore")).thenReturn("store1").thenReturn("store2");
        when(resultSet.getString("documentuid")).thenReturn("uid1").thenReturn("uid2");
        when(resultSet.getObject("additionaldetails")).thenReturn(null).thenReturn(null);

        // Act
        Map<UUID, List<Document>> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(uuid1));
        assertTrue(result.containsKey(uuid2));
        assertEquals(2, result.get(uuid1).size());
        assertEquals(1, result.get(uuid2).size());
    }

    @Test
    void shouldHandleNullAdditionalDetails() throws SQLException {
        // Arrange
        UUID uuid = UUID.randomUUID();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("hearingid")).thenReturn(uuid.toString());
        when(resultSet.getString("id")).thenReturn("doc1");
        when(resultSet.getString("documenttype")).thenReturn("type1");
        when(resultSet.getString("filestore")).thenReturn("store1");
        when(resultSet.getString("documentuid")).thenReturn("uid1");
        when(resultSet.getObject("additionaldetails")).thenReturn(null);

        // Act
        Map<UUID, List<Document>> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(uuid));
        assertEquals(1, result.get(uuid).size());
        assertNull(result.get(uuid).get(0).getAdditionalDetails());
    }

    @Test
    void shouldHandleEmptyResultSet() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        Map<UUID, List<Document>> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleSQLExceptionGracefully() throws SQLException {
        // Arrange
        when(resultSet.next()).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(CustomException.class, () -> rowMapper.extractData(resultSet));
    }
}
