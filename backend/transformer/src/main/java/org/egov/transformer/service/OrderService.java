package org.egov.transformer.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.transformer.config.ServiceConstants;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Order;
import org.egov.transformer.producer.OrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final CaseService caseService;
    private  final TransformerProperties properties;
    private  final OrderProducer producer;

    @Autowired
    public OrderService(CaseService caseService, TransformerProperties properties, OrderProducer producer) {
        this.caseService = caseService;
        this.properties = properties;
        this.producer = producer;
    }

    public void addOrderDetails(Order order){
        if (order.getFilingNumber() != null
                && (order.getOrderType().equalsIgnoreCase(ServiceConstants.BAIL_ORDER_TYPE)
                    || order.getOrderType().equalsIgnoreCase(ServiceConstants.JUDGEMENT_ORDER_TYPE))) {
                caseService.updateCase(order);
            }
        producer.push(properties.getOrderCreateTopic(), order);
        }

}
