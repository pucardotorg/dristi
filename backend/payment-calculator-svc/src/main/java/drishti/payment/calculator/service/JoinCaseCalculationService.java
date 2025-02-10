package drishti.payment.calculator.service;


import com.fasterxml.jackson.databind.JsonNode;
import drishti.payment.calculator.util.CaseUtil;
import drishti.payment.calculator.util.EFillingUtil;
import drishti.payment.calculator.web.models.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class JoinCaseCalculationService {

    private final EFillingUtil eFillingUtil;
    private final CaseUtil caseUtil;

    @Autowired
    public JoinCaseCalculationService(EFillingUtil eFillingUtil, CaseUtil caseUtil) {
        this.eFillingUtil = eFillingUtil;
        this.caseUtil = caseUtil;
    }


    public List<Calculation> calculateFees(@Valid JoinCaseRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();
        List<JoinCaseCalculationCriteria> calculationCriteria = request.getCalculationCriteria();
        EFilingParam eFillingDefaultData = eFillingUtil.getEFillingDefaultData(requestInfo, calculationCriteria.get(0).getTenantId());
        Double vakalathnamaFee = eFillingDefaultData.getVakalathnamaFee();

        for (JoinCaseCalculationCriteria criteria : request.getCalculationCriteria()) {

            // fetch case with filing number and tenant id
            // litigant Advocate map
            Map<String, List<JsonNode>> advocateForLitigant = caseUtil.getAdvocateForLitigant(requestInfo, criteria.getFilingNumber(), criteria.getTenantId());

            for (JoiningFor joiningFor : criteria.getJoiningFor()) {
                String partyId = joiningFor.getLitigantId();

                // here check for null
                // if litigant is not there means litigant and advocate joining together
//                UUID uuid = UUID.fromString(partyId);

//                List<JsonNode> jsonNodes = advocateForLitigant.get(uuid);


                // if the size is less than 3 add 25 rupees and 12 for advocate and clerk
                //

            }

        }
        return new ArrayList<>();

    }
}
