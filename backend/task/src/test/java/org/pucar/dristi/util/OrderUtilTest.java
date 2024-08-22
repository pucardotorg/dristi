package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderExistsRequest;
import org.pucar.dristi.web.models.OrderExistsResponse;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderUtilTest {

    @InjectMocks
    private OrderUtil orderUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    private RequestInfo requestInfo;
    private UUID orderId;
    private String orderHost;
    private String orderPath;


    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        orderId = UUID.randomUUID();
        orderHost = "http://localhost:8080";
        orderPath = "/order/_exists";

        when(configs.getOrderHost()).thenReturn(orderHost);
        when(configs.getOrderPath()).thenReturn(orderPath);
    }

    @Test
    void testFetchOrderDetails_Returns_false() {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("exists", false);
        responseMap.put("order", Collections.singletonList(orderDetails));

        OrderExistsResponse orderExistsResponse = new OrderExistsResponse();

        when(restTemplate.postForObject(anyString(), any(OrderExistsRequest.class), eq(Map.class))).thenReturn(responseMap);
        when(mapper.convertValue(responseMap, OrderExistsResponse.class)).thenReturn(orderExistsResponse);

        Boolean exists = orderUtil.fetchOrderDetails(requestInfo, orderId);

        assertFalse(exists);

        verify(configs).getOrderHost();
        verify(configs).getOrderPath();
        verify(restTemplate).postForObject(anyString(), any(OrderExistsRequest.class), eq(Map.class));
        verify(mapper).convertValue(responseMap, OrderExistsResponse.class);
    }
    @Test
    void testFetchOrderDetailsSuccess()  {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("exists", true);
        responseMap.put("order", Collections.singletonList(orderDetails));

        OrderExistsResponse orderExistsResponse = new OrderExistsResponse();
        OrderExists orderExists = new OrderExists();
        orderExists.setExists(true);
        orderExistsResponse.setOrder(Collections.singletonList(orderExists));

        when(restTemplate.postForObject(anyString(), any(OrderExistsRequest.class), eq(Map.class))).thenReturn(responseMap);
        when(mapper.convertValue(responseMap, OrderExistsResponse.class)).thenReturn(orderExistsResponse);

        Boolean exists = orderUtil.fetchOrderDetails(requestInfo, orderId);

        assertTrue(exists);

        verify(configs).getOrderHost();
        verify(configs).getOrderPath();
        verify(restTemplate).postForObject(anyString(), any(OrderExistsRequest.class), eq(Map.class));
        verify(mapper).convertValue(responseMap, OrderExistsResponse.class);
    }
}
