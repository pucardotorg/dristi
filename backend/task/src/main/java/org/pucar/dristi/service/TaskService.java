package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.TaskRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.WorkflowUtil;
import org.pucar.dristi.validators.TaskRegistrationValidator;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static org.pucar.dristi.config.ServiceConstants.CREATE_TASK_ERR;

@Service
@Slf4j
public class TaskService {

    private TaskRegistrationValidator validator;

    @Autowired
    private TaskRegistrationEnrichment enrichmentUtil;

    @Autowired
    private TaskRepository caseRepository;

    @Autowired
    private WorkflowUtil workflowUtil;

    @Autowired
    private Configuration config;

    @Autowired
    private Producer producer;

    @Autowired
    public void setValidator(@Lazy TaskRegistrationValidator validator) {
        this.validator = validator;
    }

    public Task createCase(TaskRequest body) {
        try {
            validator.validateCaseRegistration(body);

            enrichmentUtil.enrichTaskRegistration(body);

            workflowUtil.updateWorkflowStatus(body.getRequestInfo(), body.getTask().getTenantId(), body.getTask().getCnrNumber(),
                    config.getTaskBusinessServiceName(), body.getTask().getWorkflow(), config.getTaskBusinessName());

            producer.push(config.getTaskCreateTopic(), body);

            return body.getTask();

        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating case");
            throw new CustomException(CREATE_TASK_ERR, e.getMessage());
        }
    }

//    public void searchCases(CaseSearchRequest caseSearchRequests) {
//
//        try {
//            // Fetch applications from database according to the given search criteria
//            caseRepository.getApplications(caseSearchRequests.getCriteria());
//
//            // If no applications are found matching the given criteria, return an empty list
//            for (CaseCriteria searchCriteria : caseSearchRequests.getCriteria()){
//                searchCriteria.getResponseList().forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
//            }
//        } catch(CustomException e){
//            throw e;
//        } catch (Exception e) {
//            log.error("Error while fetching to search results");
//            throw new CustomException(SEARCH_CASE_ERR, e.getMessage());
//        }
//    }
//
//    public CourtCase updateCase(CaseRequest caseRequest) {
//
//        try {
//            // Validate whether the application that is being requested for update indeed exists
//            if (!validator.validateApplicationExistence(caseRequest.getCases(), caseRequest.getRequestInfo()))
//                throw new CustomException(VALIDATION_ERR, "Case Application does not exist");
//
//            // Enrich application upon update
//            enrichmentUtil.enrichCaseApplicationUponUpdate(caseRequest);
//
//            workflowService.updateWorkflowStatus(caseRequest);
//
//            producer.push(config.getCaseUpdateTopic(), caseRequest);
//
//            return caseRequest.getCases();
//
//        } catch(CustomException e){
//            throw e;
//        } catch (Exception e) {
//            log.error("Error occurred while updating case");
//            throw new CustomException(UPDATE_CASE_ERR, "Error occurred while updating case: " + e.getMessage());
//        }
//
//    }
//
//    public List<CaseExists> existCases(CaseExistsRequest caseExistsRequest) {
//        try {
//            // Fetch applications from database according to the given search criteria
//            return caseRepository.checkCaseExists(caseExistsRequest.getCriteria());
//        } catch(CustomException e){
//            throw e;
//        } catch (Exception e) {
//            log.error("Error while fetching to exist case");
//            throw new CustomException(CASE_EXIST_ERR, e.getMessage());
//        }
//    }
}