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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderApiControllerTest {
    
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
    public void testAdvocateV1CreatePost_Success() {
        // Mock AdvocateService response
        Order expectedAdvocates =new Order();
        when(orderRegistrationService.createOrder(any(OrderRequest.class)))
                .thenReturn(expectedAdvocates);

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
        assertEquals(expectedAdvocates, actualResponse.getOrder());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testAdvocateV1SearchPost_Success() {
        // Mock AdvocateService response
        List<Order> expectedAdvocates = Collections.singletonList(new Order());
        when(orderRegistrationService.searchOrder(any()))
                .thenReturn(expectedAdvocates);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class),anyString()))
                .thenReturn(expectedResponseInfo);
        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setCriteria(new OrderCriteria());
        orderSearchRequest.setRequestInfo(new RequestInfo());
        // Perform POST request
        ResponseEntity<OrderListResponse> response = controller.orderV1SearchPost(orderSearchRequest);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testAdvocateV1UpdatePost_Success() throws Exception {
        // Mock AdvocateService response
        when(orderRegistrationService.updateOrder(any(OrderRequest.class)))
                .thenReturn(new Order());

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class),anyString()))
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
    public void testAdvocateV1CreatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        OrderRequest requestBody = new OrderRequest();  // Missing required fields

        // Expected validation error
        when(orderRegistrationService.createOrder(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try{
            controller.orderV1CreatePost(requestBody);
        }
        catch (Exception e){
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }
    @Test
    public void testAdvocateV1CreatePost_EmptyList() throws Exception {
        // Mock service to return empty list
        when(orderRegistrationService.createOrder(any(OrderRequest.class))).thenReturn(new Order());

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class),anyString()))
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
    public void testAdvocateV1SearchPost_InvalidRequest() throws Exception {
        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setCriteria(new OrderCriteria());
        // Expected validation error
        when(orderRegistrationService.searchOrder(any())).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
             controller.orderV1SearchPost(orderSearchRequest);
        }
        catch (Exception e){
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    public void testAdvocateV1SearchPost_EmptyList() throws Exception {
        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setCriteria(new OrderCriteria());
        orderSearchRequest.setRequestInfo(new RequestInfo());

        // Mock service to return empty list
        List<Order> emptyList = Collections.emptyList();
        when(orderRegistrationService.searchOrder(any())).thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class),anyString()))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<OrderListResponse> response = controller.orderV1SearchPost(orderSearchRequest);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
    }

    @Test
    public void testAdvocateV1UpdatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        OrderRequest requestBody = new OrderRequest();  // Missing required fields

        // Expected validation error
        when(orderRegistrationService.updateOrder(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.orderV1UpdatePost(requestBody);
        }
        catch (Exception e){
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

}
