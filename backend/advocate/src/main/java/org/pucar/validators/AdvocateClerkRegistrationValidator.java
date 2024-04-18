package org.pucar.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateClerkRegistrationRepository;
import org.pucar.service.IndividualService;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AdvocateClerkRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private AdvocateClerkRegistrationRepository repository;

    public void validateAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) throws CustomException{
        RequestInfo requestInfo = advocateClerkRequest.getRequestInfo();

        advocateClerkRequest.getClerks().forEach(clerk -> {
            if(ObjectUtils.isEmpty(clerk.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating advocate");
            if (!individualService.searchIndividual(requestInfo,clerk.getIndividualId()))
                throw new IllegalArgumentException("Individual not found");
        });
    }
}