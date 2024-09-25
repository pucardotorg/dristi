package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class ApplicationEnrichment {
    private final IdgenUtil idgenUtil;
    private final Configuration configuration;

    @Autowired
    public ApplicationEnrichment(IdgenUtil idgenUtil, Configuration configuration) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
    }

    public void enrichApplication(ApplicationRequest applicationRequest) {
        try {
            if(applicationRequest.getRequestInfo().getUserInfo() != null) {
                String tenantId = applicationRequest.getApplication().getCnrNumber();
                String idName = configuration.getApplicationConfig();
                String idFormat = configuration.getApplicationFormat();

                List<String> applicationIdList = idgenUtil.getIdList(applicationRequest.getRequestInfo(), tenantId, idName, idFormat, 1,false);
                Application application = applicationRequest.getApplication();
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
                application.setApplicationNumber(application.getCnrNumber()+"-"+applicationIdList.get(0));

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
        }
        catch (CustomException e){
            log.error("Exception occurred while enriching application");
            throw e;
        }
        catch (Exception e) {
            log.error("Error occurred while enriching application: {}", e.getMessage());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichApplicationUponUpdate(ApplicationRequest applicationRequest) {
            try {
                // Enrich lastModifiedTime and lastModifiedBy in case of update
                Application application = applicationRequest.getApplication();
                application.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                application.getAuditDetails().setLastModifiedBy(applicationRequest.getRequestInfo().getUserInfo().getUuid());

                if (application.getDocuments() != null) {
                    application.getDocuments().forEach(document -> {
                        if(document.getId()==null)
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
