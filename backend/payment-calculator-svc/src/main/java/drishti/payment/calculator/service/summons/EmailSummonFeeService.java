package drishti.payment.calculator.service.summons;


import drishti.payment.calculator.service.SummonPayment;
import drishti.payment.calculator.util.SummonUtil;
import drishti.payment.calculator.web.models.BreakDown;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.EPostConfigParams;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;

import static drishti.payment.calculator.config.ServiceConstants.COURT_FEE;

@Service
@Slf4j
public class EmailSummonFeeService implements SummonPayment {

    private final SummonUtil summonUtil;

    @Autowired
    public EmailSummonFeeService(SummonUtil summonUtil) {
        this.summonUtil = summonUtil;
    }

    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {

        EPostConfigParams ePostConfigParams = summonUtil.getIPostFeesDefaultData(requestInfo, criteria.getTenantId());
        Double courtFee = summonUtil.calculateCourtFees(ePostConfigParams);
        return Calculation.builder()
                .applicationId(criteria.getSummonId())
                .tenantId(criteria.getTenantId())
                .totalAmount(courtFee)
                .breakDown(Collections.singletonList(new BreakDown(COURT_FEE, courtFee, new HashMap<>()))).build();
    }
}
