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

import java.util.ArrayList;
import java.util.List;

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
        } catch (CustomException e){
            log.error("Custom Exception occurred while creating advocate");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate");
            throw new CustomException(ADVOCATE_CREATE_EXCEPTION,e.getMessage());
        }
    }

public List<Advocate> searchAdvocate(RequestInfo requestInfo, List<AdvocateSearchCriteria> advocateSearchCriteria, List<String> statusList, String applicationNumber) {
    try {
        // Fetch applications from database according to the given search criteria
        List<Advocate> applications = advocateRepository.getApplications(advocateSearchCriteria, statusList, applicationNumber);

        // If no applications are found matching the given criteria, return an empty list
        if(CollectionUtils.isEmpty(applications))
            return new ArrayList<>();
        applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
        return applications;
    }
    catch (CustomException e){
        log.error("Custom Exception occurred while searching");
        throw e;
    }
    catch (Exception e){
        log.error("Error while fetching to search results");
        throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,e.getMessage());
    }
}

    public List<Advocate> updateAdvocate(AdvocateRequest advocateRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            List<Advocate> advocatesList = new ArrayList<>();
            advocateRequest.getAdvocates().forEach(advocate -> {
                Advocate existingApplication;
                try {
                    existingApplication = validator.validateApplicationExistence(advocate);
                    if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(existingApplication.getStatus())) {
                        existingApplication.setIsActive(true);
                    }
                } catch (Exception e) {
                    log.error("Error validating existing application");
                    throw new CustomException(VALIDATION_EXCEPTION,"Error validating existing application: "+ e.getMessage());
                }
                existingApplication.setWorkflow(advocate.getWorkflow());
                advocatesList.add(existingApplication);
            });
            advocateRequest.setAdvocates(advocatesList);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateApplicationUponUpdate(advocateRequest);

            workflowService.updateWorkflowStatus(advocateRequest);

            producer.push(config.getAdvocateUpdateTopic(), advocateRequest);

            return advocateRequest.getAdvocates();

        } catch (CustomException e){
            log.error("Custom Exception occurred while updating advocate");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while updating advocate");
            throw new CustomException(ADVOCATE_UPDATE_EXCEPTION,"Error occurred while updating advocate: " + e.getMessage());
        }

    }

}
