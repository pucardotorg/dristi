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
        } catch(CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate clerk :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_CREATE_EXCEPTION,e.getMessage());
        }
    }
    public void searchAdvocateClerkApplications(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria, String tenantId, Integer limit, Integer offset) {

        try {
            if(limit == null)
                limit = 10;
            if(offset == null)
                offset = 0;
            // Fetch applications from database according to the given search criteria
            advocateClerkRepository.getApplications(advocateClerkSearchCriteria, tenantId, limit, offset);

            for (AdvocateClerkSearchCriteria searchCriteria : advocateClerkSearchCriteria){
                searchCriteria.getResponseList().forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));
            }

        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public List<AdvocateClerk> searchAdvocateClerkApplicationsByAppNumber(RequestInfo requestInfo, String applicationNumber, String tenantId, Integer limit, Integer offset) {
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

            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));

            // Otherwise return the found applications
            return applications;
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public List<AdvocateClerk> searchAdvocateClerkApplicationsByStatus(RequestInfo requestInfo, String status, String tenantId, Integer limit, Integer offset) {
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

            applications.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber()))));

            // Otherwise return the found applications
            return applications;
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,e.getMessage());
        }
    }

    public AdvocateClerk updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists
            AdvocateClerk existingApplication;
            try {
                existingApplication = validator.validateApplicationExistence(advocateClerkRequest.getClerk());
            } catch (Exception e){
                log.error("Error validating existing application :: {}",e.toString());
                throw new CustomException(VALIDATION_EXCEPTION,"Error validating existing application: "+ e.getMessage());
            }
            existingApplication.setWorkflow(advocateClerkRequest.getClerk().getWorkflow());

            advocateClerkRequest.setClerk(existingApplication);

            // Enrich application upon update
            enrichmentUtil.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

            workflowService.updateWorkflowStatus(advocateClerkRequest);

            if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(advocateClerkRequest.getClerk().getStatus())) {
                //setting true once application approved
                advocateClerkRequest.getClerk().setIsActive(true);
            }

            producer.push(config.getAdvClerkUpdateTopic(), advocateClerkRequest);

            return advocateClerkRequest.getClerk();
        } catch(CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while updating advocate clerk :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_UPDATE_EXCEPTION,"Error occurred while updating advocate clerk: " + e.getMessage());
        }
    }
}