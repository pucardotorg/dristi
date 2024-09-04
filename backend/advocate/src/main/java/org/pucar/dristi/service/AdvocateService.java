package org.pucar.dristi.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.validators.AdvocateRegistrationValidator;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class AdvocateService {

    private final AdvocateRegistrationValidator validator;
    private final AdvocateRegistrationEnrichment enrichmentUtil;
    private final WorkflowService workflowService;
    private final AdvocateRepository advocateRepository;
    private final Producer producer;
    private final Configuration config;

    @Autowired
    public AdvocateService(
            AdvocateRegistrationValidator validator,
            AdvocateRegistrationEnrichment enrichmentUtil,
            WorkflowService workflowService,
            AdvocateRepository advocateRepository,
            Producer producer,
            Configuration config) {
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.workflowService = workflowService;
        this.advocateRepository = advocateRepository;
        this.producer = producer;
        this.config = config;
    }

    public Advocate createAdvocate(AdvocateRequest body) {
        try {

            // Validate applications
            validator.validateAdvocateRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichAdvocateRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getAdvocateCreateTopic(), body);

            return body.getAdvocate();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating advocate :: {}", e.toString());
            throw new CustomException(ADVOCATE_CREATE_EXCEPTION, e.getMessage());
        }
    }

    public void searchAdvocate(RequestInfo requestInfo, List<AdvocateSearchCriteria> advocateSearchCriteria, String tenantId, Integer limit, Integer offset) {

        try {
            if (limit == null)
                limit = 10;
            if (offset == null)
                offset = 0;

            // Fetch applications from database according to the given search criteria
            advocateRepository.getAdvocates(advocateSearchCriteria, tenantId, limit, offset);

            // If no applications are found matching the given criteria, return an empty list

            for (AdvocateSearchCriteria searchCriteria : advocateSearchCriteria){
                searchCriteria.getResponseList().forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
            }

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results : {}", e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public List<Advocate> searchAdvocateByStatus(RequestInfo requestInfo, String status, String tenantId, Integer limit, Integer offset) {
        try {
            if (limit == null)
                limit = 10;
            if (offset == null)
                offset = 0;

            // Fetch applications from database according to the given search criteria
            List<Advocate> applications = advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset);
            log.info("Application size :: {}", applications.size());

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();

            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));

            return applications;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public List<Advocate> searchAdvocateByApplicationNumber(RequestInfo requestInfo, String applicationNumber, String tenantId, Integer limit, Integer offset) {
        try {
            if (limit == null)
                limit = 10;
            if (offset == null)
                offset = 0;

            // Fetch applications from database according to the given search criteria
            List<Advocate> applications = advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset);
            log.info("Application size :: {}", applications.size());

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(applications))
                return new ArrayList<>();

            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));

            return applications;

        } catch (CustomException e) {
            log.error("Custom Exception occurred while searching :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public Advocate updateAdvocate(AdvocateRequest advocateRequest) {

        try {
            Advocate existingApplication = validateExistingApplication(advocateRequest.getAdvocate());
            existingApplication.setWorkflow(advocateRequest.getAdvocate().getWorkflow());
            advocateRequest.setAdvocate(existingApplication);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateApplicationUponUpdate(advocateRequest);

            workflowService.updateWorkflowStatus(advocateRequest);

            if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(advocateRequest.getAdvocate().getStatus())) {
                //setting true once application approved
                advocateRequest.getAdvocate().setIsActive(true);
            }

            producer.push(config.getAdvocateUpdateTopic(), advocateRequest);

            return advocateRequest.getAdvocate();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating advocate :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating advocate :: {}", e.toString());
            throw new CustomException(ADVOCATE_UPDATE_EXCEPTION, "Error occurred while updating advocate: " + e.getMessage());
        }

    }

    private Advocate validateExistingApplication(Advocate advocate) {
        try {
            return validator.validateApplicationExistence(advocate);
        } catch (Exception e) {
            log.error("Error validating existing application :: {}", e.toString());
            throw new CustomException(VALIDATION_EXCEPTION, "Error validating existing application: " + e.getMessage());
        }
    }


}
