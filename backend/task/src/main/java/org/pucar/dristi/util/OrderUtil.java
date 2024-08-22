package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderExistsRequest;
import org.pucar.dristi.web.models.OrderExistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class OrderUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final Configuration configs;

    @Autowired
    public OrderUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
    }

    public Boolean fetchOrderDetails(RequestInfo requestInfo, UUID orderId) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getOrderHost()).append(configs.getOrderPath());

        OrderExistsRequest orderExistsRequest = new OrderExistsRequest();
        orderExistsRequest.setRequestInfo(requestInfo);
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderId(orderId);
        List<OrderExists> criteriaList = new ArrayList<>();
        criteriaList.add(orderExists);
        orderExistsRequest.setOrder(criteriaList);

        Object response = new HashMap<>();
        OrderExistsResponse orderExistsResponse = new OrderExistsResponse();
        try {
            response = restTemplate.postForObject(uri.toString(), orderExistsRequest, Map.class);
            orderExistsResponse = mapper.convertValue(response, OrderExistsResponse.class);
        } catch (Exception e) {
            log.error("ERROR_WHILE_FETCHING_FROM_ORDER:: {}", e.toString());
        }

        if(orderExistsResponse.getOrder() == null|| orderExistsResponse.getOrder().isEmpty())
            return false;

        return orderExistsResponse.getOrder().get(0).getExists();
    }
}