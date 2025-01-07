package org.pucar.dristi.kafka;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.service.EvidenceStatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class EvidenceStatusUpdateConsumer {

    private final EvidenceStatusUpdateService evidenceStatusUpdateService;

    @Autowired
    public EvidenceStatusUpdateConsumer(EvidenceStatusUpdateService evidenceStatusUpdateService) {
        this.evidenceStatusUpdateService = evidenceStatusUpdateService;
    }


    @KafkaListener(topics = {"${application.kafka.update.topic}", "${application.kafka.status.update.topic}"})
    public void applicationUpdate(final Map<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received record for topic {} , method: applicationUpdate , result: InProgress", topic);
        try {
            evidenceStatusUpdateService.updateEvidenceStatus(record);
        } catch (Exception e) {
            log.error("Error while listening the record:{} in topic {} , method: applicationUpdate , result: Failure ", record, topic);
            log.error("Error: {}", e.toString());
        }

    }


}
