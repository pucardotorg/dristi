package org.pucar.dristi.repository.querybuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.PARSING_ERROR;
import static org.pucar.dristi.config.ServiceConstants.SEARCH_QUERY_EXCEPTION;

class HearingQueryBuilderTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private HearingQueryBuilder hearingQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHearingSearchQuery_NoCriteria() {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        HearingCriteria criteria = HearingCriteria.builder().build();

        // Act
        String query = hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, criteria);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("SELECT * FROM dristi_hearing WHERE 1=1"));
    }

    @Test
    void getHearingSearchQuery_WithCriteria() {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        String cnrNumber = "CNR123";
        String applicationNumber = "APP456";
        String hearingId = "HEARING789";
        String hearingType = "type1";
        String filingNumber = "FILE123";
        String tenantId = "tenant1";
        Long fromDate = LocalDate.of(2024, 1, 1).atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        Long toDate = LocalDate.of(2025, 1, 1).atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        String attendeeIndividualId = "Ind-01";

        HearingCriteria criteria = HearingCriteria.builder()
                .cnrNumber(cnrNumber)
                .applicationNumber(applicationNumber)
                .hearingId(hearingId)
                .filingNumber(filingNumber)
                .tenantId(tenantId)
                .fromDate(fromDate)
                .toDate(toDate)
                .hearingType(hearingType)
                .attendeeIndividualId(attendeeIndividualId)
                .build();

        // Act
        String query = hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, criteria);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("AND cnrNumbers @> ?::jsonb"));
        assertTrue(query.contains("AND applicationNumbers @> ?::jsonb"));
        assertTrue(query.contains("AND hearingid = ?"));
        assertTrue(query.contains("AND filingNumber @> ?::jsonb"));
        assertTrue(query.contains("AND tenantId = ?"));
        assertTrue(query.contains("AND startTime >= ?"));
        assertTrue(query.contains("AND startTime < ?"));
        assertTrue(query.contains("AND hearingtype = ?"));
        assertTrue(query.contains("AND EXISTS (SELECT 1 FROM jsonb_array_elements(attendees) elem WHERE elem->>'individualId' = ?)"));
        assertEquals(9, preparedStmtList.size());
        assertEquals("[\"CNR123\"]", preparedStmtList.get(0));
        assertEquals("[\"APP456\"]", preparedStmtList.get(1));
        assertEquals("HEARING789", preparedStmtList.get(2));
        assertEquals(hearingType, preparedStmtList.get(3));
        assertEquals("[\"FILE123\"]", preparedStmtList.get(4));
        assertEquals("tenant1", preparedStmtList.get(5));
        assertEquals(fromDate, preparedStmtList.get(6));
        assertEquals(toDate, preparedStmtList.get(7));
        assertEquals(attendeeIndividualId, preparedStmtList.get(8));
    }

    @Test
    void getHearingSearchQuery_Exception() {
        // Arrange
        List<Object> preparedStmtList = null;
        HearingCriteria criteria = HearingCriteria.builder().hearingId("Hearing123").build();

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, criteria));
        assertEquals(SEARCH_QUERY_EXCEPTION, exception.getCode());
    }

    @Test
    void getDocumentSearchQuery() {
        // Arrange
        List<String> ids = List.of("doc1", "doc2");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = hearingQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_hearing_document doc"));
        assertTrue(query.contains("WHERE doc.hearingid IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals("doc1", preparedStmtList.get(0));
        assertEquals("doc2", preparedStmtList.get(1));
    }

    @Test
    void getDocumentSearchQuery_EmptyIds() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = hearingQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_hearing_document doc"));
        assertFalse(query.contains("WHERE doc.hearingid IN"));
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void getDocumentSearchQuery_Exception() {
        // Arrange
        List<String> ids = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList));
    }

    @Test
    void buildUpdateTranscriptAdditionalAttendeesQuery_Success() throws JsonProcessingException {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        String hearingId = "hearing123";
        String tenantId = "tenant1";
        String vcLink = "test_vc_link";
        String notes = "updatedNote";
        List<String> transcriptList = List.of("transcript1", "transcript2");
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setLastModifiedBy("user1");
        auditDetails.setLastModifiedTime(123456789L);
        Object additionalDetails = new HashMap<String, Object>();
        List<Attendee> attendees = new ArrayList<>();

        Hearing hearing = Hearing.builder()
                .hearingId(hearingId)
                .tenantId(tenantId)
                .vcLink(vcLink)
                .transcript(transcriptList)
                .auditDetails(auditDetails)
                .additionalDetails(additionalDetails)
                .attendees(attendees)
                .notes(notes)
                .build();

        String transcriptJson = "[\"transcript1\",\"transcript2\"]";
        String additionalDetailsJson = "{}";
        String attendeesJson = "[{}]";

        when(mapper.writeValueAsString(transcriptList)).thenReturn(transcriptJson);
        when(mapper.writeValueAsString(additionalDetails)).thenReturn(additionalDetailsJson);
        when(mapper.writeValueAsString(attendees)).thenReturn(attendeesJson);

        // Act
        String query = hearingQueryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(preparedStmtList, hearing);

        // Assert
        assertEquals("UPDATE dristi_hearing SET transcript = ?::jsonb , additionaldetails = ?::jsonb , attendees = ?::jsonb , vclink = ? , notes = ? , lastModifiedBy = ? , lastModifiedTime = ? WHERE hearingId = ? AND tenantId = ?", query);
        assertEquals(9, preparedStmtList.size());
        assertEquals(transcriptJson, preparedStmtList.get(0));
        assertEquals(additionalDetailsJson, preparedStmtList.get(1));
        assertEquals(attendeesJson, preparedStmtList.get(2));
        assertEquals(vcLink,preparedStmtList.get(3));
        assertEquals(notes,preparedStmtList.get(4));
        assertEquals("user1", preparedStmtList.get(5));
        assertEquals(123456789L, preparedStmtList.get(6));
        assertEquals(hearingId, preparedStmtList.get(7));
        assertEquals(tenantId, preparedStmtList.get(8));
    }

    @Test
    void buildUpdateTranscriptAdditionalAttendeesQuery_JsonProcessingException() throws JsonProcessingException {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        String hearingId = "hearing123";
        String tenantId = "tenant1";
        List<String> transcriptList = List.of("transcript1", "transcript2");
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setLastModifiedBy("user1");
        auditDetails.setLastModifiedTime(123456789L);
        Object additionalDetails = new HashMap<String, Object>();
        List<Attendee> attendees = new ArrayList<>();

        Hearing hearing = Hearing.builder()
                .hearingId(hearingId)
                .tenantId(tenantId)
                .transcript(transcriptList)
                .auditDetails(auditDetails)
                .additionalDetails(additionalDetails)
                .attendees(attendees)
                .build();

        when(mapper.writeValueAsString(transcriptList)).thenThrow(new JsonProcessingException("Error") {});
        when(mapper.writeValueAsString(additionalDetails)).thenReturn("{}");
        when(mapper.writeValueAsString(attendees)).thenReturn("[{}]");

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingQueryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(preparedStmtList, hearing));
        assertEquals(PARSING_ERROR, exception.getCode());
    }

    @Test
    void addOrderByQueryTest() {
        // Arrange
        String query = "SELECT * FROM dristi_hearing WHERE 1=1";
        Pagination pagination = Pagination.builder().sortBy("startTime").order(Order.ASC).build();

        // Act
        query = hearingQueryBuilder.addOrderByQuery(query, pagination);

        // Assert
        assertTrue(query.contains("ORDER BY startTime ASC"));
    }

    @Test
    void getTotalCountQuery() {
        // Arrange
        String searchQuery = "SELECT * FROM dristi_hearing WHERE 1=1 AND tenantId = ?";
        String expectedCountQuery = "SELECT COUNT(*) FROM (SELECT * FROM dristi_hearing WHERE 1=1 AND tenantId = ?) total_result";

        // Act
        String countQuery = hearingQueryBuilder.getTotalCountQuery(searchQuery);

        // Assert
        assertEquals(expectedCountQuery, countQuery);
    }

    @Test
    void addPaginationQuery() {
        // Arrange
        String query = "SELECT * FROM dristi_hearing WHERE 1=1";
        List<Object> preparedStmtList = new ArrayList<>();
        Pagination pagination = Pagination.builder().limit(10d).offSet(20d).build();

        // Act
        query = hearingQueryBuilder.addPaginationQuery(query, pagination, preparedStmtList);

        // Assert
        assertTrue(query.contains(" LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(10d, preparedStmtList.get(0));
        assertEquals(20d, preparedStmtList.get(1));
    }

    @Test
    void addCriteriaString_WithCriteria() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM dristi_hearing WHERE 1=1");
        List<Object> preparedStmtList = new ArrayList<>();
        String criteria = "CNR123";
        String str = " AND cnrNumbers @> ?::jsonb";
        Object listItem = "[\"CNR123\"]";

        // Act
        hearingQueryBuilder.addCriteriaString(criteria, query, str, preparedStmtList, listItem);

        // Assert
        assertTrue(query.toString().contains("AND cnrNumbers @> ?::jsonb"));
        assertEquals(1, preparedStmtList.size());
        assertEquals("[\"CNR123\"]", preparedStmtList.get(0).toString());
    }

    @Test
    void addCriteriaString_NullCriteria() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM dristi_hearing WHERE 1=1");
        List<Object> preparedStmtList = new ArrayList<>();
        String criteria = null;
        String str = " AND cnrNumbers @> ?::jsonb";
        Object listItem = "[\"CNR123\"]";

        // Act
        hearingQueryBuilder.addCriteriaString(criteria, query, str, preparedStmtList, listItem);

        // Assert
        assertFalse(query.toString().contains("AND cnrNumbers @> ?::jsonb"));
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void addCriteriaDate_WithDate() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM dristi_hearing WHERE 1=1");
        List<Object> preparedStmtList = new ArrayList<>();
        Long criteria = LocalDate.of(2023, 1, 1).atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        String str = " AND startTime >= ?";

        // Act
        hearingQueryBuilder.addCriteriaDate(criteria, query, str, preparedStmtList);

        // Assert
        assertTrue(query.toString().contains("AND startTime >= ?"));
        assertEquals(1, preparedStmtList.size());
        assertEquals(criteria, preparedStmtList.get(0));
    }

    @Test
    void addCriteriaDate_NullDate() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM dristi_hearing WHERE 1=1");
        List<Object> preparedStmtList = new ArrayList<>();
        String str = " AND startTime >= ?";

        // Act
        hearingQueryBuilder.addCriteriaDate(null, query, str, preparedStmtList);

        // Assert
        assertFalse(query.toString().contains("AND startTime >= ?"));
        assertTrue(preparedStmtList.isEmpty());
    }
}
