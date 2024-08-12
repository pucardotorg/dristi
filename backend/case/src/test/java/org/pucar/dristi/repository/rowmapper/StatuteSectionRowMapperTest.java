package org.pucar.dristi.repository.rowmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.StatuteSection;

@ExtendWith(MockitoExtension.class)
class StatuteSectionRowMapperTest {

    @Mock
    private ResultSet rs;

    private StatuteSectionRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new StatuteSectionRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("case_id")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(rs.getString("sections")).thenReturn("Section1|Section2");
        when(rs.getString("subsections")).thenReturn("Subsection1|Subsection2");
        when(rs.getLong("createdtime")).thenReturn(1000000L);
        when(rs.getString("createdby")).thenReturn("User2");
        when(rs.getString("tenantid")).thenReturn("tenantid");
        when(rs.getString("lastmodifiedby")).thenReturn("User2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1000001L);

        PGobject pgObject = mock(PGobject.class);
        when(pgObject.getValue()).thenReturn("{\"key\":\"value\"}");
        when(rs.getObject("additionalDetails")).thenReturn(pgObject);

        Map<UUID, List<StatuteSection>> result = rowMapper.extractData(rs);

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        List<StatuteSection> sections = result.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        assertEquals(1, sections.size());

        StatuteSection section = sections.get(0);
        assertNotNull(section.getAdditionalDetails());

        verify(rs, times(1)).getString("case_id");
    }

    @Test
    void testStringToList() {
        String input = "Item1,Item2,Item3";
        List<String> result = rowMapper.stringToList(input);
        assertEquals(3, result.size());
    }

    @Test
    void testExtractData_Exception() throws Exception {
        when(rs.next()).thenThrow(new SQLException("Database error"));

        assertThrows(Exception.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        when(rs.next()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }
}

