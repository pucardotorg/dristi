package org.pucar.dristi.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.service.PendingTaskService;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

@Slf4j
public class PendingTaskUpdateConsumer {

    private final PendingTaskService pendingTaskService;
    private final ObjectMapper objectMapper;

    public PendingTaskUpdateConsumer(PendingTaskService pendingTaskService, ObjectMapper objectMapper) {
        this.pendingTaskService = pendingTaskService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = {"${kafka.topics.representative.join.case}", "${kafka.topics.litigant.join.case}"})
    public void listener(ConsumerRecord<String, String> consumerRecord) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(consumerRecord.value(), Map.class);
            pendingTaskService.updatePendingTask(consumerRecord.topic(), jsonMap);
        } catch (Exception e){
            log.error("Error in updating PendingTask for join case.");
        }
    }

}
