package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderQueryBuilderTest {

    private OrderQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        queryBuilder = new OrderQueryBuilder();
    }

    @Test
    void testCheckOrderExistQuery() {
        // Arrange
        String orderNumber = "ORDER123";
        String cnrNumber = "CNR123";
        String filingNumber = "FILING123";
        String applicationNumber = "APP123";
        UUID orderId = UUID.randomUUID();
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.checkOrderExistQuery(orderNumber, cnrNumber, filingNumber, applicationNumber, orderId, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_orders orders WHERE orders.cnrNumber = ? AND orders.filingNumber = ? AND orders.orderNumber = ? AND orders.id = ? AND orders.applicationNumber::text LIKE ?";
        assertEquals(expectedQuery, query);
        assertEquals(5, preparedStmtList.size());
        assertEquals(cnrNumber, preparedStmtList.get(0));
        assertEquals(filingNumber, preparedStmtList.get(1));
        assertEquals(orderNumber, preparedStmtList.get(2));
        assertEquals(orderId.toString(), preparedStmtList.get(3));
        assertEquals("%" + applicationNumber + "%", preparedStmtList.get(4));
    }

    @Test
    void testGetOrderSearchQuery() {
        // Arrange
        String orderNumber = "ORDER123";
        String applicationNumber = "APP123";
        String cnrNumber = "CNR123";
        String filingNumber = "FILING123";
        String tenantId = "tenant1";
        String id = "1";
        String status = "active";
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getOrderSearchQuery(orderNumber, applicationNumber, cnrNumber, filingNumber, tenantId, id, status, preparedStmtList);

        // Assert
        String expectedQuery = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, orders.filingnumber as filingnumber, orders.comments as comments, orders.cnrnumber as cnrnumber, orders.linkedordernumber as linkedordernumber, orders.ordernumber as ordernumber, orders.applicationnumber as applicationnumber,orders.createddate as createddate, orders.ordertype as ordertype, orders.issuedby as issuedby, orders.ordercategory as ordercategory,orders.status as status, orders.isactive as isactive, orders.additionaldetails as additionaldetails, orders.createdby as createdby,orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime  FROM dristi_orders orders WHERE orders.applicationNumber::text LIKE ? AND orders.cnrNumber = ? AND orders.filingNumber = ? AND orders.tenantId = ? AND orders.id = ? AND orders.status = ? AND orders.orderNumber = ? ORDER BY orders.createdtime DESC ";
        assertEquals(expectedQuery, query);
        assertEquals(7, preparedStmtList.size());
        assertEquals("%" + applicationNumber + "%", preparedStmtList.get(0));
        assertEquals(cnrNumber, preparedStmtList.get(1));
        assertEquals(filingNumber, preparedStmtList.get(2));
        assertEquals(tenantId, preparedStmtList.get(3));
        assertEquals(id, preparedStmtList.get(4));
        assertEquals(status, preparedStmtList.get(5));
        assertEquals(orderNumber, preparedStmtList.get(6));
    }

    @Test
    void testGetDocumentSearchQuery() {
        // Arrange
        List<String> ids = new ArrayList<>();
        ids.add("1");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore,doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.order_id as order_id FROM dristi_order_document doc WHERE doc.order_id IN (?)";
        assertEquals(expectedQuery, query);
        assertEquals(1, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
    }

    @Test
    void testGetStatuteSectionSearchQuery() {
        // Arrange
        List<String> ids = new ArrayList<>();
        ids.add("1");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.order_id as order_id, stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby, stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime  FROM dristi_order_statute_section stse WHERE stse.order_id IN (?)";
        assertEquals(expectedQuery, query);
        assertEquals(1, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
    }
}