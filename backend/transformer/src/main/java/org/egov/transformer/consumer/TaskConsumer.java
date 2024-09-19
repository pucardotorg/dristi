package org.egov.transformer.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Task;
import org.egov.transformer.models.TaskRequest;
import org.egov.transformer.service.OrderService;
import org.egov.transformer.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private final ObjectMapper objectMapper;
    private final TaskService taskService;
    private final TransformerProperties transformerProperties;

    @Autowired
    public TaskConsumer(ObjectMapper objectMapper, OrderService orderService, TaskService taskService, TransformerProperties transformerProperties) {
        this.objectMapper = objectMapper;
        this.taskService = taskService;
        this.transformerProperties = transformerProperties;

    }

    @KafkaListener(topics = {"${transformer.consumer.create.task.topic}", "${transformer.consumer.update.task.topic}"})
    public void saveTask(ConsumerRecord<String, Object> payload,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishTask(payload, transformerProperties.getSaveTaskTopic());
    }

    @KafkaListener(topics = {"${transformer.consumer.create.task.topic}", "${transformer.consumer.update.task.topic}"})
    public void updateTask(ConsumerRecord<String, Object> payload,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishTask(payload, transformerProperties.getUpdateTaskTopic());
    }

    private void publishTask(ConsumerRecord<String, Object> payload,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Task task = (objectMapper.readValue((String) payload.value(), new TypeReference<TaskRequest>() {
            })).getTask();
            logger.info(objectMapper.writeValueAsString(task));
            taskService.addTaskDetails(task, topic);
        } catch (Exception exception) {
            log.error("error in saving task", exception);
        }
    }

}
