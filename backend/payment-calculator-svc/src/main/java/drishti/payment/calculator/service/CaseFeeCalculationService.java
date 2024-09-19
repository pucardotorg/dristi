package drishti.payment.calculator.service;


import drishti.payment.calculator.util.EFillingUtil;
import drishti.payment.calculator.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static drishti.payment.calculator.config.ServiceConstants.*;

@Service
@Slf4j
public class CaseFeeCalculationService {

    private final EFillingUtil eFillingUtil;

    @Autowired
    public CaseFeeCalculationService(EFillingUtil eFillingUtil) {
        this.eFillingUtil = eFillingUtil;

    }


    public List<Calculation> calculateCaseFees(EFillingCalculationReq request) {
        log.info("operation=calculateCaseFees, result=IN_PROGRESS");

        RequestInfo requestInfo = request.getRequestInfo();
        List<EFillingCalculationCriteria> calculationCriteria = request.getCalculationCriteria();
        EFilingParam eFillingDefaultData = eFillingUtil.getEFillingDefaultData(requestInfo, calculationCriteria.get(0).getTenantId());

        Double applicationFee = eFillingDefaultData.getApplicationFee();
        Double vakalathnamaFee = eFillingDefaultData.getVakalathnamaFee();
        Double advocateWelfareFund = eFillingDefaultData.getAdvocateWelfareFund();
        Double advocateClerkWelfareFund = eFillingDefaultData.getAdvocateClerkWelfareFund();
        Double delayCondonationFee = eFillingDefaultData.getDelayCondonationFee();
        Long delayCondonationPeriod = eFillingDefaultData.getDelayCondonationPeriod();

        Map<String, Range> petitionFeeRange = eFillingDefaultData.getPetitionFee();


        List<Calculation> result = new ArrayList<>();

        for (EFillingCalculationCriteria criteria : calculationCriteria) {

            Double totalApplicationFee = criteria.getNumberOfApplication() * applicationFee;
            Double petitionFee = getPetitionFee(criteria.getCheckAmount(), petitionFeeRange);
            Double delayFee = isDelayCondonationFeeApplicable(criteria.getDelayCondonation(), delayCondonationPeriod) ? delayCondonationFee : 0.0;

            List<BreakDown> feeBreakdown = getFeeBreakdown(vakalathnamaFee, advocateWelfareFund, advocateClerkWelfareFund, totalApplicationFee, petitionFee,delayFee);
            Double totalCourtFee = vakalathnamaFee + advocateWelfareFund + advocateClerkWelfareFund + totalApplicationFee + petitionFee + delayFee;
            Calculation calculation = Calculation.builder()
                    .applicationId(criteria.getCaseId())
                    .totalAmount(totalCourtFee)
                    .tenantId(criteria.getTenantId())
                    .breakDown(feeBreakdown).build();

            result.add(calculation);
        }
        log.info("operation=calculateCaseFees, result=SUCCESS");

        return result;

    }

    public List<BreakDown> getFeeBreakdown(double vakalathnamaFee, double advocateWelfareFund, double advocateClerkWelfareFund, double totalApplicationFee, double petitionFee ,double condonationFee) {
        List<BreakDown> feeBreakdowns = new ArrayList<>();

        feeBreakdowns.add(new BreakDown(VAKALATHNAMA_FEE, vakalathnamaFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(ADVOCATE_WELFARE_FUND, advocateWelfareFund, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(ADVOCATE_CLERK_WELFARE_FUND, advocateClerkWelfareFund, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(TOTAL_APPLICATION_FEE, totalApplicationFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(PETITION_FEE, petitionFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(DELAY_CONDONATION_FEE, condonationFee, new HashMap<>()));

        return feeBreakdowns;
    }


    private Double getPetitionFee(Double checkAmount, Map<String, Range> rangeMap) {

        for (Range range : rangeMap.values()) {
            Double lowerBound = range.getMin();
            Double upperBound = range.getMax();

            if (checkAmount >= lowerBound && checkAmount <= upperBound) {
                return range.getFee();
            }
        }

        return null; // Invalid check amount
    }

    private Boolean isDelayCondonationFeeApplicable(Long delayDuration, Long stdDuration) {

        return delayDuration > stdDuration;

    }

}
