package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class CaseRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private Configuration config;

    public void enrichCaseRegistration(CaseRequest caseRequest) {
        try {
            CourtCase courtCase = caseRequest.getCases();

            List<String> courtCaseRegistrationIdList = idgenUtil.getIdList(caseRequest.getRequestInfo(), courtCase.getTenantId(), config.getCaseFilingNumber(), null, 1);
            log.info("Court Case Registration Id List :: {}", courtCaseRegistrationIdList);
            AuditDetails auditDetails = AuditDetails.builder().createdBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            courtCase.setAuditdetails(auditDetails);

            courtCase.setId(UUID.randomUUID());

            CaseUtil.enrichLinkedCaseForUpdate(courtCase, auditDetails);

            CaseUtil.enrichStatutAndSectionsForUpdate(courtCase, auditDetails);

            CaseUtil.enrichLitigantsForUpdate(courtCase, auditDetails);

            CaseUtil.enrichRepresentivesForUpdate(courtCase, auditDetails);

//                    courtCase.setIsActive(false);
            CaseUtil.enrichDocumentsForUpdate(courtCase);

            courtCase.setFilingNumber(courtCaseRegistrationIdList.get(0));
            courtCase.setCaseNumber(courtCase.getFilingNumber());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error enriching case application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichCaseApplicationUponUpdate(CaseRequest caseRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            CourtCase courtCase = caseRequest.getCases();
            CaseUtil.enrichLinkedCaseForUpdate(courtCase, courtCase.getAuditdetails());

            CaseUtil.enrichStatutAndSectionsForUpdate(courtCase, courtCase.getAuditdetails());

            CaseUtil.enrichLitigantsForUpdate(courtCase, courtCase.getAuditdetails());

            CaseUtil.enrichRepresentivesForUpdate(courtCase, courtCase.getAuditdetails());

            CaseUtil.enrichDocumentsForUpdate(courtCase);

            courtCase.getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            courtCase.getAuditdetails().setLastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid());

        } catch (Exception e) {
            log.error("Error enriching case application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in case enrichment service during case update process: " + e.getMessage());
        }
    }
}