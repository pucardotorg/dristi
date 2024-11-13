package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseSummary;
import org.pucar.dristi.web.models.PartySummary;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseSummaryRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CaseSummaryRowMapper caseSummaryRowMapper;

    @Mock
    private ResultSet resultSet;

    @Test
    public void testExtractData_withValidData() throws SQLException, DataAccessException, JsonProcessingException {
        when(resultSet.next()).thenReturn(true, false);

        // Setting up case data
        when(resultSet.getString("id")).thenReturn("case1");
        when(resultSet.getString("tenantid")).thenReturn("tenant1");
        when(resultSet.getString("casetitle")).thenReturn("Case Title 1");
        when(resultSet.getLong("filingdate")).thenReturn(123456789L);
        when(resultSet.getString("stage")).thenReturn("stage1");
        when(resultSet.getString("substage")).thenReturn("substage1");
        when(resultSet.getString("outcome")).thenReturn("outcome1");
        when(resultSet.getString("courtid")).thenReturn("courtId");
        when(resultSet.getLong("registrationdate")).thenReturn(123L);
        when(resultSet.getString("cmpnumber")).thenReturn("cmp-123");

        // Setting up litigant
        when(resultSet.getString("litigant_id")).thenReturn("litigant1");
        when(resultSet.getString("litigant_partycategory")).thenReturn("partyCategory1");
        when(resultSet.getString("litigant_partytype")).thenReturn("partyType1");
        when(resultSet.getString("litigant_individualid")).thenReturn("individual1");
        when(resultSet.getString("litigant_additionaldetails")).thenReturn("{\"fullName\": \"John Doe\"}");
        when(resultSet.getString("litigant_organisationid")).thenReturn("org1");
        ObjectNode jsonNode = new ObjectNode(JsonNodeFactory.instance);
        jsonNode.put("fullName", "John Doe");

        when(objectMapper.readTree("{\"fullName\": \"John Doe\"}")).thenReturn(jsonNode);
        // Setting up representative
        when(resultSet.getString("representative_id")).thenReturn("rep1");
        when(resultSet.getString("representative_case_id")).thenReturn("case1");
        when(resultSet.getString("representative_advocateid")).thenReturn("adv1");

        // Setting up statutes and sections
        when(resultSet.getString("statute_section_id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("statute_section_tenantid")).thenReturn("tenant1");
        when(resultSet.getString("statute_section_sections")).thenReturn("section1,section2");
        when(resultSet.getString("statute_section_subsections")).thenReturn("sub1,sub2");
        when(resultSet.getString("statute_section_statutes")).thenReturn("Statute1");

        List<CaseSummary> result = caseSummaryRowMapper.extractData(resultSet);

        assertNotNull(result);
        assertEquals(1, result.size());

        CaseSummary caseSummary = result.get(0);
        assertEquals("case1", caseSummary.getId());
        assertEquals("tenant1", caseSummary.getTenantId());
        assertEquals("Case Title 1", caseSummary.getCaseTitle());
        assertEquals(123456789L, caseSummary.getFilingDate());
        assertEquals("stage1", caseSummary.getStage());
        assertEquals("outcome1", caseSummary.getOutcome());
        assertEquals(1, caseSummary.getLitigants().size());
        assertEquals(1, caseSummary.getRepresentatives().size());

        assertEquals("courtId", caseSummary.getCourtId());
        assertEquals("cmp-123", caseSummary.getRegistrationNumber());
        assertEquals(123, caseSummary.getRegistrationDate());

        // Assert litigants
        PartySummary partySummary = caseSummary.getLitigants().get(0);
        assertEquals("partyCategory1", partySummary.getPartyCategory());
        assertEquals("partyType1", partySummary.getPartyType());

        // Assert statutes and sections
        assertEquals("Statute1 section1, section2", caseSummary.getStatutesAndSections());
    }

    @Test
    public void testExtractData_withNoLitigantsOrRepresentatives() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("case1");

        List<CaseSummary> result = caseSummaryRowMapper.extractData(resultSet);
        assert result != null;
        assertEquals(1, result.size());

        CaseSummary caseSummary = result.get(0);
        assertTrue(caseSummary.getLitigants().isEmpty());
        assertTrue(caseSummary.getRepresentatives().isEmpty());
    }

    @Test
    public void testExtractData_withMultipleCases() throws SQLException {
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn("case1", "case2");
        when(resultSet.getString("tenantid")).thenReturn("tenant1", "tenant2");

        List<CaseSummary> result = caseSummaryRowMapper.extractData(resultSet);
        assert result != null;
        assertEquals(2, result.size());

        assertEquals("tenant1", result.get(0).getTenantId());
        assertEquals("tenant2", result.get(1).getTenantId());
    }

    @Test
    public void testStringToList_withValidString() {
        String str = "item1,item2,item3";
        List<String> result = caseSummaryRowMapper.stringToList(str);

        assertEquals(3, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("item2"));
        assertTrue(result.contains("item3"));
    }

    @Test
    public void testStringToList_withNullString() {
        String str = null;
        List<String> result = caseSummaryRowMapper.stringToList(str);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStatuteAndSectionsString_withMultipleSections() {
        StringBuilder existingStatutes = new StringBuilder();
        List<String> sections = Arrays.asList("section1", "section2");

        String result = caseSummaryRowMapper.getStatuteAndSectionsString(existingStatutes, "Statute1", sections);

        assertEquals("Statute1 section1, section2", result);
    }

    @Test
    public void testGetStatuteAndSectionsString_withNoSections() {
        StringBuilder existingStatutes = new StringBuilder();
        List<String> sections = new ArrayList<>();

        String result = caseSummaryRowMapper.getStatuteAndSectionsString(existingStatutes, "Statute1", sections);

        assertEquals("Statute1", result);
    }

    @Test
    public void testGetStatuteAndSectionsString_withExistingStatutes() {
        StringBuilder existingStatutes = new StringBuilder("Statute0");
        List<String> sections = List.of("section1");

        String result = caseSummaryRowMapper.getStatuteAndSectionsString(existingStatutes, "Statute1", sections);

        assertEquals("Statute0;Statute1 section1", result);
    }

    @Test
    public void testGetStatuteAndSectionsString_withMultipleExistingStatutes() {
        StringBuilder existingStatutes = new StringBuilder("Statute0;Statute1");
        List<String> sections = List.of("section1");

        String result = caseSummaryRowMapper.getStatuteAndSectionsString(existingStatutes, "Statute2", sections);

        assertEquals("Statute0;Statute1;Statute2 section1", result);
    }
}
