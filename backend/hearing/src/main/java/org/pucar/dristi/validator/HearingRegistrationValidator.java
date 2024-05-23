package org.pucar.dristi.validator;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


import java.util.HashMap;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class HearingRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private HearingRepository repository;


    /**
     * @param hearingRequest hearing application request
     * @throws CustomException VALIDATION_EXCEPTION -> if tenantId or hearingType not present
     *                         ILLEGAL_ARGUMENT_EXCEPTION_CODE-> if individualId doesn't exist
     */
    public void validateHearingRegistration(HearingRequest hearingRequest) throws CustomException {
        RequestInfo requestInfo = hearingRequest.getRequestInfo();
        if (requestInfo.getUserInfo() == null || requestInfo.getUserInfo().getTenantId() == null)
            throw new CustomException(VALIDATION_EXCEPTION, "User info not found!!!");

        if (ObjectUtils.isEmpty(hearingRequest.getHearing().getTenantId()) || ObjectUtils.isEmpty(hearingRequest.getHearing().getHearingType())) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, "tenantId and hearing type are mandatory for creating hearing");
        }
        hearingRequest.getHearing().getAttendees().forEach(attendee -> {
            if(ObjectUtils.isEmpty(attendee.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"individualId is mandatory for attendee");
            }
            //searching individual exist or not
            if(!individualService.searchIndividual(requestInfo,attendee.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
        });
        // TODO validate filing numbers
        //  cnr numbers,
        //  application numbers,
        //  hearing type,
        //  presided by judge.

    }

    /**
     * @param hearing hearing details
     * @throws CustomException VALIDATION_EXCEPTION -> if application is not present
     */
    public Hearing validateApplicationExistence(Hearing hearing) {
        //checking if application exist or not
        List<Hearing> existingApplications = repository.getHearings(hearing);
        log.info("Existing Applications :: {}", existingApplications);
        if (existingApplications.isEmpty())
            throw new CustomException(VALIDATION_EXCEPTION, "Hearing Application does not exist");
        return existingApplications.get(0);
    }
}