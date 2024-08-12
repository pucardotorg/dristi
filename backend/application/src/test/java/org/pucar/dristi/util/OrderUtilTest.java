package org.pucar.dristi.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderExistsRequest;
import org.pucar.dristi.web.models.OrderExistsResponse;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class OrderUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private OrderUtil orderUtil;

    @BeforeEach
    void setUp() {
        when(configs.getOrderHost()).thenReturn("http://localhost:8080");
        when(configs.getOrderExistsPath()).thenReturn("/orderExists");
    }

    @Test
    void testFetchOrderDetailsSuccess() {
        OrderExistsRequest request = new OrderExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", true)));

        OrderExistsResponse orderExistsResponse = OrderExistsResponse.builder()
                .order(List.of(OrderExists.builder().exists(true).build()))
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, OrderExistsResponse.class))
                .thenReturn(orderExistsResponse);

        Boolean result = orderUtil.fetchOrderDetails(request);
        assertTrue(result);
    }

    @Test
    void testFetchOrderDetailsDoesNotExist() {
        OrderExistsRequest request = new OrderExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", false)));

        OrderExistsResponse orderExistsResponse = OrderExistsResponse.builder()
                .order(List.of(OrderExists.builder().exists(false).build()))
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, OrderExistsResponse.class))
                .thenReturn(orderExistsResponse);

        Boolean result = orderUtil.fetchOrderDetails(request);
        assertFalse(result);
    }

    @Test
    void testFetchOrderDetailsException() {
        OrderExistsRequest request = new OrderExistsRequest();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> {
            orderUtil.fetchOrderDetails(request);
        });
    }
}
