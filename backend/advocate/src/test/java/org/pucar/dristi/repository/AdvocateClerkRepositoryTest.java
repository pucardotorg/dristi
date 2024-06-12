package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvocateClerkRepositoryTest {

    @InjectMocks
    private AdvocateClerkRepository advocateClerkRepository;

    @Mock
    private AdvocateClerkQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateClerkRowMapper rowMapper;

    @Mock
    private AdvocateClerkDocumentRowMapper documentRowMapper;

    private List<AdvocateClerkSearchCriteria> searchCriteriaList;
    private AdvocateClerkSearchCriteria searchCriteria;
    private List<AdvocateClerk> advocateClerkList;
    private Map<UUID, List<Document>> documentMap;
    private AtomicReference<Boolean> isIndividualLoggedInUser;

    @BeforeEach
    public void setUp() {
        searchCriteria = new AdvocateClerkSearchCriteria();
        searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(searchCriteria);

        advocateClerkList = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setId(UUID.randomUUID());
        advocateClerkList.add(clerk);

        documentMap = new HashMap<>();
        documentMap.put(clerk.getId(), new ArrayList<>());
    }

    @Test
    public void testGetApplications() {
        when(queryBuilder.getAdvocateClerkSearchQuery(any(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("SELECT * FROM documents");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class)))
                .thenReturn(documentMap);

        List<AdvocateClerk> result = advocateClerkRepository.getApplications(
                searchCriteriaList, "tenantId", 10, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getAdvocateClerkSearchQuery(any(), anyList(), anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class));
        verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class));
    }

    @Test
    public void testGetApplicationsEmpty() {
        advocateClerkList = new ArrayList<>();
        when(queryBuilder.getAdvocateClerkSearchQuery(any(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);

        List<AdvocateClerk> result = advocateClerkRepository.getApplications(
                searchCriteriaList, "tenantId", 10, 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetApplicationsByStatus() {
        when(queryBuilder.getAdvocateClerkSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk WHERE status = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("SELECT * FROM documents");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class)))
                .thenReturn(documentMap);

        List<AdvocateClerk> result = advocateClerkRepository.getApplicationsByStatus(
                "status", "tenantId", 10, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getAdvocateClerkSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class));
        verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class));
    }

    @Test
    public void testGetApplicationsByStatusWithEmptySuccess() {
        advocateClerkList = new ArrayList<>();
        when(queryBuilder.getAdvocateClerkSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk WHERE status = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);

        List<AdvocateClerk> result = advocateClerkRepository.getApplicationsByStatus(
                "status", "tenantId", 10, 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetApplicationsByAppNumber() {
        when(queryBuilder.getAdvocateClerkSearchQueryByAppNumber(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk WHERE application_number = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("SELECT * FROM documents");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class)))
                .thenReturn(documentMap);

        List<AdvocateClerk> result = advocateClerkRepository.getApplicationsByAppNumber(
                "appNumber", "tenantId", 10, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getAdvocateClerkSearchQueryByAppNumber(anyString(), anyList(), anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class));
        verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateClerkDocumentRowMapper.class));
    }

    @Test
    public void testGetApplicationsByAppNumberWithEmptySuccess() {
        advocateClerkList = new ArrayList<>();
        when(queryBuilder.getAdvocateClerkSearchQueryByAppNumber(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenReturn("SELECT * FROM advocate_clerk WHERE application_number = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateClerkRowMapper.class)))
                .thenReturn(advocateClerkList);

        List<AdvocateClerk> result = advocateClerkRepository.getApplicationsByAppNumber(
                "appNumber", "tenantId", 10, 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetApplications_Exception() {
        when(queryBuilder.getAdvocateClerkSearchQuery(any(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> advocateClerkRepository.getApplications(
                searchCriteriaList, "tenantId", 10, 0));
    }

    @Test
    public void testGetApplications_CustomException() {
        when(queryBuilder.getAdvocateClerkSearchQuery(any(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> advocateClerkRepository.getApplications(
                searchCriteriaList, "tenantId", 10, 0));
    }

    @Test
    public void testGetApplicationsByStatus_CustomException() {
        when(queryBuilder.getAdvocateClerkSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> advocateClerkRepository.getApplicationsByStatus(
                "status", "tenantId", 10, 0));
    }

    @Test
    public void testGetApplicationsByStatus_Exception() {
        when(queryBuilder.getAdvocateClerkSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> advocateClerkRepository.getApplicationsByStatus(
                "status", "tenantId", 10, 0));
    }

    @Test
    public void testGetApplicationsByAppNumber_CustomException() {
        when(queryBuilder.getAdvocateClerkSearchQueryByAppNumber(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> advocateClerkRepository.getApplicationsByAppNumber(
                "appNumber", "tenantId", 10, 0));
    }

    @Test
    public void testGetApplicationsByAppNumber_Exception() {
        when(queryBuilder.getAdvocateClerkSearchQueryByAppNumber(anyString(), anyList(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> advocateClerkRepository.getApplicationsByAppNumber(
                "appNumber", "tenantId", 10, 0));
    }

}
