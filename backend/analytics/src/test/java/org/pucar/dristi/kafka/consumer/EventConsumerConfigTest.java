package org.pucar.dristi.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.pucar.dristi.kafka.consumer.EventConsumerConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventConsumerConfigTest {

    @InjectMocks
    @Spy
    private EventConsumerConfig eventConsumerConfig;

    @Mock
    private ApplicationArguments applicationArguments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(eventConsumerConfig, "brokerAddress", "localhost:9092");
        ReflectionTestUtils.setField(eventConsumerConfig, "consumerGroup", "test-group");
        ReflectionTestUtils.setField(eventConsumerConfig, "consumerTopics", "test-topic1,test-topic2");
    }

    @Test
    void testSetTopics() {
        String expectedTopics = "[test-topic1, test-topic2]";
        String actualTopics = eventConsumerConfig.setTopics();
        assertEquals(expectedTopics, actualTopics);
    }

    @Test
    void testConsumerFactory() {
        Map<String, Object> expectedProps = new HashMap<>();
        expectedProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        expectedProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        expectedProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        expectedProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        expectedProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        expectedProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        expectedProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "600000");
        expectedProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "300");
        expectedProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        expectedProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        Map<String, Object> actualProps = eventConsumerConfig.consumerFactory().getConfigurationProperties();
        assertEquals(expectedProps, actualProps);
    }

    @Test
    void testKafkaListenerContainerFactory() {
        ConcurrentMessageListenerContainer<String, String> container = eventConsumerConfig.kafkaListenerContainerFactory().createContainer("test-topic");
        assertNotNull(container);
        assertEquals(30000, container.getContainerProperties().getPollTimeout());
    }

    @Test
    void testContainer() throws Exception {
        KafkaMessageListenerContainer<String, String> container = eventConsumerConfig.container();
        assertNotNull(container);
        assertEquals("test-topic1", Objects.requireNonNull(container.getContainerProperties().getTopics())[0]);
        assertEquals("test-topic2", container.getContainerProperties().getTopics()[1]);

    }
    @Test
    void testInitializeContainer() {
        KafkaMessageListenerContainer<String, String> containerMock = mock(KafkaMessageListenerContainer.class);
        doNothing().when(containerMock).start();

        try {
            doReturn(containerMock).when(eventConsumerConfig).container();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        boolean initialized = eventConsumerConfig.initializeContainer();
        assertTrue(initialized);
        verify(containerMock, times(1)).start();
    }

    @Test
    void testPauseContainer() {
        KafkaMessageListenerContainer<String, String> containerMock = mock(KafkaMessageListenerContainer.class);
        EventConsumerConfig.kafkContainer = containerMock;
        doNothing().when(containerMock).stop();

        boolean paused = EventConsumerConfig.pauseContainer();
        assertTrue(paused);
        verify(containerMock, times(1)).stop();
    }

    @Test
    void testResumeContainer() {
        KafkaMessageListenerContainer<String, String> containerMock = mock(KafkaMessageListenerContainer.class);
        EventConsumerConfig.kafkContainer = containerMock;
        doNothing().when(containerMock).start();

        boolean resumed = EventConsumerConfig.resumeContainer();
        assertTrue(resumed);
        verify(containerMock, times(1)).start();
    }

    @Test
    void testRun() {
        try {
            doReturn(true).when(eventConsumerConfig).initializeContainer();
            eventConsumerConfig.run(applicationArguments);
            verify(eventConsumerConfig, times(1)).initializeContainer();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
