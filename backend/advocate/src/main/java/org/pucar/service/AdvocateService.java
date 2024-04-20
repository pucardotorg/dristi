package org.pucar.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.pucar.web.models.AdvocateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private AdvocateRepository advocateRepository;

    @Autowired
    private Producer producer;

    public List<Advocate> createAdvocate(AdvocateRequest body) {
        // Validate applications
        validator.validateAdvocateRegistration(body);

        // Enrich applications
       enrichmentUtil.enrichAdvocateRegistration(body);

        // Initiate workflow for the new application-
       workflowService.updateWorkflowStatus(body);

        // Push the application to the topic for persister to listen and persist

        // FIXME
        producer.push("save-advocate-application", body);

        return body.getAdvocates();
    }

public List<Advocate> searchAdvocate(RequestInfo requestInfo, List<AdvocateSearchCriteria> advocateSearchCriteria, List<String> statusList) {
    // Fetch applications from database according to the given search criteria
    List<Advocate> applications = advocateRepository.getApplications(advocateSearchCriteria, statusList);

    // If no applications are found matching the given criteria, return an empty list
    if(CollectionUtils.isEmpty(applications))
        return new ArrayList<>();

    return applications;
}

    public List<Advocate> updateAdvocate(AdvocateRequest advocateRequest, List<String> statusList) {
        // Validate whether the application that is being requested for update indeed exists
        Advocate existingApplication = validator.validateApplicationExistence(advocateRequest.getAdvocates().get(0), statusList);
        existingApplication.setWorkflow(advocateRequest.getAdvocates().get(0).getWorkflow());
        advocateRequest.setAdvocates(Collections.singletonList(existingApplication));

        // Enrich application upon update
        enrichmentUtil.enrichBirthApplicationUponUpdate(advocateRequest);

        workflowService.updateWorkflowStatus(advocateRequest);

        return advocateRequest.getAdvocates();
    }

}
