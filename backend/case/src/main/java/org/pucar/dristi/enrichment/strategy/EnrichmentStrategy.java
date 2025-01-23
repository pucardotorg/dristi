package org.pucar.dristi.enrichment.strategy;

import org.pucar.dristi.web.models.CaseRequest;


public interface EnrichmentStrategy {

    boolean canEnrich(CaseRequest courtCase);
    void enrich(CaseRequest courtCase);
}
