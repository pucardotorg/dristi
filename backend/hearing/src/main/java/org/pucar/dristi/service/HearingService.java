package org.pucar.dristi.service;


import lombok.extern.slf4j.Slf4j;
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

    public List<Hearing> searchHearing(String cnrNumber, String applicationNumber, String id, String fightingNumber, String tenentId, LocalDate fromDate, LocalDate toDate, Integer limit, Integer offset, String sortBy) {
        if (limit == null || limit<1) limit =10;
        if (offset == null || offset<0) offset =0;
        if (sortBy == null || sortBy !="DESC") sortBy = "ASC";
        List<Hearing> hearingList = hearingRepository.getHearings(cnrNumber,applicationNumber,id,fightingNumber,tenentId,fromDate,toDate,limit,offset,sortBy);
        return hearingList;
//        AtomicReference<Boolean> isIndividualLoggedInUser = new AtomicReference<>(false);
//        Map<String, String> individualUserUUID = new HashMap<>();
//
//        try {
//            if (!EMPLOYEE.equalsIgnoreCase(requestInfo.getUserInfo().getType()) && hearingSearchCriteria != null) {
//                Optional<HearingSearchCriteria> firstNonNull = hearingSearchCriteria.stream()
//
//                        // Filter out objects with non-null individualId
//                        .filter(criteria -> Objects.nonNull(criteria.getIndividualId()))
//                        .findFirst();
//
//                firstNonNull.ifPresent(value -> {
//                    log.info("Search Criteria :: {}", value);
//                    if (individualService.searchIndividual(requestInfo, value.getIndividualId(), individualUserUUID)) {
//                        if (requestInfo.getUserInfo().getUuid().equals(individualUserUUID.get("userUuid"))) {
//                            isIndividualLoggedInUser.set(true);
//                        }
//                    }
//                });
//                limit = null;
//                offset = null;
//            } else if (EMPLOYEE.equalsIgnoreCase(requestInfo.getUserInfo().getType())) {
//                if (limit == null)
//                    limit = 10;
//                if (offset == null)
//                    offset = 0;
//            }
//
//            // Fetch applications from database according to the given search criteria
//            List<Hearing> applications = hearingRepository.getApplications(hearingSearchCriteria, statusList, applicationNumber, isIndividualLoggedInUser, limit, offset);
//            log.info("Application size :: {}", applications.size());
//
//            // If no applications are found matching the given criteria, return an empty list
//            if (CollectionUtils.isEmpty(applications))
//                return new ArrayList<>();
//            if (isIndividualLoggedInUser.get()) {
//                if (applications.size() > 1)
//                    applications.subList(1, applications.size()).clear();
//            }
//            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
//            return applications;
//        } catch (CustomException e) {
//            log.error("Custom Exception occurred while searching");
//            throw e;
//        } catch (Exception e) {
//            log.error("Error while fetching to search results");
//            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, e.getMessage());
//        }
    }

    public Hearing updateHearing(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
//            Hearing hearing = validator.validateApplicationExistence(hearingRequest.getHearing());
//            hearing.setWorkflow(hearingRequest.getHearing().getWorkflow());
//            hearingRequest.setHearing(hearing);
            Hearing hearing = hearingRequest.getHearing();

            workflowService.updateWorkflowStatus(hearingRequest);
            if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(hearing.getStatus())) {
                //setting true once application approved
                hearing.setIsActive(true);
            }

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
        return new HearingExists();
    }
}
