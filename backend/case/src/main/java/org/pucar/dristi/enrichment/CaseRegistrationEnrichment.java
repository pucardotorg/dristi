package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.AdvocateUtil;
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

    @Autowired
    private CaseUtil caseUtil;

    public void enrichCaseRegistration(CaseRequest caseRequest) {
        try {
            CourtCase courtCase = caseRequest.getCases();

            List<String> courtCaseRegistrationFillingNumberIdList = idgenUtil.getIdList(caseRequest.getRequestInfo(), courtCase.getTenantId(), config.getCaseFilingNumberCp(), null, 1);
            List<String> courtCaseRegistrationCaseNumberIdList = idgenUtil.getIdList(caseRequest.getRequestInfo(), courtCase.getTenantId(), config.getCaseNumberCc(), null, 1);
            log.info("Court Case Registration Filling Number cp Id List :: {}", courtCaseRegistrationFillingNumberIdList);
            log.info("Court Case Registration Case Number CC Id List :: {}", courtCaseRegistrationCaseNumberIdList);
            AuditDetails auditDetails = AuditDetails.builder().createdBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            courtCase.setAuditdetails(auditDetails);

            courtCase.setId(UUID.randomUUID());
            courtCase.getLinkedCases().forEach(linkedCase -> {
                linkedCase.setId(UUID.randomUUID());
                linkedCase.setAuditdetails(auditDetails);
                linkedCase.getDocuments().forEach(document -> {
                    document.setId(String.valueOf(UUID.randomUUID()));
                    document.setDocumentUid(document.getId());
                });
            });

            courtCase.getStatutesAndSections().forEach(statuteSection -> {
                statuteSection.setId(UUID.randomUUID());
                statuteSection.setStrSections(listToString(statuteSection.getSections()));
                statuteSection.setStrSubsections(listToString(statuteSection.getSubsections()));
                statuteSection.setAuditdetails(auditDetails);
            });

            courtCase.getLitigants().forEach(party -> {
                party.setId((UUID.randomUUID()));
                party.setAuditDetails(auditDetails);
                if (party.getDocuments() != null) {
                    party.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });
                }
            });

            courtCase.getRepresentatives().forEach(advocateMapping -> {
                advocateMapping.setId(String.valueOf(UUID.randomUUID()));
                advocateMapping.setAuditDetails(auditDetails);
                if (advocateMapping.getDocuments() != null) {
                    advocateMapping.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });
                }

                advocateMapping.getRepresenting().forEach(party -> {
                    party.setId((UUID.randomUUID()));
                    party.setCaseId(courtCase.getId().toString());
                    party.setAuditDetails(auditDetails);
                    if (party.getDocuments() != null) {
                        party.getDocuments().forEach(document -> {
                            document.setId(String.valueOf(UUID.randomUUID()));
                            document.setDocumentUid(document.getId());
                        });
                    }
                });
            });

//                    courtCase.setIsActive(false);
            if (courtCase.getDocuments() != null) {
                courtCase.getDocuments().forEach(document -> {
                    document.setId(String.valueOf(UUID.randomUUID()));
                    document.setDocumentUid(document.getId());
                });
            }

            courtCase.setFilingNumber(courtCaseRegistrationFillingNumberIdList.get(0));
            courtCase.setCaseNumber(courtCaseRegistrationCaseNumberIdList.get(0));
            courtCase.setCourCaseNumber(caseUtil.getCNRNumber(courtCase.getFilingNumber()));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error enriching case application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public String listToString(List<String> list) {
        StringBuilder stB = new StringBuilder();
        boolean isFirst = true;
        for (String doc : list) {
            if (isFirst) {
                isFirst = false;
                stB.append(doc);
            } else {
                stB.append("," + doc);
            }
        }

        return stB.toString();
    }

    public void enrichCaseApplicationUponUpdate(CaseRequest caseRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            CourtCase courtCase = caseRequest.getCases();
            courtCase.getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
            courtCase.getAuditdetails().setLastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid());

        } catch (Exception e) {
            log.error("Error enriching case application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in case enrichment service during case update process: " + e.getMessage());
        }
    }
}