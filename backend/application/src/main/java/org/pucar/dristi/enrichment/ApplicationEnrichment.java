package org.pucar.dristi.enrichment;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class ApplicationEnrichment {
    private final IdgenUtil idgenUtil;
    private final Configuration configuration;
    private final CaseUtil caseUtil;

    @Autowired
    public ApplicationEnrichment(IdgenUtil idgenUtil, Configuration configuration, CaseUtil caseUtil) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
        this.caseUtil = caseUtil;
    }

    public void enrichApplication(ApplicationRequest applicationRequest) {
        try {
            if (applicationRequest.getRequestInfo().getUserInfo() != null) {

                String idName = configuration.getApplicationConfig();
                String idFormat = configuration.getApplicationFormat();
                String tenantId = applicationRequest.getApplication().getFilingNumber().replace("-","");
                List<String> applicationIdList = idgenUtil.getIdList(applicationRequest.getRequestInfo(), tenantId, idName, idFormat, 1, false);

                Application application = applicationRequest.getApplication();
                application.setApplicationNumber(applicationRequest.getApplication().getFilingNumber() + "-" + applicationIdList.get(0));

                AuditDetails auditDetails = AuditDetails
                        .builder()
                        .createdBy(applicationRequest.getRequestInfo().getUserInfo().getUuid())
                        .createdTime(System.currentTimeMillis())
                        .lastModifiedBy(applicationRequest.getRequestInfo().getUserInfo().getUuid())
                        .lastModifiedTime(System.currentTimeMillis())
                        .build();
                application.setAuditDetails(auditDetails);
                application.setId(UUID.randomUUID());
                application.setCreatedDate(System.currentTimeMillis());
                application.setIsActive(true);

                if (application.getStatuteSection() != null) {
                    application.getStatuteSection().setId(UUID.randomUUID());
                    application.getStatuteSection().setAuditdetails(auditDetails);
                }
                if (application.getDocuments() != null) {
                    application.getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                    });
                }
            }
        } catch (CustomException e) {
            log.error("Exception occurred while enriching application");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while enriching application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichApplicationNumberByCMPNumber(ApplicationRequest applicationRequest) {
        try {
            CaseSearchRequest caseSearchRequest = createCaseSearchRequest(applicationRequest.getRequestInfo(), applicationRequest.getApplication());
            JsonNode caseDetails = caseUtil.searchCaseDetails(caseSearchRequest);
            if (caseDetails == null) {
                log.error("Case details not found for the filingNumber :: {}", applicationRequest.getApplication().getFilingNumber());
                throw new CustomException(ENRICHMENT_EXCEPTION, "Case details not found for the filingNumber: " + applicationRequest.getApplication().getFilingNumber());
            }

            String courtId = caseDetails.has("courtId") ? caseDetails.get("courtId").asText() : "";
            if (courtId == null || courtId.isEmpty()) {
                log.error("CourtId not found for the filingNumber :: {}", applicationRequest.getApplication().getFilingNumber());
                throw new CustomException(ENRICHMENT_EXCEPTION, "CourtId not found for the filingNumber :: " + applicationRequest.getApplication().getFilingNumber());
            }
            String idName = configuration.getCmpConfig();
            String idFormat = configuration.getCmpFormat();
            List<String> cmpNumberIdList = idgenUtil.getIdList(applicationRequest.getRequestInfo(), courtId, idName, idFormat, 1, false);
            applicationRequest.getApplication().setApplicationCMPNumber(cmpNumberIdList.get(0));
        } catch (CustomException e) {
            log.error("Custom Exception while enriching application number by CMP number: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Custom Exception in case enrichment service while enriching application number: " + e);
        } catch (Exception e) {
            log.error("Error enriching application number by CMP number: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in case enrichment service while enriching application number: " + e);
        }
    }

    public CaseSearchRequest createCaseSearchRequest(RequestInfo requestInfo, Application application) {
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setRequestInfo(requestInfo);
        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(application.getFilingNumber()).defaultFields(false).build();
        caseSearchRequest.addCriteriaItem(caseCriteria);
        return caseSearchRequest;
    }

    public void enrichApplicationUponUpdate(ApplicationRequest applicationRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            Application application = applicationRequest.getApplication();
            application.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            application.getAuditDetails().setLastModifiedBy(applicationRequest.getRequestInfo().getUserInfo().getUuid());

            if (application.getDocuments() != null) {
                application.getDocuments().forEach(document -> {
                    if (document.getId() == null)
                        document.setId(String.valueOf(UUID.randomUUID()));
                });
            }
        } catch (Exception e) {
            log.error("Error enriching application upon update: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error enriching application upon update: " + e.getMessage());
        }
    }

    public void enrichCommentUponCreate(Comment comment, AuditDetails auditDetails) {
        try {
            comment.setId(UUID.randomUUID());
            comment.setAuditdetails(auditDetails);
        } catch (Exception e) {
            log.error("Error enriching comment upon create: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error enriching comment upon create: " + e.getMessage());
        }
    }
}
