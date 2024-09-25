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
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdvocateRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;
    @Mock
    private Configuration configuration;
    @InjectMocks
    private AdvocateRegistrationEnrichment advocateRegistrationEnrichment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void enrichAdvocateRegistrationTest() {
//        // Setup mock request and expected results
//        AdvocateRequest advocateRequest = new AdvocateRequest();
//        Advocate advocate = new Advocate();
//        advocate.setTenantId("tenantId");
//        Document document = new Document();
//        document.setId("id");
//        document.setDocumentUid("documentUid");
//        List<Document> list = new ArrayList<>();
//        list.add(document);
//        advocate.setDocuments(list);
//        advocateRequest.setAdvocate(advocate);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        requestInfo.getUserInfo().setTenantId("tenantId");
//        advocateRequest.setRequestInfo(requestInfo);
//        List<String> idList = List.of("P-2021-01-01-001");
//        when(idgenUtil.getIdList(any(), anyString(), any(), any(), anyInt(),any())).thenReturn(idList);
//
//        // Call the method to test
//        advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest);
//
//        // Verify IdgenUtil was called correctly
//        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId",   configuration.getAdvApplicationNumberConfig(),
//                null, 1,any());
//
//        // Assert that each advocate has been enriched as expected
//        assertNotNull(advocate.getId());
//        assertNotNull(advocate.getAuditDetails());
//        assertEquals(false, advocate.getIsActive());
//        assertEquals("user-uuid", advocate.getAuditDetails().getCreatedBy());
//        assertNotNull(advocate.getAuditDetails().getCreatedTime());
//        assertEquals("user-uuid", advocate.getAuditDetails().getLastModifiedBy());
//        assertNotNull(advocate.getAuditDetails().getLastModifiedTime());
//    }

    @Test
    void enrichAdvocateRegistrationUponUpdateTest() {
        // Setup mock request and expected results
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        advocate.setAuditDetails(auditDetails);
        advocateRequest.setAdvocate(advocate);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateRequest.setRequestInfo(requestInfo);

        // Call the method to test
        advocateRegistrationEnrichment.enrichAdvocateApplicationUponUpdate(advocateRequest);

        // Assert that each advocate has been enriched as expected
        assertNotNull(advocate.getAuditDetails());
        assertEquals("user-uuid-2", advocate.getAuditDetails().getLastModifiedBy());
        assertNotNull(advocate.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void enrichAdvocateRegistrationUponUpdateExceptionTest() {
        // Setup mock request and expected results
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
        advocate.setAuditDetails(auditDetails);
        advocateRequest.setAdvocate(null);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid-2");
        requestInfo.setUserInfo(userInfo);
        advocateRequest.setRequestInfo(requestInfo);

        // Assert that each advocate has been enriched as expected
        assertThrows(Exception.class, () -> {
            advocateRegistrationEnrichment.enrichAdvocateApplicationUponUpdate(advocateRequest);
        });
    }

    @Test
     void testEnrichAdvocateRegistration_MissingUserInfo() {
        // Setup request with missing user info
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocate(new Advocate());
        advocateRequest.setRequestInfo(new RequestInfo());

        // Expect exception due to missing user info
        assertThrows(CustomException.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
        assertThrows(Exception.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
    }

    @Test
     void testEnrichAdvocateRegistration_IdgenUtilException() {
        // Setup mock request
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        advocateRequest.setAdvocate(advocate);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        advocateRequest.setRequestInfo(requestInfo);

        // Mock IdgenUtil to throw exception
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt(),any())).thenThrow(new RuntimeException("Mocked Exception"));

        // Expect exception to propagate
        assertThrows(Exception.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
    }

    @Test
    void enrichAdvocateRegistrationTest_CustomException() {
        // Setup mock request and expected results
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("tenantId");
        Document document = new Document();
        document.setId("id");
        document.setDocumentUid("documentUid");
        List<Document> list = new ArrayList<>();
        list.add(document);
        advocate.setDocuments(list);
        advocateRequest.setAdvocate(advocate);
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);
        requestInfo.getUserInfo().setTenantId("tenantId");
        advocateRequest.setRequestInfo(requestInfo);
        when(idgenUtil.getIdList(any(), anyString(), any(), any(), anyInt(),any())).thenThrow(new CustomException());

        assertThrows(Exception.class, () -> advocateRegistrationEnrichment.enrichAdvocateRegistration(advocateRequest));
    }


}
