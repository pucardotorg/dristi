package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.CaseListLineItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenApiCaseListRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OpenApiCaseListRowMapper rowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractData_WithValidData() throws SQLException {
        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("case-123");
        when(resultSet.getString("casetitle")).thenReturn("Case Title");
        when(resultSet.getString("casetype")).thenReturn("ST");
        when(resultSet.getString("courtcasenumber")).thenReturn("ST-001");

        // Invoke the method
        List<CaseListLineItem> result = rowMapper.extractData(resultSet);

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ST-001", result.get(0).getCaseNumber());
        assertEquals("Case Title", result.get(0).getCaseTitle());

        verify(resultSet, times(1)).getString("id");
        verify(resultSet, times(1)).getString("casetitle");
        verify(resultSet, times(1)).getString("casetype");
        verify(resultSet, times(1)).getString("courtcasenumber");
    }

    @Test
    void testExtractData_WithMultipleRows() throws SQLException {
        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true, true, false);

        // First row
        when(resultSet.getString("id")).thenReturn("case-123", "case-456");
        when(resultSet.getString("casetitle")).thenReturn("Case Title 1", "Case Title 2");
        when(resultSet.getString("casetype")).thenReturn("ST", "CMP");
        when(resultSet.getString("courtcasenumber")).thenReturn("ST-001", (String) null);
        when(resultSet.getString("cmpnumber")).thenReturn(null, "CMP-002");

        // Invoke the method
        List<CaseListLineItem> result = rowMapper.extractData(resultSet);

        // Verify the results
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("ST-001", result.get(0).getCaseNumber());
        assertEquals("Case Title 1", result.get(0).getCaseTitle());
        assertEquals("Case Title 2", result.get(1).getCaseTitle());

        verify(resultSet, times(2)).getString("id");
        verify(resultSet, times(2)).getString("casetitle");
        verify(resultSet, times(2)).getString("casetype");
        verify(resultSet, times(1)).getString("courtcasenumber");
        verify(resultSet, times(1)).getString("cmpnumber");
    }

    @Test
    void testGetCaseNumber_WithValidCaseType() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("ST");
        when(resultSet.getString("courtcasenumber")).thenReturn("ST-001");

        String caseNumber = rowMapper.getCaseNumber(resultSet);

        assertEquals("ST-001", caseNumber);
        verify(resultSet, times(1)).getString("casetype");
        verify(resultSet, times(1)).getString("courtcasenumber");
    }

    @Test
    void testGetCaseNumber_WithInvalidCaseType() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("UNKNOWN");

        String caseNumber = rowMapper.getCaseNumber(resultSet);

        assertNull(caseNumber);
        verify(resultSet, times(1)).getString("casetype");
    }

    @Test
    void testExtractData_WithEmptyResultSet() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        List<CaseListLineItem> result = rowMapper.extractData(resultSet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(resultSet, times(1)).next();
    }
}
