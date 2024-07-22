package org.pucar.dristi.repository.rowmapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.repository.rowmapper.EvidenceRowMapper;
import org.pucar.dristi.web.models.Artifact;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EvidenceRowMapperTest {

    private EvidenceRowMapper evidenceRowMapper;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        evidenceRowMapper = new EvidenceRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void testExtractData_Mapping() throws Exception {
        // Mock ResultSet to provide data for a single artifact
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(1622120800000L); // Example time value
        when(resultSet.getString("createdby")).thenReturn("user1");
        when(resultSet.getLong("createdtime")).thenReturn(1622120800000L); // Example time value
        when(resultSet.getString("lastmodifiedby")).thenReturn("user2");
        when(resultSet.getString("tenantid")).thenReturn("tenant1");
        when(resultSet.getString("artifactNumber")).thenReturn("artifactNumber1");
        when(resultSet.getString("evidenceNumber")).thenReturn("evidenceNumber1");

        PGobject artifactDetailsObject = new PGobject();
        artifactDetailsObject.setType("json");
        artifactDetailsObject.setValue("{\"key\":\"value\"}");
        when(resultSet.getObject("artifactDetails")).thenReturn(artifactDetailsObject);

        PGobject additionalDetailsObject = new PGobject();
        additionalDetailsObject.setType("json");
        additionalDetailsObject.setValue("{\"additionalKey\":\"additionalValue\"}");
        when(resultSet.getObject("additionalDetails")).thenReturn(additionalDetailsObject);

        // Invoke extractData method
        List<Artifact> artifacts = evidenceRowMapper.extractData(resultSet);

        // Verify artifacts list is not null and contains one artifact
        assertNotNull(artifacts);
        assertEquals(1, artifacts.size());

        // Verify artifact details
        Artifact artifact = artifacts.get(0);
        assertNotNull(artifact);
        assertEquals("123e4567-e89b-12d3-a456-556642440000", artifact.getId().toString());
        assertEquals("tenant1", artifact.getTenantId());
        assertEquals("artifactNumber1", artifact.getArtifactNumber());
        assertEquals("evidenceNumber1", artifact.getEvidenceNumber());
        // Verify other properties...

        // Verify artifact details and additional details
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode artifactDetailsJson = objectMapper.readTree(artifactDetailsObject.getValue());
        assertEquals("value", artifactDetailsJson.get("key").asText());

        JsonNode additionalDetailsJson = (JsonNode) artifact.getAdditionalDetails(); // Assuming getAdditionalDetails() returns a JsonNode
        assertEquals("additionalValue", additionalDetailsJson.get("additionalKey").asText());
    }

    @Test
    public void testExtractData_Exception() throws Exception {
        // Mock ResultSet to throw SQLException
        when(resultSet.next()).thenThrow(new SQLException("Test Exception"));

        // Verify CustomException is thrown
        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceRowMapper.extractData(resultSet);
        });

        assertEquals("ROW_MAPPER_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Test Exception"));
    }
}
