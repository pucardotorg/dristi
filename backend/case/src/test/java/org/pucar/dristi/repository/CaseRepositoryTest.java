package org.pucar.dristi.repository;

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
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private CaseQueryBuilder queryBuilder;
    @Mock
    private CaseRowMapper rowMapper;
    @Mock
    private LinkedCaseRowMapper linkedCaseRowMapper;
    @Mock
    private LitigantRowMapper litigantRowMapper;
    @Mock
    private StatuteSectionRowMapper statuteSectionRowMapper;
    @Mock
    private RepresentativeRowMapper representativeRowMapper;
    @Mock
    private RepresentingRowMapper representingRowMapper;
    @Mock
    private DocumentRowMapper documentRowMapper;
    @Mock
    private LinkedCaseDocumentRowMapper linkedCaseDocumentRowMapper;

    @Mock
    private LitigantDocumentRowMapper litigantDocumentRowMapper;

    @Mock
    private RepresentiveDocumentRowMapper representativeDocumentRowMapper;

    @Mock
    private RepresentingDocumentRowMapper representingDocumentRowMapper;

    @InjectMocks
    private CaseRepository caseRepository;

    private CaseRequest caseRequest;
    private CourtCase courtCase;
    private RequestInfo requestInfo;
    private User userInfo;

    @BeforeEach
    void setUp() {
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
        courtCase.setId(UUID.randomUUID());
        courtCase.setTenantId("tenant-id");
        List<LinkedCase> linkedCases = new ArrayList<>();
        linkedCases.add(LinkedCase.builder().id(UUID.randomUUID()).caseNumber("caseNumber").documents(documentsList).build());
        courtCase.setLinkedCases(linkedCases);

        List<Party> listLitigants = new ArrayList<>();
        listLitigants.add(Party.builder().id(UUID.randomUUID()).partyCategory("ctaegory1").documents(documentsList).build());
        listLitigants.add(Party.builder().id(UUID.randomUUID()).tenantId("pg").partyCategory("ctaegory2").documents(documentsList).build());
        courtCase.setLitigants(listLitigants);

        List<AdvocateMapping> advocateMappingList = new ArrayList<>();
        List<Party> representingList = new ArrayList<>();
        representingList.add(Party.builder().id(UUID.randomUUID()).tenantId("pg").documents(documentsList).build());
        advocateMappingList.add(AdvocateMapping.builder().id("d747abff-6d5d-47d7-99e2-fc70eaa856cb").tenantId("pg").representing(representingList).documents(documentsList).build());
        courtCase.setRepresentatives(advocateMappingList);

        List<StatuteSection> statuteSectionList = new ArrayList<>();
        List<String> sections = new ArrayList<>();
        sections.add("section1");
        sections.add("section2");
        List<String> subSections = new ArrayList<>();
        subSections.add("subsection1");
        subSections.add("subsection2");
        statuteSectionList.add(StatuteSection.builder().id(UUID.randomUUID()).tenantId("pg").sections(sections).subsections(subSections).build());
        courtCase.setStatutesAndSections(statuteSectionList);

        documentsList.add(Document.builder().fileStore("fileStore").build());
        courtCase.setDocuments(documentsList);
        caseRequest.setCases(courtCase);

        // Set the request info in the case request
        caseRequest.setRequestInfo(requestInfo);
    }

    @Test
    void getApplications_ShouldReturnListOfCourtCases() {
        // Prepare test data
        List<CaseCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new CaseCriteria());

        // Mock dependencies
        Map<UUID, List<LinkedCase>> linkedCasesMap = new HashMap<>();
        linkedCasesMap.put(courtCase.getId(), courtCase.getLinkedCases());

        Map<UUID, List<Party>> litigantMap = new HashMap<>();
        litigantMap.put(courtCase.getId(), courtCase.getLitigants());

        Map<UUID, List<StatuteSection>> statuteSectionsMap = new HashMap<>();
        statuteSectionsMap.put(courtCase.getId(), courtCase.getStatutesAndSections());

        Map<UUID, List<AdvocateMapping>> representativeMap = new HashMap<>();
        representativeMap.put(courtCase.getId(), courtCase.getRepresentatives());

        Map<UUID, List<Party>> representingMap = new HashMap<>();
        representingMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting());

        Map<UUID, List<Document>> caseDocumentMap = new HashMap<>();
        caseDocumentMap.put(courtCase.getId(), courtCase.getDocuments());

        Map<UUID, List<Document>> caseLitigantDocumentMap = new HashMap<>();
        caseLitigantDocumentMap.put(courtCase.getId(), courtCase.getLitigants().get(0).getDocuments());

        Map<UUID, List<Document>> caseLinkedCaseDocumentMap = new HashMap<>();
        caseLinkedCaseDocumentMap.put(courtCase.getId(), courtCase.getLinkedCases().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentiveDocumentMap = new HashMap<>();
        caseRepresentiveDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentingDocumentMap = new HashMap<>();
        caseRepresentingDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting().get(0).getDocuments());

        List<CourtCase> expectedCourtCaseList = new ArrayList<>(); // Add expected court cases
        expectedCourtCaseList.add(courtCase);
        when(queryBuilder.getCasesSearchQuery(any(), any())).thenReturn("SELECT * FROM cases WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CaseRowMapper.class))).thenReturn(expectedCourtCaseList);

        when(queryBuilder.getLinkedCaseSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_linked_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(LinkedCaseRowMapper.class))).thenReturn(linkedCasesMap);

        when(queryBuilder.getLitigantSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_litigant_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(LitigantRowMapper.class))).thenReturn(litigantMap);

        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_statute_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(StatuteSectionRowMapper.class))).thenReturn(statuteSectionsMap);

        when(queryBuilder.getRepresentativesSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_representive_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RepresentativeRowMapper.class))).thenReturn(representativeMap);

        when(queryBuilder.getRepresentingSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_representing_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RepresentingRowMapper.class))).thenReturn(representingMap);

        when(queryBuilder.getDocumentSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(DocumentRowMapper.class))).thenReturn(caseDocumentMap);

        when(queryBuilder.getLitigantDocumentSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_document_bis_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(LitigantDocumentRowMapper.class))).thenReturn(caseLitigantDocumentMap);

        when(queryBuilder.getLinkedCaseDocumentSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(LinkedCaseDocumentRowMapper.class))).thenReturn(caseLinkedCaseDocumentMap);

        when(queryBuilder.getRepresentativeDocumentSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RepresentiveDocumentRowMapper.class))).thenReturn(caseRepresentiveDocumentMap);

        when(queryBuilder.getRepresentingDocumentSearchQuery(anyList(), any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RepresentingDocumentRowMapper.class))).thenReturn(caseRepresentingDocumentMap);

        // Invoke the method
        List<CaseCriteria> resultCourtCaseList = caseRepository.getApplications(searchCriteria);

        // Verify interactions
        verify(queryBuilder, times(1)).getCasesSearchQuery(any(), any());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(CaseRowMapper.class));

        // Assert result
        assertEquals(expectedCourtCaseList, resultCourtCaseList.get(0).getResponseList());
    }

    @Test
    void getApplications_Exception() {
        // Prepare test data
        List<CaseCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new CaseCriteria());

        // Mock dependencies
        Map<UUID, List<LinkedCase>> linkedCasesMap = new HashMap<>();
        linkedCasesMap.put(courtCase.getId(), courtCase.getLinkedCases());

        Map<UUID, List<Party>> litigantMap = new HashMap<>();
        litigantMap.put(courtCase.getId(), courtCase.getLitigants());

        Map<UUID, List<StatuteSection>> statuteSectionsMap = new HashMap<>();
        statuteSectionsMap.put(courtCase.getId(), courtCase.getStatutesAndSections());

        Map<UUID, List<AdvocateMapping>> representativeMap = new HashMap<>();
        representativeMap.put(courtCase.getId(), courtCase.getRepresentatives());

        Map<UUID, List<Party>> representingMap = new HashMap<>();
        representingMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting());

        Map<UUID, List<Document>> caseDocumentMap = new HashMap<>();
        caseDocumentMap.put(courtCase.getId(), courtCase.getDocuments());

        Map<UUID, List<Document>> caseLitigantDocumentMap = new HashMap<>();
        caseLitigantDocumentMap.put(courtCase.getId(), courtCase.getLitigants().get(0).getDocuments());

        Map<UUID, List<Document>> caseLinkedCaseDocumentMap = new HashMap<>();
        caseLinkedCaseDocumentMap.put(courtCase.getId(), courtCase.getLinkedCases().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentiveDocumentMap = new HashMap<>();
        caseRepresentiveDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentingDocumentMap = new HashMap<>();
        caseRepresentingDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting().get(0).getDocuments());

        List<CourtCase> expectedCourtCaseList = new ArrayList<>(); // Add expected court cases
        expectedCourtCaseList.add(courtCase);
        when(queryBuilder.getCasesSearchQuery(any(), any())).thenReturn("SELECT * FROM cases WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CaseRowMapper.class))).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            caseRepository.getApplications(searchCriteria);
        });
    }

    @Test
    void getApplications_CustomException() {
        // Prepare test data
        List<CaseCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new CaseCriteria());

        // Mock dependencies
        Map<UUID, List<LinkedCase>> linkedCasesMap = new HashMap<>();
        linkedCasesMap.put(courtCase.getId(), courtCase.getLinkedCases());

        Map<UUID, List<Party>> litigantMap = new HashMap<>();
        litigantMap.put(courtCase.getId(), courtCase.getLitigants());

        Map<UUID, List<StatuteSection>> statuteSectionsMap = new HashMap<>();
        statuteSectionsMap.put(courtCase.getId(), courtCase.getStatutesAndSections());

        Map<UUID, List<AdvocateMapping>> representativeMap = new HashMap<>();
        representativeMap.put(courtCase.getId(), courtCase.getRepresentatives());

        Map<UUID, List<Party>> representingMap = new HashMap<>();
        representingMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting());

        Map<UUID, List<Document>> caseDocumentMap = new HashMap<>();
        caseDocumentMap.put(courtCase.getId(), courtCase.getDocuments());

        Map<UUID, List<Document>> caseLitigantDocumentMap = new HashMap<>();
        caseLitigantDocumentMap.put(courtCase.getId(), courtCase.getLitigants().get(0).getDocuments());

        Map<UUID, List<Document>> caseLinkedCaseDocumentMap = new HashMap<>();
        caseLinkedCaseDocumentMap.put(courtCase.getId(), courtCase.getLinkedCases().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentiveDocumentMap = new HashMap<>();
        caseRepresentiveDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getDocuments());

        Map<UUID, List<Document>> caseRepresentingDocumentMap = new HashMap<>();
        caseRepresentingDocumentMap.put(courtCase.getId(), courtCase.getRepresentatives().get(0).getRepresenting().get(0).getDocuments());

        List<CourtCase> expectedCourtCaseList = new ArrayList<>(); // Add expected court cases
        expectedCourtCaseList.add(courtCase);
        when(queryBuilder.getCasesSearchQuery(any(), any())).thenReturn("SELECT * FROM cases WHERE ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CaseRowMapper.class))).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            caseRepository.getApplications(searchCriteria);
        });
    }

    @Test
    void checkCaseExists_ShouldReturnCaseExistsListWithCorrectExistenceStatus() {
        // Prepare test data
        List<CaseExists> caseExistsList = new ArrayList<>();
        CaseExists caseExists1 = CaseExists.builder().caseId("12").courtCaseNumber("courtCaseNumber1").cnrNumber("cnrNumber1").filingNumber("filingNumber").build();
        CaseExists caseExists2 = CaseExists.builder().caseId(null).courtCaseNumber(null).cnrNumber(null).filingNumber(null).build();; // Test case where all fields are null
        caseExistsList.add(caseExists1);
        caseExistsList.add(caseExists2);

        // Mock dependencies
        when(queryBuilder.checkCaseExistQuery(anyString(), anyString(), anyString(), anyString())).thenReturn("SELECT COUNT(*) FROM cases WHERE ...");
        when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(1); // Assuming case exists

        // Invoke the method
        List<CaseExists> result = caseRepository.checkCaseExists(caseExistsList);

        // Verify interactions
        verify(queryBuilder, times(1)).checkCaseExistQuery(anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Class.class));

        // Assert result
        assertEquals(2, result.size());
        assertEquals(true, result.get(0).getExists()); // Assuming case exists
        assertEquals(false, result.get(1).getExists()); // Assuming case does not exist
    }

    @Test
    void checkCaseExists_Exception() {
        // Prepare test data
        List<CaseExists> caseExistsList = new ArrayList<>();
        CaseExists caseExists1 = CaseExists.builder().caseId("12").courtCaseNumber("courtCaseNumber1").cnrNumber("cnrNumber1").filingNumber("filingNumber").build();
        CaseExists caseExists2 = CaseExists.builder().caseId(null).courtCaseNumber(null).cnrNumber(null).filingNumber(null).build();; // Test case where all fields are null
        caseExistsList.add(caseExists1);
        caseExistsList.add(caseExists2);

        // Mock dependencies
        when(queryBuilder.checkCaseExistQuery(anyString(), anyString(), anyString(), anyString())).thenThrow(new RuntimeException());
//        when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(1); // Assuming case exists

        assertThrows(Exception.class, () -> {
            caseRepository.checkCaseExists(caseExistsList);
        });
    }

    @Test
    void checkCaseExists_CustomException() {
        // Prepare test data
        List<CaseExists> caseExistsList = new ArrayList<>();
        CaseExists caseExists1 = CaseExists.builder().caseId("12").courtCaseNumber("courtCaseNumber1").cnrNumber("cnrNumber1").filingNumber("filingNumber").build();
        CaseExists caseExists2 = CaseExists.builder().caseId(null).courtCaseNumber(null).cnrNumber(null).filingNumber(null).build();; // Test case where all fields are null
        caseExistsList.add(caseExists1);
        caseExistsList.add(caseExists2);

        // Mock dependencies
        when(queryBuilder.checkCaseExistQuery(anyString(), anyString(), anyString(), anyString())).thenThrow(new CustomException());
//        when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(1); // Assuming case exists

        assertThrows(CustomException.class, () -> {
            caseRepository.checkCaseExists(caseExistsList);
        });
    }

}
