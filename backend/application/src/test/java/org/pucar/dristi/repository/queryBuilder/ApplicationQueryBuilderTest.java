package org.pucar.dristi.repository.queryBuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.ApplicationCriteria;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_SEARCH_QUERY_EXCEPTION;

class ApplicationQueryBuilderTest {

    @InjectMocks
    private ApplicationQueryBuilder applicationQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Mock
    private Pagination pagination;
    private static final String BASE_APPLICATION_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_application app WHERE ";

    @Test
    void testGetApplicationSearchQuery() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setId("test-id");
        criteria.setTenantId("test-tenant");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.caseid as caseid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber, app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby, app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber, app.statuteSection as statuteSection, app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive, app.additionaldetails as additionaldetails, app.reason_for_application as reason_for_application, app.application_details as application_details, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status  FROM dristi_application app WHERE app.id = ? AND app.tenantId = ?";
        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtArgList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals("test-id", preparedStmtList.get(0));
        assertEquals("test-tenant", preparedStmtList.get(1));
    }

    @Test
    public void testAddOrderByQuery_withPagination() {
        // Setup
        ApplicationQueryBuilder queryBuilder = new ApplicationQueryBuilder();
        String query = "SELECT * FROM applications";
        Pagination paginationNotNull = new Pagination();
        paginationNotNull.setSortBy("columnName");
        paginationNotNull.setOrder(Order.ASC);

        // Execute
        String resultQuery = queryBuilder.addOrderByQuery(query, paginationNotNull);

        // Assert
        String expectedQuery = "SELECT * FROM applications ORDER BY app.columnName ASC ";
        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    public void testAddOrderByQuery_withDefaultOrderBy() {
        String baseQuery = "SELECT * FROM applications";
        String expectedQuery = "SELECT * FROM applications ORDER BY app.createdtime DESC ";

        pagination.setSortBy(null);
        pagination.setOrder(null);

        String actualQuery = applicationQueryBuilder.addOrderByQuery(baseQuery, pagination);

        assertEquals(expectedQuery, actualQuery);
    }
    @Test
    public void testGetApplicationSearchQuery_exception() {
        // Create and set up ApplicationCriteria with test data
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setApplicationType("testtype");
        criteria.setId("testId");
        criteria.setFilingNumber("testFilingNumber");
        criteria.setCnrNumber("testCnrNumber");
        criteria.setTenantId("testTenantId");
        criteria.setStatus("testStatus");
        criteria.setApplicationNumber("testApplicationNumber");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Inject a scenario that causes an exception
        ApplicationQueryBuilder spyQueryBuilder = spy(applicationQueryBuilder);
        doThrow(new RuntimeException("Test Exception")).when(spyQueryBuilder).addCriteria(anyString(), any(), anyBoolean(), anyString(), anyList(),anyList());

        // Execute the method and assert that the CustomException is thrown
        CustomException exception = assertThrows(CustomException.class, () -> {
            spyQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtArgList);
        });

        // Verify that the correct exception is thrown with the expected message
        assertTrue(exception.getMessage().contains("Error occurred while building the application search query: Test Exception"));
    }


    @Test
    void testGetApplicationSearchQueryWithNoCriteria() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();


        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.caseid as caseid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber, app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby, app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber, app.statuteSection as statuteSection, app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive, app.additionaldetails as additionaldetails, app.reason_for_application as reason_for_application, app.application_details as application_details, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status  FROM dristi_application app";

        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtArgList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithApplicationNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setApplicationNumber("applicationNumber123");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtargList = new ArrayList<>();

        String expectedQueryPart = "app.applicationNumber = ?";

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtargList);

        assertFalse(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyApplicationNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setApplicationNumber("");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtargList = new ArrayList<>();

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtargList);

        assertFalse(query.contains("app.applicationNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithFilingNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setFilingNumber("filingNumber123");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtargList = new ArrayList<>();

        String expectedQueryPart = "app.filingNumber = ?";

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList, preparedStmtargList);

        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("filingNumber123", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyFilingNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setFilingNumber("");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();


        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertFalse(query.contains("app.filingNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithCnrNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setCnrNumber("CNR123456");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQueryPart = "app.cnrNumber = ?";

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("CNR123456", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithNullFields() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertFalse(query.contains("app.cnrNumber ="));
        assertFalse(query.contains("app.status ="));
        assertFalse(query.contains("app.filingNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyCnrNumber() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setCnrNumber("");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertFalse(query.contains("app.cnrNumber ="));
        assertEquals(0, preparedStmtList.size());
    }

    @Test
    void testGetApplicationSearchQueryWithStatus() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setStatus("status123");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQueryPart = "app.status = ?";
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtArgList);

        assertTrue(query.contains(expectedQueryPart));
        assertEquals(1, preparedStmtList.size());
        assertEquals("status123", preparedStmtList.get(0));
    }

    @Test
    void testGetApplicationSearchQueryWithEmptyStatus() {
        ApplicationCriteria criteria = new ApplicationCriteria();
        criteria.setStatus("");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String query = applicationQueryBuilder.getApplicationSearchQuery(criteria, preparedStmtList,preparedStmtArgList);

        assertFalse(query.contains("app.status ="));
        assertEquals(0, preparedStmtList.size());
    }



    @Test
    void testGetDocumentSearchQueryWithNoIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = new java.util.ArrayList<>();

        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
                "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.application_id as application_id FROM dristi_application_document doc";
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String actualQuery = applicationQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }




    @Test
    void testGetDocumentSearchQueryThrowsException() {
        assertThrows(CustomException.class, () -> {
            applicationQueryBuilder.getDocumentSearchQuery(null, null,null);
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
            applicationQueryBuilder.getApplicationSearchQuery(new ApplicationCriteria(), new ArrayList<>(),new ArrayList<>());
        } catch (Exception e) {
            assertEquals(APPLICATION_SEARCH_QUERY_EXCEPTION, e.getMessage());
        }
    }

    @Test
    void testGetApplicationsSearchQueryCustomException() {
        try {
            applicationQueryBuilder.getApplicationSearchQuery( new ApplicationCriteria(), new ArrayList<>(), new ArrayList<>());
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
        pagination.setLimit(2d);
        pagination.setOffSet(0d);
        List<Object> preparedStmtList = new ArrayList<>();
        String paginatedQuery = applicationQueryBuilder.addPaginationQuery(query, pagination, preparedStmtList,new ArrayList<>());

        String expectedQuery = "SELECT * FROM dristi_application app WHERE app.id = '111' LIMIT ? OFFSET ?";

        assertEquals(expectedQuery, paginatedQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals(0d, preparedStmtList.get(0));
        assertEquals(0d, preparedStmtList.get(1));
    }
}
