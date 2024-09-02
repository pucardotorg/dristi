package drishti.payment.calculator.service;


import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Service;

@Service
public class EPoliceFeesCalculation implements SummonPayment {
    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {
        return Calculation.builder()
                .applicationId(criteria.getSummonId())
                .tenantId(criteria.getTenantId())
                .totalAmount(0.0).build();
    }
}
