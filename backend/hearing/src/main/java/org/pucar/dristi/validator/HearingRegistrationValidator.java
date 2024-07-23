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
    private final IndividualService individualService;
    private final HearingRepository repository;
    private final MdmsUtil mdmsUtil;
    private final Configuration config;
    private final ObjectMapper mapper;
    private final CaseUtil caseUtil;
    private final ApplicationUtil applicationUtil;

    @Autowired
    public HearingRegistrationValidator(
            IndividualService individualService,
            HearingRepository repository,
            MdmsUtil mdmsUtil,
            Configuration config,
            ObjectMapper mapper,
            CaseUtil caseUtil,
            ApplicationUtil applicationUtil) {
        this.individualService = individualService;
        this.repository = repository;
        this.mdmsUtil = mdmsUtil;
        this.config = config;
        this.mapper = mapper;
        this.caseUtil = caseUtil;
        this.applicationUtil = applicationUtil;
    }

    /**
     * @param hearingRequest hearing application request
     * @throws CustomException VALIDATION_EXCEPTION -> if tenantId or hearingType not present
     *                         ILLEGAL_ARGUMENT_EXCEPTION_CODE-> if individualId doesn't exist
     */
    public void validateHearingRegistration(HearingRequest hearingRequest) throws CustomException {
        RequestInfo requestInfo = hearingRequest.getRequestInfo();
        Hearing hearing = hearingRequest.getHearing();

        // Validate userInfo and tenantId
        baseValidations(requestInfo, hearing);

        // Validate individual ids
        if(config.getVerifyAttendeeIndividualId())
            validateIndividualExistence(requestInfo, hearing);

        // Validating Hearing Type
        validateHearingType(requestInfo, hearing);

        // Validate cnrNumbers and filingNumbers
        validateCaseExistence(requestInfo, hearing);

        // Validate applicationNumbers
        validateApplicationExistence(requestInfo, hearing);

    }

    private void baseValidations(RequestInfo requestInfo, Hearing hearing){
        if (requestInfo.getUserInfo() == null || requestInfo.getUserInfo().getTenantId() == null)
            throw new CustomException(VALIDATION_EXCEPTION, "User info not found!!!");

        if (ObjectUtils.isEmpty(hearing.getTenantId()) || ObjectUtils.isEmpty(hearing.getHearingType())) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, "tenantId and hearing type are mandatory for creating hearing");
        }
    }

    private void validateHearingType(RequestInfo requestInfo, Hearing hearing){
        JSONArray hearingTypeList = mdmsUtil.fetchMdmsData(requestInfo,requestInfo.getUserInfo().getTenantId(),config.getMdmsHearingModuleName(),Collections.singletonList(config.getMdmsHearingTypeMasterName()))
                .get(config.getMdmsHearingModuleName()).get(config.getMdmsHearingTypeMasterName());

        boolean validateHearingType = false;
        for (Object o : hearingTypeList) {
            HearingType hearingType = mapper.convertValue(o, HearingType.class);
            if (hearingType.getType().equals(hearing.getHearingType())) validateHearingType = true;
        }
        if (!validateHearingType)
            throw new CustomException(VALIDATION_EXCEPTION, "Could not validate Hearing Type!!!");
    }

    private void validateIndividualExistence(RequestInfo requestInfo, Hearing hearing){
        hearing.getAttendees().forEach(attendee -> {
            if(ObjectUtils.isEmpty(attendee.getIndividualId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"individualId is mandatory for attendee");
            }
            //searching individual exist or not
            if(!individualService.searchIndividual(requestInfo,attendee.getIndividualId(), new HashMap<>()))
                throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist. ID: "+ attendee.getIndividualId());
        });
    }

    private void validateCaseExistence(RequestInfo requestInfo, Hearing hearing){
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
    }

    private void validateApplicationExistence(RequestInfo requestInfo, Hearing hearing){
        ApplicationExistsRequest applicationExistsRequest = createApplicationExistRequest(requestInfo,hearing);
        ApplicationExistsResponse applicationExistsResponse = applicationUtil.fetchApplicationDetails(applicationExistsRequest);
        applicationExistsResponse.getApplicationExists().forEach(applicationExists -> {
            if(!applicationExists.getExists()){
                String error = "Application Number: " + applicationExists.getApplicationNumber() + " does not exist ";
                throw new CustomException(VALIDATION_EXCEPTION, error);
            }
        });
    }

    /**
     * @param hearing hearing details
     * @throws CustomException VALIDATION_EXCEPTION -> if hearing is not present
     */
    public Hearing validateHearingExistence(RequestInfo requestInfo,Hearing hearing) {
        //checking if hearing exist or not
        List<Hearing> existingHearings = repository.checkHearingsExist(hearing);
        log.info("Existing Hearing :: {}", existingHearings);
        if (existingHearings.isEmpty())
            throw new CustomException(VALIDATION_EXCEPTION, "Hearing does not exist");

        if(config.getVerifyAttendeeIndividualId())
            validateIndividualExistence(requestInfo, hearing);

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