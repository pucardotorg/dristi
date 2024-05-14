package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;


@Service
@Slf4j
public class    CaseService {

    @Autowired
    private CaseRegistrationValidator validator;

    @Autowired
    private CaseRegistrationEnrichment enrichmentUtil;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private Configuration config;
    @Autowired
    private Producer producer;

    public List<CourtCase> createCase(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);
            enrichmentUtil.enrichCaseRegistration(body);
            workflowService.updateWorkflowStatus(body);

            producer.push(config.getCaseCreateTopic(), body);
            return body.getCases();
        } catch (Exception e){
            log.error("Error occurred while creating case");
            throw new CustomException(CREATE_CASE_ERR,e.getMessage());
        }


    }

    public List<CourtCase> searchCases(CaseSearchRequest caseSearchRequests) {

        try {
            // Fetch applications from database according to the given search criteria
            List<CourtCase> courtCases = caseRepository.getApplications(caseSearchRequests.getCriteria());
            log.info("Court Case Applications Size :: {}", courtCases.size());
            // If no applications are found matching the given criteria, return an empty list
            if(CollectionUtils.isEmpty(courtCases))
                return new ArrayList<>();
            courtCases.forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
            return courtCases;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(SEARCH_CASE_ERR,e.getMessage());
        }
    }

    public List<CourtCase> updateCase(CaseRequest caseRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists
            caseRequest.getCases().forEach(courtCase -> {
                if(!validator.validateApplicationExistence(courtCase, caseRequest.getRequestInfo()))
                    throw new CustomException(VALIDATION_ERR,"Error validating existing application: ");
            });
            // Enrich application upon update
            enrichmentUtil.enrichCaseApplicationUponUpdate(caseRequest);

            workflowService.updateWorkflowStatus(caseRequest);

            producer.push(config.getCaseUpdateTopic(), caseRequest);

            return caseRequest.getCases();

        }catch (Exception e){
            log.error("Error occurred while updating case");
            throw new CustomException(UPDATE_CASE_ERR,"Error occurred while updating case: " + e.getMessage());
        }

    }

    public List<CaseExists> existCases(CaseSearchRequest caseSearchRequests) {
        try {
            // Fetch applications from database according to the given search criteria
            List<CourtCase> courtCases = caseRepository.getApplications(caseSearchRequests.getCriteria());
            log.info("Court Case Applications Size :: {}", courtCases.size());

            List<CaseExists> caseExistsList = new ArrayList<>();

            for(CaseCriteria caseCriteria: caseSearchRequests.getCriteria()){
              boolean notExists = courtCases.stream().filter(c->c.getFilingNumber().equalsIgnoreCase(caseCriteria.getFilingNumber())
                      && c.getCaseNumber().equalsIgnoreCase(caseCriteria.getCnrNumber())).toList().isEmpty();
              CaseExists caseExists = new CaseExists(caseCriteria.getCourtCaseNumber(),caseCriteria.getCnrNumber(), caseCriteria.getFilingNumber(), !notExists);
              caseExistsList.add(caseExists);
            }

            // If no applications are found matching the given criteria, return an empty list
            if(CollectionUtils.isEmpty(courtCases))
                return new ArrayList<>();
            courtCases.forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
            return caseExistsList;
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException(CASE_EXIST_ERR,e.getMessage());
        }
    }
}