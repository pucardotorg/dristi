package org.pucar.dristi.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @RequestMapping(value = "/order/v1/create", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> orderV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new order + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody OrderRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                Order order = orderService.createOrder(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
                return new ResponseEntity<>(orderResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<OrderResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<OrderResponse>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/order/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<OrderExistsResponse> orderV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the order(S) exists", required = true, schema = @Schema()) @Valid @RequestBody OrderExistsRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<OrderExistsResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"orderList\" : [ [ {    \"filingNumber\" : \"filingNumber\",    \"orderNumber\" : \"orderNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  }, {    \"filingNumber\" : \"filingNumber\",    \"orderNumber\" : \"orderNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  } ], [ {    \"filingNumber\" : \"filingNumber\",    \"orderNumber\" : \"orderNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  }, {    \"filingNumber\" : \"filingNumber\",    \"orderNumber\" : \"orderNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  } ] ]}", OrderExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<OrderExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<OrderExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @param applicationNumber
     * @param filingNumber
     * @param cnrNumber
     * @param id
     * @param tenantId
     * @param status
     * @return
     */
    @RequestMapping(value = "/order/v1/search", method = RequestMethod.POST)
    public ResponseEntity<OrderListResponse> orderV1SearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the aapplicationNumber whose order(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "applicationNumber", required = true) UUID applicationNumber, @NotNull @Parameter(in = ParameterIn.QUERY, description = "the filingNumber of the case whose order(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "filingNumber", required = true) String filingNumber, @NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose order(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber, @Parameter(in = ParameterIn.QUERY, description = "id of the order being searched", schema = @Schema()) @Valid @RequestParam(value = "id", required = false) String id, @Parameter(in = ParameterIn.QUERY, description = "tenantId whose order(s) are being searched", schema = @Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId, @Parameter(in = ParameterIn.QUERY, description = "the status of the order(s) being searched", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                List<Order> orders = orderService.searchOrder(String.valueOf(applicationNumber),cnrNumber,filingNumber, tenantId, id, status);
             //   ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                OrderListResponse orderListResponse = OrderListResponse.builder().totalCount(orders.size()).responseInfo(null).build();
                return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<OrderListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<OrderListResponse>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/order/v1/update", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> orderV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update order(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody OrderRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                Order order = orderService.updateOrder(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
                return new ResponseEntity<>(orderResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<OrderResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<OrderResponse>(HttpStatus.BAD_REQUEST);

    }
}
