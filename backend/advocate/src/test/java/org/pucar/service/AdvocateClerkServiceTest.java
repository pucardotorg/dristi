package org.pucar.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRegistrationRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;

@RunWith(MockitoJUnitRunner.class)
public class AdvocateClerkServiceTest {

    @Mock
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateClerkRegistrationRepository advocateRegistrationRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateClerkService advocateClerkService;

    private AdvocateClerkRequest validRequest;

    @Before
    public void setUp() {
        validRequest = new AdvocateClerkRequest();
        // Initialize validRequest with necessary data
    }

    @Test
    public void testRegisterAdvocateRequest_Success() {
        // Mocking
        doNothing().when(validator).validateAdvocateClerkRegistration(validRequest);
        doNothing().when(enrichmentUtil).enrichAdvocateClerkRegistration(validRequest);
        doNothing().when(workflowService).updateWorkflowStatus(validRequest);

        // Method call
        List<AdvocateClerk> result = advocateClerkService.registerAdvocateRequest(validRequest);

        // Verification
        verify(validator).validateAdvocateClerkRegistration(validRequest);
        verify(enrichmentUtil).enrichAdvocateClerkRegistration(validRequest);
        verify(workflowService).updateWorkflowStatus(validRequest);
        verify(producer).push(anyString(), any());

        // Assertions
        assertEquals(validRequest.getClerks(), result);
    }

    @Test
    public void testRegisterAdvocateRequest_EmptyClerksList() {
        // Modify request to have empty clerks list
        validRequest.setClerks(new ArrayList<>());

        // Mocking
        doNothing().when(validator).validateAdvocateClerkRegistration(validRequest);
        doNothing().when(enrichmentUtil).enrichAdvocateClerkRegistration(validRequest);
        doNothing().when(workflowService).updateWorkflowStatus(validRequest);

        // Method call
        List<AdvocateClerk> result = advocateClerkService.registerAdvocateRequest(validRequest);

        // Verification
        verify(validator).validateAdvocateClerkRegistration(validRequest);
        verify(enrichmentUtil).enrichAdvocateClerkRegistration(validRequest);
        verify(workflowService).updateWorkflowStatus(validRequest);
        verify(producer).push(anyString(), any());

        // Assertions
        assertEquals(validRequest.getClerks(), result);
    }

    // Add more test cases as needed
}
