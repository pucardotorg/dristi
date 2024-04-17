package org.pucar.service;


import org.egov.common.models.individual.IndividualRequest;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AdvocateClerkService {

    @Autowired
    private AdvocateClerkRegistrationValidator validator;

    @Autowired
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AdvocateClerkRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<AdvocateClerk> registerAdvocateRequest(AdvocateClerkRequest body) {
        validator.validateAdvocateClerkRegistration(body);
        enrichmentUtil.enrichAdvocateClerkRegistration(body);
        workflowService.updateWorkflowStatus(body);

        if (!individualService.searchIndividual(body)) {
            throw new IllegalArgumentException("Individual not found");
        }
        producer.push("save-advocate-clerk", body);
        return body.getClerks();
    }
    public List<AdvocateClerk> updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {
        // Validate whether the application that is being requested for update indeed exists
        AdvocateClerk existingApplication = validator.validateApplicationExistence(advocateClerkRequest.getClerks().get(0));
        existingApplication.setWorkflow(advocateClerkRequest.getClerks().get(0).getWorkflow());
        advocateClerkRequest.getClerks();

        // Enrich application upon update
        enrichmentUtil.enrichBirthApplicationUponUpdate(advocateClerkRequest);

        workflowService.updateWorkflowStatus(advocateClerkRequest);

//        //Individual search
//        Boolean isIndividualExist = individualService.searchIndividual(advocateClerkRequest);
//        if(!isIndividualExist)
//            throw new IllegalArgumentException("Individual Id doesn't exist");
//
         //Push the application to the topic for persister to listen and persist
               producer.push("save-advocate-clerk-application", advocateClerkRequest);

        return advocateClerkRequest.getClerks();
    }

}