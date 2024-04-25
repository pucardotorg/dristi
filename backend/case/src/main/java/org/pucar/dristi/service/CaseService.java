package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CaseService {

    @Autowired
    private CaseRegistrationValidator validator;

    @Autowired
    private CaseRegistrationEnrichment enrichmentUtil;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private Producer producer;

    public List<CourtCase> registerCaseRequest(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);
            enrichmentUtil.enrichCaseRegistration(body);
            workflowService.updateWorkflowStatus(body);

            producer.push("save-advocate-clerk", body);
            return body.getCases();
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while creating advocate");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate");
            throw new CustomException("CASE_CREATE_EXCEPTION",e.getMessage());
        }


    }

    public List<CourtCase> searchCases(CaseSearchRequest caseSearchRequests) {

        try {
            // Fetch applications from database according to the given search criteria
            List<CourtCase> courtCases = caseRepository.getApplications(caseSearchRequests.getCriteria());

            // If no applications are found matching the given criteria, return an empty list
            if(CollectionUtils.isEmpty(courtCases))
                return new ArrayList<>();
            courtCases.forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
            return courtCases;
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException("CASE_SEARCH_EXCEPTION",e.getMessage());
        }
    }
}