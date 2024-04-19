package org.pucar.service;


import lombok.extern.slf4j.Slf4j;
import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AdvocateService {

    @Autowired
    private AdvocateRegistrationValidator validator;

    @Autowired
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private AdvocateRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<Advocate> createAdvocate(AdvocateRequest body) {
        // Validate applications
        validator.validateAdvocateRegistration(body);

        // Enrich applications
       enrichmentUtil.enrichAdvocateRegistration(body);

        // Initiate workflow for the new application-
       workflowService.updateWorkflowStatus(body);

        //Individual search
        Boolean isIndividualExist = individualService.searchIndividual(body);
        if(!isIndividualExist)
            throw new IllegalArgumentException("Individual Id doesn't exist");

        // Push the application to the topic for persister to listen and persist
        producer.push("save-advocate-application", body);

        return body.getAdvocates();
    }

    public List<Advocate> searchAdvocates(AdvocateSearchRequest body) {
        return null;
    }

    public List<Advocate> updateAdvocate(AdvocateRequest advocateRequest) {
        // Validate whether the application that is being requested for update indeed exists
        Advocate existingApplication = validator.validateApplicationExistence(advocateRequest.getAdvocates().get(0));
        existingApplication.setWorkflow(advocateRequest.getAdvocates().get(0).getWorkflow());
        advocateRequest.setAdvocates(Collections.singletonList(existingApplication));

        // Enrich application upon update
        enrichmentUtil.enrichBirthApplicationUponUpdate(advocateRequest);

        workflowService.updateWorkflowStatus(advocateRequest);

        //Individual search
//        Boolean isIndividualExist = individualService.searchIndividual(advocateRequest);
//        if(!isIndividualExist)
//            throw new IllegalArgumentException("Individual Id doesn't exist");

        // Push the application to the topic for persister to listen and persist
 //       producer.push("save-advocate-application", advocateRequest);

        return advocateRequest.getAdvocates();
    }
}
