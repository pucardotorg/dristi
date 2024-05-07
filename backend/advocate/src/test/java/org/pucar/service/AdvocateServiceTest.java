package org.pucar.service;

import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.config.Configuration;
import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
public class AdvocateServiceTest {

    @Mock
    private AdvocateRegistrationValidator validator;

    @Mock
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Mock
    private IndividualService individualService;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateRepository advocateRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateService service;
    @Mock
    private Configuration config;

    @Test
    void testCreateAdvocateRequest() {
        // Prepare the request
        AdvocateRequest request = new AdvocateRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenant1");
        advocate.setIndividualId("individualId");
        request.setAdvocates(Collections.singletonList(advocate));
        when(config.getAdvocateCreateTopic()).thenReturn("save-advocate-application");

        // Execute the method under test
        List<Advocate> result = service.createAdvocate(request);

        // Verify the interactions
        verify(validator, times(1)).validateAdvocateRegistration(request);
        verify(enrichmentUtil, times(1)).enrichAdvocateRegistration(request);
        verify(workflowService, times(1)).updateWorkflowStatus(request);
        verify(producer, times(1)).push("save-advocate-application", request);

        // Assert the result
        assertNotNull(result);
        assertEquals("tenant1", result.get(0).getTenantId());
    }

    @Test
    public void testSearchAdvocateApplications() {
        // Setup
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setType("CITIZEN");
        requestInfo.setUserInfo(user);
        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria(null, null, "APP123", null);
        advocateSearchCriteria.add(criteria);
        String applicationNumber = new String();

        List<String> statusList = Arrays.asList("APPROVED","PENDING");
        when(advocateRepository.getApplications(
                eq(Collections.singletonList(new AdvocateSearchCriteria(null, null, "APP123", null))),
                eq(Arrays.asList("APPROVED", "PENDING")),
                eq(""), any(),any(),any()))
                .thenReturn(Collections.emptyList());

        // Invoke
        List<Advocate> result = service.searchAdvocate(requestInfo, advocateSearchCriteria,statusList, applicationNumber,1,1);

        // Verify
        assertEquals(0, result.size());
        verify(advocateRepository, times(1)).getApplications(eq(advocateSearchCriteria), eq(Arrays.asList("APPROVED", "PENDING")), eq(applicationNumber), any(),any(),any());
    }

    @Test
    void testUpdateAdvocateRequest() {
        // Prepare the request
        AdvocateRequest request = new AdvocateRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenant1");
        advocate.setIndividualId("individualId");
        advocate.setWorkflow(new Workflow());
        advocate.setStatus("ACTIVE");
        request.setAdvocates(Collections.singletonList(advocate));

        when(validator.validateApplicationExistence(any())).thenReturn(advocate);
        when(config.getAdvocateUpdateTopic()).thenReturn("update-advocate-application");

        // Execute the method under test
        List<Advocate> result = service.updateAdvocate(request);

        // Verify the interactions
        verify(validator, times(1)).validateApplicationExistence(request.getAdvocates().get(0));
        verify(enrichmentUtil, times(1)).enrichAdvocateApplicationUponUpdate(request);
        verify(workflowService, times(1)).updateWorkflowStatus(request);
        verify(producer, times(1)).push("update-advocate-application", request);

        // Assert the result
        assertNotNull(result);
        assertEquals("tenant1", result.get(0).getTenantId());
    }
}
