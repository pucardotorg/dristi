package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdvocateClerkRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;
    @Mock
    private Configuration configuration;
    @InjectMocks
    private AdvocateClerkRegistrationEnrichment advocateClerkRegistrationEnrichment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrichAdvocateClerkRegistrationTest() {
        // Setup mock request and expected results
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        clerks.add(clerk);
        advocateClerkRequest.setClerks(clerks);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        requestInfo.getUserInfo().setTenantId("tenantId");
        advocateClerkRequest.setRequestInfo(requestInfo);
        List<String> idList = List.of("C-2021-01-01-001");
        when(idgenUtil.getIdList(any(), anyString(), any(), any(), anyInt())).thenReturn(idList);

        // Call the method to test
        advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest);

        // Verify IdgenUtil was called correctly
        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId",   configuration.getAdvClerkApplicationNumberConfig(),
                null, 1);

        // Assert that each clerk has been enriched as expected
        assertNotNull(clerks.get(0).getId());
        assertNotNull(clerks.get(0).getAuditDetails());
        assertEquals(false, clerks.get(0).getIsActive());
        assertEquals("user-uuid", clerks.get(0).getAuditDetails().getCreatedBy());
        assertNotNull(clerks.get(0).getAuditDetails().getCreatedTime());
        assertEquals("user-uuid", clerks.get(0).getAuditDetails().getLastModifiedBy());
        assertNotNull(clerks.get(0).getAuditDetails().getLastModifiedTime());
    }

    @Test
    void enrichAdvocateClerkRegistrationUponUpdateTest() {
        // Setup mock request and expected results
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        clerk.setAuditDetails(auditDetails);
        clerks.add(clerk);
        advocateClerkRequest.setClerks(clerks);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Call the method to test
        advocateClerkRegistrationEnrichment.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

        // Assert that each clerk has been enriched as expected
        assertNotNull(clerks.get(0).getAuditDetails());
        assertEquals("user-uuid-2", clerks.get(0).getAuditDetails().getLastModifiedBy());
        assertNotNull(clerks.get(0).getAuditDetails().getLastModifiedTime());
    }

    @Test
    public void testEnrichAdvocateClerkRegistration_MissingUserInfo() {
        // Setup request with missing user info
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        advocateClerkRequest.setClerks(Collections.singletonList(new AdvocateClerk()));
        advocateClerkRequest.setRequestInfo(new RequestInfo());

        // Expect exception due to missing user info
        assertThrows(CustomException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    public void testEnrichAdvocateClerkRegistration_IdgenUtilException() {
        // Setup mock request
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        clerks.add(clerk);
        advocateClerkRequest.setClerks(clerks);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Mock IdgenUtil to throw exception
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenThrow(new RuntimeException("Mocked Exception"));

        // Expect exception to propagate
        assertThrows(RuntimeException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    public void testEnrichAdvocateClerkRegistration_EmptyClerkList() {
        // Setup request with empty clerk list
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        advocateClerkRequest.setRequestInfo(new RequestInfo());
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        advocateClerkRequest.getRequestInfo().setUserInfo(userInfo);
        advocateClerkRequest.setClerks(new ArrayList<>());

        // Call the method
        advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest);

        // No assertions needed, method should not throw exceptions
    }

}



