package org.pucar.dristi.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.validators.AdvocateClerkRegistrationValidator;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdvocateClerkServiceTest {

    @InjectMocks
    private AdvocateClerkService advocateClerkService;

    @Mock
    private AdvocateClerkRepository advocateClerkRepository;

    @Mock
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterAdvocateClerkRequest_Success() {
        // Mock data
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        // Setup mocks
        when(config.getAdvClerkcreateTopic()).thenReturn("advClerkCreateTopic");
        // Test method
        advocateClerkService.registerAdvocateClerkRequest(request);
        // Verify interactions
        verify(validator).validateAdvocateClerkRegistration(request);
        verify(enrichmentUtil).enrichAdvocateClerkRegistration(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("advClerkCreateTopic", request);
    }

}

