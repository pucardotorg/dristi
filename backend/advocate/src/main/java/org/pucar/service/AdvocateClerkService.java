package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.pucar.config.ServiceConstants.APPLICATION_ACTIVE_STATUS;

@Service
@Slf4j
public class AdvocateClerkService {

    @Autowired
    private AdvocateClerkRepository advocateClerkRepository;

    @Autowired
    private AdvocateClerkRegistrationValidator validator;

    @Autowired
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;


    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private Producer producer;

    public List<AdvocateClerk> registerAdvocateRequest(AdvocateClerkRequest body) {
        validator.validateAdvocateClerkRegistration(body);
        enrichmentUtil.enrichAdvocateClerkRegistration(body);
        workflowService.updateWorkflowStatus(body);

        producer.push("save-advocate-clerk", body);
        return body.getClerks();
    }
    public List<AdvocateClerk> searchAdvocateApplications(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria, List<String> statusList) {
        // Fetch applications from database according to the given search criteria
        List<AdvocateClerk> applications = new ArrayList<>();
        applications = advocateClerkRepository.getApplications(advocateClerkSearchCriteria, statusList);
        // If no applications are found matching the given criteria, return an empty list
        if(CollectionUtils.isEmpty(applications))
            return new ArrayList<>();

        applications.forEach(application -> {
            application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, application.getTenantId(), application.getApplicationNumber())));
        });
        // Otherwise return the found applications
        return applications;
    }

    public List<AdvocateClerk> updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {
        // Validate whether the application that is being requested for update indeed exists
        AdvocateClerk existingApplication = validator.validateApplicationExistence(advocateClerkRequest.getClerks().get(0));
        existingApplication.setWorkflow(advocateClerkRequest.getClerks().get(0).getWorkflow());
        advocateClerkRequest.setClerks(Collections.singletonList(existingApplication));

        // Enrich application upon update
        enrichmentUtil.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

        workflowService.updateWorkflowStatus(advocateClerkRequest);

        AdvocateClerk advocateClerk = advocateClerkRequest.getClerks().get(0);

        if(APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(advocateClerk.getStatus())) {
            advocateClerk.setIsActive(true);
        }

        producer.push("update-advocate-clerk-application", advocateClerkRequest);

        return advocateClerkRequest.getClerks();
    }
}