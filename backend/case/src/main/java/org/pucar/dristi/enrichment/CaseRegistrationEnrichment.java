package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.util.UserUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class CaseRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;
    @Autowired
    private Configuration config;
    @Autowired
    private UserUtil userUtils;

    public void enrichCaseRegistration(CaseRequest caseRequest) {
        try {
            if(caseRequest.getRequestInfo().getUserInfo() != null) {
                List<String> courtCaseRegistrationIdList = idgenUtil.getIdList(caseRequest.getRequestInfo(), caseRequest.getCases().get(0).getTenantId(), config.getCaseFilingNumber(), null, caseRequest.getCases().size());
                Integer index = 0;
                for (CourtCase courtCase : caseRequest.getCases()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    courtCase.setAuditdetails(auditDetails);

                    courtCase.setId(UUID.randomUUID());

                    courtCase.getStatutesAndSections().forEach(statuteSection -> {
                        statuteSection.setId(UUID.randomUUID());
                        statuteSection.setStrSections(listToString(statuteSection.getSections()));
                        statuteSection.setStrSubsections(listToString(statuteSection.getSubsections()));
                        statuteSection.setAuditdetails(auditDetails);
                    });

                    courtCase.getLitigants().forEach(party -> {
                        party.setId((UUID.randomUUID()));
                        party.setAuditDetails(auditDetails);
                        if (party.getDocuments()!=null){
                            party.getDocuments().forEach(document -> {
                                document.setId(String.valueOf(UUID.randomUUID()));
                                document.setDocumentUid(document.getId());
                            });
                        }
                    });

                    courtCase.getRepresentatives().forEach(advocateMapping -> {
                        advocateMapping.setId(String.valueOf(UUID.randomUUID()));
                        advocateMapping.setAuditDetails(auditDetails);
                        if (advocateMapping.getDocuments()!=null){
                            advocateMapping.getDocuments().forEach(document -> {
                                document.setId(String.valueOf(UUID.randomUUID()));
                                document.setDocumentUid(document.getId());
                            });
                        }

                        advocateMapping.getRepresenting().forEach(party -> {
                            party.setId((UUID.randomUUID()));
                            party.setCaseId(courtCase.getId().toString());
                            party.setAuditDetails(auditDetails);
                            if (party.getDocuments()!=null){
                                party.getDocuments().forEach(document -> {
                                    document.setId(String.valueOf(UUID.randomUUID()));
                                    document.setDocumentUid(document.getId());
                                });
                            }
                        });
                    });

//                    courtCase.setIsActive(false);
                    if (courtCase.getDocuments()!=null){
                        courtCase.getDocuments().forEach(document -> {
                            document.setId(String.valueOf(UUID.randomUUID()));
                            document.setDocumentUid(document.getId());
                        });
                    }

                    courtCase.setFilingNumber(courtCaseRegistrationIdList.get(index++));
                    courtCase.setCaseNumber(courtCase.getFilingNumber());
                  //  courtCase.setCourtCaseNumber(courtCase.getCourtCaseNumber());
                  //  courtCase.setCnrNumber(courtCase.getCnrNumber());
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
            throw e;
        }
    }

    public String listToString(List<String> list){
        StringBuilder stB = new StringBuilder();
        boolean isFirst = true;
        for (String doc : list) {
            if(isFirst){
                isFirst = false;
                stB.append(doc);
            }
            else{
                stB.append(","+doc);
            }
        }

        return stB.toString();
    }
    public void enrichCaseApplicationUponUpdate(CaseRequest caseRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            for (CourtCase courtCase : caseRequest.getCases()) {
                courtCase.getAuditdetails().setLastModifiedTime(System.currentTimeMillis());
                courtCase.getAuditdetails().setLastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid());
            }
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update: {}", e.getMessage());
            // Handle the exception or throw a custom exception
            throw new CustomException("ENRICHMENT_EXCEPTION","Error in advocate enrichment service during advocate update process: "+ e.getMessage());
        }
    }
}