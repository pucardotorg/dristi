package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderQueryBuilderTest {

    private OrderQueryBuilder orderQueryBuilder;

    @BeforeEach
    void setUp() {
        orderQueryBuilder = new OrderQueryBuilder();
    }

    @Test
    void testGetOrderSearchQuery() {
        String applicationNumber = "APP-123";
        String cnrNumber = "CNR-123";
        String filingNumber = "FN-123";
        String tenantId = "tenant-123";
        String id = "ID-123";
        String status = "Active";

        String expectedQuery = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, " +
                "orders.filingnumber as filingnumber, orders.cnrNumber as cnrNumber, orders.orderNumber as orderNumber," +
                "orders.status as status, orders.isactive as isactive, orders.additionaldetails as additionaldetails, orders.createdby as createdby," +
                "orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime " +
                " FROM dristi_orders orders WHERE orders.applicationnumber ='APP-123' OR orders.cnrNumber = 'CNR-123' OR orders.filingnumber ='FN-123' OR orders.tenantid ='tenant-123' OR orders.id = 'ID-123' OR orders.status ='Active' ORDER BY orders.createdtime DESC ";

        String actualQuery = orderQueryBuilder.getOrderSearchQuery(applicationNumber, cnrNumber, filingNumber, tenantId, id, status);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testGetOrderSearchQueryWithNullValues() {
        String applicationNumber = null;
        String cnrNumber = null;
        String filingNumber = "FN-123";
        String tenantId = "tenant-123";
        String id = null;
        String status = "Active";

        String expectedQuery = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, " +
                "orders.filingnumber as filingnumber, orders.cnrNumber as cnrNumber, orders.orderNumber as orderNumber," +
                "orders.status as status, orders.isactive as isactive, orders.additionaldetails as additionaldetails, orders.createdby as createdby," +
                "orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime " +
                " FROM dristi_orders orders WHERE orders.filingnumber ='FN-123' OR orders.tenantid ='tenant-123' OR orders.status ='Active' ORDER BY orders.createdtime DESC ";

        String actualQuery = orderQueryBuilder.getOrderSearchQuery(applicationNumber, cnrNumber, filingNumber, tenantId, id, status);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testGetDocumentSearchQueryWithEmptyIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = Collections.emptyList();

        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
                "doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.order_id as order_id, doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_order_document doc";

        String actualQuery = orderQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void testGetStatuteSectionSearchQueryWithEmptyIds() {
        List<String> ids = Collections.emptyList();
        List<Object> preparedStmtList = Collections.emptyList();

        String expectedQuery = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.order_id as order_id, " +
                "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
                " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime  FROM dristi_order_statute_section stse";

        String actualQuery = orderQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    void testGetOrderSearchQueryException() {
        try {
            orderQueryBuilder.getOrderSearchQuery(null, null, null, null, null, null);
        } catch (CustomException e) {
            assertEquals("ORDER_SEARCH_QUERY_EXCEPTION", e.getCode());
        }
    }

    @Test
    void testGetDocumentSearchQueryException() {
        try {
            orderQueryBuilder.getDocumentSearchQuery(null, null);
        } catch (CustomException e) {
            assertEquals("DOCUMENT_SEARCH_QUERY_EXCEPTION", e.getCode());
        }
    }

    @Test
    void testGetStatuteSectionSearchQueryException() {
        try {
            orderQueryBuilder.getStatuteSectionSearchQuery(null, null);
        } catch (CustomException e) {
            assertEquals("DOCUMENT_SEARCH_QUERY_EXCEPTION", e.getCode());
        }
    }
}
