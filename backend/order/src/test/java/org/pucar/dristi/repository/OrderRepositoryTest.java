package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
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
    public void testGetApplications_success() {
        String applicationNumber = "appNum";
        String cnrNumber = "cnrNum";
        String filingNumber = "filingNum";
        String tenantId = "tenant";
        String id = "id";
        String status = "status";

        List<Order> mockOrderList = new ArrayList<>();
        Order mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrderList.add(mockOrder);

        when(queryBuilder.getOrderSearchQuery( anyString(),anyString(), anyString(), anyString(), anyString(), anyString()))
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

        List<Order> result = orderRepository.getApplications(applicationNumber,cnrNumber, filingNumber, tenantId, id, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getOrderSearchQuery(applicationNumber,cnrNumber, filingNumber, tenantId, id, status);
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
        verify(queryBuilder, times(1)).getStatuteSectionSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), eq(statuteSectionRowMapper));
        verify(queryBuilder, times(1)).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), eq(documentRowMapper));
    }

    @Test
    public void testGetApplications_emptyResult() {
        when(queryBuilder.getOrderSearchQuery( anyString(),anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenReturn(Collections.emptyList());

        List<Order> result = orderRepository.getApplications( "appNum","cnrNum", "filingNum", "tenant", "id", "status");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(queryBuilder, times(1)).getOrderSearchQuery( anyString(),anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
        verify(queryBuilder, never()).getStatuteSectionSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, never()).query(anyString(), any(Object[].class), eq(statuteSectionRowMapper));
        verify(queryBuilder, never()).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate, never()).query(anyString(), any(Object[].class), eq(documentRowMapper));
    }

    @Test
    public void testGetApplications_customException() {
        when(queryBuilder.getOrderSearchQuery(anyString(),anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenThrow(new CustomException("TEST_EXCEPTION", "Test exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRepository.getApplications("appNum","cnrNum", "filingNum", "tenant", "id", "status"));

        assertEquals("TEST_EXCEPTION", exception.getCode());
        assertEquals("Test exception", exception.getMessage());
        verify(queryBuilder, times(1)).getOrderSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
    }

    @Test
    public void testGetApplications_genericException() {
        when(queryBuilder.getOrderSearchQuery( anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("orderQuery");
        when(jdbcTemplate.query(anyString(), any(OrderRowMapper.class)))
                .thenThrow(new RuntimeException("Test runtime exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRepository.getApplications("appNum","cnrNum", "filingNum", "tenant", "id", "status"));

        assertEquals(ORDER_SEARCH_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Error while fetching order list: Test runtime exception"));
        verify(queryBuilder, times(1)).getOrderSearchQuery(anyString(),anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query("orderQuery", rowMapper);
    }
}
