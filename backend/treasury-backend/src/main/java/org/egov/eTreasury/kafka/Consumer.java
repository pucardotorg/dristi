package org.egov.eTreasury.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.model.TreasuryPaymentRequest;
import org.egov.eTreasury.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@EnableAsync
public class Consumer {

    private final ObjectMapper objectMapper;

    private final PaymentService paymentService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, PaymentService paymentService) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "save-treasury-payment-data")
    @Async
    public void listenForGenerateSummonsDocument(final Map<String, Object> recordMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            TreasuryPaymentRequest request = objectMapper.convertValue(recordMap, TreasuryPaymentRequest.class);
            log.info("In eTreasury module received payment record {} from topic {} : ",recordMap,topic);
            paymentService.callCollectionServiceAndUpdatePayment(request);
        } catch (final Exception e) {
            log.error("Error while listening to value: {}: ", recordMap, e);
        }
    }
}
