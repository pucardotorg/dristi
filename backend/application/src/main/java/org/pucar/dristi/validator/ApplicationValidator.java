package org.pucar.dristi.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
public class ApplicationValidator {
    @Autowired
    private ApplicationRepository repository;
    public void validateApplication(ApplicationRequest applicationRequest) throws CustomException {
        RequestInfo requestInfo = applicationRequest.getRequestInfo();

        Application application = applicationRequest.getApplication();

            if(ObjectUtils.isEmpty(application.getTenantId())) {
                throw new CustomException(CREATE_APPLICATION_ERR, "tenantId is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getCreatedDate())) {
                throw new CustomException(CREATE_APPLICATION_ERR, "createdDate is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getCreatedBy())) {
                throw new CustomException(CREATE_APPLICATION_ERR, "createdBy is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(applicationRequest.getRequestInfo().getUserInfo())){
                throw new CustomException(CREATE_APPLICATION_ERR, "user info is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getOnBehalfOf())){
                throw new CustomException(CREATE_APPLICATION_ERR, "onBehalfOf is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getCnrNumber())){
                throw new CustomException(CREATE_APPLICATION_ERR, "cnrNumber is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getFilingNumber())){
                throw new CustomException(CREATE_APPLICATION_ERR, "filingNumber is mandatory for creating application");
            }
            if(ObjectUtils.isEmpty(application.getReferenceId())){
                throw new CustomException(CREATE_APPLICATION_ERR, "referenceId is mandatory for creating application");
            }

            //TODO ADD ALL THE VALIDATION FOR CREATING APPLICATION
    }
    public Application validateApplicationExistence(RequestInfo requestInfo ,Application application) {
        if(ObjectUtils.isEmpty(application.getId())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "id is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getCnrNumber())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "cnrNumber is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getFilingNumber())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "filingNumber is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getReferenceId())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "referenceId is mandatory for updating application");
        }
        Application existingApplication = repository.getApplications(application.getId(), application.getFilingNumber(),
                application.getCnrNumber(), application.getTenantId(), null, null);//fixme pass limit and offset
// TODO get existing application
        if(existingApplication == null) {
            throw new CustomException(VALIDATION_ERR, "Application does not exist");
        }        return existingApplication;
    }
}
