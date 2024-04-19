package org.pucar.enrichment;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.pucar.util.IdgenUtil;
import org.pucar.util.UserUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdvocateRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private UserUtil userUtils;

    @InjectMocks
    private AdvocateRegistrationEnrichment advocateRegistrationEnrichment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrichAdvocateRegistrationTest() {
        // Setup mock request and expected results
        AdvocateRequest advocateRequest = new AdvocateRequest();
        List<Advocate> advocates = new ArrayList<>();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        advocates.add(advocate);
        advocateRequest.setAdvocates(advocates);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        advocateRequest.setRequestInfo(requestInfo);
        List<String> idList = List.of("P-2021-01-01-001");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), anyString(), anyInt())).thenReturn(idList);

        // Call the method to test
        advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest);

        // Verify IdgenUtil was called correctly
        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId",   "advocate.id",
                null, 1);

        // Assert that each advocate has been enriched as expected
        assertNotNull(advocates.get(0).getId());
        assertNotNull(advocates.get(0).getAuditDetails());
        assertEquals("user-uuid", advocates.get(0).getAuditDetails().getCreatedBy());
        assertNotNull(advocates.get(0).getAuditDetails().getCreatedTime());
        assertEquals("user-uuid", advocates.get(0).getAuditDetails().getLastModifiedBy());
        assertNotNull(advocates.get(0).getAuditDetails().getLastModifiedTime());
    }
}
