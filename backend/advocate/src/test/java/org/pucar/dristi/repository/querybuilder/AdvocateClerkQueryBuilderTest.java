package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.pucar.dristi.web.models.Pagination;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class AdvocateClerkQueryBuilderTest {

    private AdvocateClerkQueryBuilder queryBuilder;
    Integer limit;
    Integer offset;
    private AdvocateClerkSearchCriteria criteria;

    @BeforeEach
    public void setUp() {
         limit = 10;
         offset = 0;
        criteria = new AdvocateClerkSearchCriteria();
        criteria.setId("123");
        criteria.setStateRegnNumber("456");
        criteria.setApplicationNumber("4567");
        criteria.setIndividualId("indivID");

        MockitoAnnotations.openMocks(this);
        queryBuilder = Mockito.spy(new AdvocateClerkQueryBuilder());
    }

    @Test
    void testGetAdvocateClerkSearchQuery() {
        testQuery(null, null, "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc ORDER BY advc.createdtime DESC  LIMIT ? OFFSET ?", List.of(10, 0));

        testQuery(criteria, "tenant1", "SELECT advc.id as id", List.of("123", "456", "4567", "indivID", "tenant1", 10, 0));
    }

    @Test
    void testGetAdvocateClerkSearchQueryByStatus() {
        testStatusQuery(null, null, 2);

        String status = "Active";
        testStatusQuery(status, "tenant1", 4);
    }

    @Test
    void testGetAdvocateClerkSearchQueryByAppNumber() {
        testAppNumberQuery(null, null, 2);

        String applicationNumber = "APP001";
        testAppNumberQuery(applicationNumber, "tenant1", 4);
    }

    private void testQuery(AdvocateClerkSearchCriteria criteria, String tenantId, String expectedQuery, List<Object> expectedPreparedStmtList) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQuery(criteria, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(expectedPreparedStmtList.size(), preparedStmtList.size());
        assertEquals(expectedPreparedStmtList, preparedStmtList);
    }

    private void testStatusQuery(String status, String tenantId, int expectedSize) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(expectedSize, preparedStmtList.size());
    }

    private void testAppNumberQuery(String applicationNumber, String tenantId, int expectedSize) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQueryByAppNumber(applicationNumber, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(expectedSize, preparedStmtList.size());
    }

    @Test
    void testCustomExceptionHandling() {
        doThrow(new CustomException("Error", "Simulated Exception"))
                .when(queryBuilder)
                .getAdvocateClerkSearchQuery(any(), any(), any(), any(), any(), any());

        CustomException thrown = assertThrows(CustomException.class, this::invokeGetClerkException);
        assertEquals("Error", thrown.getCode());
        assertEquals("Simulated Exception", thrown.getMessage());
    }

    private void invokeGetClerkException() {
        List<Object> preparedStmtList = new ArrayList<>();
        queryBuilder.getAdvocateClerkSearchQuery(criteria, preparedStmtList, new ArrayList<>(), "tenant1", 10, 0);
    }

    @Test
    void testGetDocumentSearchQuery() {
        testDocumentSearchQuery(List.of("clerk1", "clerk2"), "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc WHERE doc.clerk_id IN (?,?)", List.of("clerk1", "clerk2"));
        testDocumentSearchQuery(new ArrayList<>(), "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc", new ArrayList<>());
    }

    private void testDocumentSearchQuery(List<String> ids, String expectedQuery, List<Object> expectedPreparedStmtList) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList, new ArrayList<>());

        assertEquals(expectedQuery, query);
        assertEquals(expectedPreparedStmtList, preparedStmtList);
    }

    @Test
    void testAddClauseIfRequired() {
        testAddClauseIfRequired(new ArrayList<>(), " WHERE ");
        testAddClauseIfRequired(List.of("mockObject"), "InitialQuery  AND ");
    }

    private void testAddClauseIfRequired(List<Object> preparedStmtList, String expectedQuery) {
        StringBuilder query = new StringBuilder(preparedStmtList.isEmpty() ? "" : "InitialQuery ");
        new AdvocateClerkQueryBuilder().addClauseIfRequired(query, preparedStmtList);
        assertEquals(expectedQuery, query.toString());
    }
}
