package digit.repository;

import digit.repository.querybuilder.DiaryEntryQueryBuilder;
import digit.repository.rowmapper.DiaryEntryRowMapper;
import digit.web.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaryEntryRepositoryTest {

    @Mock
    private DiaryEntryQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DiaryEntryRowMapper diaryEntryRowMapper;

    @InjectMocks
    private DiaryEntryRepository diaryEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCaseDiaryEntries_Success_NoPagination() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        searchRequest.setPagination(null);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = "SELECT * FROM diary_entries";
        List<CaseDiaryEntry> expectedResult = Collections.singletonList(new CaseDiaryEntry());

        // Mock behavior
        when(queryBuilder.getDiaryEntryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(query);
        when(queryBuilder.addOrderByQuery(query, null)).thenReturn(query);
        when(jdbcTemplate.query(query, preparedStmtList.toArray(), new int[0], diaryEntryRowMapper)).thenReturn(expectedResult);

        // Execute the method
        List<CaseDiaryEntry> result = diaryEntryRepository.getCaseDiaryEntries(searchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedResult, result);

        // Verify interactions
        verify(queryBuilder).getDiaryEntryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
        verify(queryBuilder).addOrderByQuery(query, null);
        verify(jdbcTemplate).query(query, preparedStmtList.toArray(), new int[0], diaryEntryRowMapper);
    }

    @Test
    void getCaseDiaryEntries_Success_WithPagination() {
        // Prepare mock objects
        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setCriteria(new CaseDiarySearchCriteria());
        Pagination pagination = new Pagination();
        pagination.setTotalCount(0.0);
        searchRequest.setPagination(pagination);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String baseQuery = "SELECT * FROM diary_entries";
        String queryWithPagination = "SELECT * FROM diary_entries LIMIT 10 OFFSET 0";
        List<CaseDiaryEntry> expectedResult = Collections.singletonList(new CaseDiaryEntry());

        // Mock behavior
        when(queryBuilder.getDiaryEntryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList)).thenReturn(baseQuery);
        when(queryBuilder.addOrderByQuery(baseQuery, pagination)).thenReturn(baseQuery);
        when(queryBuilder.addPaginationQuery(baseQuery, preparedStmtList, pagination, preparedStmtArgList)).thenReturn(queryWithPagination);
        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(queryWithPagination);
        when(jdbcTemplate.query(baseQuery, preparedStmtList.toArray(), new int[0], diaryEntryRowMapper)).thenReturn(expectedResult);
        when(jdbcTemplate.queryForObject(queryBuilder.getTotalCountQuery(baseQuery), Integer.class, preparedStmtList.toArray())).thenReturn(1);

        // Execute the method
        List<CaseDiaryEntry> result = diaryEntryRepository.getCaseDiaryEntries(searchRequest);

        assertNull(result);

        // Verify interactions
        verify(queryBuilder).getDiaryEntryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
        verify(queryBuilder).addOrderByQuery(baseQuery, pagination);
        verify(queryBuilder).addPaginationQuery(baseQuery, preparedStmtList, pagination, preparedStmtArgList);
        verify(jdbcTemplate).queryForObject(queryBuilder.getTotalCountQuery(baseQuery), Integer.class, preparedStmtList.toArray());
    }

    @Test
    void getTotalCount_Success() {
        // Prepare mock objects
        String baseQuery = "SELECT * FROM diary_entries";
        List<Object> preparedStmtList = new ArrayList<>();

        String countQuery = "SELECT COUNT(*) FROM diary_entries";
        Integer expectedCount = 10;

        // Mock behavior
        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray())).thenReturn(expectedCount);

        // Execute the method
        Integer result = diaryEntryRepository.getTotalCount(baseQuery, preparedStmtList);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedCount, result);

        // Verify interactions
        verify(queryBuilder).getTotalCountQuery(baseQuery);
        verify(jdbcTemplate).queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }

    @Test
    void getExistingCaseDiaryEntries_Success_NoPagination() {
        // Prepare mock objects
        CaseDiaryExistCriteria searchRequest = new CaseDiaryExistCriteria();
        searchRequest.setId(UUID.randomUUID());
        searchRequest.setTenantId("kl");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = "SELECT * FROM diary_entries";
        List<CaseDiaryEntry> expectedResult = Collections.singletonList(new CaseDiaryEntry());

        // Mock behavior
        when(queryBuilder.getExistingDiaryEntry(searchRequest, preparedStmtList, preparedStmtArgList)).thenReturn(query);
        when(queryBuilder.addOrderByQuery(query, null)).thenReturn(query);
        when(jdbcTemplate.query(query, preparedStmtList.toArray(), new int[0], diaryEntryRowMapper)).thenReturn(expectedResult);

        // Execute the method
        List<CaseDiaryEntry> result = diaryEntryRepository.getExistingDiaryEntry(searchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
}
