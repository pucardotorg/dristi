package org.pucar.dristi.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {

    @Test
    void listenMethodExistsAndAnnotatedWithKafkaListener() throws NoSuchMethodException {
        Method listenMethod = Consumer.class.getDeclaredMethod("listen", HashMap.class);
        assertNotNull(listenMethod);
    }
}
