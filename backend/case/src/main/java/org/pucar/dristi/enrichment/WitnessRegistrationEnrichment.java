package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.util.UserUtil;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class WitnessRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserUtil userUtils;

    public void enrichWitnessRegistration(WitnessRequest witnessRequest) {
        try {
            if(witnessRequest.getRequestInfo().getUserInfo() != null) {
                for (Witness witness : witnessRequest.getWitnesses()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(witnessRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(witnessRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    witness.setAuditDetails(auditDetails);

                    witness.setId(UUID.randomUUID());

                    witness.setIsActive(false);

                    witness.setFilingNumber(UUID.randomUUID().toString());
                    witness.setCnrNumber(witness.getFilingNumber());
                }
            }
        }
        catch (CustomException e){
            log.error("Exception occurred while Enriching witness");
            throw e;
        }
        catch (Exception e) {
            log.error("Error enriching witness application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichWitnessApplicationUponUpdate(WitnessRequest witnessRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in witness of update
            for (Witness witness : witnessRequest.getWitnesses()) {
                witness.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                witness.getAuditDetails().setLastModifiedBy(witnessRequest.getRequestInfo().getUserInfo().getUuid());
            }
        } catch (Exception e) {
            log.error("Error enriching witness application upon update: {}", e.getMessage());
            // Handle the exception or throw a custom exception
            throw new CustomException(ENRICHMENT_EXCEPTION,"Error in witness enrichment service during witness update process: "+ e.getMessage());
        }
    }
}