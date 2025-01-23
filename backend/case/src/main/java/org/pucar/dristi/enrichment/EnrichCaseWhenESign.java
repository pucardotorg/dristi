package org.pucar.dristi.enrichment;

import jakarta.validation.Valid;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.enrichment.strategy.EnrichmentStrategy;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.AbstractMap;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.E_SIGN;
import static org.pucar.dristi.config.ServiceConstants.E_SIGN_COMPLETE;

@Component
public class EnrichCaseWhenESign implements EnrichmentStrategy {

    private final IndividualService individualService;
    private final AdvocateUtil advocateUtil;

    @Autowired
    public EnrichCaseWhenESign(IndividualService individualService, AdvocateUtil advocateUtil) {
        this.individualService = individualService;
        this.advocateUtil = advocateUtil;
    }

    @Override
    public boolean canEnrich(CaseRequest caseRequest) {
        return E_SIGN.equalsIgnoreCase(caseRequest.getCases().getWorkflow().getAction());
    }

    @Override
    public void enrich(CaseRequest caseRequest) {
        RequestInfo requestInfo = caseRequest.getRequestInfo();
        String individualId = individualService.getIndividualId(requestInfo);

        boolean isLitigantSigned = caseRequest.getCases().getLitigants().stream()
                .filter(party -> individualId.equals(party.getIndividualId()))
                .findFirst()
                .map(party -> {
                    party.setHasSigned(true);
                    return true;
                })
                .orElse(false);

        if (!isLitigantSigned) {

            List<Advocate> advocates = advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId);

            List<Advocate> activeAdvocate = advocates.stream()
                    .filter(Advocate::getIsActive)
                    .toList();

            if (!activeAdvocate.isEmpty()) {
                //expecting only one advocate
                String advocateId = activeAdvocate.get(0).getId().toString();

                boolean isAdvocateSigned = caseRequest.getCases().getRepresentatives().stream()
                        .filter(advocate -> advocateId.equals(advocate.getAdvocateId()))
                        .findFirst()
                        .map(advocate -> {
                            advocate.setHasSigned(true);
                            return true;
                        })
                        .orElse(false);
            }


        }
    }

}
