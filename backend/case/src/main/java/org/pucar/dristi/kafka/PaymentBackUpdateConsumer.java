package org.pucar.dristi.kafka;

import java.util.Map;

import org.pucar.dristi.service.PaymentUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PaymentBackUpdateConsumer {

    public static final Logger logger = LoggerFactory.getLogger(PaymentBackUpdateConsumer.class);

    private PaymentUpdateService paymentUpdateService;

    @Autowired
    public PaymentBackUpdateConsumer(PaymentUpdateService paymentUpdateService) {
        this.paymentUpdateService = paymentUpdateService;
    }

    @KafkaListener(topics = {"${kafka.topics.receipt.create}"})
    public void listenPayments(final Map<String, Object> data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            logger.info("Received record: {} on topic: {}", data, topic);
            paymentUpdateService.process(data);
        } catch (final Exception e) {
            logger.error("Error while listening to value: {} on topic: {}: ", data, topic, e);
        }
    }
}
