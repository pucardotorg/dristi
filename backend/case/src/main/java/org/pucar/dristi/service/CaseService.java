package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        validator.validateCaseRegistration(body);
        enrichmentUtil.enrichCaseRegistration(body);
        workflowService.updateWorkflowStatus(body);

        producer.push("save-advocate-clerk", body);
        return body.getCases();
    }
}