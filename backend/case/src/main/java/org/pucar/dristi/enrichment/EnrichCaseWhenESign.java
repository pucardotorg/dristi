package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.enrichment.strategy.EnrichmentStrategy;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.pucar.dristi.config.ServiceConstants.E_SIGN;

@Component
@Slf4j
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
        log.info("Method=EnrichCaseWhenESign,Result=IN_PROGRESS, CaseId={}", caseRequest.getCases().getId());

        RequestInfo requestInfo = caseRequest.getRequestInfo();
        String individualId = individualService.getIndividualId(requestInfo);
        log.info("Method=EnrichCaseWhenESign,Result=IN_PROGRESS, IndividualId={}", individualId);

        boolean isLitigantSigned = Optional.ofNullable(caseRequest.getCases().getLitigants()).orElse(Collections.emptyList()).stream()
                .filter(party -> individualId.equals(party.getIndividualId()))
                .findFirst()
                .map(party -> {
                    party.setHasSigned(true);
                    return true;
                })
                .orElse(false);
        log.info("Method=EnrichCaseWhenESign,Result=IN_PROGRESS, LitigantSigned={}", isLitigantSigned);

        if (!isLitigantSigned) {
            log.info("Method=EnrichCaseWhenESign,Result=IN_PROGRESS, checking if advocate signed");

            List<Advocate> advocates = advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId);

            List<Advocate> activeAdvocate = advocates.stream()
                    .filter(Advocate::getIsActive)
                    .toList();

            if (!activeAdvocate.isEmpty()) {
                //expecting only one advocate
                String advocateId = activeAdvocate.get(0).getId().toString();

                boolean isAdvocateSigned = Optional.ofNullable(caseRequest.getCases().getRepresentatives())
                        .orElse(Collections.emptyList()).stream()
                        .filter(advocate -> advocateId.equals(advocate.getAdvocateId()))
                        .findFirst()
                        .map(advocate -> {
                            advocate.setHasSigned(true);
                            return true;
                        })
                        .orElse(false);
                log.info("Method=EnrichCaseWhenESign,Result=IN_PROGRESS, AdvocateSigned={}", isAdvocateSigned);
            }
        }
        log.info("Method=EnrichCaseWhenESign,Result=SUCCESS, CaseId={}", caseRequest.getCases().getId());
    }

}
