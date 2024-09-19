package org.egov.transformer.producer;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransformerProducer {
    private final CustomKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TransformerProducer(CustomKafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void push(String topic, Object value) {
        this.kafkaTemplate.send(topic, value);
    }
}