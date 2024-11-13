package org.pucar.dristi.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.model.EPostResponse;
import org.pucar.dristi.model.EPostTracker;
import org.pucar.dristi.model.EPostTrackerSearchCriteria;
import org.pucar.dristi.model.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EPostRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EPostQueryBuilder queryBuilder;

    @Mock
    private EPostRowMapper rowMapper;

    @InjectMocks
    private EPostRepository ePostRepository;

    @Test
    void testGetEPostTrackerResponse() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = new EPostTrackerSearchCriteria();
        Pagination pagination = new Pagination();
        searchCriteria.setPagination(pagination);
        List<EPostTracker> ePostTrackerList = Collections.singletonList(new EPostTracker());
        Integer totalRecords = 100;

        when(ePostRepository.getEPostTrackerList(searchCriteria, 10, 0)).thenReturn(ePostTrackerList);
        when(ePostRepository.getTotalCountQuery(searchCriteria)).thenReturn(totalRecords);

        // Act
        EPostResponse result = ePostRepository.getEPostTrackerResponse(searchCriteria, 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(ePostTrackerList, result.getEPostTrackers());
        assertEquals(totalRecords, result.getPagination().getTotalCount());
    }

    @Test
    void testGetEPostTrackerList() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = new EPostTrackerSearchCriteria();
        List<Object> preparedStmtList = Collections.emptyList();
        String query = "SELECT * FROM epost_tracker";
        when(queryBuilder.getEPostTrackerSearchQuery(searchCriteria, preparedStmtList)).thenReturn(query);
        when(queryBuilder.addPaginationQuery(query, preparedStmtList, searchCriteria.getPagination(), 10, 0))
                .thenReturn(query);
        List<EPostTracker> expectedList = Collections.singletonList(new EPostTracker());
        when(jdbcTemplate.query(query,rowMapper,preparedStmtList.toArray())).thenReturn(expectedList);

        // Act
        List<EPostTracker> result = ePostRepository.getEPostTrackerList(searchCriteria, 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
    }

    @Test
    void testGetTotalCountQuery() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = new EPostTrackerSearchCriteria();
        List<Object> preparedStmtList = Collections.emptyList();
        String query = "SELECT * FROM epost_tracker";
        String countQuery = "SELECT COUNT(*) FROM epost_tracker";
        when(queryBuilder.getEPostTrackerSearchQuery(searchCriteria, preparedStmtList)).thenReturn(query);
        when(queryBuilder.getTotalCountQuery(query)).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray())).thenReturn(100);

        // Act
        Integer result = ePostRepository.getTotalCountQuery(searchCriteria);

        // Assert
        assertNotNull(result);
        assertEquals(100, result);
    }
}
