package org.pucar.dristi.service;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.validators.AdvocateRegistrationValidator;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdvocateServiceTest {

    @InjectMocks
    private AdvocateService advocateService;

    @Mock
    private AdvocateRegistrationValidator validator;

    @Mock
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateRepository advocateRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;




    @Test
    public void testSearchAdvocateByStatus_Success() {
        // Setup
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        // Mock behavior
        when(advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset)).thenReturn(Collections.singletonList(new Advocate() {
        }));

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByStatus(status, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }

    @Test
    public void testSearchAdvocateByApplicationNumber_Success() {
        // Setup
        String applicationNumber = "testApplicationNumber";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        // Mock behavior
        when(advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset)).thenReturn(Collections.singletonList(new Advocate() {
        }));

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByApplicationNumber(applicationNumber, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }
}


