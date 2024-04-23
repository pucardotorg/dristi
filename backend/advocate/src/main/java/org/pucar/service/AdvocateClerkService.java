package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.config.ServiceConstants.*;

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
    private Producer producer;
    @Autowired
    private Configuration config;

    public List<AdvocateClerk> registerAdvocateClerkRequest(AdvocateClerkRequest body) {
        try {
            validator.validateAdvocateClerkRegistration(body);
            enrichmentUtil.enrichAdvocateClerkRegistration(body);
            workflowService.updateWorkflowStatus(body);

            producer.push(config.getAdvClerkcreateTopic(), body);
            return body.getClerks();
        } catch (CustomException e){
            log.error("Custom Exception occurred while creating advocate clerk");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate clerk");
            throw new CustomException(ADVOCATE_CLERK_CREATE_EXCEPTION,e.getMessage());
        }
    }
    public List<AdvocateClerk> searchAdvocateClerkApplications(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria, List<String> statusList) {
        try {
            // Fetch applications from database according to the given search criteria
            List<AdvocateClerk> applications = new ArrayList<>();
            applications = advocateClerkRepository.getApplications(advocateClerkSearchCriteria, statusList);
            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();

            applications.forEach(application -> {
                application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber())));
            });
            // Otherwise return the found applications
            return applications;
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public List<AdvocateClerk> updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists

            List<AdvocateClerk> advocateClerkList= new ArrayList<>();
            advocateClerkRequest.getClerks().forEach(advocateClerk -> {
                AdvocateClerk existingApplication = null;
                try {
                    existingApplication = validator.validateApplicationExistence(advocateClerk);
                    if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(existingApplication.getStatus())) {
                        existingApplication.setIsActive(true);
                    }
                } catch (Exception e){
                    log.error("Error validating existing application");
                    throw new CustomException(VALIDATION_EXCEPTION,"Error validating existing application: "+ e.getMessage());
                }
                existingApplication.setWorkflow(advocateClerk.getWorkflow());
                advocateClerkList.add(existingApplication);
            });
            advocateClerkRequest.setClerks(advocateClerkList);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

            workflowService.updateWorkflowStatus(advocateClerkRequest);

            producer.push(config.getAdvClerkUpdateTopic(), advocateClerkRequest);

            return advocateClerkRequest.getClerks();
        } catch (CustomException e){
            log.error("Custom Exception occurred while updating advocate clerk");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate clerk");
            throw new CustomException(e.getMessage(),e.getMessage());
        }
    }
}