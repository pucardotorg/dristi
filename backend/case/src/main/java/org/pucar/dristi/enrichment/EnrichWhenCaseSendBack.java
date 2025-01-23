package org.pucar.dristi.enrichment;

import org.pucar.dristi.enrichment.strategy.EnrichmentStrategy;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.SEND_BACK;


@Component
public class EnrichWhenCaseSendBack implements EnrichmentStrategy {
    @Override
    public boolean canEnrich(CaseRequest caseRequest) {
        return SEND_BACK.equalsIgnoreCase(
                caseRequest.getCases().getWorkflow().getAction());
    }

    @Override
    public void enrich(CaseRequest caseRequest) {
        caseRequest.getCases().getLitigants().forEach((ele) -> {
            ele.setHasSigned(false);
        });
        caseRequest.getCases().getRepresentatives().forEach((ele) -> {
            ele.setHasSigned(false);
        });

    }
}
