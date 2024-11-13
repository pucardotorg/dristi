package com.egov.icops_integrationkerala.kafka;

import com.egov.icops_integrationkerala.model.IcopsRequest;
import com.egov.icops_integrationkerala.util.SummonsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

    @KafkaListener(topics = "update-icops-tracker")
    @Async
    public void listenForGenerateSummonsDocument(final Map<String, Object> recordMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            IcopsRequest icopsRequest = objectMapper.convertValue(recordMap, IcopsRequest.class);
            log.info("received paylod",icopsRequest);
            summonsUtil.updateSummonsDeliveryStatus(icopsRequest);
        } catch (final Exception e) {
            log.error("Error while listening to value: {}: ", recordMap, e);
        }
    }
}
