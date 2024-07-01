package org.pucar.dristi.repository;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.pucar.dristi.repository.querybuilder.OrderQueryBuilder;
import org.pucar.dristi.repository.rowmapper.OrderRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ORDER_SEARCH_EXCEPTION;

@ExtendWith(SpringExtension.class)
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
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrders_success() {
        String applicationNumber = "appNum";
        String cnrNumber = "cnrNum";
        String filingNumber = "filingNum";
        String tenantId = "tenant";
        String id = "id";
        String status = "status";
        String orderNumber = "ORDER-123";

        List<Order> mockOrderList = new ArrayList<>();
        Order mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrderList.add(mockOrder);

        when(queryBuilder.getOrderSearchQuery( anyString(),anyString(), anyString(), anyString(), anyString(), anyString(),anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenReturn(mockOrderList);

        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), anyList()))
                .thenReturn("statuteSectionQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(StatuteSectionRowMapper.class)))
                .thenReturn(Collections.singletonMap(mockOrder.getId(), new StatuteSection()));

        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("documentQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(DocumentRowMapper.class)))
                .thenReturn(Collections.singletonMap(mockOrder.getId(), new ArrayList<>()));

        List<Order> result = orderRepository.getOrders(orderNumber,applicationNumber,cnrNumber, filingNumber, tenantId, id, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getOrderSearchQuery(orderNumber,applicationNumber,cnrNumber, filingNumber, tenantId, id, status);
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
        verify(queryBuilder, times(1)).getStatuteSectionSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), eq(statuteSectionRowMapper));
        verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), eq(documentRowMapper));
    }

    @Test
    public void testGetOrders_emptyResult() {
        when(queryBuilder.getOrderSearchQuery(anyString(), anyString(),anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
         when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class))).thenAnswer(invocation -> {throw new CustomException("EMPTY_RESULT", "No orders found");});

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRepository.getOrders("order-no","appNum","cnrNum", "filingNum", "tenant", "id", "status"));

        assertEquals("EMPTY_RESULT", exception.getCode());
        verify(queryBuilder, times(1)).getOrderSearchQuery( anyString(),anyString(),anyString(),anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
        verify(queryBuilder, never()).getStatuteSectionSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, never()).query(anyString(), any(Object[].class), eq(statuteSectionRowMapper));
        verify(queryBuilder, never()).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, never()).query(anyString(), any(Object[].class), eq(documentRowMapper));
    }

    @Test
    public void testGetOrders_customException() {
        when(queryBuilder.getOrderSearchQuery(anyString(),anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenThrow(new CustomException("TEST_EXCEPTION", "Test exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRepository.getOrders("order-no","appNum","cnrNum", "filingNum", "tenant", "id", "status"));

        assertEquals("TEST_EXCEPTION", exception.getCode());
        assertEquals("Test exception", exception.getMessage());
        verify(queryBuilder, times(1)).getOrderSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
    }

    @Test
    public void testGetOrders_genericException() {
        when(queryBuilder.getOrderSearchQuery( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenThrow(new RuntimeException("Test runtime exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRepository.getOrders("order-no","appNum","cnrNum", "filingNum", "tenant", "id", "status"));

        assertEquals(ORDER_SEARCH_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Error while fetching order list: Test runtime exception"));
        verify(queryBuilder, times(1)).getOrderSearchQuery(anyString(),anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
    }

    @Test
    void testCheckOrderExists_AllEmpty() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        OrderExistsList.add(orderExists);

        List<OrderExists> result = orderRepository.checkOrderExists(OrderExistsList);

        assertFalse(result.get(0).getExists());
    }
    @Test
    void testCheckOrderExists_FilingNumber() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setFilingNumber("123");
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE filingnumber = '123'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<OrderExists> result = orderRepository.checkOrderExists(OrderExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckOrderExists_CnrNumber() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setCnrNumber("456");
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE cnrnumber = '456'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<OrderExists> result = orderRepository.checkOrderExists(OrderExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckOrderExists_OrderNumber() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderNumber("111");
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE ordernumber = '111'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<OrderExists> result = orderRepository.checkOrderExists(OrderExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckOrderExists_Id() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        UUID id = UUID.randomUUID();
        orderExists.setOrderId(id);
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE id = '"+id+"'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<OrderExists> result = orderRepository.checkOrderExists(OrderExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckOrderExists_Exception() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setCnrNumber("456");
        orderExists.setFilingNumber("123");
        orderExists.setApplicationNumber("789");
        orderExists.setOrderNumber("111");
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE filingnumber = '123' AND cnrnumber = '456' AND applicationnumber::text LIKE '%789%'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(CustomException.class, () -> orderRepository.checkOrderExists(OrderExistsList));
    }

    @Test
    void testCheckOrderExists_Throws_CustomException() {
        List<OrderExists> OrderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setCnrNumber("456");
        orderExists.setFilingNumber("123");
        orderExists.setApplicationNumber("789");
        orderExists.setOrderNumber("111");
        OrderExistsList.add(orderExists);

        when(queryBuilder.checkOrderExistQuery(any(), any(), any(),any(),any())).thenReturn("SELECT COUNT(*) FROM orders WHERE filingnumber = '123' AND cnrnumber = '456' AND applicationnumber::text LIKE '%789%'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenThrow(new CustomException("ORDER_EXIST_EXCEPTION", "Error occurred while building the application exist query : " ));

        assertThrows(CustomException.class, () -> orderRepository.checkOrderExists(OrderExistsList));
    }
}
