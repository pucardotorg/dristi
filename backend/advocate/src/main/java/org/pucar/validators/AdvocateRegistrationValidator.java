package org.pucar.validators;

import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateRegistrationRepository;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AdvocateRegistrationValidator {

    @Autowired
    private AdvocateRegistrationRepository repository;

    public void validateAdvocateRegistration(AdvocateRequest advocateRequest) throws CustomException{
        advocateRequest.getAdvocates().forEach(cases -> {
            if(ObjectUtils.isEmpty(cases.getTenantId()))
                throw new CustomException("EG_ADV_APP_ERR", "tenantId is mandatory for creating advocate");
        });
    }
}