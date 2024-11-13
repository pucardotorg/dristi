package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
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
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);
        Document document = new Document();
        document.setId("id");
        document.setDocumentUid("documentUid");
        List<Document> list = new ArrayList<>();
        list.add(document);
        clerk.setDocuments(list);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        requestInfo.getUserInfo().setTenantId("tenantId");
        advocateClerkRequest.setRequestInfo(requestInfo);
        List<String> idList = List.of("C-2021-01-01-001");
        when(idgenUtil.getIdList(any(), anyString(), any(), any(), anyInt(),any())).thenReturn(idList);

        // Call the method to test
        advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest);

        // Verify IdgenUtil was called correctly
        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId",   configuration.getAdvClerkApplicationNumberConfig(),
                null, 1,true);

        // Assert that each clerk has been enriched as expected
        assertNotNull(clerk.getId());
        assertNotNull(clerk.getAuditDetails());
        assertEquals(false, clerk.getIsActive());
        assertEquals("user-uuid", clerk.getAuditDetails().getCreatedBy());
        assertNotNull(clerk.getAuditDetails().getCreatedTime());
        assertEquals("user-uuid", clerk.getAuditDetails().getLastModifiedBy());
        assertNotNull(clerk.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void enrichAdvocateClerkRegistrationUponUpdateTest() {
        // Setup mock request and expected results
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        clerk.setAuditDetails(auditDetails);
        advocateClerkRequest.setClerk(clerk);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Call the method to test
        advocateClerkRegistrationEnrichment.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);

        // Assert that each clerk has been enriched as expected
        assertNotNull(clerk.getAuditDetails());
        assertEquals("user-uuid-2", clerk.getAuditDetails().getLastModifiedBy());
        assertNotNull(clerk.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void enrichAdvocateClerkRegistrationUponUpdateExceptionTest() {
        // Setup mock request and expected results
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        clerk.setAuditDetails(auditDetails);
        advocateClerkRequest.setClerk(null);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Assert that each clerk has been enriched as expected
        assertThrows(Exception.class, () -> {
            advocateClerkRegistrationEnrichment.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest);
        });
    }

    @Test
     void testEnrichAdvocateClerkRegistration_MissingUserInfo() {
        // Setup request with missing user info
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        advocateClerkRequest.setClerk(new AdvocateClerk());
        advocateClerkRequest.setRequestInfo(new RequestInfo());

        // Expect exception due to missing user info
        assertThrows(CustomException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
        assertThrows(Exception.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
     void testEnrichAdvocateClerkRegistration_CustomException() {
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);
        Document document = new Document();
        document.setId("id");
        document.setDocumentUid("documentUid");
        List<Document> list = new ArrayList<>();
        list.add(document);
        clerk.setDocuments(list);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        requestInfo.getUserInfo().setTenantId("tenantId");
        advocateClerkRequest.setRequestInfo(requestInfo);

        when(idgenUtil.getIdList(any(), anyString(), any(), any(), anyInt(),any())).thenThrow(new CustomException());

        // Expect exception due to missing user info
        assertThrows(CustomException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
     void testEnrichAdvocateClerkRegistration_IdgenUtilException() {
        // Setup mock request
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Mock IdgenUtil to throw exception
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt(),any())).thenThrow(new RuntimeException("Mocked Exception"));

        // Expect exception to propagate
        assertThrows(RuntimeException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    void enrichAdvocateClerkRegistrationUponUpdateTest_CustomException() {
        // Setup mock request and expected results
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        clerk.setAuditDetails(auditDetails);
        advocateClerkRequest.setClerk(null);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateClerkRequest.setRequestInfo(requestInfo);

        // Call the method to test
        assertThrows(CustomException.class, () -> advocateClerkRegistrationEnrichment.enrichAdvocateClerkApplicationUponUpdate(advocateClerkRequest));
    }

}



