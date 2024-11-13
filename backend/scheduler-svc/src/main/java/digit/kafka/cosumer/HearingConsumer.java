package digit.kafka.cosumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.service.hearing.HearingProcessor;
import digit.web.models.ReScheduleHearing;
import digit.web.models.hearing.HearingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class HearingConsumer {

    private final ObjectMapper mapper;
    private final HearingProcessor processor;

    @Autowired
    public HearingConsumer(ObjectMapper mapper, HearingProcessor processor) {
        this.mapper = mapper;
        this.processor = processor;
    }

    @KafkaListener(topics = {"create-hearing-application"})
    public void listenScheduleHearing(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        HearingRequest hearingRequest = mapper.convertValue(record, HearingRequest.class);
        processor.processCreateHearingRequest(hearingRequest);

    }


}



