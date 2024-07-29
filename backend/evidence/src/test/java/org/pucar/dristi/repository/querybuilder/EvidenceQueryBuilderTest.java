package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;
import org.egov.tracer.model.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EvidenceQueryBuilderTest {

    @InjectMocks
    private EvidenceQueryBuilder evidenceQueryBuilder;

    private EvidenceSearchCriteria criteria;
    private List<Object> preparedStmtList;
    private List<Integer> preparedStmtArgList;

    @BeforeEach
    public void setUp() {
        criteria = new EvidenceSearchCriteria();
        preparedStmtList = new ArrayList<>();
        preparedStmtArgList = new ArrayList<>();
    }

    @Test
    public void testGetArtifactSearchQuery() {
        criteria.setOwner(UUID.randomUUID());
        criteria.setArtifactType("type");
        criteria.setEvidenceStatus(true);
        criteria.setId("id");
        criteria.setCaseId("caseId");
        criteria.setApplicationNumber("appNum");
        criteria.setFilingNumber("fileNum");
        criteria.setHearing("hearing");
        criteria.setOrder("order");
        criteria.setSourceId("sourceId");
        criteria.setSourceName("sourceName");
        criteria.setArtifactNumber("artNum");

        String query = evidenceQueryBuilder.getArtifactSearchQuery(preparedStmtList, preparedStmtArgList, criteria);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertFalse(preparedStmtArgList.isEmpty());
    }

    @Test
    public void testGetArtifactSearchQueryWithException() {
        assertThrows(CustomException.class, () -> {
            evidenceQueryBuilder.getArtifactSearchQuery(null, null, null);
        });
    }

    @Test
    public void testGetTotalCountQuery() {
        String baseQuery = "SELECT * FROM table";
        String countQuery = evidenceQueryBuilder.getTotalCountQuery(baseQuery);

        assertNotNull(countQuery);
        assertTrue(countQuery.contains("SELECT COUNT(*) FROM"));
    }

    @Test
    public void testGetDocumentSearchQuery() {
        List<String> ids = List.of("id1", "id2");
        List<Integer> preparedStmtArgDocList = new ArrayList<>();

        String query = evidenceQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList, preparedStmtArgDocList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertFalse(preparedStmtArgDocList.isEmpty());
    }

    @Test
    public void testGetDocumentSearchQueryWithException() {
        assertThrows(CustomException.class, () -> {
            evidenceQueryBuilder.getDocumentSearchQuery(null, null, null);
        });
    }

    @Test
    public void testAddOrderByQuery() {
        Pagination pagination = new Pagination();
        pagination.setSortBy("createdTime");
        pagination.setOrder(Order.DESC);

        String query = "SELECT * FROM table";
        String orderedQuery = evidenceQueryBuilder.addOrderByQuery(query, pagination);

        assertNotNull(orderedQuery);
        assertTrue(orderedQuery.contains("ORDER BY"));
    }

    @Test
    public void testAddOrderByQueryWithDefault() {
        String query = "SELECT * FROM table";
        String orderedQuery = evidenceQueryBuilder.addOrderByQuery(query, null);

        assertNotNull(orderedQuery);
        assertTrue(orderedQuery.contains("ORDER BY art.createdtime DESC"));
    }

    @Test
    public void testAddPaginationQuery() {
        Pagination pagination = new Pagination();
        pagination.setLimit(10.0);
        pagination.setOffSet(0.0);

        String query = "SELECT * FROM table";
        List<Object> preparedStatementList = new ArrayList<>();
        String paginatedQuery = evidenceQueryBuilder.addPaginationQuery(query, pagination, preparedStatementList,new ArrayList<>());

        assertNotNull(paginatedQuery);
        assertTrue(paginatedQuery.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStatementList.size());
    }

    @Test
    public void testGetCommentSearchQuery() {
        List<String> artifactIds = List.of("id1", "id2");
        List<Integer> preparedStmtArgComList = new ArrayList<>();

        String query = evidenceQueryBuilder.getCommentSearchQuery(artifactIds, preparedStmtList, preparedStmtArgComList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertFalse(preparedStmtArgComList.isEmpty());
    }

}
