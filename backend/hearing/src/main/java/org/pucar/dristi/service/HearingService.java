package org.pucar.dristi.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.HearingRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.validator.HearingRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.WITNESS_DEPOSITION_UPDATE_EXCEPTION;

@Service
@Slf4j
public class HearingService {

    private final HearingRegistrationValidator validator;
    private final HearingRegistrationEnrichment enrichmentUtil;
    private final WorkflowService workflowService;
    private final HearingRepository hearingRepository;
    private final Producer producer;
    private final Configuration config;

    @Autowired
    public HearingService(
            HearingRegistrationValidator validator,
            HearingRegistrationEnrichment enrichmentUtil,
            WorkflowService workflowService,
            HearingRepository hearingRepository,
            Producer producer,
            Configuration config) {
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.workflowService = workflowService;
        this.hearingRepository = hearingRepository;
        this.producer = producer;
        this.config = config;
    }

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

    public List<Hearing> searchHearing(HearingSearchRequest request) {

        try {
            return hearingRepository.getHearings(request);
        } catch (CustomException e) {
            log.error("Custom Exception occurred while searching");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching search results");
            throw new CustomException(HEARING_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public Hearing updateHearing(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(), hearingRequest.getHearing());

            // Updating Hearing request
            hearing.setWorkflow(hearingRequest.getHearing().getWorkflow());
            hearing.setStartTime(hearingRequest.getHearing().getStartTime());
            hearing.setEndTime(hearingRequest.getHearing().getEndTime());
            hearing.setTranscript(hearingRequest.getHearing().getTranscript());
            hearing.setNotes(hearingRequest.getHearing().getNotes());
            hearing.setAttendees(hearingRequest.getHearing().getAttendees());
            hearing.setDocuments(hearingRequest.getHearing().getDocuments());
            hearing.setAdditionalDetails(hearingRequest.getHearing().getAdditionalDetails());
            hearing.setVcLink(hearingRequest.getHearing().getVcLink());
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
        try {
            HearingExists order = body.getOrder();
            HearingCriteria criteria = HearingCriteria.builder().cnrNumber(order.getCnrNumber())
                    .filingNumber( order.getFilingNumber()).applicationNumber(order.getApplicationNumber()).hearingId(order.getHearingId()).tenantId(body.getRequestInfo().getUserInfo().getTenantId()).build();
            Pagination pagination = Pagination.builder().limit(1.0).offSet((double) 0).build();
            HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();
            List<Hearing> hearingList = hearingRepository.getHearings(hearingSearchRequest);
            order.setExists(!hearingList.isEmpty());
            return order;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while verifying hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while verifying hearing");
            throw new CustomException(HEARING_SEARCH_EXCEPTION, "Error occurred while searching hearing: " + e.getMessage());
        }
    }

    public Hearing updateTranscriptAdditionalAttendees(HearingRequest hearingRequest) {
        try {
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(),hearingRequest.getHearing());
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);
            hearingRepository.updateTranscriptAdditionalAttendees(hearingRequest.getHearing());
            hearing.setTranscript(hearingRequest.getHearing().getTranscript());
            hearing.setAuditDetails(hearingRequest.getHearing().getAuditDetails());
            hearing.setAdditionalDetails(hearingRequest.getHearing().getAdditionalDetails());
            hearing.setAttendees(hearingRequest.getHearing().getAttendees());
            hearing.setVcLink(hearingRequest.getHearing().getVcLink());
            hearing.setNotes(hearingRequest.getHearing().getNotes());
            return hearing;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while verifying hearing");
            throw e;
        } catch (Exception e) {
            throw new CustomException(HEARING_UPDATE_EXCEPTION, "Error occurred while updating hearing: " + e.getMessage());
        }
    }

    public void updateStartAndTime(UpdateTimeRequest body) {
        try {
            for(Hearing hearing : body.getHearings()){
                if(hearing.getHearingId()==null || hearing.getHearingId().isEmpty()){
                    throw new CustomException(HEARING_UPDATE_TIME_EXCEPTION, "Hearing Id is required for updating start and end time");
                }
                Hearing existingHearing = validator.validateHearingExistence(body.getRequestInfo(),hearing);
                existingHearing.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                existingHearing.getAuditDetails().setLastModifiedBy(body.getRequestInfo().getUserInfo().getUuid());
                hearing.setAuditDetails(existingHearing.getAuditDetails());

                HearingRequest hearingRequest = HearingRequest.builder().requestInfo(body.getRequestInfo())
                        .hearing(hearing).build();
                producer.push(config.getStartEndTimeUpdateTopic(), hearingRequest);
            }

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating hearing start and end time");
            throw e;
        } catch (Exception e) {
            throw new CustomException(HEARING_UPDATE_TIME_EXCEPTION, "Error occurred while updating hearing start and end time: " + e.getMessage());
        }
    }

    public Hearing uploadWitnessDeposition(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(), hearingRequest.getHearing());

            // Updating Hearing request

            hearing.setDocuments(hearingRequest.getHearing().getDocuments());
            hearingRequest.setHearing(hearing);

            // Enrich application upon update
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);

            producer.push(config.getHearingUpdateTopic(), hearingRequest);

            return hearingRequest.getHearing();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while uploading witness deposition pdf");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while uploading witness deposition pdf");
            throw new CustomException(WITNESS_DEPOSITION_UPDATE_EXCEPTION, "Error occurred while uploading witness deposition pdf: " + e.getMessage());
        }

    }
}
