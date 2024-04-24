package org.pucar.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateRepository;
import org.pucar.service.IndividualService;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.pucar.config.ServiceConstants.*;

@Component
public class AdvocateRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private AdvocateRepository repository;

    public void validateAdvocateRegistration(AdvocateRequest advocateRequest) throws CustomException{
        RequestInfo requestInfo = advocateRequest.getRequestInfo();

        advocateRequest.getAdvocates().forEach(advocate -> {
            if(ObjectUtils.isEmpty(advocate.getTenantId()) || ObjectUtils.isEmpty(advocate.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"tenantId and individualId are mandatory for creating advocate");
            }
            if(!individualService.searchIndividual(requestInfo,advocate.getIndividualId()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
        });
    }

    public Advocate validateApplicationExistence(Advocate advocate) {
        List<Advocate> existingApplications = repository.getApplications(Collections.singletonList(AdvocateSearchCriteria.builder().applicationNumber(advocate.getApplicationNumber()).build()), new ArrayList<>());
        if(existingApplications.isEmpty()) throw new CustomException(VALIDATION_EXCEPTION,"Advocate Application does not exist");
        return existingApplications.get(0);
    }
}