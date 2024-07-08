package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.OrderCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderQueryBuilderTest {

    @InjectMocks
    private OrderQueryBuilder orderQueryBuilder;

    private List<Object> preparedStmt;
    private List<String> ids;

    @BeforeEach
    void setUp() {
        preparedStmt = new ArrayList<>();
        ids = new ArrayList<>();
    }

    @Test
    void testCheckOrderExistQuery() {
        UUID orderId = UUID.randomUUID();
        String query = orderQueryBuilder.checkOrderExistQuery("order123", "cnr123", "filing123", "app123", orderId, preparedStmt);

        assertNotNull(query);
        assertFalse(preparedStmt.isEmpty());
        assertEquals(5, preparedStmt.size());
    }

    @Test
    void testCheckOrderExistQueryWithNullValues() {
        UUID orderId = null;
        String query = orderQueryBuilder.checkOrderExistQuery(null, null, null, null, orderId, preparedStmt);

        assertNotNull(query);
        assertTrue(preparedStmt.isEmpty());
    }

    @Test
    void testGetOrderSearchQuery() {
        OrderCriteria criteria = new OrderCriteria();
        criteria.setId("order123");
        criteria.setApplicationNumber("app123");
        criteria.setCnrNumber("cnr123");
        criteria.setFilingNumber("filing123");
        criteria.setTenantId("tenant123");
        criteria.setId("id123");
        criteria.setStatus("status123");
        criteria.setOrderNumber("order123");

        String query = orderQueryBuilder.getOrderSearchQuery(criteria, preparedStmt);

        assertNotNull(query);
        assertFalse(preparedStmt.isEmpty());
        assertEquals(7, preparedStmt.size());
    }

    @Test
    void testGetOrderSearchQueryWithEmptyCriteria() {
        OrderCriteria criteria = new OrderCriteria();
        String query = orderQueryBuilder.getOrderSearchQuery(criteria, preparedStmt);

        assertNotNull(query);
        assertTrue(preparedStmt.isEmpty());
    }

    @Test
    void testGetDocumentSearchQuery() {
        ids.add("id123");
        String query = orderQueryBuilder.getDocumentSearchQuery(ids, preparedStmt);

        assertNotNull(query);
        assertFalse(preparedStmt.isEmpty());
        assertEquals(1, preparedStmt.size());
    }

    @Test
    void testGetDocumentSearchQueryWithEmptyIds() {
        String query = orderQueryBuilder.getDocumentSearchQuery(ids, preparedStmt);

        assertNotNull(query);
        assertTrue(preparedStmt.isEmpty());
    }

    @Test
    void testGetStatuteSectionSearchQuery() {
        ids.add("id123");
        String query = orderQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmt);

        assertNotNull(query);
        assertFalse(preparedStmt.isEmpty());
        assertEquals(1, preparedStmt.size());
    }

    @Test
    void testGetStatuteSectionSearchQueryWithEmptyIds() {
        String query = orderQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmt);

        assertNotNull(query);
        assertTrue(preparedStmt.isEmpty());
    }

    @Test
    void testCheckOrderExistQuery_Exception() {
        List<Object> preparedStmtList = null;
        String orderNumber = "order123";
        String cnrNumber = null;
        String filingNumber = null;
        String applicationNumber = null;
        UUID orderId = null;

        assertThrows(CustomException.class, () -> {
            orderQueryBuilder.checkOrderExistQuery(orderNumber, cnrNumber, filingNumber, applicationNumber, orderId, preparedStmtList);
        });
    }

    @Test
    void testGetOrderSearchQuery_Exception() {
        List<Object> preparedStmtList = null;
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setOrderNumber("order123");

        assertThrows(CustomException.class, () -> {
            orderQueryBuilder.getOrderSearchQuery(orderCriteria, preparedStmtList);
        });
    }

    @Test
    void testGetStatuteSectionSearchQuery_Exception() {
        List<Object> preparedStmtList = null;

        assertThrows(CustomException.class, () -> {
            orderQueryBuilder.getStatuteSectionSearchQuery(null, preparedStmtList);
        });
    }

    @Test
    void testGetDocumentSearchQuery_Exception() {
        List<Object> preparedStmtList = null;

        assertThrows(CustomException.class, () -> {
            orderQueryBuilder.getDocumentSearchQuery(null, preparedStmtList);
        });
    }
}
