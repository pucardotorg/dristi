package drishti.payment.calculator.service.summons;


import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.helper.EPostConfigParamsTestBuilder;
import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.util.SpeedPostUtil;
import drishti.payment.calculator.util.SummonUtil;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.EPostConfigParams;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import drishti.payment.calculator.web.models.enums.Classification;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EPostSummonFeeServiceTest {

    @Mock
    private SummonUtil summonUtil;

    @Mock
    private PostalHubRepository repository;

    @Mock
    private SpeedPostUtil speedPostUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private EPostSummonFeeService ePostSummonFeeService;


    @Test
    @DisplayName("do calculate e-post summon fee")
    public void doCalculateEPostSummonFee() {

        EPostConfigParams configParams = EPostConfigParamsTestBuilder.builder().withSpeedPost().withConfig().build();

        when(repository.getPostalHub(any())).thenReturn(Collections.singletonList(PostalHub.builder().classification(Classification.LTD).build()));
        when(summonUtil.getIPostFeesDefaultData(any(), anyString())).thenReturn(configParams);
        Double courtFee = 100.0;
        when(summonUtil.calculateCourtFees(any())).thenReturn(courtFee);
        when(config.getNumberOfPgOfSummon()).thenReturn(2);
        SummonCalculationCriteria criteria = new SummonCalculationCriteria();
        criteria.setTenantId("pb");

        Calculation result = ePostSummonFeeService.calculatePayment(new RequestInfo(), criteria);

        assertNotEquals(courtFee, result.getTotalAmount());

    }


    @Test
    @DisplayName("do calculate e-post fee")
    public void doCalculateEPostFee() {

        EPostConfigParams configParams = EPostConfigParamsTestBuilder.builder().withSpeedPost().withConfig().build();
        when(speedPostUtil.getSpeedPostFee(anyDouble(), any(Classification.class), any())).thenReturn(10.0);

        Double result = ePostSummonFeeService.calculateEPostFee(2, Classification.LTD, configParams);

        assertEquals(22.0, result);


    }
}
