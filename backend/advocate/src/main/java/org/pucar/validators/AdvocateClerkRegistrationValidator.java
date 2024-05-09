package org.pucar.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.service.IndividualService;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.config.ServiceConstants.INDIVIDUAL_NOT_FOUND;
import static org.pucar.config.ServiceConstants.VALIDATION_EXCEPTION;

@Component
public class AdvocateClerkRegistrationValidator {
    @Autowired
    private IndividualService individualService;

    @Autowired
    private AdvocateClerkRepository repository;

    public void validateAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) throws CustomException{
        RequestInfo requestInfo = advocateClerkRequest.getRequestInfo();

        advocateClerkRequest.getClerks().forEach(clerk -> {
            if(ObjectUtils.isEmpty(clerk.getTenantId()) || ObjectUtils.isEmpty(clerk.getIndividualId()))
                throw new CustomException("ILLEGAL_ARGUMENT_EXCEPTION_CODE", "tenantId and individualId are mandatory for creating advocate clerk");
            if (!individualService.searchIndividual(requestInfo,clerk.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
        });
    }
    public AdvocateClerk validateApplicationExistence(AdvocateClerk advocateClerk) {
        List<AdvocateClerk> existingApplications =  repository.getApplications(Collections.singletonList(AdvocateClerkSearchCriteria.builder().applicationNumber(advocateClerk.getApplicationNumber()).build()), new ArrayList<>(), new String(), new AtomicReference<>(false), 1,0, new Pagination());
        if(existingApplications.isEmpty()) throw new CustomException(VALIDATION_EXCEPTION,"Advocate clerk Application does not exist");
        return existingApplications.get(0);
    }
}