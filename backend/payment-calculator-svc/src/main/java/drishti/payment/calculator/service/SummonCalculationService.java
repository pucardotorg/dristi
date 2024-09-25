package drishti.payment.calculator.service;


import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.factory.SummonContext;
import drishti.payment.calculator.factory.SummonFactory;
import drishti.payment.calculator.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummonCalculationService {

    private final SummonFactory summonFactory;
    private final Configuration config;

    @Autowired
    public SummonCalculationService(SummonFactory summonFactory,  Configuration config) {
        this.summonFactory = summonFactory;
        this.config = config;
    }


    public List<Calculation> calculateSummonFees(SummonCalculationReq request) {
        List<SummonCalculationCriteria> calculationCriteria = request.getCalculationCriteria();
        RequestInfo requestInfo = request.getRequestInfo();
        List<Calculation> response = new ArrayList<>();
        for (SummonCalculationCriteria criteria : calculationCriteria) {
            String channelId = criteria.getChannelId();
            SummonPayment channel = summonFactory.getChannelById(channelId);

            SummonContext context = new SummonContext(channel);
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
            SummonPayment channel = summonFactory.getChannelById(channelId);
            SummonContext context = new SummonContext(channel);
            Calculation calculation = context.calculatePayment(requestInfo, criteria);
            response.add(calculation);
        }
        return response;

    }


}
