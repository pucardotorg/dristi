package org.pucar.dristi.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.pucar.dristi.web.models.Document;
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
import org.pucar.dristi.repository.querybuilder.CaseSummaryQueryBuilder;
import org.pucar.dristi.repository.querybuilder.OpenApiCaseSummaryQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.OpenApiCaseSummary;
import org.pucar.dristi.web.models.*;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class CaseRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private CaseQueryBuilder queryBuilder;
    @Mock
    private OpenApiCaseSummaryQueryBuilder openApiCaseSummaryQueryBuilder;

    @Mock
    private OpenApiCaseSummaryRowMapper openApiCaseSummaryRowMapper;

    @Mock
    private OpenApiCaseListRowMapper openApiCaseListRowMapper;
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

    @Mock
    private CaseSummaryQueryBuilder caseSummaryQueryBuilder;

    @Mock
    private CaseSummaryRowMapper caseSummaryRowMapper;

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
        lenient().when(queryBuilder.getCasesSearchQuery(any(), any(), any(),any())).thenReturn("SELECT * FROM cases WHERE ...");
        lenient().when(queryBuilder.addOrderByQuery(any(), any())).thenReturn("SELECT * FROM cases WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(CaseRowMapper.class))).thenReturn(expectedCourtCaseList);

        lenient().when(queryBuilder.getLinkedCaseSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_linked_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(LinkedCaseRowMapper.class))).thenReturn(linkedCasesMap);

        lenient().when(queryBuilder.getLitigantSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_litigant_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(LitigantRowMapper.class))).thenReturn(litigantMap);

        lenient().when(queryBuilder.getStatuteSectionSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_statute_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(StatuteSectionRowMapper.class))).thenReturn(statuteSectionsMap);

        lenient().when(queryBuilder.getRepresentativesSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_representive_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(RepresentativeRowMapper.class))).thenReturn(representativeMap);

        lenient().when(queryBuilder.getRepresentingSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_representing_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(RepresentingRowMapper.class))).thenReturn(representingMap);

        lenient().when(queryBuilder.getDocumentSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(DocumentRowMapper.class))).thenReturn(caseDocumentMap);

        lenient().when(queryBuilder.getLitigantDocumentSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_document_bis_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(LitigantDocumentRowMapper.class))).thenReturn(caseLitigantDocumentMap);

        lenient().when(queryBuilder.getLinkedCaseDocumentSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(LinkedCaseDocumentRowMapper.class))).thenReturn(caseLinkedCaseDocumentMap);

        lenient().when(queryBuilder.getRepresentativeDocumentSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(RepresentiveDocumentRowMapper.class))).thenReturn(caseRepresentiveDocumentMap);

        lenient().when(queryBuilder.getRepresentingDocumentSearchQuery(anyList(), any(),any())).thenReturn("SELECT * FROM dristi_document_case WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(RepresentingDocumentRowMapper.class))).thenReturn(caseRepresentingDocumentMap);

        // Invoke the method
        List<CaseCriteria> resultCourtCaseList = caseRepository.getCases(searchCriteria, requestInfo);

        // Verify interactions
        verify(queryBuilder, times(1)).getCasesSearchQuery(any(), any(), any(),any());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(), any(CaseRowMapper.class));

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
        lenient().when(queryBuilder.getCasesSearchQuery(any(), any(), any(),any())).thenReturn("SELECT * FROM cases WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(CaseRowMapper.class))).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            caseRepository.getCases(searchCriteria, requestInfo);
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
        lenient().when(queryBuilder.getCasesSearchQuery(any(), any(), any(),any())).thenReturn("SELECT * FROM cases WHERE ...");
        lenient().when(jdbcTemplate.query(anyString(), any(Object[].class), any(CaseRowMapper.class))).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            caseRepository.getCases(searchCriteria, requestInfo);
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
        lenient().when(queryBuilder.checkCaseExistQuery(any(), anyList(), anyList())).thenReturn("SELECT COUNT(*) FROM cases WHERE ...");
        lenient().when(jdbcTemplate.queryForObject(anyString(), any(), any(), any(Class.class))).thenReturn(1); // Assuming case exists

        // Invoke the method
        List<CaseExists> result = caseRepository.checkCaseExists(caseExistsList);

        // Verify interactions
        verify(queryBuilder, times(1)).checkCaseExistQuery(any(), anyList(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(), any(), any(Class.class));

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
        lenient().when(queryBuilder.checkCaseExistQuery(any(), anyList(), anyList())).thenThrow(new RuntimeException());
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
        lenient().when(queryBuilder.checkCaseExistQuery(any(), anyList(), anyList())).thenThrow(new CustomException());
//        when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(1); // Assuming case exists

        assertThrows(CustomException.class, () -> {
            caseRepository.checkCaseExists(caseExistsList);
        });
    }
    @Test
    void checkCaseExists_returnsCorrectExistenceStatus() {
        List<CaseExists> caseExistsRequest = new ArrayList<>();
        CaseExists caseExists1 = CaseExists.builder().caseId("12").courtCaseNumber("courtCaseNumber1").cnrNumber("cnrNumber1").filingNumber("filingNumber").build();
        CaseExists caseExists2 = CaseExists.builder().caseId(null).courtCaseNumber(null).cnrNumber(null).filingNumber(null).build();
        caseExistsRequest.add(caseExists1);
        caseExistsRequest.add(caseExists2);

        when(queryBuilder.checkCaseExistQuery(any(), anyList(), anyList())).thenReturn("SELECT COUNT(*) FROM cases WHERE ...");
        when(jdbcTemplate.queryForObject(anyString(), any(), any(),any(Class.class))).thenReturn(1);

        List<CaseExists> result = caseRepository.checkCaseExists(caseExistsRequest);

        verify(queryBuilder, times(1)).checkCaseExistQuery(any(), anyList(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(), any(), any(Class.class));

        assertEquals(2, result.size());
        assertEquals(true, result.get(0).getExists());
        assertEquals(false, result.get(1).getExists());
    }

    @Test
    void getCaseSummary_ShouldReturnListOfCaseSummaries() {
        // Prepare test data
        CaseSummaryRequest request = new CaseSummaryRequest();
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        request.setCriteria(criteria);
        Pagination pagination = new Pagination();
        request.setPagination(pagination);

        List<CaseSummary> expectedSummaryList = new ArrayList<>();
        CaseSummary caseSummary = new CaseSummary();
        caseSummary.setId(UUID.randomUUID().toString());
        caseSummary.setCaseTitle("CASE-2024-001");
        expectedSummaryList.add(caseSummary);

        // Mock dependencies
        when(caseSummaryQueryBuilder.getCaseBaseQuery(request.getCriteria(), new ArrayList<>(), new ArrayList<>()))
                .thenReturn("SELECT * FROM case_summary WHERE ...");
        when(caseSummaryQueryBuilder.getCaseSummarySearchQuery(any())).thenReturn("SELECT * FROM case_summary WHERE ...");
        when(caseSummaryQueryBuilder.addOrderByQuery(anyString(), any()))
                .thenReturn("SELECT * FROM case_summary WHERE ... ORDER BY ...");
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenReturn(1);
        when(caseSummaryQueryBuilder.addPaginationQuery(anyString(), anyList(), any(), anyList()))
                .thenReturn("SELECT * FROM case_summary WHERE ... ORDER BY ... LIMIT ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(CaseSummaryRowMapper.class)))
                .thenReturn(expectedSummaryList);
        when(queryBuilder.getTotalCountQuery("SELECT * FROM case_summary WHERE ... ORDER BY ...")).thenReturn("SELECT COUNT(*) FROM case_summary");

        // Invoke the method
        List<CaseSummary> result = caseRepository.getCaseSummary(request);

        // Verify interactions
        verify(caseSummaryQueryBuilder, times(1)).getCaseSummarySearchQuery(any());
        verify(caseSummaryQueryBuilder, times(1)).addOrderByQuery(anyString(), any());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
        verify(caseSummaryQueryBuilder, times(1)).addPaginationQuery(anyString(), anyList(), any(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(), any(CaseSummaryRowMapper.class));

        // Assert results
        assertNotNull(result);
        assertEquals(expectedSummaryList.size(), result.size());
        assertEquals(expectedSummaryList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedSummaryList.get(0).getCaseTitle(), result.get(0).getCaseTitle());
    }

    @Test
    void getCaseSummary_WithNullPagination_ShouldReturnListOfCaseSummaries() {
        // Prepare test data
        CaseSummaryRequest request = new CaseSummaryRequest();
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        request.setCriteria(criteria);
        // Not setting pagination, testing null case

        List<CaseSummary> expectedSummaryList = new ArrayList<>();
        CaseSummary caseSummary = new CaseSummary();
        caseSummary.setId(UUID.randomUUID().toString());
        expectedSummaryList.add(caseSummary);

        // Mock dependencies
        when(caseSummaryQueryBuilder.getCaseBaseQuery(request.getCriteria(), new ArrayList<>(), new ArrayList<>()))
                .thenReturn("SELECT * FROM case_summary WHERE ...");
        when(caseSummaryQueryBuilder.getCaseSummarySearchQuery(any()))
                .thenReturn("SELECT * FROM case_summary WHERE ...");
        when(caseSummaryQueryBuilder.addOrderByQuery(anyString(), any()))
                .thenReturn("SELECT * FROM case_summary WHERE ... ORDER BY ...");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(CaseSummaryRowMapper.class)))
                .thenReturn(expectedSummaryList);

        // Invoke the method
        List<CaseSummary> result = caseRepository.getCaseSummary(request);

        // Verify interactions
        verify(caseSummaryQueryBuilder, times(1)).getCaseSummarySearchQuery(any());
        verify(caseSummaryQueryBuilder, times(1)).addOrderByQuery(anyString(), isNull());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(), any(CaseSummaryRowMapper.class));

        // Assert results
        assertNotNull(result);
        assertEquals(expectedSummaryList.size(), result.size());
        assertEquals(expectedSummaryList.get(0).getId(), result.get(0).getId());
    }

    @Test
    void getCaseSummary_Exception() {
        // Prepare test data
        CaseSummaryRequest request = new CaseSummaryRequest();
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        request.setCriteria(criteria);
        Pagination pagination = new Pagination();
        request.setPagination(pagination);

        // Mock dependencies
        when(caseSummaryQueryBuilder.getCaseSummarySearchQuery(any()))
                .thenReturn("SELECT * FROM case_summary WHERE ...");
        when(caseSummaryQueryBuilder.addOrderByQuery(anyString(), any()))
                .thenReturn("SELECT * FROM case_summary WHERE ... ORDER BY ...");
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))
        ).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            caseRepository.getCaseSummary(request);
        });
    }

    @Test
    void testGetCaseSummaryByCnrNumber_Success() {
        OpenApiCaseSummaryRequest request = new OpenApiCaseSummaryRequest();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();
        preparedStmtList.add("param1");
        preparedStmtArgList.add(java.sql.Types.VARCHAR);

        String baseQuery = "SELECT * FROM case_summary WHERE ...";
        String finalQuery = "SELECT * FROM case_summary WHERE ...";

        when(openApiCaseSummaryQueryBuilder.getCaseBaseQuery(eq(request), anyList(), anyList())).thenReturn(baseQuery);
        when(openApiCaseSummaryQueryBuilder.getCaseSummarySearchQuery(baseQuery)).thenReturn(finalQuery);
        List<OpenApiCaseSummary> resultList = List.of(new OpenApiCaseSummary());
        when(jdbcTemplate.query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper)))
                .thenReturn(resultList);

        OpenApiCaseSummary result = caseRepository.getCaseSummaryByCnrNumber(request);

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper));
    }

    @Test
    void testGetCaseSummaryByCnrNumber_MultipleResults() {
        OpenApiCaseSummaryRequest request = new OpenApiCaseSummaryRequest();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String baseQuery = "SELECT * FROM case_summary WHERE ...";
        String finalQuery = "SELECT * FROM case_summary WHERE ...";

        when(openApiCaseSummaryQueryBuilder.getCaseBaseQuery(eq(request), anyList(), anyList())).thenReturn(baseQuery);
        when(openApiCaseSummaryQueryBuilder.getCaseSummarySearchQuery(baseQuery)).thenReturn(finalQuery);
        List<OpenApiCaseSummary> resultList = List.of(new OpenApiCaseSummary(), new OpenApiCaseSummary());
        when(jdbcTemplate.query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper)))
                .thenReturn(resultList);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseRepository.getCaseSummaryByCnrNumber(request);
        });

        assertEquals("CASE_SUMMARY_SEARCH_QUERY_EXCEPTION", exception.getCode());
    }

    @Test
    void testGetCaseSummaryListByCaseType_Success() {
        OpenApiCaseSummaryRequest request = new OpenApiCaseSummaryRequest();
        Pagination pagination = new Pagination();
        request.setPagination(pagination);

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String baseQuery = "SELECT * FROM case_summary WHERE ...";
        String paginatedQuery = "SELECT * FROM case_summary WHERE ... ORDER BY ... LIMIT ...";

        when(openApiCaseSummaryQueryBuilder.getCaseBaseQuery(eq(request), anyList(), anyList())).thenReturn(baseQuery);
        when(openApiCaseSummaryQueryBuilder.addOrderByQuery(eq(baseQuery), eq(pagination))).thenReturn(paginatedQuery);
        when(openApiCaseSummaryQueryBuilder.addPaginationQuery(eq(paginatedQuery), anyList(), eq(pagination), anyList()))
                .thenReturn(paginatedQuery);
        when(jdbcTemplate.query(eq(paginatedQuery), any(Object[].class), any(int[].class), eq(openApiCaseListRowMapper)))
                .thenReturn(new ArrayList<>());
        when(queryBuilder.getTotalCountQuery(eq(paginatedQuery))).thenReturn("SELECT COUNT(*) FROM case_summary");
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM case_summary"), any(Object[].class), eq(Integer.class))
        ).thenReturn(1);
        List<CaseListLineItem> result = caseRepository.getCaseSummaryListByCaseType(request);

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq(paginatedQuery), any(Object[].class), any(int[].class), eq(openApiCaseListRowMapper));
    }

    @Test
    void testGetCaseSummaryByCaseNumber_Success() {
        OpenApiCaseSummaryRequest request = new OpenApiCaseSummaryRequest();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();
        preparedStmtList.add("param1");
        preparedStmtArgList.add(java.sql.Types.VARCHAR);

        String baseQuery = "SELECT * FROM case_summary WHERE ...";
        String finalQuery = "SELECT * FROM case_summary WHERE ...";

        when(openApiCaseSummaryQueryBuilder.getCaseBaseQuery(eq(request), anyList(), anyList())).thenReturn(baseQuery);
        when(openApiCaseSummaryQueryBuilder.getCaseSummarySearchQuery(baseQuery)).thenReturn(finalQuery);
        List<OpenApiCaseSummary> resultList = List.of(new OpenApiCaseSummary());
        when(jdbcTemplate.query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper)))
                .thenReturn(resultList);

        OpenApiCaseSummary result = caseRepository.getCaseSummaryByCaseNumber(request);

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper));
    }

    @Test
    void testGetCaseSummaryByCaseNumber_Exception() {
        OpenApiCaseSummaryRequest request = new OpenApiCaseSummaryRequest();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String baseQuery = "SELECT * FROM case_summary WHERE ...";
        String finalQuery = "SELECT * FROM case_summary WHERE ...";

        when(openApiCaseSummaryQueryBuilder.getCaseBaseQuery(eq(request), anyList(), anyList())).thenReturn(baseQuery);
        when(openApiCaseSummaryQueryBuilder.getCaseSummarySearchQuery(baseQuery)).thenReturn(finalQuery);
        when(jdbcTemplate.query(eq(finalQuery), any(Object[].class), any(int[].class), eq(openApiCaseSummaryRowMapper)))
                .thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseRepository.getCaseSummaryByCaseNumber(request);
        });

        assertEquals("CASE_SUMMARY_SEARCH_QUERY_EXCEPTION", exception.getCode());
    }
}
