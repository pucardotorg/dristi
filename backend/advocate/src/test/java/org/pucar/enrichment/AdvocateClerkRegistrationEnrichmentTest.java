package org.pucar.enrichment;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.util.IdgenUtil;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AdvocateClerkRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @InjectMocks
    private AdvocateClerkRegistrationEnrichment enrichment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrichAdvocateClerkRegistration_ValidRequest_EnrichmentSuccessful() {
        // Create a mock AdvocateClerkRequest with necessary data
        AdvocateClerk clerk = new AdvocateClerk();
        String tenantId = "P-2024-04-09-001";
        clerk.setTenantId(tenantId);
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        request.setClerks(Collections.singletonList(clerk));

        // Create a mock RequestInfo object and set it to the AdvocateClerkRequest
        RequestInfo requestInfo = new RequestInfo();
        User dummyUser = new User();

        requestInfo.setUserInfo(dummyUser);

        request.setRequestInfo(requestInfo);

        // Mock the behavior of IdgenUtil
        when(idgenUtil.getIdList(any(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Collections.singletonList("P-2024-04-09-001"));

        // Call the method under test
        enrichment.enrichAdvocateClerkRegistration(request);

        assertEquals(tenantId, clerk.getApplicationNumber());
        assertNotNull(clerk.getId());
    }

}

