package digit.channel;

import digit.kafka.Producer;
import digit.web.models.ChannelMessage;
import digit.web.models.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailChannel implements ExternalChannel{

    private final Producer producer;


    public EmailChannel(Producer producer) {
        this.producer = producer;
    }

    @Override
    public ChannelMessage sendSummons(TaskRequest request) {
        producer.push("egov.core.notification.email", request);
        return ChannelMessage.builder().acknowledgementStatus("success").build();
    }
}
