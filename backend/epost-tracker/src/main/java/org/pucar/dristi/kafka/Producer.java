package org.pucar.dristi.kafka;


// NOTE: If tracer is disabled change CustomKafkaTemplate to KafkaTemplate in autowiring

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Producer {

    private final CustomKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public Producer(CustomKafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void push(String topic, Object value) {
        kafkaTemplate.send(topic, value);
    }
}
