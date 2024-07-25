package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.Comment;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentRowMapperTest {

    @InjectMocks
    private CommentRowMapper commentRowMapper;

    private ResultSet rs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rs = mock(ResultSet.class);
    }

    @Test
    public void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getString("artifactId")).thenReturn("123e4567-e89b-12d3-a456-556642440000").thenReturn("123e4567-e89b-12d3-a456-556642440000");
        when(rs.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440001").thenReturn("123e4567-e89b-12d3-a456-556642440002");
        when(rs.getString("tenantId")).thenReturn("tenant1");
        when(rs.getString("individualId")).thenReturn("individual1");
        when(rs.getString("comment")).thenReturn("This is a comment");
        when(rs.getBoolean("isActive")).thenReturn(true);
        when(rs.getLong("createdtime")).thenReturn(1609459200000L);
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1609545600000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");

        PGobject pgObject = new PGobject();
        pgObject.setType("jsonb");
        pgObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionalDetails")).thenReturn(pgObject);

        Map<UUID, List<Comment>> result = commentRowMapper.extractData(rs);

        assertNotNull(result);
        assertEquals(1, result.size());

        UUID artifactId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        List<Comment> comments = result.get(artifactId);
        assertNotNull(comments);
        assertEquals(2, comments.size());

        Comment comment1 = comments.get(0);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"), comment1.getId());
        assertEquals("tenant1", comment1.getTenantId());
        assertEquals("individual1", comment1.getIndividualId());
        assertEquals("This is a comment", comment1.getComment());
        assertTrue(comment1.getIsActive());
        assertNotNull(comment1.getAuditdetails());
        assertEquals(null, comment1.getAuditdetails().getCreatedBy());
        assertEquals(0, comment1.getAuditdetails().getCreatedTime());
        assertEquals(null, comment1.getAuditdetails().getLastModifiedBy());
        assertEquals(0, comment1.getAuditdetails().getLastModifiedTime());
        assertNotNull(comment1.getAdditionalDetails());

        Comment comment2 = comments.get(1);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440002"), comment2.getId());
        assertEquals("tenant1", comment2.getTenantId());
        assertEquals("individual1", comment2.getIndividualId());
        assertEquals("This is a comment", comment2.getComment());
        assertTrue(comment2.getIsActive());
        assertNotNull(comment2.getAuditdetails());
        assertEquals(null, comment2.getAuditdetails().getCreatedBy());
        assertEquals(0, comment2.getAuditdetails().getCreatedTime());
        assertEquals(null, comment2.getAuditdetails().getLastModifiedBy());
        assertEquals(0, comment2.getAuditdetails().getLastModifiedTime());
        assertNotNull(comment2.getAdditionalDetails());
    }

    @Test
    public void testExtractDataWithException() throws SQLException {
        when(rs.next()).thenThrow(new SQLException("Test exception"));

        assertThrows(CustomException.class, () -> commentRowMapper.extractData(rs));
    }
}
