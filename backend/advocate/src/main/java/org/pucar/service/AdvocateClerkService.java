package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

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
    private AdvocateClerkRegistrationRepository advocateRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<AdvocateClerk> registerAdvocateRequest(AdvocateClerkRequest body) {
        validator.validateAdvocateClerkRegistration(body);
        enrichmentUtil.enrichAdvocateClerkRegistration(body);
        workflowService.updateWorkflowStatus(body);

        producer.push("save-advocate-clerk", body);
        return body.getClerks();
    }

    public List<AdvocateClerk> searchAdvocateApplications(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria) {
        // Fetch applications from database according to the given search criteria
        List<AdvocateClerk> applications = advocateClerkRepository.getApplications(advocateClerkSearchCriteria);

        // If no applications are found matching the given criteria, return an empty list
        if(CollectionUtils.isEmpty(applications))
            return new ArrayList<>();

        // Otherwise return the found applications
        return applications;
    }
}