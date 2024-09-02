package drishti.payment.calculator.service;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.factory.SummonContext;
import drishti.payment.calculator.factory.SummonFactory;
import drishti.payment.calculator.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummonCalculationServiceTest {

    @Mock
    private SummonFactory summonFactory;

    @Mock
    private Configuration config;

    @InjectMocks
    private SummonCalculationService summonCalculationService;

    @Mock
    private SummonContext summonContext;

    @Mock
    private SummonPayment summonPayment;

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
        when(summonFactory.getChannelById(anyString())).thenReturn(summonPayment);

        List<Calculation> calculations = summonCalculationService.calculateSummonFees(summonCalculationReq);

        assertNotNull(calculations);
        assertEquals(1, calculations.size());

        verify(summonFactory, times(1)).getChannelById(anyString());
    }
}
