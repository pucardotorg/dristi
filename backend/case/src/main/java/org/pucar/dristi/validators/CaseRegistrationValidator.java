package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class CaseRegistrationValidator {
    @Autowired
    private IndividualService individualService;

    public void validateCaseRegistration(CaseRequest caseRequest) throws CustomException{
        RequestInfo requestInfo = caseRequest.getRequestInfo();

        caseRequest.getCases().forEach(courtCase -> {
            if(ObjectUtils.isEmpty(courtCase.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating advocate");
            if (!individualService.searchIndividual(requestInfo,courtCase.getLitigants().get(0).getIndividualId()))
                throw new IllegalArgumentException("Individual not found");
        });
    }
}