package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class AdvocateRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private AdvocateRepository repository;


    /**
     * @param advocateRequest  advocate application request
     * @throws CustomException VALIDATION_EXCEPTION -> if tenantId or individualId not present
     * INDIVIDUAL_NOT_FOUND-> if individualId doesn't exist
     */
    public void validateAdvocateRegistration(AdvocateRequest advocateRequest) throws CustomException{
        RequestInfo requestInfo = advocateRequest.getRequestInfo();
         if(requestInfo.getUserInfo() == null)
            throw new CustomException(VALIDATION_EXCEPTION,"User info not found!!!");
        advocateRequest.getAdvocates().forEach(advocate -> {
            if(ObjectUtils.isEmpty(advocate.getTenantId()) || ObjectUtils.isEmpty(advocate.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"tenantId and individualId are mandatory for creating advocate");
            }
            //searching individual exist or not
            if(!individualService.searchIndividual(requestInfo,advocate.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
        });
    }

    /**
     * @param advocate  advocate details
     * @throws CustomException VALIDATION_EXCEPTION -> if application is not present
     */
    public Advocate validateApplicationExistence(Advocate advocate) {
        //checking if application exist or not
        List<Advocate> existingApplications = repository.getApplications(Collections.singletonList(AdvocateSearchCriteria.builder().applicationNumber(advocate.getApplicationNumber()).build()), new ArrayList<>(), new String(), new AtomicReference<>(false),1,0);
        log.info("Existing Applications :: {}", existingApplications);
        if(existingApplications.isEmpty()) throw new CustomException(VALIDATION_EXCEPTION,"Advocate Application does not exist");
        return existingApplications.get(0);
    }
}