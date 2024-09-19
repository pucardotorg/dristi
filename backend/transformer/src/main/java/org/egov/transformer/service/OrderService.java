package org.egov.transformer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.egov.transformer.config.ServiceConstants;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Order;
import org.egov.transformer.models.OrderData;
import org.egov.transformer.models.OrderRequest;
import org.egov.transformer.models.Task;
import org.egov.transformer.producer.TransformerProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final ElasticSearchService elasticSearchService;
    private final ObjectMapper objectMapper;
    private final CaseService caseService;
    private final TransformerProperties properties;
    private final TransformerProducer producer;
    private final ApplicationService applicationService;


    @Autowired
    public OrderService(ElasticSearchService elasticSearchService, ObjectMapper objectMapper, CaseService caseService, TransformerProperties properties, TransformerProducer producer, ApplicationService applicationService) {
        this.elasticSearchService = elasticSearchService;
        this.objectMapper = objectMapper;
        this.caseService = caseService;
        this.properties = properties;
        this.producer = producer;
        this.applicationService = applicationService;
    }


    public Order fetchOrder(UUID orderId) throws IOException {
        LinkedHashMap<String, Object> sourceMap = elasticSearchService.getDocumentByField(ServiceConstants.ORDER_INDEX, ServiceConstants.ORDER_ID, String.valueOf(orderId));
        if (null == sourceMap || null == sourceMap.get("Data")) {
            logger.error("No order data found for {}", orderId);
            throw new CustomException("ORDER_SEARCH_EMPTY", ServiceConstants.ORDER_SEARCH_EMPTY);
        }

        OrderData data = objectMapper.convertValue(sourceMap.get("Data"), OrderData.class);
        return data.getOrderDetails();
    }

    public void updateOrder(Task task) {

        try {

            Order order = fetchOrder(task.getOrderId());
            order.setTaskDetails(task);

            addOrderDetails(order, properties.getUpdateOrderTopic());

        } catch (Exception e) {
            log.error("error executing order search query", e);
            throw new CustomException("ERROR_ORDER_SEARCH", ServiceConstants.ERROR_ORDER_SEARCH);
        }
    }


    private void addOrderDetailsToCase(Order order) {
        if (order.getFilingNumber() != null
                && (order.getOrderType().equalsIgnoreCase(ServiceConstants.BAIL_ORDER_TYPE)
                || order.getOrderType().equalsIgnoreCase(ServiceConstants.JUDGEMENT_ORDER_TYPE))) {
            caseService.updateCase(order);
        }
    }

    private void addOrderDetailsToApplication(Order order) {
        for (String applicationNumber : order.getApplicationNumber()) {
            applicationService.updateApplication(order, applicationNumber);
        }

    }

    public void addOrderDetails(Order order, String topic) {
        addOrderDetailsToCase(order);
        addOrderDetailsToApplication(order);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);
        logger.info("Order : {}", order);
        producer.push(topic, orderRequest);
    }
}
