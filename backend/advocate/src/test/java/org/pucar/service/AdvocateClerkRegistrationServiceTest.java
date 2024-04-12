package org.pucar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRegistrationRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdvocateClerkRegistrationServiceTest {

    @Mock
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Mock
    private IndividualService individualService;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateClerkRegistrationRepository advocateRegistrationRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateClerkService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerAdvocateRequest_SuccessfulRegistration_ReturnsRegisteredClerks() {
        // Mock data
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        request.setClerks(Collections.singletonList(clerk));

        // Mock behavior
        doNothing().when(enrichmentUtil).enrichAdvocateClerkRegistration(request);
        doNothing().when(workflowService).updateWorkflowStatus(request);

        // Perform registration
        List<AdvocateClerk> result = service.registerAdvocateRequest(request);

        // Verify interactions and result
        verify(validator).validateAdvocateClerkRegistration(request);
        verify(enrichmentUtil).enrichAdvocateClerkRegistration(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("save-adv-application", request);
        assertEquals(Collections.singletonList(clerk), result);
    }

}
