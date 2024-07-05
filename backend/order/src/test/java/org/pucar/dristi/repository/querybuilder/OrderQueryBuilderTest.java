package org.pucar.dristi.repository.querybuilder;

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
public class OrderQueryBuilderTest {

    @InjectMocks
    private OrderQueryBuilder orderQueryBuilder;

    private List<Object> preparedStmtList;
    private List<String> ids;

    @BeforeEach
    public void setUp() {
        preparedStmtList = new ArrayList<>();
        ids = new ArrayList<>();
    }

    @Test
    public void testCheckOrderExistQuery() {
        UUID orderId = UUID.randomUUID();
        String query = orderQueryBuilder.checkOrderExistQuery("order123", "cnr123", "filing123", "app123", orderId, preparedStmtList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertEquals(5, preparedStmtList.size());
    }

    @Test
    public void testCheckOrderExistQueryWithNullValues() {
        UUID orderId = null;
        String query = orderQueryBuilder.checkOrderExistQuery(null, null, null, null, orderId, preparedStmtList);

        assertNotNull(query);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    public void testGetOrderSearchQuery() {
        OrderCriteria criteria = new OrderCriteria();
        criteria.setId("order123");
        criteria.setApplicationNumber("app123");
        criteria.setCnrNumber("cnr123");
        criteria.setFilingNumber("filing123");
        criteria.setTenantId("tenant123");
        criteria.setId("id123");
        criteria.setStatus("status123");

        String query = orderQueryBuilder.getOrderSearchQuery(criteria, preparedStmtList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertEquals(6, preparedStmtList.size());
    }

    @Test
    public void testGetOrderSearchQueryWithEmptyCriteria() {
        OrderCriteria criteria = new OrderCriteria();
        String query = orderQueryBuilder.getOrderSearchQuery(criteria, preparedStmtList);

        assertNotNull(query);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    public void testGetDocumentSearchQuery() {
        ids.add("id123");
        String query = orderQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    public void testGetDocumentSearchQueryWithEmptyIds() {
        String query = orderQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertTrue(preparedStmtList.isEmpty());
    }

    @Test
    public void testGetStatuteSectionSearchQuery() {
        ids.add("id123");
        String query = orderQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertFalse(preparedStmtList.isEmpty());
        assertEquals(1, preparedStmtList.size());
    }

    @Test
    public void testGetStatuteSectionSearchQueryWithEmptyIds() {
        String query = orderQueryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertTrue(preparedStmtList.isEmpty());
    }
}
