package drishti.payment.calculator.service;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.factory.PaymentContext;
import drishti.payment.calculator.factory.PaymentFactory;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import drishti.payment.calculator.web.models.SummonCalculationReq;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCalculationServiceTest {

    @Mock
    private PaymentFactory paymentFactory;

    @Mock
    private Configuration config;

    @InjectMocks
    private PaymentCalculationService PaymentCalculationService;

    @Mock
    private PaymentContext paymentContext;

    @Mock
    private Payment payment;

    private SummonCalculationReq summonCalculationReq;
    private RequestInfo requestInfo;
    private List<SummonCalculationCriteria> calculationCriteria;
    private SummonCalculationCriteria criteria;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        criteria = SummonCalculationCriteria.builder().channelId("channel1").build();
        calculationCriteria = Collections.singletonList(criteria);

        summonCalculationReq = SummonCalculationReq.builder()
                .requestInfo(requestInfo)
                .calculationCriteria(calculationCriteria)
                .build();
    }

    @Test
    void testCalculateSummonFees() {
        when(paymentFactory.getChannelById(anyString())).thenReturn(payment);

        List<Calculation> calculations = PaymentCalculationService.calculateSummonFees(summonCalculationReq);

        assertNotNull(calculations);
        assertEquals(1, calculations.size());

        verify(paymentFactory, times(1)).getChannelById(anyString());
    }
}
