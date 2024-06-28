package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
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

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testCheckOrderExists_AllFieldsNull() {
        // Test case where all fields in OrderExists are null
        List<OrderExists> orderExistsList = new ArrayList<>();
        orderExistsList.add(new OrderExists());

        List<OrderExists> result = orderRepository.checkOrderExists(orderExistsList);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertFalse(result.get(0).getExists());
    }

    @Test
    public void testCheckOrderExists_AllFieldsNotNull() {
        // Test case where all fields in OrderExists are not null
        List<OrderExists> orderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderId(UUID.fromString("7733e843-483a-4b94-9ae4-a92c79a3023"));
        orderExists.setOrderNumber("test-ord");
        orderExists.setFilingNumber("test-ord");
        orderExists.setOrderNumber("test-ord");
        orderExists.setCnrNumber("test-ord");
        orderExists.setApplicationNumber("test-ord");
        orderExistsList.add(orderExists);

        // Mock behavior for queryBuilder and jdbcTemplate
        when(queryBuilder.checkOrderExistQuery(any(), any(), any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM orders WHERE ...");
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        List<OrderExists> result = orderRepository.checkOrderExists(orderExistsList);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getExists());
    }

    @Test
    public void testCheckOrderExists_CustomException() {
        // Test case for CustomException being thrown
        List<OrderExists> orderExistsList = new ArrayList<>();
        orderExistsList.add(new OrderExists());

        // Mock behavior to throw CustomException
        when(queryBuilder.checkOrderExistQuery(any(), any(), any(), any(), any(), any())).thenThrow(new CustomException());

        orderRepository.checkOrderExists(orderExistsList);
    }
}
