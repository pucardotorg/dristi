package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.OpenApiCaseSummaryRequest;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenApiCaseSummaryQueryBuilderTest {

    @InjectMocks
    private OpenApiCaseSummaryQueryBuilder queryBuilder;

    private OpenApiCaseSummaryRequest searchCriteria;
    private List<Object> preparedStatementValues;
    private List<Integer> preparedStatementValueTypes;

    @BeforeEach
    void setUp() {
        searchCriteria = new OpenApiCaseSummaryRequest();
        preparedStatementValues = new ArrayList<>();
        preparedStatementValueTypes = new ArrayList<>();
    }

    @Test
    void testGetCaseBaseQuery_WithTenantId() {
        searchCriteria.setTenantId("tenant-123");

        String query = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("cases.tenantId = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("tenant-123", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementValueTypes.get(0));
    }

    @Test
    void testGetCaseBaseQuery_WithCnrNumber() {
        searchCriteria.setCnrNumber("CNR123");

        String query = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("cases.cnrNumber = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("CNR123", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementValueTypes.get(0));
    }

    @Test
    void testGetCaseBaseQuery_WithCaseType() {
        searchCriteria.setCaseType("CMP");
        searchCriteria.setCaseNumber(123);

        String query = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("cases.caseType = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("CMP", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementValueTypes.get(0));
    }

    @Test
    void testGetCaseBaseQuery_WithCaseTypeAndYearRange() {
        searchCriteria.setCaseType("CMP");
        searchCriteria.setYear(2020);
        searchCriteria.setStartYear(2020L);
        searchCriteria.setEndYear(2021L);

        String query = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("cases.caseType = ?"));
        assertTrue(query.contains("cases.registrationdate BETWEEN ? AND ?"));
        assertEquals(3, preparedStatementValues.size());
        assertEquals("CMP", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementValueTypes.get(0));
        assertEquals(2020L, preparedStatementValues.get(1));
        assertEquals(2021L, preparedStatementValues.get(2));
    }

    @Test
    void testGetCaseBaseQuery_WithCaseTypeST() {
        searchCriteria.setCaseType("ST");
        searchCriteria.setYear(2020);
        searchCriteria.setCaseNumber(123);

        String query = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("cases.caseType = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("ST", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementValueTypes.get(0));
    }

    @Test
    void testGetCaseSummarySearchQuery() {
        searchCriteria.setTenantId("tenant-123");
        String baseQuery = queryBuilder.getCaseBaseQuery(searchCriteria, preparedStatementValues, preparedStatementValueTypes);

        String query = queryBuilder.getCaseSummarySearchQuery(baseQuery);

        assertNotNull(query);
        assertTrue(query.contains("WITH unique_cases AS ("));
        assertTrue(query.contains("LEFT JOIN"));
    }

    @Test
    void testAddOrderByQuery_WithPagination() {
        Pagination pagination = new Pagination();
        pagination.setSortBy("filingdate");
        pagination.setOrder(Order.ASC);

        String query = queryBuilder.addOrderByQuery("SELECT * FROM cases", pagination);

        assertNotNull(query);
        assertTrue(query.contains("ORDER BY cases.filingdate ASC"));
    }

    @Test
    void testAddOrderByQuery_WithoutPagination() {
        String query = queryBuilder.addOrderByQuery("SELECT * FROM cases", null);

        assertNotNull(query);
        assertTrue(query.contains("ORDER BY cases.registrationDate DESC"));
    }

    @Test
    void testAddPaginationQuery() {
        Pagination pagination = new Pagination();
        pagination.setLimit(10);
        pagination.setOffSet(20);

        String query = queryBuilder.addPaginationQuery("SELECT * FROM cases", preparedStatementValues, pagination, preparedStatementValueTypes);

        assertNotNull(query);
        assertTrue(query.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStatementValues.size());
        assertEquals(10, preparedStatementValues.get(0));
        assertEquals(20, preparedStatementValues.get(1));
        assertEquals(Types.INTEGER, preparedStatementValueTypes.get(0));
        assertEquals(Types.INTEGER, preparedStatementValueTypes.get(1));
    }
}
