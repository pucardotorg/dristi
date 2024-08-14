package org.pucar.dristi.enrichment;


import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WitnessRegistrationEnrichment {

    public void enrichWitnessRegistration(WitnessRequest witnessRequest) {
        try {
            if (witnessRequest.getRequestInfo().getUserInfo() != null) {
                Witness witness = witnessRequest.getWitness();
                AuditDetails auditDetails = AuditDetails.builder().createdBy(witnessRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(witnessRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                witness.setAuditDetails(auditDetails);

                witness.setId(UUID.randomUUID());

                witness.setIsActive(false);

                witness.setFilingNumber(UUID.randomUUID().toString());
                witness.setCnrNumber(witness.getFilingNumber());
            }
        } catch (Exception e) {
            log.error("Error enriching witness application :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichWitnessApplicationUponUpdate(WitnessRequest witnessRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in witness of update
            Witness witness = witnessRequest.getWitness();
            witness.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            witness.getAuditDetails().setLastModifiedBy(witnessRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching witness application upon update :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in witness enrichment service during witness update process: " + e.getMessage());
        }
    }
}