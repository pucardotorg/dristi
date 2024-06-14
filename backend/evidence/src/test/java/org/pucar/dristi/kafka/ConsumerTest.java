package org.pucar.dristi.kafka;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {

    @Test
    void listenMethodExistsAndIsPublic() throws NoSuchMethodException {
        Method listenMethod = Consumer.class.getDeclaredMethod("listen", HashMap.class);
        assertNotNull(listenMethod);
        assertTrue(Modifier.isPublic(listenMethod.getModifiers()));
    }
}
