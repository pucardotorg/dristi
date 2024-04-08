package org.pucar.web.controllers.enrichment;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.util.IdgenUtil;
import org.pucar.util.UserUtil;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
public class AdvocateClerkRegistrationEnrichmentTest {

    @InjectMocks
    private AdvocateClerkRegistrationEnrichment enrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private UserUtil userUtil;

    @Test
    public void testEnrichAdvocateClerkRegistration() {
        MockitoAnnotations.initMocks(this);

        // Mock data
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        request.setRequestInfo(new RequestInfo());
        request.setClerks(new ArrayList<>()); // Initialize the list of clerks
        request.getClerks().add(createAdvocateClerk()); // Add advocate clerk to the list
        when(idgenUtil.getIdList(request.getRequestInfo(), "tenantId", "product.id", "P-[cy:yyyy-MM-dd]-[SEQ_PRODUCT_P]", 1))
                .thenReturn(List.of("P-2024-03-14-1"));

        // Execute
        enrichment.enrichAdvocateClerkRegistration(request);

        // Verify
        AdvocateClerk clerk = request.getClerks().get(0);
        assertEquals("P-2024-03-14-1", clerk.getApplicationNumber());
        assertEquals(clerk.getAuditDetails().getCreatedBy(), clerk.getAuditDetails().getLastModifiedBy());
    }

    private AdvocateClerk createAdvocateClerk() {
        return AdvocateClerk.builder()
                .tenantId("tenantId")
                .stateRegnNumber("stateRegnNumber")
                .individualId("individualId")
                .isActive(true)
                .additionalDetails(new Object())
                .build();
    }
}

