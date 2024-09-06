package digit.kafka.producer;

import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerTest {

    @InjectMocks
    private Producer producer;

    @Mock
    private CustomKafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testPush() {
        String topic = "test-topic";
        Object value = new Object();

        producer.push(topic, value);

        verify(kafkaTemplate, times(1)).send(topic, value);
    }

    @Test
    void testPush_nullTopic() {
        Object value = new Object();

        producer.push(null, value);

        verify(kafkaTemplate, times(1)).send(null, value);
    }

    @Test
    void testPush_nullValue() {
        String topic = "test-topic";

        producer.push(topic, null);

        verify(kafkaTemplate, times(1)).send(topic, null);
    }
}