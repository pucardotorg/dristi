package org.pucar.dristi.enrichment;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

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
    void testEnrichCaseRegistrationOnCreate() {
        // Mock the config to return specific values
        when(config.getCaseFilingNumber()).thenReturn("caseFilingNumber");

        // Mock the ID generation to return case-indexer.yml list of IDs
        List<String> idList = Collections.singletonList("generated-id");
        when(idgenUtil.getIdList(any(RequestInfo.class), anyString(), anyString(), any(), anyInt()))
                .thenReturn(idList);

        // Call the method to test
        caseRegistrationEnrichment.enrichCaseRegistrationOnCreate(caseRequest);

        // Verify the behavior and assert the results
        verify(idgenUtil).getIdList(any(RequestInfo.class), eq("tenant-id"), eq("caseFilingNumber"), eq(null), eq(1));
        assertNotNull(courtCase.getAuditdetails());
        assertNotNull(courtCase.getLinkedCases());
        assertNotNull(courtCase.getLitigants());
        assertNotNull(courtCase.getRepresentatives());
        assertEquals("generated-id", courtCase.getFilingNumber());
        assertEquals("generated-id", courtCase.getCaseNumber());
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
}

