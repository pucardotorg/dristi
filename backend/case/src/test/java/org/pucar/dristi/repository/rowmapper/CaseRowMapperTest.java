package org.pucar.dristi.repository.rowmapper;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CourtCase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CaseRowMapperTest {

    @Mock
    private ResultSet rs;

    private CaseRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rowMapper = new CaseRowMapper();
    }

    @Test
    void testExtractData() throws Exception {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("casenumber")).thenReturn("case123");
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getLong("createdtime")).thenReturn(1609459200000L);
        when(rs.getLong("lastmodifiedtime")).thenReturn(1609459200000L);
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getString("lastmodifiedby")).thenReturn("user1");

        // Return null for fields that are not mandatory or could be null
        when(rs.getObject("additionalDetails")).thenReturn(null);

        List<CourtCase> cases = rowMapper.extractData(rs);

        assertNotNull(cases);
        assertEquals(1, cases.size());
        assertEquals("tenant1", cases.get(0).getTenantId());
        assertEquals("user1", cases.get(0).getAuditdetails().getCreatedBy());

        verify(rs, times(1)).getString("casenumber");
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

