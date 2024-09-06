package org.pucar.dristi.enrichment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.LinkedCase;
import org.pucar.dristi.web.models.Party;
import org.pucar.dristi.web.models.StatuteSection;

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
    void enrichCaseNumberAndCourtCaseNumber_generatesCaseNumberAndCourtCaseNumber() {
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("2022-12345");
        caseRequest.setCases(courtCase);
        when(idgenUtil.getIdList(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList("12345"));
        caseRegistrationEnrichment.enrichCaseNumberAndCourtCaseNumber(caseRequest);

        assertNotNull(caseRequest.getCases().getCaseNumber());
        assertNotNull(caseRequest.getCases().getCourtCaseNumber());
    }

    @Test
    void enrichCaseNumberAndCNRNumber_handlesException() {
        caseRequest.setCases(null);

        assertThrows(CustomException.class, () -> caseRegistrationEnrichment.enrichCaseNumberAndCourtCaseNumber(caseRequest));
    }

    @Test
    void enrichAccessCode_handlesException() {
        caseRequest.setCases(null);

        assertThrows(CustomException.class, () -> caseRegistrationEnrichment.enrichAccessCode(caseRequest));
    }

    @Test
    public void testEnrichLitigantsOnCreate() {
        // Create a new litigant without an ID (to be created)
        Party newParty = new Party();
        newParty.setDocuments(new ArrayList<>());
        courtCase.setId(UUID.randomUUID());
        courtCase.getLitigants().add(newParty);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate(courtCase, auditDetails);
        // Assert that the new party has been assigned an ID, case ID, and audit details
        assertEquals(courtCase.getId().toString(), newParty.getCaseId());
        assertEquals(auditDetails, newParty.getAuditDetails());
    }
    @Test
    public void testEnrichRepOnCreate() {
        AdvocateMapping representative = new AdvocateMapping();
        representative.setDocuments(new ArrayList<>());
        courtCase.setId(UUID.randomUUID());
        courtCase.getRepresentatives().add(representative);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate(courtCase, auditDetails);
        assertEquals(courtCase.getId().toString(), representative.getCaseId());
        assertEquals(auditDetails, representative.getAuditDetails());
    }
    @Test
    public void testNoLitigants() {
        // No litigants in the court case
        courtCase.setLitigants(null);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate(courtCase, auditDetails);
        // Assert that nothing breaks when there are no litigants
        assertEquals(null, courtCase.getLitigants());
    }
    @Test
    public void testNoRepresentatives() {
        courtCase.setId(UUID.randomUUID());
        courtCase.setRepresentatives(null);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate(courtCase, auditDetails);
        assertEquals(null, courtCase.getRepresentatives());
    }
    @Test
    public void testEnrichLitigantsOnUpdate() {
        // Create an existing litigant with an ID (to be updated)
        Party existingParty = new Party();
        existingParty.setId(UUID.randomUUID());
        existingParty.setDocuments(new ArrayList<>());
        courtCase.setId(UUID.randomUUID());
        courtCase.getLitigants().add(existingParty);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate(courtCase, auditDetails);

        // Assert that the existing party's audit details have been updated
        assertEquals(auditDetails, existingParty.getAuditDetails());
    }
    @Test
    public void testEnrichRepresentativeOnUpdate() {
        AdvocateMapping existingRepresentative = new AdvocateMapping();
        existingRepresentative.setId("rep_id");
        existingRepresentative.setDocuments(new ArrayList<>());
        courtCase.setId(UUID.randomUUID());
        courtCase.getRepresentatives().add(existingRepresentative);
        AuditDetails auditDetails = new AuditDetails("createdBy", "lastModifiedBy", System.currentTimeMillis(), System.currentTimeMillis());
        CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate(courtCase, auditDetails);
        assertEquals(auditDetails, existingRepresentative.getAuditDetails());
    }
}

