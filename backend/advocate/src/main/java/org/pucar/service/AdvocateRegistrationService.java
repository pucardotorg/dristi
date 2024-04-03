package org.pucar.service;


import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRegistrationRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AdvocateRegistrationService {

    @Autowired
    private AdvocateRegistrationValidator validator;

    @Autowired
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AdvocateRegistrationRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<Advocate> registerAdvocateRequest(AdvocateRequest body) {
        // Validate applications
        validator.validateAdvocateRegistration(body);

        // Enrich applications
        enrichmentUtil.enrichAdvocateRegistration(body);

        // Enrich/Upsert user in upon registration
        //userService.callUserService(body);

        // Initiate workflow for the new application-
        workflowService.updateWorkflowStatus(body);

        // Push the application to the topic for persister to listen and persist
        producer.push("save-adv-application", body);

        // Return the response back to user
        return body.getCases();
    }
}
