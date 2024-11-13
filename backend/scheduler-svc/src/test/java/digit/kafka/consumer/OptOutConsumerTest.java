package digit.kafka.consumer;

import digit.kafka.cosumer.OptOutConsumer;
import digit.service.hearing.OptOutProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptOutConsumerTest {

    @Mock
    private OptOutProcessor optOutProcessor;

    @InjectMocks
    private OptOutConsumer optOutConsumer;

    @Test
    void testListenOptOut_Success() {
        // Prepare test data
        HashMap<String, Object> record = new HashMap<>();
        record.put("key", "value");

        // No exception should be thrown, so no need to mock an exception
        optOutConsumer.listenOptOut(record, "reschedule-opt-out");

        // Verify that the processor's method was called with the correct arguments
        verify(optOutProcessor, times(1)).checkAndScheduleHearingForOptOut(record);
    }

    @Test
    void testListenOptOut_Exception() {
        // Prepare test data
        HashMap<String, Object> record = new HashMap<>();
        record.put("key", "value");

        // Simulate an exception thrown by optOutProcessor
        doThrow(new RuntimeException("Test Exception")).when(optOutProcessor).checkAndScheduleHearingForOptOut(record);

        // Call the method, exception will be caught internally
        optOutConsumer.listenOptOut(record, "reschedule-opt-out");

        // Verify that the method was called, even if it threw an exception
        verify(optOutProcessor, times(1)).checkAndScheduleHearingForOptOut(record);
    }
}
