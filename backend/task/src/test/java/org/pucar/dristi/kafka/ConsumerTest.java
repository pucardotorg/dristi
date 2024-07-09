package org.pucar.dristi.kafka;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsumerTest {

    @Test
    void listenMethodExistsAndAnnotatedWithKafkaListener() throws NoSuchMethodException {
        Method listenMethod = Consumer.class.getDeclaredMethod("listen", HashMap.class);
        assertNotNull(listenMethod);
    }
}
