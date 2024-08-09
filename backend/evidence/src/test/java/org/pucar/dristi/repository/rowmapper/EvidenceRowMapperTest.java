package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import org.egov.common.contract.models.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.Comment;
import org.egov.tracer.model.CustomException;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvidenceRowMapperTest {

    @InjectMocks
    private EvidenceRowMapper evidenceRowMapper;

    @Mock
    private ResultSet rs;

    private void mockResultSet() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(rs.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000").thenReturn("123e4567-e89b-12d3-a456-556642440001");
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("artifactNumber")).thenReturn("artifactNumber1");
        when(rs.getString("evidenceNumber")).thenReturn("evidenceNumber1");
        when(rs.getString("externalRefNumber")).thenReturn("externalRefNumber1");
        when(rs.getString("caseId")).thenReturn("caseId1");
        when(rs.getString("application")).thenReturn("application1");
        when(rs.getString("filingNumber")).thenReturn("filingNumber1");
        when(rs.getString("hearing")).thenReturn("hearing1");
        when(rs.getString("orders")).thenReturn("order1");
        when(rs.getString("mediaType")).thenReturn("mediaType1");
        when(rs.getString("artifactType")).thenReturn("artifactType1");
        when(rs.getString("sourceType")).thenReturn("sourceType1");
        when(rs.getString("sourceID")).thenReturn("sourceID1");
        when(rs.getString("sourceName")).thenReturn("sourceName1");
        when(rs.getString("applicableTo")).thenReturn("[\"applicable1\"]");
        when(rs.getString("comments")).thenReturn("[{\"id\":\"123e4567-e89b-12d3-a456-556642440002\",\"tenantId\":\"tenant1\",\"artifactId\":\"123e4567-e89b-12d3-a456-556642440000\",\"individualId\":\"individual1\",\"comment\":\"This is a comment\",\"isActive\":true,\"additionalDetails\":{\"key\":\"value\"},\"auditdetails\":{\"createdBy\":\"user1\",\"createdTime\":1609459200000,\"lastModifiedBy\":\"user2\",\"lastModifiedTime\":1609545600000}}]");
        when(rs.getString("file")).thenReturn("{\"id\":\"123e4567-e89b-12d3-a456-556642440003\",\"documentType\":\"type1\",\"fileStore\":\"fileStore1\",\"documentUid\":\"documentUid1\",\"additionalDetails\":{\"key\":\"value\"}}");
        when(rs.getLong("createdDate")).thenReturn(20210101l);
        when(rs.getBoolean("isActive")).thenReturn(true);
        when(rs.getBoolean("isEvidence")).thenReturn(true);
        when(rs.getString("status")).thenReturn("status1");
        when(rs.getString("description")).thenReturn("description1");
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1609459200000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1609545600000L);

        PGobject artifactDetailsObject = new PGobject();
        artifactDetailsObject.setType("jsonb");
        artifactDetailsObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("artifactDetails")).thenReturn(artifactDetailsObject);

        PGobject additionalDetailsObject = new PGobject();
        additionalDetailsObject.setType("jsonb");
        additionalDetailsObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionalDetails")).thenReturn(additionalDetailsObject);
    }

    @Test
    void testExtractDataBasicProperties() throws Exception {
        mockResultSet();
        List<Artifact> artifacts = evidenceRowMapper.extractData(rs);

        assertNotNull(artifacts);
        assertEquals(2, artifacts.size());

        Artifact artifact1 = artifacts.get(0);
        assertNotNull(artifact1);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"), artifact1.getId());
        assertEquals("tenant1", artifact1.getTenantId());
        assertEquals("artifactNumber1", artifact1.getArtifactNumber());
        assertEquals("evidenceNumber1", artifact1.getEvidenceNumber());
        assertEquals("externalRefNumber1", artifact1.getExternalRefNumber());
        assertEquals("caseId1", artifact1.getCaseId());
        assertEquals("application1", artifact1.getApplication());
        assertEquals("filingNumber1", artifact1.getFilingNumber());
        assertEquals("hearing1", artifact1.getHearing());
        assertEquals("order1", artifact1.getOrder());
        assertEquals("mediaType1", artifact1.getMediaType());
        assertEquals("artifactType1", artifact1.getArtifactType());
        assertEquals("sourceType1", artifact1.getSourceType());
        assertEquals("sourceID1", artifact1.getSourceID());
        assertEquals("sourceName1", artifact1.getSourceName());
        assertEquals(List.of("applicable1"), artifact1.getApplicableTo());
    }

    @Test
    void testExtractDataCommentsAndFile() throws Exception {
        mockResultSet();
        List<Artifact> artifacts = evidenceRowMapper.extractData(rs);

        Artifact artifact1 = artifacts.get(0);
        assertEquals(1, artifact1.getComments().size());

        Comment comment = artifact1.getComments().get(0);
        assertNotNull(comment);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440002"), comment.getId());
        assertEquals("tenant1", comment.getTenantId());
        assertEquals("individual1", comment.getIndividualId());
        assertEquals("This is a comment", comment.getComment());
        assertTrue(comment.getIsActive());
        assertNotNull(comment.getAdditionalDetails());

        Document file = artifact1.getFile();
        assertNotNull(file);
    }

    @Test
    void testExtractDataAuditDetails() throws Exception {
        mockResultSet();
        List<Artifact> artifacts = evidenceRowMapper.extractData(rs);

        Artifact artifact1 = artifacts.get(0);
        assertNotNull(artifact1.getAuditdetails());
        assertEquals("user1", artifact1.getAuditdetails().getCreatedBy());
        assertEquals(1609459200000L, artifact1.getAuditdetails().getCreatedTime());
        assertEquals("user2", artifact1.getAuditdetails().getLastModifiedBy());
        assertEquals(1609545600000L, artifact1.getAuditdetails().getLastModifiedTime());
    }

    @Test
    void testExtractDataAdditionalDetails() throws Exception {
        mockResultSet();
        List<Artifact> artifacts = evidenceRowMapper.extractData(rs);

        Artifact artifact1 = artifacts.get(0);
        assertNotNull(artifact1.getArtifactDetails());
        assertNotNull(artifact1.getAdditionalDetails());
    }

    @Test
    void testExtractDataStatusAndDescription() throws Exception {
        mockResultSet();
        List<Artifact> artifacts = evidenceRowMapper.extractData(rs);

        Artifact artifact1 = artifacts.get(0);
        assertEquals(20210101l, artifact1.getCreatedDate());
        assertTrue(artifact1.getIsActive());
        assertTrue(artifact1.getIsEvidence());
        assertEquals("status1", artifact1.getStatus());
        assertEquals("description1", artifact1.getDescription());
    }

    @Test
    void testGetObjectFromJsonWithValidJson() {
        String json = "[\"applicable1\", \"applicable2\"]";
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        List<String> result = evidenceRowMapper.getObjectFromJson(json, typeRef);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("applicable1", result.get(0));
        assertEquals("applicable2", result.get(1));
    }

    @Test
    void testGetObjectFromJsonWithEmptyJsonForList() {
        String json = "[]";
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        List<String> result = evidenceRowMapper.getObjectFromJson(json, typeRef);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetObjectFromJsonWithEmptyJsonForObject() {
        String json = "{}";
        TypeReference<Document> typeRef = new TypeReference<Document>() {};
        Document result = evidenceRowMapper.getObjectFromJson(json, typeRef);
        assertNotNull(result);
    }

    @Test
    void testGetObjectFromJsonWithNullJson() {
        String json = null;
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        List<String> result = evidenceRowMapper.getObjectFromJson(json, typeRef);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void testGetObjectFromJsonWithInvalidJson() {
        String json = "invalid_json";
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        assertThrows(CustomException.class, () -> evidenceRowMapper.getObjectFromJson(json, typeRef));
    }
}
