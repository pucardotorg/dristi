package org.pucar.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.service.IndividualService;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class AdvocateClerkRegistrationValidator {
    @Autowired
    private IndividualService individualService;

    @Autowired
    private AdvocateClerkRepository repository;

    public void validateAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) throws CustomException{
        RequestInfo requestInfo = advocateClerkRequest.getRequestInfo();

        advocateClerkRequest.getClerks().forEach(clerk -> {
            if(ObjectUtils.isEmpty(clerk.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating advocate");
            if (!individualService.searchIndividual(requestInfo,clerk.getIndividualId()))
                throw new IllegalArgumentException("Individual not found");
        });
    }
    public AdvocateClerk validateApplicationExistence(AdvocateClerk advocateClerk) {
        return repository.getApplications(Collections.singletonList(AdvocateClerkSearchCriteria.builder().applicationNumber(advocateClerk.getApplicationNumber()).build()), new ArrayList<>(), new String()).get(0);
    }
}