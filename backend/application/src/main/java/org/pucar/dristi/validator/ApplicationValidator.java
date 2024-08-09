package org.pucar.dristi.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.ORDER_EXCEPTION;

@Component
public class ApplicationValidator {
    private final ApplicationRepository repository;
    private final CaseUtil caseUtil;
    private final OrderUtil orderUtil;
    private final FileStoreUtil fileStoreUtil;
    @Autowired
    public ApplicationValidator(ApplicationRepository repository, CaseUtil caseUtil, OrderUtil orderUtil, FileStoreUtil fileStoreUtil) {
        this.repository = repository;
        this.caseUtil = caseUtil;
        this.orderUtil = orderUtil;
        this.fileStoreUtil = fileStoreUtil;
    }

    public void validateApplication(ApplicationRequest applicationRequest) throws CustomException {
        RequestInfo requestInfo = applicationRequest.getRequestInfo();
        Application application = applicationRequest.getApplication();

        //validate documents
        validateDocuments(application);

        if(ObjectUtils.isEmpty(application.getCaseId())) {
            throw new CustomException(VALIDATION_ERR, "caseId is mandatory for creating application");
        }

        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo, application);

        if(!caseUtil.fetchCaseDetails(caseExistsRequest)){
            throw new CustomException(VALIDATION_ERR, "case does not exist");
            }
    }

    public Boolean validateApplicationExistence(RequestInfo requestInfo ,Application application) {
        //validate documents
        validateDocuments(application);

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
    public OrderExistsRequest createOrderExistRequest(RequestInfo requestInfo, Application application){
        OrderExistsRequest orderExistsRequest = new OrderExistsRequest();
        orderExistsRequest.setRequestInfo(requestInfo);
        List<OrderExists> criteriaList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderId(application.getReferenceId());
        criteriaList.add(orderExists);
        orderExistsRequest.setOrder(criteriaList);
        return orderExistsRequest;
    }
    public void validateOrderDetails(ApplicationRequest applicationRequest) {
        if (applicationRequest.getApplication().getReferenceId() != null) {
            OrderExistsRequest orderExistsRequest = createOrderExistRequest(applicationRequest.getRequestInfo(), applicationRequest.getApplication());
            if (!orderUtil.fetchOrderDetails(orderExistsRequest)) {
                throw new CustomException(ORDER_EXCEPTION, "Order does not exist");
            }
        }
    }

    private void validateDocuments(Application application){
        if (application.getDocuments() != null && !application.getDocuments().isEmpty()) {
            application.getDocuments().forEach(document -> {
                if (document.getFileStore() != null) {
                    if (!fileStoreUtil.doesFileExist(application.getTenantId(), document.getFileStore()))
                        throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
                } else
                    throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);

            });
        }
    }
}
