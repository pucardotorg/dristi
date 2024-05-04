package org.pucar.dristi.repository.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.LinkedCase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class LinkedCaseRowMapperTest {

    @Mock
    private ResultSet rs;

    private LinkedCaseRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rowMapper = new LinkedCaseRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("case_id")).thenReturn("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1609459200000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1609459300000L);
        when(rs.getString("relationshiptype")).thenReturn("Sibling");
        when(rs.getString("casenumbers")).thenReturn("Case12345");
        when(rs.getBoolean("isactive")).thenReturn(true);

        Map<UUID, List<LinkedCase>> result = rowMapper.extractData(rs);

        assertNotNull(result);
        assertTrue(result.containsKey(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")));
        assertEquals(1, result.size());
        LinkedCase linkedCase = result.get(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")).get(0);
        assertEquals("Sibling", linkedCase.getRelationshipType());
        assertEquals("Case12345", linkedCase.getCaseNumber());
        assertTrue(linkedCase.getIsActive());
        assertNotNull(linkedCase.getAuditdetails());

        verify(rs, times(1)).getString("case_id");
        verify(rs, times(1)).getString("id");
        verify(rs, times(1)).getString("relationshiptype");
    }

    @Test
    void testExtractDataWithNullCaseId() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(rs.getString("case_id")).thenReturn(null);
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1609459200000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1609459300000L);
        when(rs.getString("relationshiptype")).thenReturn("Sibling");
        when(rs.getString("casenumbers")).thenReturn("Case12345");
        Map<UUID, List<LinkedCase>> result = rowMapper.extractData(rs);

        assertTrue(result.containsKey(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        assertEquals(1, result.get(UUID.fromString("00000000-0000-0000-0000-000000000000")).size());
    }
}


