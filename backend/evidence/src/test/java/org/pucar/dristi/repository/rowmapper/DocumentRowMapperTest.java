package org.pucar.dristi.repository.rowmapper;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class DocumentRowMapperTest {

    @InjectMocks
    private DocumentRowMapper documentRowMapper;

    private ResultSet rs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rs = mock(ResultSet.class);
    }

    @Test
     void testExtractData() throws Exception {
        // Mock ResultSet behavior
        when(rs.next()).thenReturn(true).thenReturn(false); // Simulate having only one row
        when(rs.getString("artifactId")).thenReturn("123e4567-e89b-12d3-a456-556642440000");
        when(rs.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440001");
        when(rs.getString("documenttype")).thenReturn("type1");
        when(rs.getString("filestore")).thenReturn("fileStore1");
        when(rs.getString("documentuid")).thenReturn("uid1");

        PGobject pgObject = new PGobject();
        pgObject.setType("jsonb");
        pgObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionaldetails")).thenReturn(pgObject);

        // Call the method under test
        Map<UUID, Document> result = documentRowMapper.extractData(rs);

        // Debug output
        System.out.println("Result map: " + result);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());

        UUID applicationId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Document document = result.get(applicationId);
        // Ensure the document is not null
        assertNotNull(document);
    }
    @Test
     void testExtractDataWithException() throws SQLException {
        when(rs.next()).thenThrow(new SQLException("Test exception"));

        assertThrows(CustomException.class, () -> documentRowMapper.extractData(rs));
    }
}
