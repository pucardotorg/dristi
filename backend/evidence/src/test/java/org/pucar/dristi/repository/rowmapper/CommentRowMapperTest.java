package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pucar.dristi.web.models.Comment;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentRowMapperTest {

    @Test
    void testExtractDataWithValidResultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("artifactId")).thenReturn("artifact1");
        when(rs.getString("individualId")).thenReturn("individual1");
        when(rs.getString("comment")).thenReturn("Test comment");
        when(rs.getBoolean("isActive")).thenReturn(true);
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1627389102000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1627389202000L);

        PGobject additionalDetailsObject = new PGobject();
        additionalDetailsObject.setType("json");
        additionalDetailsObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionalDetails")).thenReturn(additionalDetailsObject);

        CommentRowMapper rowMapper = new CommentRowMapper();
        List<Comment> comments = rowMapper.extractData(rs);

        assertNotNull(comments);
        assertEquals(1, comments.size());

        Comment comment = comments.get(0);
        assertEquals(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"), comment.getId());
        assertEquals("tenant1", comment.getTenantId());
        assertEquals("artifact1", comment.getArtifactId());
        assertEquals("individual1", comment.getIndividualId());
        assertEquals("Test comment", comment.getComment());
        assertTrue(comment.getIsActive());

        AuditDetails auditDetails = comment.getAuditdetails();
        assertEquals("user1", auditDetails.getCreatedBy());
        assertEquals(1627389102000L, auditDetails.getCreatedTime());
        assertEquals("user2", auditDetails.getLastModifiedBy());
        assertEquals(1627389202000L, auditDetails.getLastModifiedTime());

        assertEquals("{\"key\":\"value\"}", comment.getAdditionalDetails());
    }

    @Test
    void testExtractDataWithException() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenThrow(new SQLException("Test SQL exception"));

        CommentRowMapper rowMapper = new CommentRowMapper();
        CustomException exception = assertThrows(CustomException.class, () -> rowMapper.extractData(rs));

        assertEquals("ROW_MAPPER_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Test SQL exception"));
    }

    @Test
    void testExtractDataWithNullAdditionalDetails() throws Exception {
        ResultSet rs = mock(ResultSet.class);

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("artifactId")).thenReturn("artifact1");
        when(rs.getString("individualId")).thenReturn("individual1");
        when(rs.getString("comment")).thenReturn("Test comment");
        when(rs.getBoolean("isActive")).thenReturn(true);
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1627389102000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1627389202000L);
        when(rs.getObject("additionalDetails")).thenReturn(null);

        CommentRowMapper rowMapper = new CommentRowMapper();
        List<Comment> comments = rowMapper.extractData(rs);

        assertNotNull(comments);
        assertEquals(1, comments.size());

        Comment comment = comments.get(0);
        assertEquals(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"), comment.getId());
        assertEquals("tenant1", comment.getTenantId());
        assertEquals("artifact1", comment.getArtifactId());
        assertEquals("individual1", comment.getIndividualId());
        assertEquals("Test comment", comment.getComment());
        assertTrue(comment.getIsActive());

        AuditDetails auditDetails = comment.getAuditdetails();
        assertEquals("user1", auditDetails.getCreatedBy());
        assertEquals(1627389102000L, auditDetails.getCreatedTime());
        assertEquals("user2", auditDetails.getLastModifiedBy());
        assertEquals(1627389202000L, auditDetails.getLastModifiedTime());

        assertNull(comment.getAdditionalDetails());
    }
}
