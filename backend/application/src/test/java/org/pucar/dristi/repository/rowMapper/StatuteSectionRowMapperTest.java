package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.repository.rowMapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.StatuteSection;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.STATUTE_ROW_MAPPER_EXCEPTION;

class StatuteSectionRowMapperTest {

    private StatuteSectionRowMapper statuteSectionRowMapper;
    private ResultSet resultSet;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        statuteSectionRowMapper = new StatuteSectionRowMapper();
        resultSet = mock(ResultSet.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("application_id")).thenReturn("123e4567-e89b-12d3-a456-556642440000");
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
        when(resultSet.getObject("additionalDetails")).thenReturn(pgObject);

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
    }

    @Test
    void testExtractDataWithNullValues() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("case_id")).thenReturn(null);
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getString("tenantid")).thenReturn("tenant-123");
        when(resultSet.getString("sections")).thenReturn(null);
        when(resultSet.getString("subsections")).thenReturn(null);
        when(resultSet.getString("strsections")).thenReturn(null);
        when(resultSet.getString("strsubsections")).thenReturn(null);
        when(resultSet.getString("statute")).thenReturn(null);
        when(resultSet.getString("createdby")).thenReturn(null);
        when(resultSet.getString("lastmodifiedby")).thenReturn(null);
        when(resultSet.wasNull()).thenReturn(true);

        Map<UUID, StatuteSection> statuteSectionMap = statuteSectionRowMapper.extractData(resultSet);

        assertNotNull(statuteSectionMap);
        assertEquals(1, statuteSectionMap.size());
        StatuteSection statuteSection = statuteSectionMap.get(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"), statuteSection.getId());
        assertEquals("tenant-123", statuteSection.getTenantId());
        assertNull(statuteSection.getStatute());
        assertNotNull(statuteSection.getAuditdetails());
        assertNull(statuteSection.getAuditdetails().getCreatedBy());
        assertEquals(0, statuteSection.getAuditdetails().getCreatedTime());
        assertNull(statuteSection.getAuditdetails().getLastModifiedBy());
        assertEquals(0, statuteSection.getAuditdetails().getLastModifiedTime());
        assertNull(statuteSection.getAdditionalDetails());
    }

    @Test
    void testExtractDataWithException() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Test exception"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            statuteSectionRowMapper.extractData(resultSet);
        });

        assertEquals(STATUTE_ROW_MAPPER_EXCEPTION, thrown.getCode());
        assertTrue(thrown.getMessage().contains("Test exception"));
    }

    @Test
    void testStringToList() {
        StatuteSectionRowMapper statuteSectionRowMapper = new StatuteSectionRowMapper();
        List<String> result = statuteSectionRowMapper.stringToList("[\"item\"]");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("item", result.get(0));
    }

    @Test
    void testStringToListWithNull() {
        List<String> result = statuteSectionRowMapper.stringToList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}