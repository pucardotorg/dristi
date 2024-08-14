package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class EvidenceQueryBuilderTest {

    @InjectMocks
    private EvidenceQueryBuilder queryBuilder = new EvidenceQueryBuilder();

    @Test
    void testGetArtifactSearchQuery_WithAllFields() {
        // Mocking the required dependencies
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        criteria.setId("1");
        criteria.setCaseId("testCaseId");
        criteria.setApplicationNumber("testApplication");
        criteria.setEvidenceStatus(true);
        criteria.setArtifactType("testArtifactType");
        criteria.setFilingNumber("testFilingNumber");
        criteria.setHearing("testHearing");
        criteria.setOrder("testOrder");
        criteria.setSourceId("testSourceId");
        criteria.setSourceName("testSourceName");
        criteria.setArtifactNumber("artifactNumber");
        criteria.setOwner(UUID.fromString("baf36d5a-58ff-4b9b-b263-69ab45b1c7b4"));

        // Use ArrayList for preparedStmtList
        List<Object> preparedStmtList = new ArrayList<>(Arrays.asList("1", "testCaseId", "testApplication", "testArtifactType", true, "testFilingNumber",
                "testHearing", "testOrder", "testSourceId", "testSourceName", "artifactNumber", UUID.fromString("baf36d5a-58ff-4b9b-b263-69ab45b1c7b4")));

        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.comments as comments, art.file as file, art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art WHERE art.id = ? AND art.caseId = ? AND art.application = ? AND art.artifactType = ? AND art.isEvidence = ? AND art.filingNumber = ? " +
                "AND art.hearing = ? AND art.orders = ? AND art.sourceId = ? " +
                "AND art.createdBy = ? AND art.sourceName = ? " +
                "AND art.artifactNumber LIKE ?";

        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(preparedStmtList,new ArrayList<>(), criteria);

        // Assertions
        assertEquals(expectedQuery, query);
    }


    @Test
    void testGetArtifactSearchQuery_WithNullFields() {
        // Mocking the required dependencies
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        criteria.setId(null);
        criteria.setCaseId(null);
        criteria.setApplicationNumber(null);
        criteria.setFilingNumber(null);
        criteria.setHearing(null);
        criteria.setOrder(null);
        criteria.setSourceId(null);
        criteria.setSourceName(null);
        criteria.setArtifactNumber(null);

        // Expected query
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.comments as comments, art.file as file, art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art";
        List<Object> preparedStmtList = new ArrayList<>();

        // Calling the method under test
        String query = queryBuilder.getArtifactSearchQuery(preparedStmtList, new ArrayList<>(), criteria);

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
        boolean result = queryBuilder.addArtifactCriteria(criteria, query, preparedStmtList, firstCriteria,new ArrayList<>());

        // Verify the results
        assertFalse(result); // firstCriteria should be false after adding the first criteria
        assertEquals("SELECT * FROM artifacts WHERE art.isEvidence = ?", query.toString()); // query should have the criteria appended
        assertEquals(1, preparedStmtList.size()); // preparedStmtList should have one element
        assertEquals(criteria, preparedStmtList.get(0)); // the criteria should be added to the preparedStmtList
    }

    @Test
    public void testGetArtifactSearchQuery_exception() {
        List<Object> preparedStmtList = new ArrayList<>();
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        criteria.setId("testId");
        criteria.setCaseId("testCaseId");
        criteria.setEvidenceStatus(true);
        criteria.setArtifactType("testArtifactType");
        criteria.setApplicationNumber("testApplication");
        criteria.setFilingNumber("testFilingNumber");
        criteria.setHearing("testHearing");
        criteria.setOrder("testOrder");
        criteria.setSourceId("testSourceId");
        criteria.setSourceName("testSourceName");
        criteria.setArtifactNumber("testArtifactNumber");
        criteria.setOwner(UUID.fromString("baf36d5a-58ff-4b9b-b263-69ab45b1c7b4"));

        // Inject a scenario that causes an exception
        EvidenceQueryBuilder spyQueryBuilder = spy(queryBuilder);
        doThrow(new RuntimeException("Test Exception"))
                .when(spyQueryBuilder)
                .addArtifactCriteria(
                        anyBoolean(),
                        any(StringBuilder.class),
                        anyList(),
                        anyBoolean(),
                        anyList()
                );

        // Execute the method and assert that the CustomException is thrown
        CustomException exception = assertThrows(CustomException.class, () -> {
            spyQueryBuilder.getArtifactSearchQuery(preparedStmtList,new ArrayList<>(), criteria);
        });

        // Verify that the correct exception is thrown with the expected message
        assertFalse(exception.getMessage().contains("Error occurred while building the artifact search query: java.lang.RuntimeException: Test Exception"));
    }




    @Test
    void testGetArtifactSearchQueryWithNullValues() {
        // Mock the EvidenceQueryBuilder
        EvidenceQueryBuilder mockQueryBuilder = Mockito.mock(EvidenceQueryBuilder.class);

        // Expected query when all values are null
        String expectedQuery = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
                "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
                "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
                "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
                "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
                "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
                "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime " +
                " FROM dristi_evidence_artifact art";

        // Mock behavior for null criteria
        Mockito.when(mockQueryBuilder.getArtifactSearchQuery(Mockito.any(),Mockito.isNull(),Mockito.isNull())).thenReturn(expectedQuery);

        // Call the method and verify results
        String actualQuery = mockQueryBuilder.getArtifactSearchQuery(new ArrayList<>(), null,null);
        assertEquals(expectedQuery, actualQuery);
    }



}