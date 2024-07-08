package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.OrderRegistrationService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderApiControllerTest {

    @InjectMocks
    private OrderApiController controller;

    @Mock
    private OrderRegistrationService orderRegistrationService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    void setUp() {
        controller.setMockInjects(orderRegistrationService, responseInfoFactory);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testOrderV1CreatePost_Success() {
        // Mock OrderService response
        Order expectedOrders = new Order();
        when(orderRegistrationService.createOrder(any(OrderRequest.class)))
                .thenReturn(expectedOrders);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Create mock OrderRequest
        OrderRequest requestBody = new OrderRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<OrderResponse> response = controller.orderV1CreatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedOrders, actualResponse.getOrder());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testOrderV1Exist_Success() {
        // Mock OrderService response
        OrderExists orderExists = new OrderExists();
        List<OrderExists> orders = new ArrayList<>();
        orders.add(orderExists);
        when(orderRegistrationService.existsOrder(any(OrderExistsRequest.class)))
                .thenReturn(orders);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Create mock OrderRequest
        OrderExistsRequest requestBody = new OrderExistsRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<OrderExistsResponse> response = controller.orderV1ExistsPost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderExistsResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(orderExists, actualResponse.getOrder().get(0));
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testOrderV1SearchPost_Success() {

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setCriteria(new OrderCriteria());
        orderSearchRequest.setRequestInfo(new RequestInfo());

        // Mock OrderService response
        List<Order> expectedOrders = Collections.singletonList(new Order());
        when(orderRegistrationService.searchOrder(any()))
                .thenReturn(expectedOrders);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<OrderListResponse> response = controller.orderV1SearchPost(orderSearchRequest);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testOrderV1UpdatePost_Success() throws Exception {
        // Mock OrderService response
        when(orderRegistrationService.updateOrder(any(OrderRequest.class)))
                .thenReturn(new Order());

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Create mock OrderRequest
        OrderRequest requestBody = new OrderRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<OrderResponse> response = controller.orderV1UpdatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testOrderV1CreatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        OrderRequest requestBody = new OrderRequest();  // Missing required fields

        // Expected validation error
        when(orderRegistrationService.createOrder(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.orderV1CreatePost(requestBody);
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    void testOrderV1CreatePost_EmptyList() throws Exception {
        // Mock service to return empty list
        when(orderRegistrationService.createOrder(any(OrderRequest.class))).thenReturn(new Order());

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        OrderRequest requestBody = new OrderRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<OrderResponse> response = controller.orderV1CreatePost(requestBody);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
    }

    @Test
    void testOrderV1SearchPost_InvalidRequest() throws Exception {

        // Expected validation error
        when(orderRegistrationService.searchOrder(any())).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.orderV1SearchPost(new OrderSearchRequest());
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    void testOrderV1SearchPost_EmptyList() throws Exception {
        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setCriteria(new OrderCriteria());
        orderSearchRequest.setRequestInfo(new RequestInfo());

        // Mock service to return empty list
        List<Order> emptyList = Collections.emptyList();
        when(orderRegistrationService.searchOrder(any())).thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class), anyString()))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<OrderListResponse> response = controller.orderV1SearchPost(orderSearchRequest);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
    }

    @Test
    void testOrderV1UpdatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        OrderRequest requestBody = new OrderRequest();  // Missing required fields

        // Expected validation error
        when(orderRegistrationService.updateOrder(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.orderV1UpdatePost(requestBody);
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

}
