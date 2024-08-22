package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_ORDER;

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

    public Boolean fetchOrderDetails(OrderExistsRequest orderExistsRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getOrderHost()).append(configs.getOrderExistsPath());

        Object response = new HashMap<>();
        OrderExistsResponse orderExistsResponse = new OrderExistsResponse();
        try {
            response = restTemplate.postForObject(uri.toString(), orderExistsRequest, Map.class);
            orderExistsResponse = mapper.convertValue(response, OrderExistsResponse.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_ORDER, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_ORDER, e.getMessage());

        }
        return orderExistsResponse.getOrder().get(0).getExists();
    }
}