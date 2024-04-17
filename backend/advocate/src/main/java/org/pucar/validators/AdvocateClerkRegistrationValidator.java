package org.pucar.validators;

import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AdvocateClerkRegistrationValidator {

    @Autowired
    private AdvocateClerkRepository repository;

    public void validateAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) throws CustomException{
        advocateClerkRequest.getClerks().forEach(cases -> {
            if(ObjectUtils.isEmpty(cases.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating advocate");
        });
    }
    public AdvocateClerk validateApplicationExistence(AdvocateClerk advocateClerk) {
        return repository.getApplications(AdvocateClerkSearchCriteria.builder().applicationNumber(advocateClerk.getApplicationNumber()).build()).get(0);
    }
}