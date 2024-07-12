package org.pucar.dristi.web.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.*;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
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
    private FileDownloadService fileDownloadService;

    @Autowired
    private PDFSummonsOrderService pdfSummonsOrderService;


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
            Order order = orderService.createOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<OrderExistsResponse> orderV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the order(S) exists", required = true, schema = @Schema()) @Valid @RequestBody OrderExistsRequest body) {
            List<OrderExists> order = orderService.existsOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderExistsResponse orderExistsResponse = OrderExistsResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/v1/search", method = RequestMethod.POST)
    public ResponseEntity<OrderListResponse> orderV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, required=true, schema=@Schema()) @Valid @RequestBody OrderSearchRequest request) {
            List<Order> orders = orderService.searchOrder(request);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderListResponse orderListResponse = OrderListResponse.builder().list(orders).totalCount(orders.size()).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/v1/update", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> orderV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update order(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody OrderRequest body) {
            Order order = orderService.updateOrder(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true, HttpStatus.OK.getReasonPhrase());
            OrderResponse orderResponse = OrderResponse.builder().order(order).responseInfo(responseInfo).build();
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @PostMapping("/process-file")
    public ResponseEntity<String> processFile() throws Exception {
            String response = fileDownloadService.downloadAndExtractSignature();
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getpdf", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource>getPdf() throws IOException {
        return pdfSummonsOrderService.getPdfService("33ed5611-6c08-4d43-a78f-4eb7ad486a7f","summon");
    }
}

