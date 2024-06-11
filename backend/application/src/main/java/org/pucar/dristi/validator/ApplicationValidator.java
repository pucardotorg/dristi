package org.pucar.dristi.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
public class ApplicationValidator {
    @Autowired
    private CaseUtil caseUtil;
    @Autowired
    private ApplicationRepository repository;
    public void validateApplication(ApplicationRequest applicationRequest) throws CustomException {
        RequestInfo requestInfo = applicationRequest.getRequestInfo();
        Application application = applicationRequest.getApplication();

        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo, application);

        if(!caseUtil.fetchCaseDetails(caseExistsRequest)){
            throw new CustomException(VALIDATION_ERR, "case does not exist");
            }
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
//        if(ObjectUtils.isEmpty(application.getCnrNumber())){
//                throw new CustomException(CREATE_APPLICATION_ERR, "cnrNumber is mandatory for creating application");
//            }
        if(ObjectUtils.isEmpty(application.getFilingNumber())){
                throw new CustomException(CREATE_APPLICATION_ERR, "filingNumber is mandatory for creating application");
            }
        if(ObjectUtils.isEmpty(application.getReferenceId())){
                throw new CustomException(CREATE_APPLICATION_ERR, "referenceId is mandatory for creating application");
            }

            //TODO ADD ALL THE VALIDATION FOR CREATING APPLICATION
    }
    public Boolean validateApplicationExistence(RequestInfo requestInfo ,Application application) {
        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo, application);

        if(!caseUtil.fetchCaseDetails(caseExistsRequest)){
            throw new CustomException(VALIDATION_ERR, "case does not exist");
        }
        if(ObjectUtils.isEmpty(application.getId())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "id is mandatory for updating application");
        }
//        if(ObjectUtils.isEmpty(application.getCnrNumber())){
//            throw new CustomException(UPDATE_APPLICATION_ERR, "cnrNumber is mandatory for updating application");
//        }
        if(ObjectUtils.isEmpty(application.getFilingNumber())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "filingNumber is mandatory for updating application");
        }
        if(ObjectUtils.isEmpty(application.getReferenceId())){
            throw new CustomException(UPDATE_APPLICATION_ERR, "referenceId is mandatory for updating application");
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
