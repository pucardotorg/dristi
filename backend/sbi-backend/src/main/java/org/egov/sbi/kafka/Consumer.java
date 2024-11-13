package org.egov.sbi.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.sbi.model.TransactionRequest;
import org.egov.sbi.service.PaymentService;
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

    @KafkaListener(topics = "update-sbi-transaction-details")
    @Async
    public void listenForGenerateSummonsDocument(final Map<String, Object> recordMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            TransactionRequest request = objectMapper.convertValue(recordMap, TransactionRequest.class);
            log.info("received paylod",request);
            paymentService.callCollectionServiceAndUpdatePayment(request);
        } catch (final Exception e) {
            log.error("Error while listening to value: {}: ", recordMap, e);
        }
    }
}
