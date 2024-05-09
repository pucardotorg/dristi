package org.pucar.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.config.ServiceConstants.*;

@Service
@Slf4j
public class AdvocateService {

    @Autowired
    private AdvocateRegistrationValidator validator;

    @Autowired
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private AdvocateRepository advocateRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private Configuration config;

    public List<Advocate> createAdvocate(AdvocateRequest body) {
        try {

            // Validate applications
            validator.validateAdvocateRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichAdvocateRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getAdvocateCreateTopic(), body);

            return body.getAdvocates();
        } catch (Exception e) {
            log.error("Error occurred while creating advocate");
            throw new CustomException(ADVOCATE_CREATE_EXCEPTION, e.getMessage());
        }
    }

    public List<Advocate> searchAdvocate(RequestInfo requestInfo, List<AdvocateSearchCriteria> advocateSearchCriteria, List<String> statusList, String applicationNumber, Integer limit, Integer offset) {
        AtomicReference<Boolean> isIndividualLoggedInUser = new AtomicReference<>(false);
        Map<String, String> individualUserUUID = new HashMap<>();

        try {
            if (!EMPLOYEE_UPPER.equalsIgnoreCase(requestInfo.getUserInfo().getType()) && advocateSearchCriteria != null) {
                Optional<AdvocateSearchCriteria> firstNonNull = advocateSearchCriteria.stream()
                        .filter(criteria -> Objects.nonNull(criteria.getIndividualId())) // Filter out objects with non-null individualId
                        .findFirst();

                firstNonNull.ifPresent(value -> {
                    if (individualService.searchIndividual(requestInfo, value.getIndividualId(), individualUserUUID)) {
                        if (requestInfo.getUserInfo().getUuid().equals(individualUserUUID.get("userUuid"))) {
                            isIndividualLoggedInUser.set(true);
                        }
                    }
                });
                limit = null;
                offset = null;
            } else if (EMPLOYEE_UPPER.equalsIgnoreCase(requestInfo.getUserInfo().getType())) {
                if (limit == null)
                    limit = 10;
                if (offset == null)
                    offset = 0;
            }

            // Fetch applications from database according to the given search criteria
            List<Advocate> applications = advocateRepository.getApplications(advocateSearchCriteria, statusList, applicationNumber, isIndividualLoggedInUser, limit, offset);

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();
            if (isIndividualLoggedInUser.get()) {
                if (applications.size() > 1)
                    applications.subList(1, applications.size()).clear();
            }
            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
            return applications;
        } catch (Exception e) {
            log.error("Error while fetching to search results");
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public List<Advocate> updateAdvocate(AdvocateRequest advocateRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            List<Advocate> advocatesList = new ArrayList<>();
            advocateRequest.getAdvocates().forEach(advocate -> {
                Advocate existingApplication;
                existingApplication = validator.validateApplicationExistence(advocate);
                existingApplication.setWorkflow(advocate.getWorkflow());
                advocatesList.add(existingApplication);
            });
            advocateRequest.setAdvocates(advocatesList);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateApplicationUponUpdate(advocateRequest);

            workflowService.updateWorkflowStatus(advocateRequest);
            advocateRequest.getAdvocates().forEach(advocate -> {
                if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(advocate.getStatus())) {
                    advocate.setIsActive(true);//setting true once application is approved
                }
            });
            producer.push(config.getAdvocateUpdateTopic(), advocateRequest);

            return advocateRequest.getAdvocates();

        } catch (Exception e) {
            log.error("Error occurred while updating advocate");
            throw new CustomException(ADVOCATE_UPDATE_EXCEPTION, "Error occurred while updating advocate: " + e.getMessage());
        }

    }

}
