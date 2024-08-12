package org.pucar.dristi.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.service.IndexerService;
import org.slf4j.MDC;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventListenerTest {

    @InjectMocks
    private EventListener eventListener;

    @Mock
    private IndexerService indexerService;

    @Mock
    private Configuration config;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessage_success() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "value");

        when(config.getStateLevelTenantId()).thenReturn("tenant-id");

        eventListener.onMessage(record);

        verify(config, times(1)).getStateLevelTenantId();
        verify(indexerService, times(1)).esIndexer(record.topic(), record.value());
        Assertions.assertEquals("tenant-id", MDC.get(ServiceConstants.TENANTID_MDC_STRING));
    }

    @Test
    void testOnMessage_exception() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "value");

        when(config.getStateLevelTenantId()).thenReturn("tenant-id");
        doThrow(new RuntimeException("ES indexing error")).when(indexerService).esIndexer(anyString(), anyString());

        eventListener.onMessage(record);

        verify(config, times(1)).getStateLevelTenantId();
        verify(indexerService, times(1)).esIndexer(record.topic(), record.value());
        Assertions.assertEquals("tenant-id", MDC.get(ServiceConstants.TENANTID_MDC_STRING));
    }
}
