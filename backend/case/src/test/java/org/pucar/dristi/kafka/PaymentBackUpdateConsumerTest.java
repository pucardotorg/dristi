package org.pucar.dristi.kafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.PaymentUpdateService;

@ExtendWith(MockitoExtension.class)
public class PaymentBackUpdateConsumerTest {

    @Mock
    private PaymentUpdateService paymentUpdateService;

    @InjectMocks
    private PaymentBackUpdateConsumer paymentBackUpdateConsumer;

    @Test
    void listenPayments_processesRecordSuccessfully() {
        HashMap<String, Object> record = new HashMap<>();
        String topic = "topic";

        doNothing().when(paymentUpdateService).process(record);

        paymentBackUpdateConsumer.listenPayments(record, topic);

        verify(paymentUpdateService, times(1)).process(record);
    }

    @Test
    void listenPayments_logsErrorWhenExceptionIsThrown() {
        HashMap<String, Object> record = new HashMap<>();
        String topic = "topic";

        doThrow(new RuntimeException()).when(paymentUpdateService).process(record);

        assertDoesNotThrow(() -> paymentBackUpdateConsumer.listenPayments(record, topic));
        verify(paymentUpdateService, times(1)).process(record);
    }
}