package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_NOT_FOUND;
import static org.pucar.dristi.config.ServiceConstants.VALIDATION_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkRegistrationValidator {
    @Autowired
    private IndividualService individualService;

    @Autowired
    private AdvocateClerkRepository repository;

    /**
     * @param advocateClerkRequest  advocate clerk application request
     * @throws CustomException VALIDATION_EXCEPTION -> if tenantId or individualId not present
     * INDIVIDUAL_NOT_FOUND-> if individualId doesn't exist
     */
    public void validateAdvocateClerkRegistration(AdvocateClerkRequest advocateClerkRequest) throws CustomException{
        RequestInfo requestInfo = advocateClerkRequest.getRequestInfo();

        advocateClerkRequest.getClerks().forEach(clerk -> {
            if(ObjectUtils.isEmpty(clerk.getTenantId()) || ObjectUtils.isEmpty(clerk.getIndividualId()))
                throw new CustomException(VALIDATION_EXCEPTION, "tenantId and individualId are mandatory for creating advocate clerk");

            //searching individual exist or not
            if (!individualService.searchIndividual(requestInfo,clerk.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
        });
    }

    /**
     * @param advocateClerk  advocate clerk details
     * @throws CustomException VALIDATION_EXCEPTION -> if application is not present
     */
    public AdvocateClerk validateApplicationExistence(AdvocateClerk advocateClerk) {
        //checking if application exist or not
        List<AdvocateClerk> existingApplications =  repository.getApplications(Collections.singletonList(AdvocateClerkSearchCriteria.builder().applicationNumber(advocateClerk.getApplicationNumber()).build()), new ArrayList<>(), new String(), new AtomicReference<>(false), 1,0);
        log.info("Existing Applications :: {}", existingApplications);
        if(existingApplications.isEmpty()) throw new CustomException(VALIDATION_EXCEPTION,"Advocate clerk Application does not exist");
        return existingApplications.get(0);
    }
}