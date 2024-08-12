package org.pucar.dristi;

<<<<<<< HEAD
import static org.mockito.Mockito.mock;

=======
>>>>>>> main
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

<<<<<<< HEAD
=======
import static org.mockito.Mockito.mock;

>>>>>>> main
@Configuration
public class TestConfiguration {
    @Bean
    @SuppressWarnings("unchecked")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}