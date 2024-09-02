package digit.kafka.cosumer;


import digit.service.hearing.OptOutProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class OptOutConsumer {


    private final OptOutProcessor optOutProcessor;

    @Autowired
    public OptOutConsumer(OptOutProcessor optOutProcessor) {

        this.optOutProcessor = optOutProcessor;
    }


    @KafkaListener(topics = {"reschedule-opt-out"})
    public void listenOptOut(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {
            optOutProcessor.checkAndScheduleHearingForOptOut(record);
        } catch (Exception e) {
            log.error("error occurred while serializing", e);
        }

    }
}
