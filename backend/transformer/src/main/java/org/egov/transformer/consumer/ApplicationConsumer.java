package org.egov.transformer.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Application;
import org.egov.transformer.models.ApplicationRequest;
import org.egov.transformer.producer.TransformerProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConsumer.class);

    private final ObjectMapper objectMapper;
    private final TransformerProducer producer;
    private final TransformerProperties transformerProperties;

    @Autowired
    public ApplicationConsumer(ObjectMapper objectMapper, TransformerProducer producer, TransformerProperties transformerProperties) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.transformerProperties = transformerProperties;
    }

    @KafkaListener(topics = {"${transformer.consumer.create.application.topic}"})
    public void saveApplication(ConsumerRecord<String, Object> payload,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishApplication(payload, transformerProperties.getSaveApplicationTopic());
    }

    @KafkaListener(topics = {"${transformer.consumer.update.application.topic}"})
    public void updateApplication(ConsumerRecord<String, Object> payload,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishApplication(payload, transformerProperties.getUpdateApplicationTopic());
    }

    @KafkaListener(topics = {"${transformer.consumer.application.status.update.topic}"})
    public void updateApplicationStatus(ConsumerRecord<String, Object> payload,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishApplication(payload, transformerProperties.getUpdateApplicationTopic());
    }

    @KafkaListener(topics = {"${transformer.consumer.application.comments.update.topic}"})
    public void updateApplicationComments(ConsumerRecord<String, Object> payload,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        publishApplication(payload, transformerProperties.getUpdateApplicationTopic());
    }

    private void publishApplication(ConsumerRecord<String, Object> payload,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Application application = (objectMapper.readValue((String) payload.value(), new TypeReference<ApplicationRequest>() {
            })).getApplication();
            logger.info(objectMapper.writeValueAsString(application));
            ApplicationRequest applicationRequest = new ApplicationRequest();
            applicationRequest.setApplication(application);
            producer.push(topic, applicationRequest);
        } catch (Exception exception) {
            logger.error("error in saving application", exception);
        }
    }
}
