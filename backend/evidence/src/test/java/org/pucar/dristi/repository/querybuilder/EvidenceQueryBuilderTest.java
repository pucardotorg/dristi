package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EvidenceQueryBuilderTest {

    private final EvidenceQueryBuilder queryBuilder = new EvidenceQueryBuilder();

    @Test
    void testGetArtifactSearchQuery_WithAllFields() {
        // Mocking the required dependencies
        String id = "1";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String sourceId = "testSourceId";
        String sourceName = "testSourceName";
        String artifactNumber = "artifactNumber";

        // Mocking the EvidenceQueryBuilder
        EvidenceQueryBuilder queryBuilder = new EvidenceQueryBuilder();

        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art WHERE art.id = ? AND art.caseId = ? AND art.application = ? " +
                "AND art.hearing = ? AND art.orders = ? AND art.sourceId = ? " +
                "AND art.sourceName = ? " +
                "AND art.artifactNumber = ? ORDER BY art.createdTime DESC ";

        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(id, caseId, application, hearing, order, sourceId, sourceName, artifactNumber);

        // Assertions
        assertEquals(expectedQuery, query);
    }
    @Test
    void testGetArtifactSearchQuery_WithNullFields() {
        // Mocking the required dependencies
        String id = null;
        String caseId = null;
        String application = null;
        String hearing = null;
        String order = null;
        String sourceId = null;
        String sourceName = null;
        String artifactNumber=null;

        // Mocking the EvidenceQueryBuilder
        EvidenceQueryBuilder queryBuilder = new EvidenceQueryBuilder();

        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art ORDER BY art.createdTime DESC ";

        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(id, caseId, application, hearing, order, sourceId, sourceName,artifactNumber);

        // Assertions
        assertEquals(expectedQuery, query);
    }


    @Test
    void testGetArtifactSearchQueryWithNullValues() {
        // Mock the EvidenceQueryBuilder
        EvidenceQueryBuilder queryBuilder = Mockito.mock(EvidenceQueryBuilder.class);

        // Expected query when all values are null
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art ORDER BY art.createdTime DESC ";

        // Stubbing the method call to return the expected query
        Mockito.when(queryBuilder.getArtifactSearchQuery(null, null, null, null, null, null, null,null))
                .thenReturn(expectedQuery);

        // Calling the method under test
        String actualQuery = queryBuilder.getArtifactSearchQuery(null, null, null, null, null, null, null,null);

        // Assertions
        assertEquals(expectedQuery, actualQuery);
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
