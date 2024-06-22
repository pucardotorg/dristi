package org.pucar.dristi.enrichment;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;
    @Mock
    private CaseUtil caseUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private CaseRegistrationEnrichment caseRegistrationEnrichment;

    private CaseRequest caseRequest;
    private CourtCase courtCase;
    private RequestInfo requestInfo;
    private User userInfo;

    @BeforeEach
    void setUp() {
        // Initialize RequestInfo with necessary user info
        requestInfo = new RequestInfo();
        userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);

        Document document = new Document();
        document.setDocumentType("documentType");
        document.setFileStore("fileStore");
        List<Document> documentsList = new ArrayList<>();
        documentsList.add(document);

        // Create case-indexer.yml CaseRequest with case-indexer.yml single CourtCase
        caseRequest = new CaseRequest();
        courtCase = new CourtCase();
        courtCase.setTenantId("tenant-id");
        List<LinkedCase> linkedCases = new ArrayList<>();
        linkedCases.add(LinkedCase.builder().caseNumber("caseNumber").documents(documentsList).build());
        courtCase.setLinkedCases(linkedCases);

        List<Party> listLitigants = new ArrayList<>();
        listLitigants.add(Party.builder().partyCategory("ctaegory1").documents(documentsList).build());
        listLitigants.add(Party.builder().tenantId("pg").partyCategory("ctaegory2").documents(documentsList).build());
        courtCase.setLitigants(listLitigants);

        List<AdvocateMapping> advocateMappingList = new ArrayList<>();
        List<Party> representingList = new ArrayList<>();
        representingList.add(Party.builder().tenantId("pg").documents(documentsList).build());
        advocateMappingList.add(AdvocateMapping.builder().tenantId("pg").representing(representingList).documents(documentsList).build());
        courtCase.setRepresentatives(advocateMappingList);

        List<StatuteSection> statuteSectionList = new ArrayList<>();
        List<String> sections = new ArrayList<>();
        sections.add("section1");
        sections.add("section2");
        List<String> subSections = new ArrayList<>();
        subSections.add("subsection1");
        subSections.add("subsection2");
        statuteSectionList.add(StatuteSection.builder().tenantId("pg").sections(sections).subsections(subSections).build());
        courtCase.setStatutesAndSections(statuteSectionList);

        documentsList.add(Document.builder().fileStore("fileStore").build());
        courtCase.setDocuments(documentsList);
        caseRequest.setCases(courtCase);

        // Set the request info in the case request
        caseRequest.setRequestInfo(requestInfo);
    }

    @Test
    void testEnrichCaseRegistration() {
// Setup mocks
        List<String> idList = Collections.singletonList("fillingNumberId");
        doReturn(idList).when(idgenUtil).getIdList(any(RequestInfo.class), eq("tenantId"), any(), isNull(), eq(1));
        courtCase = new CourtCase();
        courtCase.setTenantId("tenantId");

        requestInfo = new RequestInfo();
        User user = new User();
        user.setUuid("user-uuid");
        requestInfo.setUserInfo(user);
        caseRequest = new CaseRequest();
        caseRequest.setRequestInfo(requestInfo);
        caseRequest.setCases(courtCase);
        // Call the method under test
        caseRegistrationEnrichment.enrichCaseRegistrationOnCreate(caseRequest);
        // Verify the method behavior
        verify(idgenUtil).getIdList(any(RequestInfo.class), eq("tenantId"), any(), isNull(), eq(1));
        assertNotNull(courtCase.getAuditdetails());
        assertNotNull(courtCase.getId());
        assertNotNull(courtCase.getFilingNumber());
        assertNotNull(courtCase.getAuditdetails().getCreatedBy());
        assertNotNull(courtCase.getAuditdetails().getCreatedTime());
        assertNotNull(courtCase.getAuditdetails().getLastModifiedBy());
        assertNotNull(courtCase.getAuditdetails().getLastModifiedTime());
    }
    @Test
    void enrichCaseRegistration_OnCreate_ShouldThrowCustomException_WhenErrorOccurs() {

        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenThrow(new RuntimeException("Error"));

        // Invoke the method and assert that it throws CustomException
        assertThrows(Exception.class, () -> caseRegistrationEnrichment.enrichCaseRegistrationOnCreate(caseRequest));
    }

    @Test
    void enrichCaseApplicationUponUpdate_ShouldEnrichAuditDetails() {
        userInfo.setUuid("user123");
        courtCase.setId(UUID.randomUUID());
        courtCase.setAuditdetails(new AuditDetails());
        String oldLastModifiedBy = "oldUser";
        courtCase.getAuditdetails().setLastModifiedBy(oldLastModifiedBy);
        Long oldLastModifiedTime = 123456789L;
        courtCase.getAuditdetails().setLastModifiedTime(oldLastModifiedTime);

        // Invoke the method
        caseRegistrationEnrichment.enrichCaseApplicationUponUpdate(caseRequest);

        // Assert the enriched audit details
        assertNotEquals(oldLastModifiedTime, courtCase.getAuditdetails().getLastModifiedTime());
        assertNotEquals(oldLastModifiedBy, courtCase.getAuditdetails().getLastModifiedBy());
        assertEquals("user123", courtCase.getAuditdetails().getLastModifiedBy());
    }

    @Test
    void enrichCaseApplicationUponUpdate_ShouldEnrichAuditDetailsException() {
        caseRequest.setCases(null);

        assertThrows(Exception.class, () -> caseRegistrationEnrichment.enrichCaseRegistrationOnCreate(caseRequest));
    }

    @Test
    void enrichCaseApplicationUponUpdate_Exception() {
        caseRequest.setCases(null);

        assertThrows(Exception.class, () -> caseRegistrationEnrichment.enrichCaseApplicationUponUpdate(caseRequest));
    }

    @Test
    void enrichAccessCode_generatesAccessCode() {
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());
        caseRegistrationEnrichment.enrichAccessCode(caseRequest);

        assertNotNull(caseRequest.getCases().getAccessCode());
    }

    @Test
    void enrichAccessCode_generatesUniqueAccessCodes() {
        CaseRequest caseRequest1 = new CaseRequest();
        CaseRequest caseRequest2 = new CaseRequest();
        caseRequest1.setCases(new CourtCase());
        caseRequest2.setCases(new CourtCase());

        caseRegistrationEnrichment.enrichAccessCode(caseRequest1);
        caseRegistrationEnrichment.enrichAccessCode(caseRequest2);

        assertNotEquals(caseRequest1.getCases().getAccessCode(), caseRequest2.getCases().getAccessCode());
    }

    @Test
    void listToString_returnsEmptyStringForEmptyList() {
        List<String> emptyList = new ArrayList<>();
        String result = caseRegistrationEnrichment.listToString(emptyList);
        assertEquals("", result);
    }

    @Test
    void listToString_returnsSingleElementForSingleItemList() {
        List<String> singleItemList = Collections.singletonList("item1");
        String result = caseRegistrationEnrichment.listToString(singleItemList);
        assertEquals("item1", result);
    }

    @Test
    void listToString_returnsCommaSeparatedStringForMultipleItemList() {
        List<String> multipleItemList = Arrays.asList("item1", "item2", "item3");
        String result = caseRegistrationEnrichment.listToString(multipleItemList);
        assertEquals("item1,item2,item3", result);
    }

    @Test
    void listToString_handlesNullList() {
        List<String> nullList = null;
        assertThrows(NullPointerException.class, () -> caseRegistrationEnrichment.listToString(nullList));
    }

    @Test
    void enrichCaseNumberAndCNRNumber_generatesCaseNumberAndCNRNumber() {
        CaseRequest caseRequest = new CaseRequest();
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("2022-12345");
        caseRequest.setCases(courtCase);
        when(idgenUtil.getIdList(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList("12345"));
        when(caseUtil.getCNRNumber(anyString(), anyString(), anyString(), anyString())).thenReturn("KLJL01-12345-2022");
        caseRegistrationEnrichment.enrichCaseNumberAndCNRNumber(caseRequest);

        assertNotNull(caseRequest.getCases().getCaseNumber());
        assertNotNull(caseRequest.getCases().getCourCaseNumber());
    }

    @Test
    void enrichCaseNumberAndCNRNumber_handlesException() {
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(null);

        assertThrows(CustomException.class, () -> caseRegistrationEnrichment.enrichCaseNumberAndCNRNumber(caseRequest));
    }
}

