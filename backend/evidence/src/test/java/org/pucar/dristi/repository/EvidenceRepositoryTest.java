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

        UUID artifactId = UUID.randomUUID();
        Artifact artifact = new Artifact();
        artifact.setId(artifactId);

        List<Artifact> artifactList = Collections.singletonList(artifact);

        // Mock Queries
        String artifactQuery = "SELECT * FROM artifact";
        String documentQuery = "SELECT * FROM document";
        String commentQuery = "SELECT * FROM comment";
        String countQuery = "SELECT COUNT(*) FROM artifact";

        // Mock responses
        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(artifactQuery);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(documentQuery);
        when(queryBuilder.getCommentSearchQuery(anyList(), anyList())).thenReturn(commentQuery);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(countQuery);

        // Mock count query result
        when(jdbcTemplate.queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class))).thenReturn(1);

        // Mock artifact query result
        when(jdbcTemplate.query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper))).thenReturn(artifactList);

        // Mock document query result
        Document document = new Document();
        Map<UUID, Document> documentMap = Collections.singletonMap(artifactId, document);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), eq(documentRowMapper))).thenReturn(documentMap);

        // Mock comment query result
        List<Comment> commentList = new ArrayList<>();
        Comment comment = new Comment();
        commentList.add(comment);
        Map<UUID, List<Comment>> commentMap = Collections.singletonMap(artifactId, commentList);
        when(jdbcTemplate.query(eq(commentQuery), any(Object[].class), eq(commentRowMapper))).thenReturn(commentMap);

        // Execute
        List<Artifact> result = evidenceRepository.getArtifacts(criteria, pagination);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, pagination.getTotalCount());

        Artifact resultArtifact = result.get(0);
        assertEquals(artifactId, resultArtifact.getId());

        // Verify comments and documents are set
        assertNotNull(resultArtifact.getComments());
        assertEquals(1, resultArtifact.getComments().size());
        assertEquals(comment, resultArtifact.getComments().get(0));

        assertNotNull(resultArtifact.getFile());
        assertEquals(document, resultArtifact.getFile());

        // Verify method interactions
        verify(queryBuilder).getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(queryBuilder).addOrderByQuery(anyString(), any(Pagination.class));
        verify(queryBuilder).addPaginationQuery(anyString(), any(Pagination.class), anyList());
        verify(queryBuilder).getDocumentSearchQuery(anyList(), anyList());
        verify(queryBuilder).getCommentSearchQuery(anyList(), anyList());
        verify(queryBuilder).getTotalCountQuery(anyString());
        verify(jdbcTemplate).query(eq(artifactQuery), any(Object[].class), eq(evidenceRowMapper));
        verify(jdbcTemplate).query(eq(documentQuery), any(Object[].class), eq(documentRowMapper));
        verify(jdbcTemplate).query(eq(commentQuery), any(Object[].class), eq(commentRowMapper));
        verify(jdbcTemplate).queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class));
    }

    @Test
    void testGetArtifactsWithCustomException() {
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        Pagination pagination = new Pagination();

        String artifactQuery = "SELECT * FROM artifact";

        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(),any(), any(), any(), any(), any(), any()))
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

        // Mock query builder responses
        when(queryBuilder.getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(artifactQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(artifactQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(artifactQuery);

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

        // Verify that no further methods are called after the exception is thrown
        verify(queryBuilder).getArtifactSearchQuery(anyList(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(queryBuilder).addOrderByQuery(anyString(), any(Pagination.class));
    }
}