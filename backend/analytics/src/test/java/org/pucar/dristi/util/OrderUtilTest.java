package org.pucar.dristi.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ORDER_PATH;

@ExtendWith(MockitoExtension.class)
class OrderUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @InjectMocks
    private OrderUtil orderUtil;

    private JSONObject request;
    private String orderNumber;
    private String tenantId;

    @BeforeEach
    void setUp() {
        request = new JSONObject();
        orderNumber = "order-123";
        tenantId = "tenant-123";

        when(config.getOrderHost()).thenReturn("http://localhost");
        when(config.getOrderSearchPath()).thenReturn("/order/search");
    }

    @Test
    void testGetOrder_Success() throws Exception {

        String mockResponse = "{\"orders\": [{\"id\": \"order-123\"}]}";
        JSONArray mockOrderArray = new JSONArray();
        mockOrderArray.put(new JSONObject().put("id", "order-123"));

        when(repository.fetchResult(any(), any(JSONObject.class))).thenReturn(mockResponse);
        when(util.constructArray(mockResponse, ORDER_PATH)).thenReturn(mockOrderArray);

        Object result = orderUtil.getOrder(request, orderNumber, tenantId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("order-123", ((JSONObject) result).getString("id"));

        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(1)).constructArray(mockResponse, ORDER_PATH);
    }

    @Test
    void testGetOrder_Exception() throws Exception {

        when(repository.fetchResult(any(), any(JSONObject.class))).thenThrow(new RuntimeException("Fetch error"));

        Exception exception = assertThrows(RuntimeException.class, () -> orderUtil.getOrder(request, orderNumber, tenantId));

        assertEquals("Fetch error", exception.getCause().getMessage());

        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(0)).constructArray(anyString(), anyString());
    }
}
