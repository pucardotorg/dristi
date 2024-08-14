package org.pucar.dristi.repository.rowmapper;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.web.models.StatuteSection;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatuteSectionRowMapperTest {

    private StatuteSectionRowMapper statuteSectionRowMapper;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        statuteSectionRowMapper = new StatuteSectionRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testExtractData() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("order_id")).thenReturn("123e4567-e89b-12d3-a456-556642440000");
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getString("tenantid")).thenReturn("tenant-123");
        when(resultSet.getString("sections")).thenReturn("section1|section2");
        when(resultSet.getString("subsections")).thenReturn("subsection1|subsection2");
        when(resultSet.getString("strsections")).thenReturn("strsection1|strsection2");
        when(resultSet.getString("strsubsections")).thenReturn("strsubsection1|strsubsection2");
        when(resultSet.getString("statute")).thenReturn("Statute-123");
        when(resultSet.getString("createdby")).thenReturn("user-123");
        when(resultSet.getLong("createdtime")).thenReturn(1617187200000L);
        when(resultSet.getString("lastmodifiedby")).thenReturn("user-123");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(1617187200000L);
        when(resultSet.wasNull()).thenReturn(false);

        PGobject pgObject = new PGobject();
        pgObject.setType("json");
        pgObject.setValue("{\"key\":\"value\"}");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        Map<UUID, StatuteSection> statuteSectionMap = statuteSectionRowMapper.extractData(resultSet);

        assertNotNull(statuteSectionMap);
        assertEquals(1, statuteSectionMap.size());
        StatuteSection statuteSection = statuteSectionMap.get(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"), statuteSection.getId());
        assertEquals("tenant-123", statuteSection.getTenantId());
        assertNotNull(statuteSection.getAuditdetails());
        assertEquals("user-123", statuteSection.getAuditdetails().getCreatedBy());
        assertEquals(1617187200000L, statuteSection.getAuditdetails().getCreatedTime());
        assertEquals("user-123", statuteSection.getAuditdetails().getLastModifiedBy());
        assertEquals(1617187200000L, statuteSection.getAuditdetails().getLastModifiedTime());
        assertNotNull(statuteSection.getAdditionalDetails());
    }

    @Test
    void testExtractDataWithException() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Test exception"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            statuteSectionRowMapper.extractData(resultSet);
        });

        assertEquals("ROW_MAPPER_EXCEPTION", thrown.getCode());
        assertTrue(thrown.getMessage().contains("Test exception"));
    }

    @Test
    void testStringToList() {
        List<String> result = statuteSectionRowMapper.stringToList("item");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testStringToListWithNull() {
        List<String> result = statuteSectionRowMapper.stringToList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
