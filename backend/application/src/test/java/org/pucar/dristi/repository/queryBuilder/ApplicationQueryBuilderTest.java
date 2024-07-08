package org.pucar.dristi.repository.queryBuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.Pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_EXIST_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_SEARCH_QUERY_EXCEPTION;

class ApplicationQueryBuilderTest {

    @InjectMocks
    private ApplicationQueryBuilder applicationQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String BASE_APPLICATION_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_application app WHERE ";

    @Test
    void testGetApplicationSearchQuery() {
        String id = "test-id";
        String filingNumber = null;
        String cnrNumber = null;
        String applicationNumber = null;
        String tenantId = "test-tenant";
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.caseid as caseid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                " app.additionaldetails as additionaldetails, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status " +
                " FROM dristi_application app WHERE app.id = ? AND app.tenantId = ? ORDER BY app.createdtime DESC ";

        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals("test-id", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithNoCriteria() {
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String applicationNumber = null;
        String tenantId = null;
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.caseid as caseid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                " app.additionaldetails as additionaldetails, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status " +
                " FROM dristi_application app ORDER BY app.createdtime DESC ";

        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithApplicationNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String applicationNumber = "applicationNumber123";
        String tenantId = null;
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Expected query part
        String expectedQueryPart = "app.applicationNumber = ?";

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        // Verify the expected part of the query is present
        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("applicationNumber123", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyApplicationNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String tenantId = null;
        String applicationNumber = "";
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertFalse(query.contains("app.applicationNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithFilingNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = "filingNumber123";
        String cnrNumber = null;
        String applicationNumber = null;
        String tenantId = null;
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Expected query part
        String expectedQueryPart = "app.filingNumber = ?";

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        // Verify the expected part of the query is present
        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("filingNumber123", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyFilingNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = "";
        String cnrNumber = null;
        String tenantId = null;
        String applicationNumber = null;
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertFalse(query.contains("app.filingNumber ="));
        assertEquals(0, preparedStmtList.size());
    }


    @Test
    void testGetApplicationSearchQueryWithCnrNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = "CNR123456";
        String tenantId = null;
        String status = null;
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Expected query part
        String expectedQueryPart = "app.cnrNumber = ?";

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        // Verify the expected part of the query is present
        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("CNR123456", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithNullFields() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String applicationNumber = null;
        String tenantId = null;
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertFalse(query.contains("app.cnrNumber ="));
        assertFalse(query.contains("app.status ="));
        assertFalse(query.contains("app.filingNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyCnrNumber() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = "";
        String tenantId = null;
        String status = null;
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertFalse(query.contains("app.cnrNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithStatus() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String tenantId = null;
        String status = "status123";
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();


        // Expected query part
        String expectedQueryPart = "app.status = ?";

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        // Verify the expected part of the query is present
        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("status123", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyStatus() {
        // Prepare inputs
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String applicationNumber = null;
        String tenantId = null;
        String status = "";
        List<Object> preparedStmtList = new ArrayList<>();

        // Call the method
        String query = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, applicationNumber, preparedStmtList);

        assertFalse(query.contains("app.status ="));
        assertEquals(0, preparedStmtList.size());
    }


    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = Arrays.asList("id1", "id2", "id3");
        List<Object> preparedStmtList = new java.util.ArrayList<>();

        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
                "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.application_id as application_id FROM dristi_application_document doc WHERE doc.application_id IN (?,?,?)";

        String actualQuery = applicationQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(ids, preparedStmtList);
    }

    @Test
    void testGetDocumentSearchQueryWithNoIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = new java.util.ArrayList<>();

        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
                "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.application_id as application_id FROM dristi_application_document doc";

        String actualQuery = applicationQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void testGetStatuteSectionSearchQuery() {
        List<String> ids = Arrays.asList("id1", "id2", "id3");
        List<Object> preparedStmtList = new java.util.ArrayList<>();

        String expectedQuery = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.application_id as application_id, " +
                "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
                " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime FROM dristi_application_statute_section stse WHERE stse.application_id IN (?,?,?)";

        String actualQuery = applicationQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(ids, preparedStmtList);
    }

    @Test
    void testGetStatuteSectionSearchQueryWithNoIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = new java.util.ArrayList<>();

        String expectedQuery = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.application_id as application_id, " +
                "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
                " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime FROM dristi_application_statute_section stse";

        String actualQuery = applicationQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void testGetDocumentSearchQueryThrowsException() {
        assertThrows(CustomException.class, () -> {
            applicationQueryBuilder.getDocumentSearchQuery(null, null);
        });
    }

    @Test
    void testGetStatuteSectionSearchQueryThrowsException() {
        assertThrows(CustomException.class, () -> {
            applicationQueryBuilder.getStatuteSectionSearchQuery(null, null);
        });
    }

    @Test
    void testAllParametersProvided() {
        String filingNumber = "FN123";
        String cnrNumber = "CNR123";
        String applicationNumber = "AN123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY +
                "app.filingNumber = ? AND " +
                "app.cnrNumber = ? AND " +
                "app.applicationNumber = ?";

        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(filingNumber, cnrNumber, applicationNumber,preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(filingNumber, preparedStmtList.get(0));
        assertEquals(cnrNumber, preparedStmtList.get(1));
        assertEquals(applicationNumber, preparedStmtList.get(2));
        assertEquals(3, preparedStmtList.size());
    }

    @Test
    void testOnlyFilingNumberProvided() {
        String filingNumber = "FN123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY + "app.filingNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(filingNumber, null, null, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(filingNumber, preparedStmtList.get(0));
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    void testOnlyCnrNumberProvided() {
        String cnrNumber = "CNR123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY + "app.cnrNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(null, cnrNumber, null, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(cnrNumber, preparedStmtList.get(0));
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    void testOnlyApplicationNumberProvided() {
        String applicationNumber = "AN123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY + "app.applicationNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(null, null, applicationNumber, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(applicationNumber, preparedStmtList.get(0));
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    void testFilingAndCnrNumbersProvided() {
        String filingNumber = "FN123";
        String cnrNumber = "CNR123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY +
                "app.filingNumber = ? AND " +
                "app.cnrNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(filingNumber, cnrNumber, null, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(filingNumber, preparedStmtList.get(0));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void testCnrAndApplicationNumbersProvided() {
        String cnrNumber = "CNR123";
        String applicationNumber = "AN123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY +
                "app.cnrNumber = ? AND " +
                "app.applicationNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(null, cnrNumber, applicationNumber, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(cnrNumber, preparedStmtList.get(0));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void testFilingAndApplicationNumbersProvided() {
        String filingNumber = "FN123";
        String applicationNumber = "AN123";

        String expectedQuery = BASE_APPLICATION_EXIST_QUERY +
                "app.filingNumber = ? AND " +
                "app.applicationNumber = ?";
        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.checkApplicationExistQuery(filingNumber, null, applicationNumber, preparedStmtList);
        assertEquals(expectedQuery, actualQuery);
        assertEquals(filingNumber, preparedStmtList.get(0));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void testGetApplicationsSearchQueryException() {
        try {
            applicationQueryBuilder.getApplicationSearchQuery(null, null, null, null, null, null, null);
        } catch (Exception e) {
            assertEquals(APPLICATION_SEARCH_QUERY_EXCEPTION, e.getMessage());
        }
    }

    @Test
    void testGetApplicationsSearchQueryCustomException() {
        try {
            applicationQueryBuilder.getApplicationSearchQuery(null, null, null, null, null, null, null);
        } catch (CustomException e) {
            assertEquals(APPLICATION_SEARCH_QUERY_EXCEPTION, e.getCode());
        }
    }

    @Test
    void testCheckApplicationsExistQueryException() {
        try {
            applicationQueryBuilder.checkApplicationExistQuery(null, null, null, null);
        } catch (Exception e) {
            assertEquals("Error occurred while building the application exist query ", e.getMessage());
        }
    }

    @Test
    void getTotalCountQuery_ShouldReturnCorrectQuery_WhenBaseQueryIsNotNull() {
        String baseQuery = "SELECT * FROM dristi_application app WHERE app.id = '111'";

        String query = applicationQueryBuilder.getTotalCountQuery(baseQuery);

        String expectedQuery = "SELECT COUNT(*) FROM (SELECT * FROM dristi_application app WHERE app.id = '111') total_result";

        assertEquals(expectedQuery, query);
    }

    @Test
    void addPagination_Query_ShouldReturnCorrectQuery_WhenPageSizeAndPageNumberAreNotNull() {
        String query = "SELECT * FROM dristi_application app WHERE app.id = '111'";
        Pagination pagination = new Pagination();
        pagination.setLimit(2d);
        pagination.setOffSet(0d);
        List<Object> preparedStmtList = new ArrayList<>();
        String paginatedQuery = applicationQueryBuilder.addPaginationQuery(query, pagination, preparedStmtList);

        String expectedQuery = "SELECT * FROM dristi_application app WHERE app.id = '111' LIMIT ? OFFSET ?";

        assertEquals(expectedQuery, paginatedQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals(2d, preparedStmtList.get(0));
        assertEquals(0d, preparedStmtList.get(1));
    }
}
