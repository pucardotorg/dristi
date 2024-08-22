package org.pucar.dristi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.*;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.querybuilder.OrderQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.*;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
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

    private OrderCriteria criteria;
    private List<Order> orderList;
    private List<String> ids;

    @BeforeEach
    void setUp() {
        criteria = new OrderCriteria();
        Order order = new Order();
        order.setId(UUID.randomUUID());
        orderList = new ArrayList<>();
        orderList.add(order);

        ids = new ArrayList<>();
        ids.add(order.getId().toString());
    }

    @Test
    void testGetOrders_Success() {
        String orderQuery = "orderQuery";
        when(queryBuilder.getOrderSearchQuery(any(OrderCriteria.class), anyList(), anyList())).thenReturn(orderQuery);
        when(jdbcTemplate.query(anyString(),any(Object[].class),any(), any(OrderRowMapper.class)))
                .thenReturn(orderList);

        String countQuery = "SELECT COUNT(*) FROM orders";

        String statuteAndSectionQuery = "statuteAndSectionQuery";
        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), anyList(), anyList())).thenReturn(statuteAndSectionQuery);
        Map<UUID, StatuteSection> statuteSectionsMap = new HashMap<>();
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(orderQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList())).thenReturn(orderQuery);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(eq(countQuery),eq(Integer.class), any(Object[].class))).thenReturn(1);

        statuteSectionsMap.put(orderList.get(0).getId(), new StatuteSection());
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(),any(StatuteSectionRowMapper.class)))
                .thenReturn(Collections.singletonMap(orderList.get(0).getId(), new StatuteSection()));

        String documentQuery = "documentQuery";
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(), anyList())).thenReturn(documentQuery);
        Map<UUID, List<Document>> documentMap = new HashMap<>();
        documentMap.put(orderList.get(0).getId(), new ArrayList<>());
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(),any(DocumentRowMapper.class))).thenReturn(Collections.singletonMap(orderList.get(0).getId(), new ArrayList<>()));

        List<Order> result = orderRepository.getOrders(criteria, new Pagination());
        assertEquals(orderList, result);
        assertEquals(statuteSectionsMap.get(orderList.get(0).getId()), orderList.get(0).getStatuteSection());
        assertEquals(documentMap.get(orderList.get(0).getId()), orderList.get(0).getDocuments());
    }

    @Test
    void testGetOrders_CustomException() {
        when(queryBuilder.getOrderSearchQuery(any(OrderCriteria.class), anyList(), anyList())).thenThrow(new CustomException("ERROR", "Custom exception"));
        assertThrows(CustomException.class, this::invokeGetOrders);
    }

    @Test
    void testGetOrders_Exception() {
        when(queryBuilder.getOrderSearchQuery(any(OrderCriteria.class), anyList(), anyList())).thenThrow(new RuntimeException("Runtime exception"));
        CustomException exception = assertThrows(CustomException.class, this::invokeGetOrders);
        assertEquals("Error while fetching order list: Runtime exception", exception.getMessage());
    }

    private void invokeGetOrders() {
        orderRepository.getOrders(criteria, new Pagination());
    }

    @Test
    void testCheckOrderExists_Success() {
        List<OrderExists> orderExistsRequest = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderNumber("orderNumber");
        orderExistsRequest.add(orderExists);

        String orderExistQuery = "orderExistQuery";
        when(queryBuilder.checkOrderExistQuery(any(), any(), any(), any(), any(), anyList())).thenReturn(orderExistQuery);

         List<OrderExists> result = orderRepository.checkOrderExists(orderExistsRequest);
        assertEquals(1, result.size());
    }

    @Test
    void testCheckOrderExists_CustomException() {
        List<OrderExists> orderExistsRequest = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderNumber("orderNumber");
        orderExistsRequest.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(), any(), any(), anyList())).thenThrow(new CustomException("ERROR", "Custom exception"));
        assertThrows(CustomException.class, () -> orderRepository.checkOrderExists(orderExistsRequest));
    }

    @Test
    void testCheckOrderExists_Exception() {
        List<OrderExists> orderExistsRequest = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderNumber("orderNumber");
        orderExistsRequest.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(), any(), any(), anyList())).thenThrow(new RuntimeException("Runtime exception"));
        CustomException exception = assertThrows(CustomException.class, () -> orderRepository.checkOrderExists(orderExistsRequest));
        assertEquals("Custom exception while checking order exist : Runtime exception", exception.getMessage());
    }
}

