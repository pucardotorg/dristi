package org.pucar.service;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateRegistrationRepository;
import org.pucar.validators.AdvocateRegistrationValidator;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdvocateRegistrationServiceTest {

    @Mock
    private AdvocateRegistrationValidator validator;

    @Mock
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Mock
    private UserService userService;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateRegistrationRepository advocateRegistrationRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateRegistrationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterAdvocateRequest() {
        // Prepare the request
        AdvocateRequest request = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenant1");
        request.setAdvocates(Collections.singletonList(advocate));

        // Execute the method under test
        List<Advocate> result = service.registerAdvocateRequest(request);

        // Verify the interactions
        verify(validator, times(1)).validateAdvocateRegistration(request);
        verify(enrichmentUtil, times(1)).enrichAdvocateRegistration(request);
        verify(workflowService, times(1)).updateWorkflowStatus(request);
        verify(producer, times(1)).push("save-advocate-application", request);

        // Assert the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tenant1", result.get(0).getTenantId());
    }

    @Test
    public void testSearchAdvocateApplications() {
        // Setup
        RequestInfo requestInfo = new RequestInfo();
        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        when(advocateRegistrationRepository.getApplications(any())).thenReturn(Collections.emptyList());

        // Invoke
        List<Advocate> result = service.searchAdvocateApplications(requestInfo, searchCriteria);

        // Verify
        assertEquals(0, result.size());
        verify(advocateRegistrationRepository, times(1)).getApplications(searchCriteria);
    }
}
