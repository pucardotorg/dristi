//package org.pucar.dristi.enrichment;
//
//import org.egov.common.contract.models.AuditDetails;
//import org.egov.common.contract.models.Document;
//import org.egov.common.contract.request.RequestInfo;
//import org.egov.common.contract.request.User;
//import org.egov.tracer.model.CustomException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.pucar.dristi.config.Configuration;
//import org.pucar.dristi.util.IdgenUtil;
//import org.pucar.dristi.web.models.Hearing;
//import org.pucar.dristi.web.models.HearingRequest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;
//
//class HearingRegistrationEnrichmentTest {
//
//    @Mock
//    private IdgenUtil idgenUtil;
//
//    @Mock
//    private Configuration configuration;
//
//    @InjectMocks
//    private HearingRegistrationEnrichment hearingRegistrationEnrichment;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void enrichHearingRegistrationTest() {
//        // Setup mock request and expected results
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearing.setTenantId("tenantId");
//        Document document = new Document();
//        List<Document> list = new ArrayList<>();
//        list.add(document);
//        hearing.setDocuments(list);
//        List<String> cnr = new ArrayList<>();
//        cnr.add("test");
//        hearing.setCnrNumbers(cnr);
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        requestInfo.getUserInfo().setTenantId("tenantId");
//        hearingRequest.setRequestInfo(requestInfo);
//        List<String> idList = List.of("Hearing123");
//        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt(),any())).thenReturn(idList);
//
//        // Call the method to test
//        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);
//
//        // Verify IdgenUtil was called correctly
//        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId", "hearing.id", null, 1,true);
//
//        // Assert that the hearing has been enriched as expected
//        assertNotNull(hearing.getId());
//        assertNotNull(hearing.getAuditDetails());
//        assertEquals(false, hearing.getIsActive());
//        assertEquals("user-uuid", hearing.getAuditDetails().getCreatedBy());
//        assertNotNull(hearing.getAuditDetails().getCreatedTime());
//        assertEquals("user-uuid", hearing.getAuditDetails().getLastModifiedBy());
//        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
//        assertEquals("Hearing123", hearing.getHearingId());
//        assertNotNull(hearing.getDocuments());
//        hearing.getDocuments().forEach(doc -> {
//            assertNotNull(doc.getId());
//            assertEquals(doc.getId(), doc.getDocumentUid());
//        });
//    }
//
//    @Test
//    void enrichHearingRegistrationNoDocumentsTest() {
//        // Setup mock request without documents
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearing.setTenantId("tenantId");
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        userInfo.setTenantId("tenantId");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//        List<String> idList = List.of("Hearing123");
//        when(idgenUtil.getIdList(any(), any(), anyString(), any(), anyInt(),any())).thenReturn(idList);
//
//        // Call the method to test
//        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);
//
//        // Verify IdgenUtil was called correctly
//        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId", "hearing.id", null, 1,true);
//
//        // Assert that the hearing has been enriched as expected
//        assertNotNull(hearing.getId());
//        assertNotNull(hearing.getAuditDetails());
//        assertEquals(false, hearing.getIsActive());
//        assertEquals("user-uuid", hearing.getAuditDetails().getCreatedBy());
//        assertNotNull(hearing.getAuditDetails().getCreatedTime());
//        assertEquals("user-uuid", hearing.getAuditDetails().getLastModifiedBy());
//        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
//        assertEquals("Hearing123", hearing.getHearingId());
//        assertTrue(hearing.getDocuments().isEmpty());
//    }
//
//    @Test
//    void enrichHearingRegistrationMultipleDocumentsTest() {
//        // Setup mock request with multiple documents
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearing.setTenantId("tenantId");
//        Document document1 = new Document();
//        Document document2 = new Document();
//        List<Document> list = new ArrayList<>();
//        list.add(document1);
//        list.add(document2);
//        hearing.setDocuments(list);
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setTenantId("tenantId");
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//        List<String> idList = List.of("Hearing123");
//        when(idgenUtil.getIdList(any(), any(), anyString(), any(), anyInt(),any())).thenReturn(idList);
//
//        // Call the method to test
//        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);
//
//        // Verify IdgenUtil was called correctly
//        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId", "hearing.id", null, 1,true);
//
//        // Assert that the hearing has been enriched as expected
//        assertNotNull(hearing.getId());
//        assertNotNull(hearing.getAuditDetails());
//        assertEquals(false, hearing.getIsActive());
//        assertEquals("user-uuid", hearing.getAuditDetails().getCreatedBy());
//        assertNotNull(hearing.getAuditDetails().getCreatedTime());
//        assertEquals("user-uuid", hearing.getAuditDetails().getLastModifiedBy());
//        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
//        assertEquals("Hearing123", hearing.getHearingId());
//        assertNotNull(hearing.getDocuments());
//        hearing.getDocuments().forEach(doc -> {
//            assertNotNull(doc.getId());
//            assertEquals(doc.getId(), doc.getDocumentUid());
//        });
//    }
//
//    @Test
//    void enrichHearingRegistrationMissingFieldsInRequestInfoTest() {
//        // Setup mock request with missing optional fields in RequestInfo
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearing.setTenantId("tenantId");
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setTenantId("tenantId");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//        List<String> idList = List.of("Hearing123");
//        when(idgenUtil.getIdList(any(), any(), anyString(), any(), anyInt(),any())).thenReturn(idList);
//
//        // Call the method to test
//        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);
//
//        // Verify IdgenUtil was called correctly
//        verify(idgenUtil, times(1)).getIdList(requestInfo, "tenantId", "hearing.id", null, 1,true);
//
//        // Assert that the hearing has been enriched as expected
//        assertNotNull(hearing.getId());
//        assertNotNull(hearing.getAuditDetails());
//        assertEquals(false, hearing.getIsActive());
//        assertNull(hearing.getAuditDetails().getCreatedBy());
//        assertNotNull(hearing.getAuditDetails().getCreatedTime());
//        assertNull(hearing.getAuditDetails().getLastModifiedBy());
//        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
//        assertEquals("Hearing123", hearing.getHearingId());
//    }
//
//    @Test
//    void enrichHearingRegistrationThrowsCustomExceptionTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//
//        // Mock IdgenUtil to throw CustomException
//        when(idgenUtil.getIdList(any(), any(), anyString(), any(), anyInt(),any())).thenThrow(new CustomException("ID_GENERATION_ERROR", "ID generation failed"));
//
//        // Expect CustomException to propagate
//        CustomException exception = assertThrows(CustomException.class, () -> hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest));
//        assertEquals("ID_GENERATION_ERROR", exception.getCode());
//    }
//
//    @Test
//    void enrichHearingRegistrationThrowsExceptionTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//
//        // Mock IdgenUtil to throw RuntimeException
//        when(idgenUtil.getIdList(any(), any(), anyString(), any(), anyInt(),any())).thenThrow(new RuntimeException("Random error"));
//
//        // Expect CustomException to be thrown and check the message
//        CustomException exception = assertThrows(CustomException.class, () -> hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest));
//        assertEquals("ENRICHMENT_EXCEPTION", exception.getCode());
//        assertTrue(exception.getMessage().contains("Random error"));
//    }
//
//    @Test
//    void enrichHearingApplicationUponUpdateTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        AuditDetails auditDetails = AuditDetails.builder().createdBy("user-uuid-1").createdTime(System.currentTimeMillis()).lastModifiedBy("user-uuid-1").lastModifiedTime(System.currentTimeMillis()).build();
//        hearing.setAuditDetails(auditDetails);
//        hearingRequest.setHearing(hearing);
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid-2");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//
//        // Call the method to test
//        hearingRegistrationEnrichment.enrichHearingApplicationUponUpdate(hearingRequest);
//
//        // Assert that the hearing has been enriched as expected
//        assertNotNull(hearing.getAuditDetails());
//        assertEquals("user-uuid-2", hearing.getAuditDetails().getLastModifiedBy());
//        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
//    }
//
//    @Test
//    void enrichHearingApplicationUponUpdateExceptionTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        hearingRequest.setHearing(null); // This should trigger an exception
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid-2");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//
//        // Expect CustomException due to null hearing
//        CustomException exception = assertThrows(CustomException.class, () -> hearingRegistrationEnrichment.enrichHearingApplicationUponUpdate(hearingRequest));
//        assertEquals("ENRICHMENT_EXCEPTION", exception.getCode());
//        assertTrue(exception.getMessage().contains("Error in hearing enrichment service during hearing update process"));
//    }
//
//    @Test
//    void enrichHearingRegistrationNullHearingTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        hearingRequest.setHearing(null); // This should trigger an exception
//        RequestInfo requestInfo = new RequestInfo();
//        User userInfo = new User();
//        userInfo.setUuid("user-uuid");
//        requestInfo.setUserInfo(userInfo);
//        hearingRequest.setRequestInfo(requestInfo);
//
//        // Expect CustomException due to null hearing
//        CustomException exception = assertThrows(CustomException.class, () -> hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest));
//        assertEquals(ENRICHMENT_EXCEPTION, exception.getCode());
//        assertTrue(exception.getMessage().contains("Error hearing in enrichment service:"));
//    }
//
//    @Test
//    void enrichHearingRegistrationNullRequestInfoTest() {
//        // Setup mock request
//        HearingRequest hearingRequest = new HearingRequest();
//        Hearing hearing = new Hearing();
//        hearing.setTenantId("tenantId");
//        hearingRequest.setHearing(hearing);
//        hearingRequest.setRequestInfo(null); // This should trigger an exception
//
//        // Expect CustomException due to null requestInfo
//        CustomException exception = assertThrows(CustomException.class, () -> hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest));
//        assertEquals(ENRICHMENT_EXCEPTION, exception.getCode());
//        assertTrue(exception.getMessage().contains("Error hearing in enrichment service:"));
//    }
//}
