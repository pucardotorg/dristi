package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import static org.pucar.config.ServiceConstants.APPLICATION_ACTIVE_STATUS;

@Service
@Slf4j
public class CaseService {

    @Autowired
    private CaseRegistrationValidator validator;

    @Autowired
    private CaseRegistrationEnrichment enrichmentUtil;


    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private Producer producer;

    public List<CourtCase> registerCaseRequest(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);
            enrichmentUtil.enrichCaseRegistration(body);
            workflowService.updateWorkflowStatus(body);

            producer.push("save-case-application", body);
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
}