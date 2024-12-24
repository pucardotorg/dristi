package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.OpenApiCaseSummary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenApiCaseSummaryRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OpenApiCaseSummaryRowMapper rowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractData_WithValidData() throws SQLException, JsonProcessingException {
        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("case-123");
        when(resultSet.getString("cnrnumber")).thenReturn("CNR123");
        when(resultSet.getString("filingnumber")).thenReturn("FN123");
        when(resultSet.getLong("filingdate")).thenReturn(1625097600000L);
        when(resultSet.getLong("registrationdate")).thenReturn(1625184000000L);
        when(resultSet.getString("cmpnumber")).thenReturn("CMP123");
        when(resultSet.getString("casetype")).thenReturn("ST");
        when(resultSet.getString("stage")).thenReturn("Stage1");
        when(resultSet.getString("substage")).thenReturn("Substage1");

        // Invoke the method
        List<OpenApiCaseSummary> result = rowMapper.extractData(resultSet);

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        OpenApiCaseSummary caseSummary = result.get(0);
        assertEquals("CNR123", caseSummary.getCnrNumber());
        assertEquals("FN123", caseSummary.getFilingNumber());
        assertEquals(1625097600000L, caseSummary.getFilingDate());
        assertEquals(1625184000000L, caseSummary.getRegistrationDate());
        assertEquals("CMP123", caseSummary.getRegistrationNumber());
        assertEquals(OpenApiCaseSummary.CaseTypeEnum.ST, caseSummary.getCaseType());
        assertEquals("Stage1 - Substage1", caseSummary.getSubStage());

        verify(resultSet, times(1)).getString("id");
        verify(resultSet, times(1)).getString("cnrnumber");
        verify(resultSet, times(1)).getString("filingnumber");
        verify(resultSet, times(1)).getLong("filingdate");
        verify(resultSet, times(1)).getLong("registrationdate");
        verify(resultSet, times(1)).getString("cmpnumber");
    }

    @Test
    void testGetCaseType_WithValidType() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("ST");

        OpenApiCaseSummary.CaseTypeEnum caseType = rowMapper.getCaseType(resultSet);

        assertEquals(OpenApiCaseSummary.CaseTypeEnum.ST, caseType);
        verify(resultSet, times(1)).getString("casetype");
    }

    @Test
    void testGetCaseType_WithInvalidType() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("INVALID");

        OpenApiCaseSummary.CaseTypeEnum caseType = rowMapper.getCaseType(resultSet);

        assertNull(caseType);
        verify(resultSet, times(1)).getString("casetype");
    }

    @Test
    void testGetStatus_Pending() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("CMP");
        when(resultSet.getString("courtcasenumber")).thenReturn(null);

        OpenApiCaseSummary.StatusEnum status = rowMapper.getStatus(resultSet);

        assertEquals(OpenApiCaseSummary.StatusEnum.PENDING, status);
        verify(resultSet, times(1)).getString("casetype");
        verify(resultSet, times(1)).getString("courtcasenumber");
    }

    @Test
    void testGetStatus_Disposed() throws SQLException {
        when(resultSet.getString("casetype")).thenReturn("ST");
        when(resultSet.getString("outcome")).thenReturn("Disposed");

        OpenApiCaseSummary.StatusEnum status = rowMapper.getStatus(resultSet);

        assertEquals(OpenApiCaseSummary.StatusEnum.DISPOSED, status);
    }

    @Test
    void testStringToList_WithValidString() {
        String input = "section1,section2,section3";

        List<String> result = rowMapper.stringToList(input);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("section1"));
        assertTrue(result.contains("section2"));
        assertTrue(result.contains("section3"));
    }

    @Test
    void testStringToList_WithNullString() {
        String input = null;

        List<String> result = rowMapper.stringToList(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetNameForLitigant_WithValidJson() throws SQLException, JsonProcessingException {
        String additionalDetails = "{\"fullName\":\"John Doe\"}";
        when(resultSet.getString("litigant_additionaldetails")).thenReturn(additionalDetails);
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(additionalDetails)).thenReturn(jsonNode);
        when(jsonNode.has("fullName")).thenReturn(true);
        when(jsonNode.get("fullName")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("John Doe");

        String result = rowMapper.getNameForLitigant(resultSet);

        assertEquals("John Doe", result);
        verify(resultSet, times(1)).getString("litigant_additionaldetails");
        verify(objectMapper, times(1)).readTree(additionalDetails);
        verify(jsonNode, times(1)).has("fullName");
        verify(jsonNode, times(1)).asText();
    }

    @Test
    void testGetNameForLitigant_WithInvalidJson() throws SQLException, JsonProcessingException {
        String additionalDetails = "invalid-json";
        when(resultSet.getString("litigant_additionaldetails")).thenReturn(additionalDetails);
        when(objectMapper.readTree(additionalDetails)).thenThrow(JsonProcessingException.class);

        CustomException exception = assertThrows(CustomException.class, () -> rowMapper.getNameForLitigant(resultSet));

        assertEquals("ERROR_FETCHING_LITIGANT_NAME", exception.getCode());
        verify(resultSet, times(1)).getString("litigant_additionaldetails");
        verify(objectMapper, times(1)).readTree(additionalDetails);
    }
}
