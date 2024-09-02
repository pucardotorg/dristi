package org.pucar.dristi.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.model.EPostRequest;
import org.pucar.dristi.util.SummonsUtil;
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

    private final SummonsUtil summonsUtil;


    @Autowired
    public Consumer(ObjectMapper objectMapper, SummonsUtil summonsUtil) {
        this.objectMapper = objectMapper;
        this.summonsUtil = summonsUtil;
    }

    @KafkaListener(topics = "update-epost-tracker")
    @Async
    public void listenForGenerateSummonsDocument(final Map<String, Object> recordMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            EPostRequest ePostRequest = objectMapper.convertValue(recordMap, EPostRequest.class);
            log.info("received paylod : {}: ",ePostRequest);
            summonsUtil.updateSummonsDeliveryStatus(ePostRequest);
        } catch (final Exception e) {
            log.error("Error while listening to value: {}: ", recordMap, e);
        }
    }
}
