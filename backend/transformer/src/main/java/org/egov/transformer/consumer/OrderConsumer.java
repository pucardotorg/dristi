package org.egov.transformer.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.transformer.models.Order;
import org.egov.transformer.models.OrderRequest;
import org.egov.transformer.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @Autowired
    public OrderConsumer(ObjectMapper objectMapper, OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @KafkaListener(topics = { "${transformer.consumer.create.order.topic}","${transformer.consumer.update.order.topic}"})
    public void orderConsumerCreate(ConsumerRecord<String, Object> payload,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
    try {
        Order order = (objectMapper.readValue((String) payload.value(), new TypeReference<OrderRequest>() {})).getOrder();
        logger.info(objectMapper.writeValueAsString(order));
        orderService.addOrderDetails (order);
        } catch (Exception exception) {
            log.error("error in saving order", exception);
        }
    }
}
