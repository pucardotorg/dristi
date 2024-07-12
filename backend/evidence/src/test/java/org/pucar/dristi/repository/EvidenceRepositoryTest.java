package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
import org.pucar.dristi.repository.rowmapper.CommentRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.EvidenceRowMapper;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.Comment;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Mock
    private DocumentRowMapper documentRowMapper;

    @Mock
    private CommentRowMapper commentRowMapper;

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

        String artifactQuery = "SELECT * FROM artifact";
        String documentQuery = "SELECT * FROM document";
        String commentQuery = "SELECT * FROM comment";
        String countQuery = "SELECT COUNT(*) FROM artifact";

        List<Artifact> artifactList = new ArrayList<>();
        Artifact artifact = new Artifact();
        artifact.setId(UUID.randomUUID());
        artifactList.add(artifact);

        List<Comment> commentList = new ArrayList<>();

        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(artifactQuery);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(documentQuery);
        when(queryBuilder.getCommentSearchQuery(anyList(), anyList())).thenReturn(commentQuery);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(countQuery);
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper))).thenReturn(artifactList);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), eq(documentRowMapper))).thenReturn(mock(Document.class));
        when(jdbcTemplate.query(eq(commentQuery), any(Object[].class), eq(commentRowMapper))).thenReturn(commentList);
        when(jdbcTemplate.queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class))).thenReturn(1);

        List<Artifact> result = evidenceRepository.getArtifacts(criteria, pagination);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, pagination.getTotalCount());
    }

    @Test
    void testGetArtifactsWithCustomException() {
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        Pagination pagination = new Pagination();

        String artifactQuery = "SELECT * FROM artifact";

        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(artifactQuery);
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

        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(artifactQuery);
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper))).thenThrow(new RuntimeException("Some error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceRepository.getArtifacts(criteria, pagination);
        });

        assertEquals("ARTIFACT_SEARCH_EXCEPTION", exception.getCode());
        assertFalse(exception.getMessage().contains("Some error"));
    }
}
