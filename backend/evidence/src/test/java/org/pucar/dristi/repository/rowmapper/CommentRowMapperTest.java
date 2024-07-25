package org.pucar.dristi.repository.rowmapper;

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
    void testExtractData() throws Exception {
        // Set up mock behavior
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

        // Call the method
        Map<UUID, List<Comment>> result = commentRowMapper.extractData(rs);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());

        UUID artifactId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        List<Comment> comments = result.get(artifactId);

        assertNotNull(comments);
        assertEquals(2, comments.size());

        // Validate common properties for both comments
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            UUID expectedId = UUID.fromString(i == 0
                    ? "123e4567-e89b-12d3-a456-556642440001"
                    : "123e4567-e89b-12d3-a456-556642440002");

            assertEquals(expectedId, comment.getId());
            assertEquals("tenant1", comment.getTenantId());
            assertEquals("individual1", comment.getIndividualId());
            assertEquals("This is a comment", comment.getComment());
            assertTrue(comment.getIsActive());
            assertNotNull(comment.getAuditdetails());
            assertEquals(null, comment.getAuditdetails().getCreatedBy());
            assertEquals(0, comment.getAuditdetails().getCreatedTime());
            assertEquals(null, comment.getAuditdetails().getLastModifiedBy());
            assertEquals(0, comment.getAuditdetails().getLastModifiedTime());
            assertNotNull(comment.getAdditionalDetails());
        }
    }


    @Test
     void testExtractDataWithException() throws SQLException {
        when(rs.next()).thenThrow(new SQLException("Test exception"));

        assertThrows(CustomException.class, () -> commentRowMapper.extractData(rs));
    }
}
