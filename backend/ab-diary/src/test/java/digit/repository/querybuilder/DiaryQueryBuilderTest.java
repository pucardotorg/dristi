package digit.repository.querybuilder;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiaryQueryBuilderTest {

    @InjectMocks
    private DiaryQueryBuilder queryBuilder;

    private CaseDiarySearchCriteria searchCriteria;
    private List<Object> preparedStatementValues;
    private List<Integer> preparedStatementTypeValues;
    private Pagination pagination;

    @BeforeEach
    void setUp() {
        searchCriteria = new CaseDiarySearchCriteria();
        preparedStatementValues = new ArrayList<>();
        preparedStatementTypeValues = new ArrayList<>();
        pagination = new Pagination();
    }

    @Test
    void getCaseDiaryQuery_WithTenantId_BuildsQuery() {
        searchCriteria.setTenantId("tenant-1");

        String query = queryBuilder.getCaseDiaryQuery(searchCriteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("dcd.tenant_id = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("tenant-1", preparedStatementValues.get(0));
        assertEquals(Types.VARCHAR, preparedStatementTypeValues.get(0));
    }

    @Test
    void getCaseDiaryQuery_WithDate_BuildsQuery() {
        searchCriteria.setDate(1627987200000L);

        String query = queryBuilder.getCaseDiaryQuery(searchCriteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("dcd.diary_date = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals(1627987200000L, preparedStatementValues.get(0));
        assertEquals(Types.BIGINT, preparedStatementTypeValues.get(0));
    }

    @Test
    void getCaseDiaryQuery_WithAllCriteria_BuildsQuery() {
        searchCriteria.setTenantId("tenant-1");
        searchCriteria.setDate(1627987200000L);
        searchCriteria.setCaseId("CASE123");
        searchCriteria.setJudgeId("JUDGE123");
        searchCriteria.setDiaryType("TYPE1");

        String query = queryBuilder.getCaseDiaryQuery(searchCriteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("dcd.tenant_id = ?"));
        assertTrue(query.contains("dcd.diary_date = ?"));
        assertTrue(query.contains("dcd.case_number = ?"));
        assertTrue(query.contains("dcd.judge_id = ?"));
        assertTrue(query.contains("dcd.diary_type = ?"));
        assertEquals(5, preparedStatementValues.size());
    }

    @Test
    void addPaginationQuery_AppendsLimitAndOffset() {
        pagination.setLimit(10.0);
        pagination.setOffSet(5.0);

        String query = queryBuilder.addPaginationQuery("SELECT * FROM diary", preparedStatementValues, pagination, preparedStatementTypeValues);

        assertTrue(query.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStatementValues.size());
        assertEquals(10.0, preparedStatementValues.get(0));
        assertEquals(5.0, preparedStatementValues.get(1));
    }

    @Test
    void addOrderByQuery_WithValidPagination_AppendsOrderByClause() {
        pagination.setSortBy("dcd.created_time");
        pagination.setOrder(Order.DESC);

        String query = queryBuilder.addOrderByQuery("SELECT * FROM diary", pagination);

        assertTrue(query.contains("ORDER BY dcd.created_time DESC"));
    }

    @Test
    void addOrderByQuery_WithInvalidSortBy_UsesDefaultOrder() {
        pagination.setSortBy("; DROP TABLE diary;"); // SQL injection attempt
        pagination.setOrder(Order.ASC);

        String query = queryBuilder.addOrderByQuery("SELECT * FROM diary", pagination);

        assertTrue(query.contains("ORDER BY dcd.created_time DESC"));
    }

    @Test
    void getTotalCountQuery_ReplacesBaseQueryPlaceholder() {
        String baseQuery = "SELECT * FROM diary";
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);

        assertTrue(countQuery.contains("FROM (SELECT * FROM diary) total_result"));
    }

    @Test
    void getSearchDiaryQuery_WithTenantId_BuildsQuery() {
        searchCriteria.setTenantId("tenant-1");

        String query = queryBuilder.getSearchDiaryQuery(searchCriteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("dcd.tenant_id = ?"));
        assertEquals(1, preparedStatementValues.size());
        assertEquals("tenant-1", preparedStatementValues.get(0));
    }

    @Test
    void getSearchDiaryQuery_WithAllCriteria_BuildsQuery() {
        searchCriteria.setTenantId("tenant-1");
        searchCriteria.setDate(1627987200000L);
        searchCriteria.setCaseId("CASE123");
        searchCriteria.setJudgeId("JUDGE123");
        searchCriteria.setDiaryType("TYPE1");

        String query = queryBuilder.getSearchDiaryQuery(searchCriteria, preparedStatementValues, preparedStatementTypeValues);

        assertTrue(query.contains("dcd.tenant_id = ?"));
        assertTrue(query.contains("dcd.diary_date = ?"));
        assertTrue(query.contains("dcd.case_number = ?"));
        assertTrue(query.contains("dcd.judge_id = ?"));
        assertTrue(query.contains("dcd.diary_type = ?"));
        assertEquals(5, preparedStatementValues.size());
    }
}
