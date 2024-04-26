package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Component
public class CaseRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    CaseRepository repository;
    public void validateCaseRegistration(CaseRequest caseRequest) throws CustomException{
        RequestInfo requestInfo = caseRequest.getRequestInfo();

        caseRequest.getCases().forEach(courtCase -> {
            if(ObjectUtils.isEmpty(courtCase.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating advocate");
            if (!individualService.searchIndividual(requestInfo,courtCase.getLitigants().get(0).getIndividualId()))
                throw new CustomException("INDIVIDUAL_NOT_FOUND","Requested Individual not found or does not exist");
        });
    }
    public CourtCase validateApplicationExistence(CourtCase courtCase) {
        List<CourtCase> existingApplications = repository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).build()));
        if(existingApplications.isEmpty()) throw new CustomException("VALIDATION EXCEPTION","Advocate Application does not exist");
        return existingApplications.get(0);
    }
}