package org.pucar.repository;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.pucar.config.ServiceConstants.TEST_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class AdvocateClerkRepositoryTest {

    @Mock
    private AdvocateClerkQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateClerkRowMapper rowMapper;

    @Mock
    private AdvocateClerkDocumentRowMapper documentRowMapper;

    @InjectMocks
    private AdvocateClerkRepository repository;

    // Positive Test Cases

    @Test
    public void testGetApplications_EmptySearchCriteria() {
        List<AdvocateClerkSearchCriteria> searchCriteria = Collections.emptyList();
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";
        repository.getApplications(searchCriteria, statusList, applicationNumber, new AtomicReference<>(false), null, null, new Pagination());
        verify(queryBuilder).getAdvocateClerkSearchQuery(eq(searchCriteria), anyList(), anyList(), anyString(), any(), any(), any(), any());
    }

    @Test
    public void testGetApplications_NoDocumentsForAdvocates() {
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        List<AdvocateClerk> mockAdvocates = createMockAdvocateClerks(1);
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";
        when(queryBuilder.getAdvocateClerkSearchQuery(anyList(), anyList(), anyList(), anyString(), any(), any(), any(), any())).thenReturn("Mocked Query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class))).thenReturn(mockAdvocates);
        List<AdvocateClerk> result = repository.getApplications(searchCriteria, statusList, applicationNumber, new AtomicReference<>(false), null, null, new Pagination());
        assertEquals(1, result.size());
        assertEquals(null, result.get(0).getDocuments());
    }

    // Negative Test Cases

    @Test()
    public void testGetApplications_QueryBuilderThrowsException() {
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";
        when(queryBuilder.getAdvocateClerkSearchQuery(anyList(), anyList(), anyList(), anyString(), any(), any(), any(), any())).thenThrow(new RuntimeException("Mocked Exception"));
        try {
            repository.getApplications(searchCriteria, statusList, applicationNumber, new AtomicReference<>(false), null, null, new Pagination());
        } catch (Exception e) {
            // Verify expected exception
            assertTrue(e instanceof CustomException);
            assertEquals("Error while fetching advocate clerk application list: Mocked Exception", e.getMessage());
        }
    }

    @Test()
    public void testGetApplications_JdbcTemplateThrowsException_AdvocateQuery() {
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";
        when(queryBuilder.getAdvocateClerkSearchQuery(anyList(), anyList(), anyList(), anyString(), any(), any(),any(), any())).thenReturn("Mocked Query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class))).thenThrow(new CustomException(TEST_EXCEPTION,"Mock test"));
        try {
            repository.getApplications(searchCriteria, statusList, applicationNumber, new AtomicReference<>(false), null, null, new Pagination());
        } catch (Exception e) {
            // Verify expected exception
            assertTrue(e instanceof CustomException);
            assertEquals("Mock test", e.getMessage());
        }
    }

    // Neutral Test Cases

    @Test
    public void testGetApplications_EmptyAdvocateClerkList() {
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";
        when(queryBuilder.getAdvocateClerkSearchQuery(anyList(), anyList(), anyList(), anyString(),any(), any(), any(), any())).thenReturn("Mocked Query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class))).thenReturn(Collections.emptyList());
        List<AdvocateClerk> result = repository.getApplications(searchCriteria, statusList, applicationNumber, new AtomicReference<>(false),null, null, new Pagination());
        assertEquals(Collections.emptyList(), result);
    }

    // Helper method to create mock AdvocateClerks
    private List<AdvocateClerk> createMockAdvocateClerks(int numClerks) {
        List<AdvocateClerk> clerks = new ArrayList<>();
        for (int i = 0; i < numClerks; i++) {
            AdvocateClerk clerk = new AdvocateClerk();
            clerk.setId(UUID.randomUUID());
            // Set other AdvocateClerk properties as needed
            clerks.add(clerk);
        }
        return clerks;
    }
}
