package digit.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ConsumerTest {

    @InjectMocks
    private Consumer consumer;

    @Test
    void testListen_ValidRecord() {
        HashMap<String, Object> mockRecord = new HashMap<>();
        mockRecord.put("key1", "value1");
        mockRecord.put("key2", "value2");

        consumer.listen(mockRecord);

        spy(consumer).listen(mockRecord);
    }

    @Test
    void testListen_EmptyRecord() {
        HashMap<String, Object> mockRecord = new HashMap<>();

        consumer.listen(mockRecord);

        spy(consumer).listen(mockRecord);
    }
}
