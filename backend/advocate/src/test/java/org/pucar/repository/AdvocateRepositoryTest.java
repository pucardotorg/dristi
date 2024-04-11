package org.pucar.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.pucar.web.models.Advocate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvocateRepositoryTest {

    @Mock
    private AdvocateClerkQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateClerkRowMapper rowMapper;

    @InjectMocks
    private AdvocateClerkRepository repository;

    @Test
    public void testGetApplications() {
        // Mock data
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        // Add necessary mock behavior for queryBuilder
        when(queryBuilder.getAdvocateClerkSearchQuery(anyList(), anyList())).thenReturn("SELECT * FROM dristi_advocate_clerk WHERE condition = ?");
        // Mock data for jdbcTemplate
        List<AdvocateClerk> mockAdvocates = new ArrayList<>();
        AdvocateClerk mockAdvocate = new AdvocateClerk();
        mockAdvocate.setId(UUID.randomUUID());
        mockAdvocate.setTenantId("tenant1");
        mockAdvocate.setApplicationNumber("app123");
        mockAdvocate.setIndividualId("individual1");
        mockAdvocate.setIsActive(true);
        mockAdvocates.add(mockAdvocate);
        // Add necessary mock behavior for jdbcTemplate
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(mockAdvocates);

        // Perform the actual method call
        List<AdvocateClerk> result = repository.getApplications(searchCriteria);

        // Verify that queryBuilder was called with correct arguments
        verify(queryBuilder).getAdvocateClerkSearchQuery(eq(searchCriteria), anyList());

        // Verify that jdbcTemplate was called with correct arguments
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class));

        // Verify that result matches the expected mock data
        assertEquals(mockAdvocates, result);
    }

}
