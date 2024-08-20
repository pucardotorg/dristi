package org.pucar.dristi.repository;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
import org.pucar.dristi.repository.rowmapper.EvidenceRowMapper;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EvidenceRepositoryTest {

    @Mock
    private EvidenceQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EvidenceRowMapper evidenceRowMapper;

    @InjectMocks
    private EvidenceRepository evidenceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArtifactsSuccess() {
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        Pagination pagination = new Pagination();

        UUID artifactId = UUID.randomUUID();
        Artifact artifact = new Artifact();
        artifact.setId(artifactId);

        List<Artifact> artifactList = Collections.singletonList(artifact);

        // Mock Queries
        String artifactQuery = "SELECT * FROM artifact";
        String countQuery = "SELECT COUNT(*) FROM artifact";

        // Mock responses
        when(queryBuilder.getArtifactSearchQuery(anyList(),anyList(), any(EvidenceSearchCriteria.class)))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList())).thenReturn(artifactQuery);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(countQuery);

        // Mock count query result
        when(jdbcTemplate.queryForObject(eq(countQuery),  eq(Integer.class), any(Object[].class))).thenReturn(1);

        // Mock artifact query result
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), any(),eq(evidenceRowMapper))).thenReturn(artifactList);

        // Execute
        List<Artifact> result = evidenceRepository.getArtifacts(criteria, pagination);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, pagination.getTotalCount());

        Artifact resultArtifact = result.get(0);
        assertEquals(artifactId, resultArtifact.getId());



        // Verify method interactions
        verify(queryBuilder).getArtifactSearchQuery(anyList(),anyList(), any(EvidenceSearchCriteria.class));
        verify(queryBuilder).addOrderByQuery(anyString(), any(Pagination.class));
        verify(queryBuilder).addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList());
        verify(queryBuilder).getTotalCountQuery(anyString());
        verify(jdbcTemplate).query(eq(artifactQuery), any(Object[].class),any(), eq(evidenceRowMapper));
        verify(jdbcTemplate).queryForObject(eq(countQuery), eq(Integer.class), any(Object[].class));
    }

    @Test
    void testGetArtifactsWithCustomException() {
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        Pagination pagination = new Pagination();

        String artifactQuery = "SELECT * FROM artifact";

        when(queryBuilder.getArtifactSearchQuery(anyList(),any(), any(EvidenceSearchCriteria.class)))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList())).thenReturn(artifactQuery);
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper))).thenThrow(new CustomException("ARTIFACT_SEARCH_EXCEPTION", "Error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceRepository.getArtifacts(criteria, pagination);
        });

        assertEquals("ARTIFACT_SEARCH_EXCEPTION", exception.getCode());
    }

    @Test
    void testGetArtifactsWithGeneralException() {
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        Pagination pagination = new Pagination();
        String artifactQuery = "SELECT * FROM artifact";

        // Mock query builder responses
        when(queryBuilder.getArtifactSearchQuery(anyList(),any(), any(EvidenceSearchCriteria.class)))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList())).thenReturn(artifactQuery);

        // Mock JDBC query to throw a RuntimeException
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper)))
                .thenThrow(new RuntimeException("Some error"));

        // Execute and assert the CustomException is thrown
        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceRepository.getArtifacts(criteria, pagination);
        });

        // Verify exception code and message
        assertEquals("ARTIFACT_SEARCH_EXCEPTION", exception.getCode());
        assertFalse(exception.getMessage().contains("Error while fetching artifact list: Some error"));

        // Verify that the methods were called in the expected order
        verify(queryBuilder).getArtifactSearchQuery(anyList(),any(), any(EvidenceSearchCriteria.class));
        verify(queryBuilder).addOrderByQuery(anyString(), any(Pagination.class));
        verify(queryBuilder).getTotalCountQuery(anyString());
        verifyNoMoreInteractions(queryBuilder); // Ensure no other methods were called
    }

}
