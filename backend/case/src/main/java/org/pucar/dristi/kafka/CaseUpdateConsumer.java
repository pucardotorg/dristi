package org.pucar.dristi.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.PaymentUpdateService;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.analytics.CaseOutcome;
import org.pucar.dristi.web.models.analytics.CaseOverallStatus;
import org.pucar.dristi.web.models.analytics.CaseStageSubStage;
import org.pucar.dristi.web.models.analytics.Outcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CaseUpdateConsumer {

    public static final Logger logger = LoggerFactory.getLogger(CaseUpdateConsumer.class);

    private CaseService caseService;

    private final ObjectMapper objectMapper;

    @Autowired
    public CaseUpdateConsumer(CaseService caseService, ObjectMapper objectMapper) {
        this.caseService = caseService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${egov.case.overall.status.topic}"})
    public void updateCaseOverallStatus(ConsumerRecord<String, Object> payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            logger.info("Received case overall status record on topic: {}", topic);
            CaseStageSubStage caseStageSubStage = objectMapper.convertValue(payload.value(), CaseStageSubStage.class);
            caseService.updateCaseOverallStatus(caseStageSubStage);
        } catch (final Exception e) {
            logger.error("Error while listening to case overall status on topic: {}: ", topic, e);
        }
    }

    @KafkaListener(topics = {"${egov.case.outcome.topic}"})
    public void updateCaseOutcome(ConsumerRecord<String, Object> payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            logger.info("Received case outcome record on topic: {}", topic);
            CaseOutcome caseOutcome = objectMapper.convertValue(payload.value(), CaseOutcome.class);
            caseService.updateCaseOutcome(caseOutcome);
        } catch (final Exception e) {
            logger.error("Error while listening to case outcome on topic: {}: ", topic, e);
        }
    }

}
