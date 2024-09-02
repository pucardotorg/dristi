package drishti.payment.calculator.service;

import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import org.egov.common.contract.request.RequestInfo;

public interface SummonPayment {

    Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria);

}
