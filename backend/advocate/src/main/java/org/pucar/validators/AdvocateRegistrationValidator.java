package org.pucar.validators;

import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateRepository;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdvocateRegistrationValidator {

    @Autowired
    private AdvocateRepository repository;

    public void validateAdvocateRegistration(AdvocateRequest advocateRequest) throws CustomException{
        advocateRequest.getAdvocates().forEach(cases -> {
            if(ObjectUtils.isEmpty(cases.getTenantId()))
                throw new CustomException("EG_ADV_APP_ERR", "tenantId is mandatory for creating advocate");
        });
    }

    public Advocate validateApplicationExistence(Advocate advocate) {
        return repository.getApplications(AdvocateSearchCriteria.builder().applicationNumber(advocate.getApplicationNumber()).build()).get(0);
    }
}