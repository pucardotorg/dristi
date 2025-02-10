package digit.repository.querybuilder;

import digit.web.models.CaseDiaryExistCriteria;
import digit.web.models.CaseDiarySearchCriteria;
import digit.web.models.Order;
import digit.web.models.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiaryEntryQueryBuilderTest {

    @InjectMocks
    private DiaryEntryQueryBuilder queryBuilder;

    private List<Object> preparedStatementValues;
    private List<Integer> preparedStatementTypeValues;
    private static final String BASE_QUERY = "SELECT dde.id as id,dde.tenant_id as tenantId,dde.case_number as caseNumber,dde.judge_id as judgeId, " +
            "dde.entry_date as entryDate,dde.businessOfDay as businessOfDay,dde.reference_id as referenceId,dde.reference_type as referenceType," +
            "dde.hearingDate as hearingDate,dde.additional_details as additionalDetails,dde.created_by as createdBy,dde.last_modified_by as lastModifiedBy," +
            "dde.created_time as createdTime,dde.last_modified_time as lastModifiedTime FROM dristi_diaryentries dde";

    @BeforeEach
    void setUp() {
        preparedStatementValues = new ArrayList<>();
        preparedStatementTypeValues = new ArrayList<>();
    }

    @Test
    void getDiaryEntryQuery_WithNullSearchCriteria_ReturnsBaseQuery() {
        String query = queryBuilder.getDiaryEntryQuery(null, preparedStatementValues, preparedStatementTypeValues);

        assertEquals(BASE_QUERY, query);
        assertTrue(preparedStatementValues.isEmpty());
        assertTrue(preparedStatementTypeValues.isEmpty());
    }

    @Test
    void getDiaryEntryQuery_WithTenantId_ReturnsQueryWithTenantFilter() {
        CaseDiarySearchCriteria criteria = CaseDiarySearchCriteria.builder()
                .tenantId("default-tenant")
                .build();

        String query = queryBuilder.getDiaryEntryQuery(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertEquals(BASE_QUERY + " WHERE dde.tenant_id = ?", query);
        assertEquals(1, preparedStatementValues.size());
        assertEquals("default-tenant", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementTypeValues.get(0).intValue());
    }

    @Test
    void getDiaryEntryQuery_WithDate_ReturnsQueryWithDateFilter() {
        Long now = 1L;
        CaseDiarySearchCriteria criteria = CaseDiarySearchCriteria.builder()
                .date(now)
                .build();

        String query = queryBuilder.getDiaryEntryQuery(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertEquals(BASE_QUERY + " WHERE dde.entry_date = ?", query);
        assertEquals(1, preparedStatementValues.size());
        assertEquals(now, preparedStatementValues.get(0));
        assertEquals(Types.BIGINT, preparedStatementTypeValues.get(0).intValue());
    }

    @Test
    void getDiaryEntryQuery_WithCaseId_ReturnsQueryWithCaseFilter() {
        CaseDiarySearchCriteria criteria = CaseDiarySearchCriteria.builder()
                .caseId("CASE-123")
                .build();

        String query = queryBuilder.getDiaryEntryQuery(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertEquals(BASE_QUERY + " WHERE dde.case_number = ?", query);
        assertEquals(1, preparedStatementValues.size());
        assertEquals("CASE-123", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementTypeValues.get(0));
    }

    @Test
    void getDiaryEntryQuery_WithJudgeId_ReturnsQueryWithJudgeFilter() {
        CaseDiarySearchCriteria criteria = CaseDiarySearchCriteria.builder()
                .judgeId("JUDGE-123")
                .build();

        String query = queryBuilder.getDiaryEntryQuery(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertEquals(BASE_QUERY + " WHERE dde.judge_id = ?", query);
        assertEquals(1, preparedStatementValues.size());
        assertEquals("JUDGE-123", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementTypeValues.get(0).intValue());
    }

    @Test
    void getDiaryEntryQuery_WithAllCriteria_ReturnsFullQuery() {
        Long now = 1L;
        CaseDiarySearchCriteria criteria = CaseDiarySearchCriteria.builder()
                .tenantId("default-tenant")
                .date(now)
                .caseId("CASE-123")
                .judgeId("JUDGE-123")
                .build();

        String query = queryBuilder.getDiaryEntryQuery(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("WHERE"));
        assertTrue(query.contains("AND"));
        assertEquals(4, preparedStatementValues.size());
        assertEquals(4, preparedStatementTypeValues.size());
    }

    @Test
    void getTotalCountQuery_ReturnsFormattedQuery() {
        String baseQuery = "SELECT * FROM table";
        String expected = "SELECT COUNT(*) FROM (SELECT * FROM table) total_result";

        String result = queryBuilder.getTotalCountQuery(baseQuery);

        assertEquals(expected, result);
    }

    @Test
    void addPaginationQuery_AddsLimitAndOffset() {
        String baseQuery = "SELECT * FROM table";
        Pagination pagination = Pagination.builder()
                .limit(10.0)
                .offSet(5.0)
                .build();

        String result = queryBuilder.addPaginationQuery(baseQuery, preparedStatementValues, pagination, preparedStatementTypeValues);

        assertEquals(baseQuery + " LIMIT ? OFFSET ?", result);
        assertEquals(2, preparedStatementValues.size());
        assertEquals(Types.INTEGER, preparedStatementTypeValues.get(0).intValue());
        assertEquals(Types.INTEGER, preparedStatementTypeValues.get(1).intValue());
    }

    @Test
    void addOrderByQuery_WithValidPagination_ReturnsOrderedQuery() {
        String baseQuery = "SELECT * FROM table";
        Pagination pagination = Pagination.builder()
                .sortBy("name")
                .order(Order.ASC)
                .build();

        String result = queryBuilder.addOrderByQuery(baseQuery, pagination);

        assertEquals(baseQuery + " ORDER BY name ASC ", result);
    }

    @Test
    void addOrderByQuery_WithNullPagination_ReturnsDefaultOrderBy() {
        String baseQuery = "SELECT * FROM table";

        String result = queryBuilder.addOrderByQuery(baseQuery, null);

        assertEquals(baseQuery + " ORDER BY createdtime DESC ", result);
    }

    @Test
    void addOrderByQuery_WithInvalidSortBy_ReturnsDefaultOrderBy() {
        String baseQuery = "SELECT * FROM table";
        Pagination pagination = Pagination.builder()
                .sortBy("name; DROP TABLE users;") // SQL injection attempt
                .order(Order.ASC)
                .build();

        String result = queryBuilder.addOrderByQuery(baseQuery, pagination);

        assertEquals(baseQuery + " ORDER BY createdtime DESC ", result);
    }

    @Test
    void addOrderByQuery_WithNullSortByOrOrder_ReturnsDefaultOrderBy() {
        String baseQuery = "SELECT * FROM table";
        Pagination pagination = Pagination.builder()
                .sortBy(null)
                .order(null)
                .build();

        String result = queryBuilder.addOrderByQuery(baseQuery, pagination);

        assertEquals(baseQuery + " ORDER BY createdtime DESC ", result);
    }

    @Test
    void getExistingDiaryEntryQuery_WithAllCriteria_ReturnsFullQuery() {
        Long now = 1L;
        UUID uuid = UUID.randomUUID();
        CaseDiaryExistCriteria criteria = CaseDiaryExistCriteria.builder()
                .tenantId("default-tenant")
                .id(uuid)
                .build();

        String query = queryBuilder.getExistingDiaryEntry(criteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("WHERE"));
        assertTrue(query.contains("AND"));
        assertEquals(2, preparedStatementValues.size());
        assertEquals(2, preparedStatementTypeValues.size());
    }

}