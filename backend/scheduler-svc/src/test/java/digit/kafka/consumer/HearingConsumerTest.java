package digit.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.kafka.cosumer.HearingConsumer;
import digit.service.hearing.HearingProcessor;
import digit.web.models.hearing.HearingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HearingConsumerTest {

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private HearingProcessor mockProcessor;

    private HearingConsumer hearingConsumer;

    @BeforeEach
    void setUp() {
        hearingConsumer = new HearingConsumer(mockMapper, mockProcessor);
    }

    @Test
    void testListenScheduleHearing() {
        // Arrange
        HashMap<String, Object> record = new HashMap<>();
        String topic = "create-hearing-application";
        HearingRequest mockHearingRequest = mock(HearingRequest.class);

        when(mockMapper.convertValue(record, HearingRequest.class)).thenReturn(mockHearingRequest);

        // Act
        hearingConsumer.listenScheduleHearing(record, topic);

        // Assert
        verify(mockMapper).convertValue(record, HearingRequest.class);
        verify(mockProcessor).processCreateHearingRequest(mockHearingRequest);
    }

    @Test
    void testConstructor() {
        // This test ensures the constructor is covered
        HearingConsumer consumer = new HearingConsumer(mockMapper, mockProcessor);
        Assertions.assertNotNull(consumer);
    }
}
