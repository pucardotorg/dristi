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
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EvidenceRepositoryTest {

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetArtifacts() {
        // Initialize the search criteria
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        evidenceSearchCriteria.setId("testId");
        evidenceSearchCriteria.setCaseId("testCaseId");
        evidenceSearchCriteria.setApplicationNumber("testApplication");
        evidenceSearchCriteria.setHearing("testHearing");
        evidenceSearchCriteria.setOrder("testOrder");
        evidenceSearchCriteria.setSourceId("testSourceId");
        evidenceSearchCriteria.setSourceName("testSourceName");
        evidenceSearchCriteria.setArtifactNumber("testArtifactNumber");

        // Mock query result from the query builder
        List<Object> preparedStmtList = new ArrayList<>();
        List<Object> preparedStmtListDoc = new ArrayList<>();
        List<Object> preparedStmtListCom = new ArrayList<>();
        String searchQuery = "SELECT * FROM artifacts WHERE ...";
        String documentQuery = "SELECT * FROM documents WHERE ...";
        String commentQuery = "SELECT * FROM comments WHERE ...";

        when(queryBuilder.getArtifactSearchQuery(preparedStmtList, evidenceSearchCriteria.getId(), evidenceSearchCriteria.getCaseId(), evidenceSearchCriteria.getApplicationNumber(), evidenceSearchCriteria.getHearing(), evidenceSearchCriteria.getOrder(), evidenceSearchCriteria.getSourceId(), evidenceSearchCriteria.getSourceName(), evidenceSearchCriteria.getArtifactNumber()))
                .thenReturn(searchQuery);

        when(queryBuilder.getDocumentSearchQuery(anyList(), eq(preparedStmtListDoc))).thenReturn(documentQuery);
        when(queryBuilder.getCommentSearchQuery(anyList(), eq(preparedStmtListCom))).thenReturn(commentQuery);

        // Mock jdbcTemplate behavior
        List<Artifact> expectedArtifacts = Collections.singletonList(new Artifact());
        List<Document> expectedDocuments = Collections.singletonList(new Document());
        List<Comment> expectedComments = Collections.singletonList(new Comment());

        when(jdbcTemplate.query(eq(searchQuery), any(Object[].class), eq(evidenceRowMapper))).thenReturn(expectedArtifacts);
        when(jdbcTemplate.query(eq(commentQuery), any(Object[].class), eq(commentRowMapper))).thenReturn(expectedComments);

        // Invoke the method under test
        List<Artifact> actualArtifacts = evidenceRepository.getArtifacts(evidenceSearchCriteria);

        // Verify behavior
        assertEquals(expectedArtifacts, actualArtifacts);
        verify(queryBuilder, times(1)).getArtifactSearchQuery(preparedStmtList, evidenceSearchCriteria.getId(), evidenceSearchCriteria.getCaseId(), evidenceSearchCriteria.getApplicationNumber(), evidenceSearchCriteria.getHearing(), evidenceSearchCriteria.getOrder(), evidenceSearchCriteria.getSourceId(), evidenceSearchCriteria.getSourceName(), evidenceSearchCriteria.getArtifactNumber());
        verify(jdbcTemplate, times(1)).query(eq(searchQuery), any(Object[].class), eq(evidenceRowMapper));

        if (!actualArtifacts.isEmpty()) {
            verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), eq(preparedStmtListDoc));
            verify(jdbcTemplate, times(1)).query(eq(documentQuery), any(Object[].class), eq(documentRowMapper));
            verify(queryBuilder, times(1)).getCommentSearchQuery(anyList(), eq(preparedStmtListCom));
            verify(jdbcTemplate, times(1)).query(eq(commentQuery), any(Object[].class), eq(commentRowMapper));
        }
    }


    @Test
    public void testGetArtifactsThrowsCustomException() {
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        evidenceSearchCriteria.setId("testId");
        List<Object> preparedStmtList = new ArrayList<>();
        String searchQuery = "SELECT * FROM artifacts WHERE ...";

        when(queryBuilder.getArtifactSearchQuery(preparedStmtList, evidenceSearchCriteria.getId(), evidenceSearchCriteria.getCaseId(), evidenceSearchCriteria.getApplicationNumber(), evidenceSearchCriteria.getHearing(), evidenceSearchCriteria.getOrder(), evidenceSearchCriteria.getSourceId(), evidenceSearchCriteria.getSourceName(), evidenceSearchCriteria.getArtifactNumber()))
                .thenReturn(searchQuery);

        when(jdbcTemplate.query(eq(searchQuery), any(Object[].class), eq(evidenceRowMapper)))
                .thenThrow(new CustomException("ERROR", "Simulated exception"));

        assertThrows(CustomException.class, () -> evidenceRepository.getArtifacts(evidenceSearchCriteria));

        verify(queryBuilder, times(1)).getArtifactSearchQuery(preparedStmtList, evidenceSearchCriteria.getId(), evidenceSearchCriteria.getCaseId(), evidenceSearchCriteria.getApplicationNumber(), evidenceSearchCriteria.getHearing(), evidenceSearchCriteria.getOrder(), evidenceSearchCriteria.getSourceId(), evidenceSearchCriteria.getSourceName(), evidenceSearchCriteria.getArtifactNumber());
        verify(jdbcTemplate, times(1)).query(eq(searchQuery), any(Object[].class), eq(evidenceRowMapper));
    }


}
