package digit.kafka;

import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProducerTest {

    @Mock
    private CustomKafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private Producer producer;

    @Test
    public void testPush() {
        // Arrange
        String topic = "test-topic";
        Object value = "test-value";

        // Act
        producer.push(topic, value);

        // Assert
        verify(kafkaTemplate, times(1)).send(topic, value);
    }
}
