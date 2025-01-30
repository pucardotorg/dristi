package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.enrichment.strategy.EnrichmentStrategy;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

import static org.pucar.dristi.config.ServiceConstants.UPLOAD;

@Component
@Slf4j
public class EnrichCaseWhenDocumentUploadSign implements EnrichmentStrategy {
    @Override
    public boolean canEnrich(CaseRequest caseRequest) {
        return UPLOAD.equalsIgnoreCase(caseRequest.getCases().getWorkflow().getAction());
    }

    @Override
    public void enrich(CaseRequest caseRequest) {

        log.info("Method=EnrichCaseWhenDocumentUploadSign, Result=IN_PROGRESS, CaseId={},Setting hasSigned to true", caseRequest.getCases().getId());
        Optional.ofNullable(caseRequest.getCases().getLitigants()).orElse(Collections.emptyList()).forEach((ele) -> {
            ele.setHasSigned(true);
        });
        Optional.ofNullable(caseRequest.getCases().getRepresentatives()).orElse(Collections.emptyList()).forEach((ele) -> {
            ele.setHasSigned(true);
        });

        log.info("Method=EnrichCaseWhenDocumentUploadSign, Result=SUCCESS, CaseId={}", caseRequest.getCases().getId());

    }
}
