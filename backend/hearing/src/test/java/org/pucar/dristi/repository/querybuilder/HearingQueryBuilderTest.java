package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HearingQueryBuilderTest {

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
        String cnrNumber = null;
        String applicationNumber = null;
        String hearingId = null;
        String filingNumber = null;
        String tenantId = null;
        LocalDate fromDate = null;
        LocalDate toDate = null;
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = null;

        // Act
        String query = hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("SELECT * FROM dristi_hearing WHERE 1=1"));
        assertTrue(query.contains("ORDER BY id"));
        assertTrue(query.contains("LIMIT ?"));
        assertTrue(query.contains("OFFSET ?"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(10, preparedStmtList.get(0));
        assertEquals(0, preparedStmtList.get(1));
    }

    @Test
    void getHearingSearchQuery_WithCriteria() {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        String cnrNumber = "CNR123";
        String applicationNumber = "APP456";
        String hearingId = "HEARING789";
        String filingNumber = "FILE123";
        String tenantId = "tenant1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "startTime";

        // Act
        String query = hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("AND cnrNumbers @> ?::jsonb"));
        assertTrue(query.contains("AND applicationNumbers @> ?::jsonb"));
        assertTrue(query.contains("AND hearingid = ?"));
        assertTrue(query.contains("AND filingNumber @> ?::jsonb"));
        assertTrue(query.contains("AND tenantId = ?"));
        assertTrue(query.contains("AND createdTime >= ?"));
        assertTrue(query.contains("AND createdTime <= ?"));
        assertTrue(query.contains("ORDER BY startTime DESC"));
        assertEquals(9, preparedStmtList.size());
    }

    @Test
    void getHearingSearchQuery_Exception() {
        // Arrange
        List<Object> preparedStmtList = null;
        String cnrNumber = "CNR123";
        String applicationNumber = "APP456";
        String hearingId = "HEARING789";
        String filingNumber = "FILE123";
        String tenantId = "tenant1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "startTime";

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingQueryBuilder.getHearingSearchQuery(preparedStmtList, cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy));
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
}
