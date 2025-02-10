package digit.repository;

import digit.repository.querybuilder.DiaryQueryBuilder;
import digit.repository.rowmapper.DiaryRowMapper;
import digit.repository.rowmapper.DiaryWithDocumentRowMapper;
import digit.web.models.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static digit.config.ServiceConstants.DIARY_SEARCH_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaryRepositoryTest {

    @Mock
    private DiaryQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DiaryRowMapper diaryRowMapper;

    @Mock
    private DiaryWithDocumentRowMapper diaryWithDocumentRowMapper;

    @InjectMocks
    private DiaryRepository diaryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCaseDiaries_Success_NoPagination() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        searchRequest.setPagination(null);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = "SELECT * FROM diaries";
        List<CaseDiaryListItem> expectedResult = Collections.singletonList(new CaseDiaryListItem());

        // Mock behavior
        when(queryBuilder.getSearchDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(query);
        when(queryBuilder.addOrderByQuery(query, null)).thenReturn(query);
        when(jdbcTemplate.query(query, preparedStmtList.toArray(), new int[0], diaryRowMapper)).thenReturn(expectedResult);

        // Execute the method
        List<CaseDiaryListItem> result = diaryRepository.getCaseDiaries(searchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedResult, result);

        // Verify interactions
        verify(queryBuilder).getSearchDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
        verify(queryBuilder).addOrderByQuery(query, null);
        verify(jdbcTemplate).query(query, preparedStmtList.toArray(), new int[0], diaryRowMapper);
    }

    @Test
    void getCaseDiaries_Success_WithPagination() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        Pagination pagination = new Pagination();
        pagination.setTotalCount(0.0);
        searchRequest.setPagination(pagination);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String baseQuery = "SELECT * FROM diaries";
        String countQuery = "SELECT COUNT(*) FROM (SELECT * FROM diaries) total_result";
        String queryWithPagination = baseQuery + " LIMIT 10 OFFSET 0";
        List<CaseDiaryListItem> expectedResult = Collections.singletonList(new CaseDiaryListItem());

        // Mock behavior
        when(queryBuilder.getSearchDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(baseQuery);
        when(queryBuilder.addOrderByQuery(baseQuery, pagination)).thenReturn(baseQuery);
        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        when(queryBuilder.addPaginationQuery(baseQuery,preparedStmtList,pagination,preparedStmtArgList)).thenReturn(queryWithPagination);

        when(jdbcTemplate.queryForObject(countQuery,Integer.class, preparedStmtList.toArray())).thenReturn(0); // Default to zero if no records

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(int[].class), eq(diaryRowMapper))).thenReturn(expectedResult);

        // Execute the method
        List<CaseDiaryListItem> result = diaryRepository.getCaseDiaries(searchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedResult, result);

        // Verify interactions
        verify(queryBuilder).getSearchDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
        verify(queryBuilder).addOrderByQuery(baseQuery, pagination);

    }


    @Test
    void getCaseDiariesWithDocuments_Success() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        searchRequest.setPagination(null);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = "SELECT * FROM diaries_with_documents";
        List<CaseDiary> expectedResult = Collections.singletonList(new CaseDiary());

        // Mock behavior
        when(queryBuilder.getCaseDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(query);
        when(queryBuilder.addOrderByQuery(query, null)).thenReturn(query);
        when(jdbcTemplate.query(query, preparedStmtList.toArray(), new int[0], diaryWithDocumentRowMapper)).thenReturn(expectedResult);

        // Execute the method
        List<CaseDiary> result = diaryRepository.getCaseDiariesWithDocuments(searchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedResult, result);

        // Verify interactions
        verify(queryBuilder).getCaseDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
        verify(queryBuilder).addOrderByQuery(query, null);
        verify(jdbcTemplate).query(query, preparedStmtList.toArray(), new int[0], diaryWithDocumentRowMapper);
    }

    @Test
    void getCaseDiariesWithDocuments_DatabaseError() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        searchRequest.setPagination(null);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = "SELECT * FROM diaries_with_documents";

        // Mock behavior
        when(queryBuilder.getCaseDiaryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(query);
        when(queryBuilder.addOrderByQuery(query, null)).thenReturn(query);
        when(jdbcTemplate.query(query, preparedStmtList.toArray(), new int[0], diaryWithDocumentRowMapper))
                .thenThrow(new RuntimeException("Database error"));

        // Execute and assert
        CustomException exception = assertThrows(CustomException.class,
                () -> diaryRepository.getCaseDiariesWithDocuments(searchRequest));
        assertEquals(DIARY_SEARCH_EXCEPTION, exception.getCode());
    }

    @Test
    void getTotalCount_Success() {
        // Prepare mock objects
        String baseQuery = "SELECT * FROM diaries";
        List<Object> preparedStmtList = new ArrayList<>();

        String countQuery = "SELECT COUNT(*) FROM diaries";
        Integer expectedCount = 10;

        // Mock behavior
        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray())).thenReturn(expectedCount);

        // Execute the method
        int result = diaryRepository.getTotalCount(baseQuery, preparedStmtList);

        // Assertions
        assertEquals(expectedCount, result);

        // Verify interactions
        verify(queryBuilder).getTotalCountQuery(baseQuery);
        verify(jdbcTemplate).queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }

    @Test
    void getTotalCount_ReturnsZero() {
        // Prepare mock objects
        String baseQuery = "SELECT * FROM diaries";
        List<Object> preparedStmtList = new ArrayList<>();

        String countQuery = "SELECT COUNT(*) FROM diaries";

        // Mock behavior
        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray())).thenReturn(0);

        // Execute the method
        Integer result = diaryRepository.getTotalCount(baseQuery, preparedStmtList);

        // Assertions
        assertEquals(0, result);

        // Verify interactions
        verify(queryBuilder).getTotalCountQuery(baseQuery);
        verify(jdbcTemplate).queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }
}