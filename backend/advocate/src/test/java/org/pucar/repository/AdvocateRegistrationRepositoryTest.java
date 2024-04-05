package org.pucar.repository;
import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.BeforeEach;
        import org.mockito.Mock;
        import org.mockito.InjectMocks;
        import org.mockito.MockitoAnnotations;
import org.pucar.repository.querybuilder.AdvocateRegistrationQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateRegistrationRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.dao.DataAccessException;
        import org.springframework.jdbc.core.JdbcTemplate;

        import java.util.ArrayList;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class AdvocateRegistrationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateRegistrationQueryBuilder queryBuilder;

    @Mock
    private AdvocateRegistrationRowMapper rowMapper;

    @InjectMocks
    private AdvocateRegistrationRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetApplications() {
        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();

        List<Object> preparedStmtList = new ArrayList<>();
        String query = "SELECT * FROM Advocate WHERE ..."; // Mocked query string

        List<Advocate> expectedAdvocates = new ArrayList<>();

        when(queryBuilder.getAdvocateSearchQuery(any(AdvocateSearchCriteria.class), anyList())).thenReturn(query);
        when(jdbcTemplate.query(eq(query), any(Object[].class), eq(rowMapper))).thenReturn(expectedAdvocates);

        // Act
        List<Advocate> result = repository.getApplications(searchCriteria);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAdvocates.size(), result.size());
    }

    @Test
    void testGetApplicationsWithDataAccessException() {
        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();

        when(queryBuilder.getAdvocateSearchQuery(any(AdvocateSearchCriteria.class), anyList())).thenReturn("SELECT * FROM Advocate WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), eq(rowMapper))).thenThrow(DataAccessException.class);

        assertThrows(DataAccessException.class, () -> repository.getApplications(searchCriteria));
    }
}
