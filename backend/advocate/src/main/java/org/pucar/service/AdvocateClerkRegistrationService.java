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
    private UserService userService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AdvocateClerkRegistrationRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<AdvocateClerk> registerAdvocateRequest(AdvocateClerkRequest body) {
        // Validate applications
        validator.validateAdvocateClerkRegistration(body);

        // Enrich applications
        enrichmentUtil.enrichAdvocateClerkRegistration(body);

        // Enrich/Upsert user in upon registration
        //userService.callUserService(body);

        // Initiate workflow for the new application-
        workflowService.updateWorkflowStatus(body);

        // Push the application to the topic for persister to listen and persist
        producer.push("save-adv-application", body);

        // Return the response back to user
        return body.getClerks();
    }
}