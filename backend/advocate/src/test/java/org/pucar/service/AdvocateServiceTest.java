package org.pucar.service;

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
import org.pucar.web.models.AdvocateResponse;

import java.util.Collections;
import java.util.List;

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
    private AdvocateRegistrationRepository advocateRegistrationRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAdvocateRequest() {
        // Prepare the request
        AdvocateRequest request = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenant1");
        advocate.setIndividualId("individualId");
        request.setAdvocates(Collections.singletonList(advocate));
        when(individualService.searchIndividual(request)).thenReturn(true);

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
}
