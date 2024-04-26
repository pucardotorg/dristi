package org.pucar.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.pucar.web.models.Advocate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvocateRepositoryTest {

    @Mock
    private AdvocateQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateRowMapper rowMapper;

    @InjectMocks
    private AdvocateRepository repository;

    @Test
    public void testGetApplications() {
        // Mock data
        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Add necessary mock behavior for queryBuilder
        when(queryBuilder.getAdvocateSearchQuery(anyList(), anyList(),anyList(), anyString())).thenReturn("SELECT * FROM advocates WHERE condition = ?");
        // Mock data for jdbcTemplate
        List<Advocate> mockAdvocates = new ArrayList<>();
        Advocate mockAdvocate = new Advocate();
        mockAdvocate.setId(UUID.randomUUID());
        mockAdvocate.setTenantId("tenant1");
        mockAdvocate.setApplicationNumber("app123");
        mockAdvocate.setBarRegistrationNumber("bar123");
        mockAdvocate.setAdvocateType("type1");
        mockAdvocate.setOrganisationID(UUID.randomUUID());
        mockAdvocate.setIndividualId("individual1");
        mockAdvocate.setIsActive(true);
        mockAdvocates.add(mockAdvocate);
        // Add necessary mock behavior for jdbcTemplate
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateRowMapper.class)))
                .thenReturn(mockAdvocates);

        List<String> statusList = Arrays.asList("APPROVED","PENDING");

        // Perform the actual method call
        String applicationNumber = new String();
        List<Advocate> result = repository.getApplications(searchCriteria, statusList, applicationNumber);

        // Verify that queryBuilder was called with correct arguments
        verify(queryBuilder).getAdvocateSearchQuery(eq(searchCriteria), anyList(), anyList(), anyString());

        // Verify that jdbcTemplate was called with correct arguments
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(AdvocateRowMapper.class));

        // Verify that result matches the expected mock data
        assertEquals(mockAdvocates, result);
    }
}
