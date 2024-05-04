package org.pucar.dristi.repository.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Party;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LitigantRowMapperTest {

    @Mock
    private ResultSet rs;

    private ResultSetExtractor<Map<UUID, List<Party>>> rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new LitigantRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("case_id")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(rs.getString("id")).thenReturn("123e4567-e89b-12d3-a456-426614174001");
        when(rs.getString("tenantid")).thenReturn("pg");
        when(rs.getString("partycategory")).thenReturn("not");
        when(rs.getString("individualid")).thenReturn("322");
        when(rs.getString("organisationid")).thenReturn("123");
        when(rs.getString("partytype")).thenReturn("party");
        when(rs.getString("createdby")).thenReturn("creator");
        when(rs.getLong("createdtime")).thenReturn(1000000L);
        when(rs.getString("lastmodifiedby")).thenReturn("modifier");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1000001L);
        when(rs.getString("isactive")).thenReturn("true");

        PGobject pgObject = mock(PGobject.class);
        when(pgObject.getValue()).thenReturn("{\"key\":\"value\"}");
        when(rs.getObject("additionalDetails")).thenReturn(pgObject);

        Map<UUID, List<Party>> result = rowMapper.extractData(rs);

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        Party party = result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).get(0);
        assertEquals("creator", party.getAuditDetails().getCreatedBy());
    }
}

