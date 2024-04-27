package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.util.UserUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class CaseRegistrationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserUtil userUtils;

    public void enrichCaseRegistration(CaseRequest caseRequest) {
        try {
            if(caseRequest.getRequestInfo().getUserInfo() != null) {
                List<String> courtCaseRegistrationIdList = idgenUtil.getIdList(caseRequest.getRequestInfo(), caseRequest.getCases().get(0).getTenantId(), "case.id", null, caseRequest.getCases().size());
                Integer index = 0;
                for (CourtCase courtCase : caseRequest.getCases()) {
                    AuditDetails auditDetails = AuditDetails.builder().createdBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(caseRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                    courtCase.setAuditdetails(auditDetails);

                    courtCase.setId(UUID.randomUUID());
                    courtCase.getLinkedCases().forEach(linkedCase -> {
                        linkedCase.setId(UUID.randomUUID());
                        linkedCase.getDocuments().forEach(document -> {
                            document.setId(String.valueOf(UUID.randomUUID()));
                            document.setDocumentUid(document.getId());
                        });
                    });

                    courtCase.getStatutesAndSections().forEach(statuteSection -> {
                        statuteSection.setId(UUID.randomUUID());
                    });

                    courtCase.getLitigants().forEach(party -> {
                        party.setId(String.valueOf(UUID.randomUUID()));
                        if (party.getDocuments()!=null){
                            party.getDocuments().forEach(document -> {
                                document.setId(String.valueOf(UUID.randomUUID()));
                                document.setDocumentUid(document.getId());
                            });
                        }
                    });

                    courtCase.getRepresentatives().forEach(advocateMapping -> {
                        advocateMapping.setId(String.valueOf(UUID.randomUUID()));
                        if (advocateMapping.getDocuments()!=null){
                            advocateMapping.getDocuments().forEach(document -> {
                                document.setId(String.valueOf(UUID.randomUUID()));
                                document.setDocumentUid(document.getId());
                            });
                        }

                        advocateMapping.getRepresenting().forEach(party -> {
                            party.setId(String.valueOf(UUID.randomUUID()));
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