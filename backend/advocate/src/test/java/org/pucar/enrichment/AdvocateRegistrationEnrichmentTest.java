package org.pucar.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.pucar.util.IdgenUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdvocateRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

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
        requestInfo.getUserInfo().setTenantId("tenantId");
        advocateRequest.setRequestInfo(requestInfo);
        List<String> idList = List.of("P-2021-01-01-001");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenReturn(idList);

        // Call the method to test
        advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest);

        // Verify IdgenUtil was called correctly
        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId",   "advocate.id",
                null, 1);

        // Assert that each advocate has been enriched as expected
        assertNotNull(advocates.get(0).getId());
        assertNotNull(advocates.get(0).getAuditDetails());
        assertEquals(false, advocates.get(0).getIsActive());
        assertEquals("user-uuid", advocates.get(0).getAuditDetails().getCreatedBy());
        assertNotNull(advocates.get(0).getAuditDetails().getCreatedTime());
        assertEquals("user-uuid", advocates.get(0).getAuditDetails().getLastModifiedBy());
        assertNotNull(advocates.get(0).getAuditDetails().getLastModifiedTime());
    }

    @Test
    void enrichAdvocateRegistrationUponUpdateTest() {
        // Setup mock request and expected results
        AdvocateRequest advocateRequest = new AdvocateRequest();
        List<Advocate> advocates = new ArrayList<>();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        advocate.setAuditDetails(auditDetails);
        advocates.add(advocate);
        advocateRequest.setAdvocates(advocates);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateRequest.setRequestInfo(requestInfo);

        // Call the method to test
        advocateRegistrationEnrichment.enrichAdvocateApplicationUponUpdate(advocateRequest);

        // Assert that each advocate has been enriched as expected
        assertNotNull(advocates.get(0).getAuditDetails());
        assertEquals("user-uuid-2", advocates.get(0).getAuditDetails().getLastModifiedBy());
        assertNotNull(advocates.get(0).getAuditDetails().getLastModifiedTime());
    }

    @Test
    public void testEnrichAdvocateRegistration_MissingUserInfo() {
        // Setup request with missing user info
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocates(Collections.singletonList(new Advocate()));
        advocateRequest.setRequestInfo(new RequestInfo());

        // Expect exception due to missing user info
        assertThrows(CustomException.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
    }

    @Test
    public void testEnrichAdvocateRegistration_IdgenUtilException() {
        // Setup mock request
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

        // Mock IdgenUtil to throw exception
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenThrow(new RuntimeException("Mocked Exception"));

        // Expect exception to propagate
        assertThrows(RuntimeException.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
    }

    @Test
    public void testEnrichAdvocateRegistration_EmptyAdvocateList() {
        // Setup request with empty advocate list
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setRequestInfo(new RequestInfo());
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        advocateRequest.getRequestInfo().setUserInfo(userInfo);
        advocateRequest.setAdvocates(new ArrayList<>());

        // Call the method
        advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest);

        // No assertions needed, method should not throw exceptions
    }


}
