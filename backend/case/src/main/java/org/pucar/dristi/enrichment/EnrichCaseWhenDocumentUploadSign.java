package org.pucar.dristi.enrichment;

import org.pucar.dristi.enrichment.strategy.EnrichmentStrategy;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.UPLOAD;

@Component
public class EnrichCaseWhenDocumentUploadSign implements EnrichmentStrategy {
    @Override
    public boolean canEnrich(CaseRequest caseRequest) {
        return UPLOAD.equalsIgnoreCase(caseRequest.getCases().getWorkflow().getAction());
    }

    @Override
    public void enrich(CaseRequest caseRequest) {
        caseRequest.getCases().getLitigants().forEach((ele) -> {
            ele.setHasSigned(true);
        });
        caseRequest.getCases().getRepresentatives().forEach((ele) -> {
            ele.setHasSigned(true);
        });

    }
}
