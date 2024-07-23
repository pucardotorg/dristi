package org.pucar.dristi.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.HearingRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.validator.HearingRegistrationValidator;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingExists;
import org.pucar.dristi.web.models.HearingExistsRequest;
import org.pucar.dristi.web.models.HearingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class HearingService {

    @Autowired
    private HearingRegistrationValidator validator;

    @Autowired
    private HearingRegistrationEnrichment enrichmentUtil;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private HearingRepository hearingRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private Configuration config;

    public Hearing createHearing(HearingRequest body) {
        try {

            // Validate applications
            validator.validateHearingRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichHearingRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getHearingCreateTopic(), body);

            return body.getHearing();
        } catch (CustomException e) {
            log.error("Custom Exception occurred while creating hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating hearing");
            throw new CustomException(HEARING_CREATE_EXCEPTION, e.getMessage());
        }
    }

    public List<Hearing> searchHearing(String cnrNumber, String applicationNumber, String hearingId, String fightingNumber, String tenentId, LocalDate fromDate, LocalDate toDate, Integer limit, Integer offset, String sortBy) {

        try{
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setUserInfo(new User());
            if (limit == null || limit<1) limit =10;
            if (offset == null || offset<0) offset =0;
            if (!Objects.equals(sortBy, "DESC")) sortBy = "ASC";
            List<Hearing> hearingList = hearingRepository.getHearings(cnrNumber,applicationNumber,hearingId,fightingNumber,tenentId,fromDate,toDate,limit,offset,sortBy);
            for (Hearing hearing : hearingList){
                hearing.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, hearing.getTenantId(), hearing.getHearingId())));
            }
            return hearingList;
        }  catch (CustomException e) {
            log.error("Custom Exception occurred while searching");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results");
            throw new CustomException(HEARING_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public Hearing updateHearing(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(),hearingRequest.getHearing());

            // Updating Hearing request
            // TODO: Extra: add previous scheduled hearing startDate and endDate to additional details with process instance id as key.
            hearing.setWorkflow(hearingRequest.getHearing().getWorkflow());
            hearing.setStartTime(hearingRequest.getHearing().getStartTime());
            hearing.setEndTime(hearingRequest.getHearing().getEndTime());
            hearing.setTranscript(hearingRequest.getHearing().getTranscript());
            hearing.setNotes(hearingRequest.getHearing().getNotes());
            hearing.setAttendees(hearingRequest.getHearing().getAttendees());
            hearingRequest.setHearing(hearing);
            workflowService.updateWorkflowStatus(hearingRequest);

            // Enrich application upon update
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);

            producer.push(config.getHearingUpdateTopic(), hearingRequest);

            return hearingRequest.getHearing();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating hearing");
            throw new CustomException(HEARING_UPDATE_EXCEPTION, "Error occurred while updating hearing: " + e.getMessage());
        }

    }

    public HearingExists isHearingExist(HearingExistsRequest body) {
        HearingExists order = body.getOrder();
        List<Hearing> hearingList = hearingRepository.getHearings(order.getCnrNumber(),order.getApplicationNumber(), order.getHearingId(),order.getFilingNumber(),body.getRequestInfo().getUserInfo().getTenantId(),null,null,1,0,null);
        if (!hearingList.isEmpty()){
            order.setExists(true);
        }
        else {
            order.setExists(false);
        }
        return order;
    }
}
