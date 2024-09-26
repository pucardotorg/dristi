package drishti.payment.calculator.service;


import drishti.payment.calculator.factory.PaymentContext;
import drishti.payment.calculator.factory.PaymentFactory;
import drishti.payment.calculator.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentCalculationService {

    private final PaymentFactory paymentFactory;


    @Autowired
    public PaymentCalculationService(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;

    }

    @Deprecated
    public List<Calculation> calculateSummonFees(SummonCalculationReq request) {
        List<SummonCalculationCriteria> calculationCriteria = request.getCalculationCriteria();
        RequestInfo requestInfo = request.getRequestInfo();
        List<Calculation> response = new ArrayList<>();
        for (SummonCalculationCriteria criteria : calculationCriteria) {
            String channelId = criteria.getChannelId();
            Payment channel = paymentFactory.getChannelById(channelId);

            PaymentContext context = new PaymentContext(channel);
            Calculation calculation = context.calculatePayment(requestInfo, criteria);
            response.add(calculation);
        }
        return response;

    }

    public List<Calculation> calculateTaskPaymentFess(TaskPaymentRequest request) {
        List<TaskPaymentCriteria> calculationCriteria = request.getCalculationCriteria();
        RequestInfo requestInfo = request.getRequestInfo();
        List<Calculation> response = new ArrayList<>();
        for (TaskPaymentCriteria criteria : calculationCriteria) {
            String channelId = criteria.getChannelId();
            Payment channel = paymentFactory.getChannelById(channelId);
            PaymentContext context = new PaymentContext(channel);
            Calculation calculation = context.calculatePayment(requestInfo, criteria);
            response.add(calculation);
        }
        return response;

    }


}
