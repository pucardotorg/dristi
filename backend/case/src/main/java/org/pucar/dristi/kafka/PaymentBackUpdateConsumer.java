package org.pucar.dristi.kafka;

import org.pucar.dristi.service.PaymentUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PaymentBackUpdateConsumer {

    public static final Logger logger = LoggerFactory.getLogger(PaymentBackUpdateConsumer.class);

    @Autowired
    private PaymentUpdateService paymentUpdateService;

    @KafkaListener(topics = {"${kafka.topics.receipt.create}"})
    public void listenPayments(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            logger.info("Received record: {} on topic: {}", record, topic);
            paymentUpdateService.process(record);
        } catch (final Exception e) {
            logger.error("Error while listening to value: {} on topic: {}: ", record, topic, e);
        }
    }
}
