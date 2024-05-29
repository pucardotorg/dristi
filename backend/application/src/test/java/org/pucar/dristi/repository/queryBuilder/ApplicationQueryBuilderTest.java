package org.pucar.dristi.repository.queryBuilder;

        import org.egov.tracer.model.CustomException;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.MockitoAnnotations;

        import java.util.Arrays;
        import java.util.Collections;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;

class ApplicationQueryBuilderTest {

    @InjectMocks
    private ApplicationQueryBuilder applicationQueryBuilder;

    ApplicationQueryBuilderTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetApplicationSearchQuery() {
        String id = "test-id";
        String filingNumber = null;
        String cnrNumber = null;
        String tenantId = "test-tenant";
        String status = null;
        Integer limit = 10;
        Integer offset = 0;

        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                " app.additionaldetails as additionaldetails, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status " +
                " FROM dristi_application app WHERE app.id ='test-id' AND app.tenantId ='test-tenant' ORDER BY app.createdtime DESC ";

        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, limit, offset);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testGetApplicationSearchQueryWithNoCriteria() {
        String id = null;
        String filingNumber = null;
        String cnrNumber = null;
        String tenantId = null;
        String status = null;
        Integer limit = 10;
        Integer offset = 0;

        String expectedQuery = " SELECT app.id as id, app.tenantid as tenantid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                " app.additionaldetails as additionaldetails, app.createdby as createdby, app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime, app.status as status " +
                " FROM dristi_application app ORDER BY app.createdtime DESC ";

        String actualQuery = applicationQueryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, status, limit, offset);

        assertEquals(expectedQuery, actualQuery);
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
}
