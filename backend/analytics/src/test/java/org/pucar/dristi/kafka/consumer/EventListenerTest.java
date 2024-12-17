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
import org.pucar.dristi.kafka.consumer.EventListener;
import org.pucar.dristi.service.BillingService;
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

    @Mock
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessage_success() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "value");

        when(config.getStateLevelTenantId()).thenReturn("tenant-id");
        when(config.getDemandGenerateTopic()).thenReturn("demand-generate-topic");
        when(config.getPaymentCollectTopic()).thenReturn("payment-collect-topic");

        eventListener.onMessage(record);

        verify(config, times(1)).getStateLevelTenantId();
        verify(indexerService, times(1)).esIndexer(record.topic(), record.value());
        Assertions.assertEquals("tenant-id", MDC.get(ServiceConstants.TENANTID_MDC_STRING));
    }

    @Test
    void testOnMessage_exception() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "value");

        when(config.getStateLevelTenantId()).thenReturn("tenant-id");
        when(config.getDemandGenerateTopic()).thenReturn("demand-generate-topic");
        when(config.getPaymentCollectTopic()).thenReturn("payment-collect-topic");

        eventListener.onMessage(record);

        verify(config, times(1)).getStateLevelTenantId();
        Assertions.assertEquals("tenant-id", MDC.get(ServiceConstants.TENANTID_MDC_STRING));
    }

    @Test
    void testOnMessage_success_billing_service() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "value");

        when(config.getStateLevelTenantId()).thenReturn("tenant-id");
        when(config.getDemandGenerateTopic()).thenReturn("test-topic");

        eventListener.onMessage(record);

        verify(config, times(1)).getStateLevelTenantId();
        verify(billingService, times(1)).process(record.topic(), record.value());
        Assertions.assertEquals("tenant-id", MDC.get(ServiceConstants.TENANTID_MDC_STRING));
    }
}
