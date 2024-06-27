package org.pucar.dristi.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.OrderRegistrationService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:13:43.389623100+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class OrderApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private OrderRegistrationService orderService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public OrderApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public void setMockInjects(OrderRegistrationService orderService, ResponseInfoFactory responseInfoFactory){
        this.orderService = orderService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "/order/v1/create", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> orderV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new order + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody OrderRequest body) {
        try {
            Order order = orderService.createOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), false, e.getMessage());
            OrderResponse orderResponse = OrderResponse.builder().order(null).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<OrderExistsResponse> orderV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the order(S) exists", required = true, schema = @Schema()) @Valid @RequestBody OrderExistsRequest body) {
        try {
            List<OrderExists> order = orderService.existsOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderExistsResponse orderExistsResponse = OrderExistsResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderExistsResponse, HttpStatus.OK);
        } catch (Exception e) {
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), false, e.getMessage());
            OrderExistsResponse orderListResponse = OrderExistsResponse.builder().order(null).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/v1/search", method = RequestMethod.POST)
    public ResponseEntity<OrderListResponse> orderV1SearchPost(@Parameter(in = ParameterIn.QUERY, description = "the applicationNumber whose order(s) are being queried", schema = @Schema()) @Valid @RequestParam(value = "applicationNumber", required = false) String applicationNumber,
                                                               @Parameter(in = ParameterIn.QUERY, description = "the filingNumber of the case whose order(s) are being queried", schema = @Schema()) @Valid @RequestParam(value = "filingNumber", required = false) String filingNumber,
                                                               @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose order(s) are being queried", schema = @Schema()) @Valid @RequestParam(value = "cnrNumber", required = false) String cnrNumber,
                                                               @Parameter(in = ParameterIn.QUERY, description = "id of the order being searched", schema = @Schema()) @Valid @RequestParam(value = "id", required = false) String id,
                                                               @Parameter(in = ParameterIn.QUERY, description = "tenantId whose order(s) are being searched", schema = @Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,
                                                               @Parameter(in = ParameterIn.QUERY, description = "the status of the order(s) being searched", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status,
                                                               @Parameter(in = ParameterIn.QUERY, description = "the orderNumber of the order(s) being searched", schema = @Schema()) @Valid @RequestParam(value = "orderNumber", required = false) String orderNumber,
                                                               @Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
        try {
            List<Order> orders = orderService.searchOrder(orderNumber,applicationNumber, cnrNumber, filingNumber, tenantId, id, status, requestInfoWrapper.getRequestInfo());
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderListResponse orderListResponse = OrderListResponse.builder().list(orders).totalCount(orders.size()).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
        } catch (Exception e) {
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), false, e.getMessage());
            OrderListResponse orderListResponse = OrderListResponse.builder().list(null).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/v1/update", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> orderV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update order(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody OrderRequest body) {
        try {
            Order order = orderService.updateOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), false, e.getMessage());
            OrderResponse orderResponse = OrderResponse.builder().order(null).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

