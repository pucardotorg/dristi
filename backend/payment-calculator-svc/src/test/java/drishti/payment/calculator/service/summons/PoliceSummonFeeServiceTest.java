package drishti.payment.calculator.service.summons;


import drishti.payment.calculator.util.SummonUtil;
import drishti.payment.calculator.web.models.BreakDown;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.EPostConfigParams;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static drishti.payment.calculator.config.ServiceConstants.COURT_FEE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PoliceSummonFeeServiceTest {

    @Mock
    private SummonUtil summonUtil;

    @InjectMocks
    private PoliceSummonFeeService policeSummonFeeService;

    @Test
    @DisplayName("do calculate police summon fee")
    public void doCalculatePoliceSummonFee() {

        when(summonUtil.getIPostFeesDefaultData(any(), anyString())).thenReturn(new EPostConfigParams());
        Double courtFee = 100.0;
        when(summonUtil.calculateCourtFees(any())).thenReturn(courtFee);
        SummonCalculationCriteria criteria = new SummonCalculationCriteria();
        criteria.setTenantId("pb");

        Calculation result = policeSummonFeeService.calculatePayment(new RequestInfo(), criteria);

        assertEquals(courtFee, result.getTotalAmount());
        assertEquals(1, result.getBreakDown().size());
        BreakDown breakDown = result.getBreakDown().get(0);
        assertEquals(COURT_FEE, breakDown.getType());
        assertEquals(courtFee, breakDown.getAmount());

    }
}
