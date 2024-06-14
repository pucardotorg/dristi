package org.pucar.dristi.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.ApplicationUtil;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class HearingRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private HearingRepository repository;
    @Autowired
    private MdmsUtil mdmsUtil;
    @Autowired
    private Configuration config;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CaseUtil caseUtil;
    @Autowired
    private ApplicationUtil applicationUtil;

    /**
     * @param hearingRequest hearing application request
     * @throws CustomException VALIDATION_EXCEPTION -> if tenantId or hearingType not present
     *                         ILLEGAL_ARGUMENT_EXCEPTION_CODE-> if individualId doesn't exist
     */
    public void validateHearingRegistration(HearingRequest hearingRequest) throws CustomException {
        RequestInfo requestInfo = hearingRequest.getRequestInfo();
        Hearing hearing = hearingRequest.getHearing();
        if (requestInfo.getUserInfo() == null || requestInfo.getUserInfo().getTenantId() == null)
            throw new CustomException(VALIDATION_EXCEPTION, "User info not found!!!");

        if (ObjectUtils.isEmpty(hearing.getTenantId()) || ObjectUtils.isEmpty(hearing.getHearingType())) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, "tenantId and hearing type are mandatory for creating hearing");
        }
        hearing.getAttendees().forEach(attendee -> {
            if(ObjectUtils.isEmpty(attendee.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"individualId is mandatory for attendee");
            }
            //searching individual exist or not
            if(!individualService.searchIndividual(requestInfo,attendee.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist. ID: "+ attendee.getIndividualId());
        });

        // Validating Hearing Type
        JSONArray hearingTypeList = mdmsUtil.fetchMdmsData(requestInfo,requestInfo.getUserInfo().getTenantId(),config.getMdmsHearingModuleName(),Collections.singletonList(config.getMdmsHearingTypeMasterName()))
                .get(config.getMdmsHearingModuleName()).get(config.getMdmsHearingTypeMasterName());

        Boolean validateHearingType = false;
        for (int i = 0; i < hearingTypeList.size(); i++) {
            HearingType hearingType = mapper.convertValue(hearingTypeList.get(i), HearingType.class);
            if(hearingType.getType().equals(hearing.getHearingType())) validateHearingType = true;
        }
        if (!validateHearingType)
            throw new CustomException(VALIDATION_EXCEPTION, "Could not validate Hearing Type!!!");

        // Validate cnrNumbers and filingNumbers
        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(requestInfo,hearing);
        CaseExistsResponse caseExistsResponse = caseUtil.fetchCaseDetails(caseExistsRequest);
        caseExistsResponse.getCriteria().forEach(caseExists -> {
            if (!caseExists.getExists()){
                String error = " does not exist ";
                if(caseExists.getCnrNumber()!=null) error = "Cnr Number: " + caseExists.getCnrNumber() + error;
                else if(caseExists.getFilingNumber()!=null) error = "Filing Number: " + caseExists.getFilingNumber() + error;
                else error="Error while validating cnrNumbers/filingNumbers";
                throw new CustomException(VALIDATION_EXCEPTION, error);
            }
        });

        // Validate applicationNumbers
        ApplicationExistsRequest applicationExistsRequest = createApplicationExistRequest(requestInfo,hearing);
        ApplicationExistsResponse applicationExistsResponse = applicationUtil.fetchApplicationDetails(applicationExistsRequest);
        applicationExistsResponse.getApplicationExists().forEach(applicationExists -> {
            if(!applicationExists.getExists()){
                String error = "Application Number: " + applicationExists.getApplicationNumber() + " does not exist ";
                throw new CustomException(VALIDATION_EXCEPTION, error);
            }
        });

        // TODO validate presided by judge.

    }

    /**
     * @param hearing hearing details
     * @throws CustomException VALIDATION_EXCEPTION -> if hearing is not present
     */
    public Hearing validateHearingExistence(RequestInfo requestInfo,Hearing hearing) {
        //checking if hearing exist or not
        List<Hearing> existingHearings = repository.getHearings(hearing);
        log.info("Existing Hearing :: {}", existingHearings);
        if (existingHearings.isEmpty())
            throw new CustomException(VALIDATION_EXCEPTION, "Hearing does not exist");

        hearing.getAttendees().forEach(attendee -> {
            if(ObjectUtils.isEmpty(attendee.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"individualId is mandatory for attendee");
            }
            //searching individual exist or not
            if(!individualService.searchIndividual(requestInfo,attendee.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist. ID: "+ attendee.getIndividualId());
        });
        return existingHearings.get(0);
    }
    public CaseExistsRequest createCaseExistsRequest(RequestInfo requestInfo, Hearing hearing){
        CaseExistsRequest caseExistsRequest = new CaseExistsRequest();
        caseExistsRequest.setRequestInfo(requestInfo);
        List<CaseExists> criteriaList = new ArrayList<>();
        hearing.getFilingNumber().forEach(filingNumber->{
            CaseExists caseExists = new CaseExists();
            caseExists.setFilingNumber(filingNumber);
            criteriaList.add(caseExists);
        });
        hearing.getCnrNumbers().forEach(cnrNumber->{
            CaseExists caseExists = new CaseExists();
            caseExists.setCnrNumber(cnrNumber);
            criteriaList.add(caseExists);
        });
        caseExistsRequest.setCriteria(criteriaList);
        return caseExistsRequest;
    }

    public ApplicationExistsRequest createApplicationExistRequest(RequestInfo requestInfo, Hearing hearing){
        ApplicationExistsRequest applicationExistsRequest = new ApplicationExistsRequest();
        applicationExistsRequest.setRequestInfo(requestInfo);
        List<ApplicationExists> criteriaList = new ArrayList<>();
        hearing.getApplicationNumbers().forEach(applicationNumber->{
            ApplicationExists applicationExists = new ApplicationExists();
            applicationExists.setApplicationNumber(applicationNumber);
            criteriaList.add(applicationExists);
        });
        applicationExistsRequest.setApplicationExists(criteriaList);
        return applicationExistsRequest;
    }
}