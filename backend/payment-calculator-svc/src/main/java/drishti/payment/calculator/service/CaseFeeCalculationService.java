package drishti.payment.calculator.service;


import com.fasterxml.jackson.databind.JsonNode;
import drishti.payment.calculator.util.CaseUtil;
import drishti.payment.calculator.util.EFillingUtil;
import drishti.payment.calculator.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static drishti.payment.calculator.config.ServiceConstants.*;

@Service
@Slf4j
public class CaseFeeCalculationService {

    private final EFillingUtil eFillingUtil;

    private final CaseUtil caseUtil;

    @Autowired
    public CaseFeeCalculationService(EFillingUtil eFillingUtil, CaseUtil caseUtil) {
        this.eFillingUtil = eFillingUtil;
        this.caseUtil = caseUtil;
    }


    public List<Calculation> calculateCaseFees(EFillingCalculationRequest request) {
        log.info("operation=calculateCaseFees, result=IN_PROGRESS");

        RequestInfo requestInfo = request.getRequestInfo();
        List<EFillingCalculationCriteria> calculationCriteria = request.getCalculationCriteria();
        EFilingParam eFillingDefaultData = eFillingUtil.getEFillingDefaultData(requestInfo, calculationCriteria.get(0).getTenantId());

        Double applicationFee = eFillingDefaultData.getApplicationFee();
        Double vakalathnamaFee = eFillingDefaultData.getVakalathnamaFee();
        Double advocateClerkWelfareFund = eFillingDefaultData.getAdvocateClerkWelfareFund();
        Double delayCondonationFee = eFillingDefaultData.getDelayCondonationFee();

        LinkedHashMap<String, HashMap<String, Integer>> noOfAdvocateFees = eFillingDefaultData.getNoOfAdvocateFees();
        Map<String, Range> petitionFeeRange = eFillingDefaultData.getPetitionFee();


        List<Calculation> result = new ArrayList<>();

        for (EFillingCalculationCriteria criteria : calculationCriteria) {

            Double totalApplicationFee = criteria.getNumberOfApplication() * applicationFee;
            Double petitionFee = getPetitionFee(criteria.getCheckAmount(), petitionFeeRange);
            Double delayFee = criteria.getIsDelayCondonation() ? delayCondonationFee : 0.0;

            Map<String, List<JsonNode>> litigantAdvocateMap = caseUtil.getAdvocateForLitigant(request.getRequestInfo(), criteria.getFilingNumber(), criteria.getTenantId());
            Double advocateFee = 0.0;
            for (Map.Entry<String, List<JsonNode>> entry : litigantAdvocateMap.entrySet()) {
                int advocateCount = entry.getValue().size();
                advocateFee += getAdvocateFee(noOfAdvocateFees, advocateCount);

            }

            List<BreakDown> feeBreakdown = getFeeBreakdown(vakalathnamaFee, advocateClerkWelfareFund, totalApplicationFee, petitionFee, delayFee, advocateFee);
            Double totalCourtFee = Math.ceil(vakalathnamaFee + advocateClerkWelfareFund + totalApplicationFee + petitionFee + delayFee + advocateFee);
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


    public List<BreakDown> getFeeBreakdown(double vakalathnamaFee, double advocateClerkWelfareFund, double totalApplicationFee, double petitionFee, double condonationFee, double advocateFee) {
        List<BreakDown> feeBreakdowns = new ArrayList<>();

        feeBreakdowns.add(new BreakDown(VAKALATHNAMA_FEE, vakalathnamaFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(ADVOCATE_CLERK_WELFARE_FUND, advocateClerkWelfareFund, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(TOTAL_APPLICATION_FEE, totalApplicationFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(PETITION_FEE, petitionFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(ADVOCATE_FEE, advocateFee, new HashMap<>()));
        if (condonationFee > 0)
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


    private Double getAdvocateFee(LinkedHashMap<String, HashMap<String, Integer>> noOfAdvocateFees, int noOfAdvocates) {
        Double advocateFee = 0.0;
        for (Map.Entry<String, HashMap<String, Integer>> entry : noOfAdvocateFees.entrySet()) {
            HashMap<String, Integer> value = entry.getValue();
            Double lowerBound = Double.valueOf(value.get("min"));
            Double upperBound = Double.valueOf(value.get("max"));

            if (noOfAdvocates >= lowerBound && noOfAdvocates <= upperBound) {
                advocateFee = Double.valueOf(value.get("advocateFee"));
                break;
            }
        }
        return advocateFee;
    }
}
