package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.pucar.dristi.repository.querybuilder.OrderQueryBuilder;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.OrderRowMapper;
import org.pucar.dristi.repository.rowmapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderCriteria;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderRepositoryTest {

    @InjectMocks
    private OrderRepository orderRepository;

    @Mock
    private OrderQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private OrderRowMapper rowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    @Mock
    private StatuteSectionRowMapper statuteSectionRowMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrders() {
        // Setup mock responses
        String orderQuery = "some query";
        String statuteSectionQuery = "some query";
        String documentQuery = "some query";
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(UUID.randomUUID());
        orders.add(order);

        // Arrange
        String orderNumber = "ORDER123";
        String applicationNumber = "APP123";
        String cnrNumber = "CNR123";
        String filingNumber = "FILING123";
        String tenantId = "tenant1";
        String id = "1";
        String status = "active";
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setOrderNumber(orderNumber);
        orderCriteria.setId(id);
        orderCriteria.setStatus(status);
        orderCriteria.setFilingNumber(filingNumber);
        orderCriteria.setCnrNumber(cnrNumber);
        orderCriteria.setTenantId(tenantId);
        orderCriteria.setApplicationNumber(applicationNumber);
        List<Object> preparedStmtList = new ArrayList<>();

        when(queryBuilder.getOrderSearchQuery(any(),any()))
                .thenReturn(orderQuery);
        when(jdbcTemplate.query(eq(orderQuery), any(Object[].class), eq(rowMapper))).thenReturn(orders);

        // Mock statute section and document queries
        Map<UUID, StatuteSection> statuteSectionMap = new HashMap<>();
        statuteSectionMap.put(UUID.randomUUID(), new StatuteSection());
        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), anyList())).thenReturn(statuteSectionQuery);
        when(jdbcTemplate.query(eq(statuteSectionQuery), any(Object[].class), eq(statuteSectionRowMapper))).thenReturn(statuteSectionMap);

        Map<UUID, List<Document>> documentMap = new HashMap<>();
        documentMap.put(UUID.randomUUID(), new ArrayList<>());
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(documentQuery);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), eq(documentRowMapper))).thenReturn(documentMap);

        // Execute the method
        List<Order> result = orderRepository.getOrders(orderCriteria);

        // Verify interactions and assertions
        verify(jdbcTemplate).query(eq(orderQuery), any(Object[].class), eq(rowMapper));
        verify(jdbcTemplate).query(eq(statuteSectionQuery), any(Object[].class), eq(statuteSectionRowMapper));
        verify(jdbcTemplate).query(eq(documentQuery), any(Object[].class), eq(documentRowMapper));
        assertNotNull(result);
    }

    @Test
    public void testCheckOrderExists() {
        // Setup mock responses
        List<OrderExists> orderExistsRequest = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExistsRequest.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(anyString(), anyString(), anyString(), anyString(), any(), anyList()))
                .thenReturn("some query");
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        // Execute the method
        List<OrderExists> result = orderRepository.checkOrderExists(orderExistsRequest);

        // Verify interactions and assertions
        assertNotNull(result);
    }
}
