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

@Component
public class AdvocateRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private AdvocateRepository repository;

    public void validateAdvocateRegistration(AdvocateRequest advocateRequest) throws CustomException{
        RequestInfo requestInfo = advocateRequest.getRequestInfo();

        advocateRequest.getAdvocates().forEach(advocate -> {
            if(ObjectUtils.isEmpty(advocate.getTenantId()))
                throw new CustomException("EG_ADV_APP_ERR", "tenantId is mandatory for creating advocate");
            if(!individualService.searchIndividual(requestInfo,advocate.getIndividualId()))
                throw new IllegalArgumentException("Individual Id doesn't exist");
        });
    }

    public Advocate validateApplicationExistence(Advocate advocate) {
        return repository.getApplications(Collections.singletonList(AdvocateSearchCriteria.builder().applicationNumber(advocate.getApplicationNumber()).build()), new ArrayList<>()).get(0);
    }
}