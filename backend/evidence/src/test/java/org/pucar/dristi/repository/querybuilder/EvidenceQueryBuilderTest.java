package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class EvidenceQueryBuilderTest {

    @InjectMocks
    private  EvidenceQueryBuilder queryBuilder = new EvidenceQueryBuilder();

    @Test
    void testGetArtifactSearchQuery_WithAllFields() {
        // Mocking the required dependencies
        String id = "1";
        String caseId = "testCaseId";
        String application = "testApplication";
        Boolean evidenceStatus = true;
        String artifactType = "testArtifactType";
        String filingNumber = "testFilingNumber";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        String artifactNumber = "artifactNumber";
        UUID owner = UUID.fromString("baf36d5a-58ff-4b9b-b263-69ab45b1c7b4");

        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art WHERE art.id = ? AND art.caseId = ? AND art.application = ? AND art.artifactType = ? AND art.isEvidence = ? AND art.filingNumber = ? " +
                "AND art.hearing = ? AND art.orders = ? AND art.sourceId = ? " +
                "AND art.createdBy = ? AND art.sourceName = ? " +
                "AND art.artifactNumber LIKE ?";
        List<Object> preparedStmtList=new ArrayList<>();

        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(preparedStmtList,owner,artifactType,evidenceStatus,id, caseId, application,filingNumber, hearing, order, sourceId, sourceName, artifactNumber);

        // Assertions
        assertEquals(expectedQuery, query);
    }
    @Test
    void testGetArtifactSearchQuery_WithNullFields() {
        // Mocking the required dependencies
        String id = null;
        String caseId = null;
        String application = null;
        String filingNumber = null;
        String hearing = null;
        String order = null;
        String sourceId = null;
        String sourceName = null;
        String artifactNumber=null;


        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art";
        List<Object> preparedStmtList=new ArrayList<>();
        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(preparedStmtList,null,null,null,id, caseId, application,filingNumber, hearing, order, sourceId, sourceName,artifactNumber);

        // Assertions
        assertEquals(expectedQuery, query);
    }
    @Test
    void testAddArtifactCriteriaWithBoolean() {
        // Initialize the variables
        Boolean criteria = true;
        StringBuilder query = new StringBuilder("SELECT * FROM artifacts");
        List<Object> preparedStmtList = new ArrayList<>();
        boolean firstCriteria = true;
        String criteriaClause = "art.isEvidence = ?";

        // Call the method under test
        boolean result = queryBuilder.addArtifactCriteria(criteria, query, preparedStmtList, firstCriteria, criteriaClause);

        // Verify the results
        assertFalse(result); // firstCriteria should be false after adding the first criteria
        assertEquals("SELECT * FROM artifacts WHERE art.isEvidence = ?", query.toString()); // query should have the criteria appended
        assertEquals(1, preparedStmtList.size()); // preparedStmtList should have one element
        assertEquals(criteria, preparedStmtList.get(0)); // the criteria should be added to the preparedStmtList
    }
    @Test
    public void testGetArtifactSearchQuery_exception() {
        List<Object> preparedStmtList = new ArrayList<>();
        String id = "testId";
        String caseId = "testCaseId";
        Boolean evidenceStatus = true;
        String artifactType = "testArtifactType";
        String application = "testApplication";
        String filingNumber = "testFilingNumber";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        String artifactNumber = "testArtifactNumber";
        UUID owner = UUID.fromString("baf36d5a-58ff-4b9b-b263-69ab45b1c7b4");

        // Inject a scenario that causes an exception
        EvidenceQueryBuilder spyQueryBuilder = spy(queryBuilder);
        doThrow(new RuntimeException("Test Exception")).when(spyQueryBuilder).addArtifactCriteria(anyString(), any(), anyList(), anyBoolean(), anyString());

        // Execute the method and assert that the CustomException is thrown
        CustomException exception = assertThrows(CustomException.class, () -> {
            spyQueryBuilder.getArtifactSearchQuery(preparedStmtList,owner,artifactType,evidenceStatus, id, caseId, application,filingNumber, hearing, order, sourceId, sourceName, artifactNumber);
        });

        // Verify that the correct exception is thrown with the expected message
        assert(exception.getMessage().contains("Error occurred while building the artifact search query: java.lang.RuntimeException: Test Exception"));
    }

    @Test
    public void testGetDocumentSearchQuery_exception() {
        List<String> ids = new ArrayList<>();
        ids.add("testId1");
        ids.add("testId2");

        List<Object> preparedStmtList = new ArrayList<>();

        // Inject a scenario that causes an exception
        EvidenceQueryBuilder spyQueryBuilder = spy(queryBuilder);
        doThrow(new RuntimeException("Test Exception")).when(spyQueryBuilder).getDocumentSearchQuery(anyList(), anyList());

        // Execute the method and assert that the RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spyQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        });

        // Verify that the correct exception is thrown with the expected message
        assert(exception.getMessage().contains("Test Exception"));
    }
    @Test
    void testGetArtifactSearchQueryWithNullValues() {
        // Mock the EvidenceQueryBuilder
        EvidenceQueryBuilder mockQueryBuilder = Mockito.mock(EvidenceQueryBuilder.class);

        // Expected query when all values are null
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application,art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art ORDER BY art.createdTime DESC ";

        // Stubbing the method call to return the expected query
        Mockito.when(mockQueryBuilder.getArtifactSearchQuery(null,null,null,true,null, null, null, null,null, null, null, null,null))
                .thenReturn(expectedQuery);

        // Calling the method under test
        String actualQuery = mockQueryBuilder.getArtifactSearchQuery(null,null,null,true,null, null, null,null, null, null, null, null,null);

        // Assertions
        assertEquals(expectedQuery, actualQuery);
    }
    @Test
    public void testAddPaginationQuery() {
        // Setup
        String query = "SELECT * FROM table";
        Pagination pagination = new Pagination();
        pagination.setLimit(10.0);
        pagination.setOffSet(20.0);

        List<Object> preparedStatementList = new ArrayList<>();

        // Invoke the method
        String resultQuery = queryBuilder.addPaginationQuery(query, pagination, preparedStatementList);

        // Asserts
        assertEquals("SELECT * FROM table LIMIT ? OFFSET ?", resultQuery);

        assertEquals(2, preparedStatementList.size());
        assertEquals(10.0, preparedStatementList.get(0)); // Check the limit value
        assertEquals(20.0, preparedStatementList.get(1)); // Check the offset value
    }
    @Test
    public void testGetTotalCountQuery() {
        String baseQuery = "SELECT * FROM table_name";
        String expectedResult = "SELECT COUNT(*) FROM (SELECT * FROM table_name) total_result";

        String result = queryBuilder.getTotalCountQuery(baseQuery);

        assertEquals(expectedResult, result);
    }
    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = List.of("doc1", "doc2", "doc3");
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT doc.id as id, doc.fileStore as fileStore, doc.documentUid as documentUid, " +
                "doc.documentType as documentType, doc.artifactId as artifactId, doc.additionalDetails as additionalDetails  FROM dristi_evidence_document doc WHERE doc.artifactId IN (?,?,?)";

        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        assertEquals(expectedQuery, query);
        assertEquals(ids, preparedStmtList);
    }
    @Test
    public void testGetCommentSearchQuery_exception() {
        // Setup
        List<String> artifactIds = new ArrayList<>();
        artifactIds.add("artifactId1");
        artifactIds.add("artifactId2");

        List<Object> preparedStmtList = new ArrayList<>();

        // Mocking the query builder to spy and throw an exception
        EvidenceQueryBuilder spyQueryBuilder = spy(queryBuilder);
        doThrow(new RuntimeException("Test Exception")).when(spyQueryBuilder).getCommentSearchQuery(anyList(), anyList());

        // Invoke the method and expect RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spyQueryBuilder.getCommentSearchQuery(artifactIds, preparedStmtList);
        });

        // Verify that the correct exception is thrown with the expected message
        assertTrue(exception.getMessage().contains("Test Exception"));
    }

    @Test
    void testGetDocumentSearchQueryWithEmptyIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT doc.id as id, doc.fileStore as fileStore, doc.documentUid as documentUid, " +
                "doc.documentType as documentType, doc.artifactId as artifactId, doc.additionalDetails as additionalDetails  FROM dristi_evidence_document doc";

        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        assertEquals(expectedQuery, query);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    public void testAddOrderByQuery_DefaultOrder() {
        String query = "SELECT * FROM table_name";
        Pagination pagination = null;
        String expectedResult = query + " ORDER BY art.createdtime DESC ";

        String result = queryBuilder.addOrderByQuery(query, pagination);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddOrderByQuery_WithSorting() {
        String query = "SELECT * FROM table_name";
        Pagination pagination = new Pagination(); // Replace with actual values
        String expectedOrderByClause = " ORDER BY art.createdtime DESC ";
        String expectedResult = query + expectedOrderByClause;

        String result = queryBuilder.addOrderByQuery(query, pagination);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetCommentSearchQuery() {
        List<String> artifactIds = List.of("art1", "art2", "art3");
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT com.id as id, com.tenantId as tenantId, com.artifactId as artifactId, " +
                "com.individualId as individualId, com.comment as comment, com.isActive as isActive, com.additionalDetails as additionalDetails, " +
                "com.createdBy as createdBy, com.lastModifiedBy as lastModifiedBy, com.createdTime as createdTime, com.lastModifiedTime as lastModifiedTime  " +
                "FROM dristi_evidence_comment com WHERE com.artifactId IN (?,?,?)";

        String query = queryBuilder.getCommentSearchQuery(artifactIds, preparedStmtList);
        assertEquals(expectedQuery, query);
        assertEquals(artifactIds, preparedStmtList);
    }
    @Test
    public void testAddOrderByQuery() {
        // Test case 1: Pagination is not null
        Pagination pagination = new Pagination();
        pagination.setSortBy("createdTime");
        pagination.setOrder(Order.DESC);

        String query1 = "SELECT * FROM table";
        String expectedQuery1 = "SELECT * FROM table ORDER BY art.createdTime DESC ";

        String result1 = queryBuilder.addOrderByQuery(query1, pagination);
        assertEquals(expectedQuery1.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name()), result1);

        // Test case 2: Pagination is null
        String query2 = "SELECT * FROM table";
        String expectedQuery2 = "SELECT * FROM table" + " ORDER BY art.createdtime DESC ";

        String result2 = queryBuilder.addOrderByQuery(query2, null);
        assertEquals(expectedQuery2, result2);
    }

    @Test
    void testGetCommentSearchQueryWithEmptyArtifactIds() {
        List<String> artifactIds = Collections.emptyList();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT com.id as id, com.tenantId as tenantId, com.artifactId as artifactId, " +
                "com.individualId as individualId, com.comment as comment, com.isActive as isActive, com.additionalDetails as additionalDetails, " +
                "com.createdBy as createdBy, com.lastModifiedBy as lastModifiedBy, com.createdTime as createdTime, com.lastModifiedTime as lastModifiedTime  " +
                "FROM dristi_evidence_comment com";

        String query = queryBuilder.getCommentSearchQuery(artifactIds, preparedStmtList);
        assertEquals(expectedQuery, query);
        assertTrue(preparedStmtList.isEmpty());
    }
}
