package org.pucar.dristi.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
public class ApplicationValidator {
    private final ApplicationRepository repository;
    private final CaseUtil caseUtil;

    @Autowired
    public ApplicationValidator(ApplicationRepository repository, CaseUtil caseUtil) {
        this.repository = repository;
        this.caseUtil = caseUtil;
    }

    public void validateApplication(ApplicationRequest applicationRequest) throws CustomException {
        RequestInfo requestInfo = applicationRequest.getRequestInfo();
        Application application = applicationRequest.getApplication();

        if(ObjectUtils.isEmpty(application.getCaseId())) {
            throw new CustomException(VALIDATION_ERR, "caseId is mandatory for creating application");
        }

        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo, application);

        if(!caseUtil.fetchCaseDetails(caseExistsRequest)){
            throw new CustomException(VALIDATION_ERR, "case does not exist");
            }
        if(ObjectUtils.isEmpty(application.getTenantId())) {
                throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for creating application");
            }
        if(ObjectUtils.isEmpty(application.getApplicationType())){
            throw new CustomException(VALIDATION_ERR, "applicationType is mandatory for creating application");
        }
    }

    public Boolean validateApplicationExistence(RequestInfo requestInfo ,Application application) {
        if(ObjectUtils.isEmpty(application.getCaseId())) {
            throw new CustomException(VALIDATION_ERR, "caseId is mandatory for updating application");
        }
        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo, application);
        if(!caseUtil.fetchCaseDetails(caseExistsRequest)){
            throw new CustomException(VALIDATION_ERR, "case does not exist");
        }
        if(ObjectUtils.isEmpty(application.getId())){
            throw new CustomException(VALIDATION_ERR, "id is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getTenantId())) {
            throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getApplicationType())){
            throw new CustomException(VALIDATION_ERR, "applicationType is mandatory for updating application");
        }

        ApplicationExists applicationExists = new ApplicationExists();
        applicationExists.setFilingNumber(application.getFilingNumber());
        applicationExists.setCnrNumber(application.getCnrNumber());
        applicationExists.setApplicationNumber(application.getApplicationNumber());
        List<ApplicationExists> criteriaList = new ArrayList<>();
        criteriaList.add(applicationExists);
        List<ApplicationExists> applicationExistsList = repository.checkApplicationExists(criteriaList);

        return applicationExistsList.get(0).getExists();
    }

    public CaseExistsRequest createCaseExistsRequest(RequestInfo requestInfo, Application application){
        CaseExistsRequest caseExistsRequest = new CaseExistsRequest();
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(application.getCaseId());
        caseExists.setFilingNumber(application.getFilingNumber());
        caseExists.setCnrNumber(application.getCnrNumber());
        List<CaseExists> criteriaList = new ArrayList<>();
        criteriaList.add(caseExists);
        caseExistsRequest.setRequestInfo(requestInfo);
        caseExistsRequest.setCriteria(criteriaList);
        return caseExistsRequest;
    }
}
