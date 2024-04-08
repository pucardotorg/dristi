package org.pucar.service;


import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRegistrationRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdvocateClerkRegistrationService {

    @Autowired
    private AdvocateClerkRegistrationValidator validator;

    @Autowired
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AdvocateClerkRegistrationRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<AdvocateClerk> registerAdvocateRequest(AdvocateClerkRequest body) {
        validator.validateAdvocateClerkRegistration(body);

        enrichmentUtil.enrichAdvocateClerkRegistration(body);

        workflowService.updateWorkflowStatus(body);

        producer.push("save-adv-application", body);

        return body.getClerks();
    }
}