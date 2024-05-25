package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.validators.AdvocateClerkRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class AdvocateClerkService {

    @Autowired
    private AdvocateClerkRepository advocateClerkRepository;

    @Autowired
    private AdvocateClerkRegistrationValidator validator;

    @Autowired
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;


    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private Producer producer;
    @Autowired
    private Configuration config;

    public AdvocateClerk registerAdvocateClerkRequest(AdvocateClerkRequest body) {
        try {
            validator.validateAdvocateClerkRegistration(body);
            enrichmentUtil.enrichAdvocateClerkRegistration(body);
            workflowService.updateWorkflowStatus(body);

            producer.push(config.getAdvClerkcreateTopic(), body);
            return body.getClerk();
        } catch (Exception e){
            log.error("Error occurred while creating advocate clerk");
            throw new CustomException(ADVOCATE_CLERK_CREATE_EXCEPTION,e.getMessage());
        }
    }
    public List<AdvocateClerk> searchAdvocateClerkApplications(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria, String tenantId, Integer limit, Integer offset) {
        AtomicReference<Boolean> isIndividualLoggedInUser = new AtomicReference<>(false);
        Map<String, String> individualUserUUID = new HashMap<>();
        List<AdvocateClerk> applications = new ArrayList<>();

        try {
            if (!EMPLOYEE.equalsIgnoreCase(requestInfo.getUserInfo().getType()) && advocateClerkSearchCriteria!=null) {
                Optional<AdvocateClerkSearchCriteria> firstNonNull = advocateClerkSearchCriteria.stream()

                        // Filter out objects with non-null individualId
                        .filter(criteria -> Objects.nonNull(criteria.getIndividualId()))
                        .findFirst();

                firstNonNull.ifPresent(value -> {
                    log.info("Search Criteria :: {}", value);
                    if (individualService.searchIndividual(requestInfo, value.getIndividualId(), individualUserUUID)) {
                        if (requestInfo.getUserInfo().getUuid().equals(individualUserUUID.get("userUuid"))) {
                            isIndividualLoggedInUser.set(true);
                        }
                    }
                });
                // setting default values for limit and offset as null when user type is NOT EMPLOYEE
                limit = null;
                offset = null;
            }
            // setting default values for limit and offset only when user type is EMPLOYEE
            else if (EMPLOYEE.equalsIgnoreCase(requestInfo.getUserInfo().getType())){
                if(limit == null)
                    limit = 10;
                if(offset == null)
                    offset = 0;
            }
            // Fetch applications from database according to the given search criteria
            advocateClerkRepository.getApplications(advocateClerkSearchCriteria, tenantId, isIndividualLoggedInUser, limit, offset);

            log.info("Application size :: {}", applications.size());
            // If no applications are found matching the given criteria, return an empty list
//            if (CollectionUtils.isEmpty(applications))
//                return new ArrayList<>();
//            if(isIndividualLoggedInUser.get()) {
//                if (applications.size() > 1)
//                    applications.subList(1, applications.size()).clear();
//            }
            for (AdvocateClerkSearchCriteria searchCriteria : advocateClerkSearchCriteria){
                searchCriteria.getResponseList().forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
            }

            // Otherwise return the found applications
            return applications;
        }

        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public List<AdvocateClerk> searchAdvocateClerkApplicationsByAppNumber(String applicationNumber, String tenantId, Integer limit, Integer offset) {
        try {
            if(limit == null)
                limit = 10;
            if(offset == null)
                offset = 0;

            // Fetch applications from database according to the given search criteria
            List<AdvocateClerk> applications;
            applications = advocateClerkRepository.getApplicationsByAppNumber(applicationNumber, tenantId, limit, offset);

            log.info("Application size :: {}", applications.size());
            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();

            // Otherwise return the found applications
            return applications;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public List<AdvocateClerk> searchAdvocateClerkApplicationsByStatus(String status, String tenantId, Integer limit, Integer offset) {
        try {
            if(limit == null)
                limit = 10;
            if(offset == null)
                offset = 0;

            // Fetch applications from database according to the given search criteria
            List<AdvocateClerk> applications;
            applications = advocateClerkRepository.getApplicationsByStatus(status, tenantId, limit, offset);

            log.info("Application size :: {}", applications.size());
            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();

            // Otherwise return the found applications
            return applications;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public AdvocateClerk updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists
            AdvocateClerk advocateClerk = new AdvocateClerk();
            AdvocateClerk existingApplication;
            try {
                existingApplication = validator.validateApplicationExistence(advocateClerk);
            } catch (Exception e){
                log.error("Error validating existing application");
                throw new CustomException(VALIDATION_EXCEPTION,"Error validating existing application: "+ e.getMessage());
            }
            existingApplication.setWorkflow(advocateClerk.getWorkflow());

            advocateClerkRequest.setClerk(existingApplication);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

            workflowService.updateWorkflowStatus(advocateClerkRequest);

            if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(advocateClerk.getStatus())) {
                //setting true once application approved
                advocateClerk.setIsActive(true);
            }

            producer.push(config.getAdvClerkUpdateTopic(), advocateClerkRequest);

            return advocateClerkRequest.getClerk();

        } catch (Exception e){
            log.error("Error occurred while updating advocate clerk");
            throw new CustomException(ADVOCATE_CLERK_UPDATE_EXCEPTION,"Error occurred while updating advocate clerk: " + e.getMessage());
        }
    }
}