package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.util.UserUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class WitnessRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserUtil userUtils;

    public void enrichCaseRegistration(WitnessRequest witnessRequest) {
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
            e.printStackTrace();
            log.error("Custom Exception occurred while Enriching advocate clerk");
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Error enriching case application: {}", e.getMessage());
        }
    }
}