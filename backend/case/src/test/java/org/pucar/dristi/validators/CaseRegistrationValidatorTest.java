package org.pucar.dristi.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.pucar.dristi.config.ServiceConstants.DELETE_DRAFT_WORKFLOW_ACTION;
import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_NOT_FOUND;
import static org.pucar.dristi.config.ServiceConstants.INVALID_ADVOCATE_ID;
import static org.pucar.dristi.config.ServiceConstants.INVALID_FILESTORE_ID;
import static org.pucar.dristi.config.ServiceConstants.SAVE_DRAFT_CASE_WORKFLOW_ACTION;
import static org.pucar.dristi.config.ServiceConstants.SUBMIT_CASE_WORKFLOW_ACTION;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
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
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.JoinCaseRequest;
import org.pucar.dristi.web.models.LinkedCase;
import org.pucar.dristi.web.models.Party;
import org.pucar.dristi.web.models.StatuteSection;

import net.minidev.json.JSONArray;

@ExtendWith(MockitoExtension.class)
public class CaseRegistrationValidatorTest {

    @Mock
    private IndividualService individualService;

    @Mock
    private Configuration configuration;

    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private CaseRegistrationValidator validator;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private AdvocateUtil advocateUtil;
    private JoinCaseRequest joinCaseRequest;
    private RequestInfo requestInfo;
    private Party litigant;
    private AdvocateMapping representative;
    private Document document;
    @BeforeEach
    void setUp() {
        // Setup done before each test
        requestInfo = new RequestInfo();
        litigant = new Party();
        representative = new AdvocateMapping();
        document = new Document();
        joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setLitigant(litigant);
        joinCaseRequest.setRepresentative(representative);
        joinCaseRequest.setRequestInfo(requestInfo);
    }

    @Test
    void testValidateCaseRegistration_WithValidData() {
        // Creating RequestInfo with necessary user info
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setId(1234L);
        userInfo.setUserName("test-user");
        requestInfo.setUserInfo(userInfo);

        // Setting up CaseRequest with valid data
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(requestInfo);

        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setCaseCategory("civil");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("category");
//        courtCase.setStatutesAndSections();

        Document document = new Document();
        document.setFileStore("123");
        courtCase.setDocuments(List.of(document));

        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("123");
        courtCase.setRepresentatives(List.of(advocateMapping));

        Party party = new Party();
        party.setIndividualId("123");
        courtCase.setLitigants(List.of(party));

        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));

        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);

        request.setCases(courtCase);

        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());

        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        // Setting necessary stubbings to lenient
        lenient().when(mdmsUtil.fetchMdmsData(requestInfo, "pg", "case", masterList)).thenReturn(mdmsRes);
        lenient().when(individualService.searchIndividual(requestInfo, "123")).thenReturn(true);
        lenient().when(fileStoreUtil.doesFileExist("pg", "123")).thenReturn(true);
        lenient().when(advocateUtil.doesAdvocateExist(requestInfo, "123")).thenReturn(true);

        // Validate the case registration
        assertDoesNotThrow(() -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingFilingDate() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingTenantId() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingCaseCategory() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingStatute() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("category1");
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingLitigants() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("category1");
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        List<StatuteSection> statuteSectionList = new ArrayList<>();
        statuteSectionList.add(StatuteSection.builder().tenantId("pb").build());
        courtCase.setStatutesAndSections(statuteSectionList);
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingLitigantsThrowNoExceptionOnDeleteCase() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("category1");
        Workflow workflow = new Workflow();
        workflow.setAction(DELETE_DRAFT_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        List<StatuteSection> statuteSectionList = new ArrayList<>();
        statuteSectionList.add(StatuteSection.builder().tenantId("pb").build());
        courtCase.setStatutesAndSections(statuteSectionList);
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingUserInfo() {
        CaseRequest request = new CaseRequest();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(null);
        request.setRequestInfo(requestInfo);
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("category1");
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        List<StatuteSection> statuteSectionList = new ArrayList<>();
        statuteSectionList.add(StatuteSection.builder().tenantId("pb").build());
        List<Party> litigantList = new ArrayList<>();
        litigantList.add(Party.builder().tenantId("pb").build());
        courtCase.setStatutesAndSections(statuteSectionList);
        courtCase.setLitigants(litigantList);
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setCaseCategory("civil");
        Document document = new Document();
        document.setFileStore("123");
        courtCase.setDocuments(List.of(document));
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("123");
        courtCase.setRepresentatives(List.of(advocateMapping));
        Party party = new Party();
        party.setIndividualId("123");
        courtCase.setLitigants(List.of(party));
        LinkedCase linkedCase = LinkedCase.builder().id(UUID.randomUUID()).caseNumber("caseNumber").build();
//        courtCase.setLinkedCases(List.of(linkedCase));
        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(linkedCase.getId().toString());
        caseCriteriaList.add(caseCriteria);
        caseSearchRequest.setRequestInfo(new RequestInfo());
        caseSearchRequest.setCriteria(caseCriteriaList);
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        User userInfo = new User();
        userInfo.setId(1234L);
        userInfo.setUserName("test-user");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userInfo);
        caseRequest.setRequestInfo(requestInfo);
        when(mdmsUtil.fetchMdmsData(requestInfo,"pg","case", masterList)).thenReturn(mdmsRes);

        lenient().when(individualService.searchIndividual(requestInfo, "123")).thenReturn(true);
        lenient().when(fileStoreUtil.doesFileExist("pg","123")).thenReturn(true);
        lenient().when(advocateUtil.doesAdvocateExist(requestInfo, "123")).thenReturn(true);
        lenient().when(configuration.getCaseBusinessServiceName()).thenReturn("case");

        lenient().when(caseRepository.getApplications(any(), any())).thenReturn((List.of(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourtCaseNumber()).build())));

        Boolean result = validator.validateApplicationExistence(caseRequest);
        assertTrue(result);
    }

    @Test
    void testValidateApplicationExistence_EmptyApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setCaseCategory("civil");
        Document document = new Document();
        document.setFileStore("123");
        courtCase.setDocuments(List.of(document));
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("123");
        courtCase.setRepresentatives(List.of(advocateMapping));
        Party party = new Party();
        party.setIndividualId("123");
        courtCase.setLitigants(List.of(party));
        LinkedCase linkedCase = LinkedCase.builder().id(UUID.randomUUID()).caseNumber("caseNumber").build();
        courtCase.setLinkedCases(List.of(linkedCase));
        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(linkedCase.getId().toString());
        caseCriteriaList.add(caseCriteria);
        caseSearchRequest.setRequestInfo(new RequestInfo());
        caseSearchRequest.setCriteria(caseCriteriaList);

        lenient().when(caseRepository.getApplications(any(), any())).thenReturn((Collections.emptyList()));
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        User userInfo = new User();
        userInfo.setId(1234L);
        userInfo.setUserName("test-user");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userInfo);
        caseRequest.setRequestInfo(requestInfo);
        Boolean result = validator.validateApplicationExistence(caseRequest);
        assertTrue(!result);
    }

    @Test
    void testValidateApplicationExistence_ExistingApplication_INVALID_LINKEDCASE_ID() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setCaseCategory("civil");
        Document document = new Document();
        document.setFileStore("123");
        courtCase.setDocuments(List.of(document));
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("123");
        courtCase.setRepresentatives(List.of(advocateMapping));
        Party party = new Party();
        party.setIndividualId("123");
        courtCase.setLitigants(List.of(party));
        LinkedCase linkedCase = LinkedCase.builder().id(UUID.randomUUID()).caseNumber("caseNumber").build();
        courtCase.setLinkedCases(List.of(linkedCase));
        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(linkedCase.getId().toString());
        caseCriteriaList.add(caseCriteria);
        caseSearchRequest.setRequestInfo(new RequestInfo());
        caseSearchRequest.setCriteria(caseCriteriaList);
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        lenient().when(mdmsUtil.fetchMdmsData(new RequestInfo(),"pg","case", masterList)).thenReturn(mdmsRes);

        lenient().when(individualService.searchIndividual(new RequestInfo(), "123")).thenReturn(true);
        lenient().when(fileStoreUtil.doesFileExist("pg","123")).thenReturn(true);
        lenient().when(advocateUtil.doesAdvocateExist(new RequestInfo(), "123")).thenReturn(true);
        lenient().when(configuration.getCaseBusinessServiceName()).thenReturn("case");

        lenient().when(caseRepository.getApplications(any(), any())).thenReturn((List.of(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourtCaseNumber()).build())));

        assertThrows(Exception.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingTenantId() {
        CaseCriteria caseCriteria = new CaseCriteria();
        CourtCase courtCase = new CourtCase();
//        courtCase.setTenantId("pg");
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingFilingDate() {
        CourtCase courtCase = new CourtCase();
        CaseCriteria caseCriteria = new CaseCriteria();
        courtCase.setTenantId("pg");
        Workflow workflow = new Workflow();
        workflow.setAction("random_action");
        courtCase.setWorkflow(workflow);
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }
    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingFilingDateNotThrowOnSaveDraft() {
        CourtCase courtCase = new CourtCase();
        CaseCriteria caseCriteria = new CaseCriteria();
        courtCase.setTenantId("pg");
        Workflow workflow = new Workflow();
        workflow.setAction(SAVE_DRAFT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplication_INDIVIDUAL_NOT_FOUND() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setCaseCategory("civil");
        Document document = new Document();
        document.setFileStore("123");
        courtCase.setDocuments(List.of(document));
        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("123");
        courtCase.setRepresentatives(List.of(advocateMapping));
        Party party = new Party();
        party.setIndividualId("123");
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        courtCase.setLitigants(List.of(party));
        LinkedCase linkedCase = LinkedCase.builder().id(UUID.randomUUID()).caseNumber("caseNumber").build();
//        courtCase.setLinkedCases(List.of(linkedCase));
        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(linkedCase.getId().toString());
        caseCriteriaList.add(caseCriteria);
        caseSearchRequest.setRequestInfo(new RequestInfo());
        caseSearchRequest.setCriteria(caseCriteriaList);

        lenient().when(mdmsUtil.fetchMdmsData(new RequestInfo(),"pg","case", masterList)).thenReturn(mdmsRes);

        lenient().when(individualService.searchIndividual(new RequestInfo(), "123")).thenThrow(new CustomException());
        lenient().when(configuration.getCaseBusinessServiceName()).thenReturn("case");

        lenient().when(caseRepository.getApplications(any(), any())).thenReturn((List.of(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourtCaseNumber()).build())));
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingCaseCategory() {
        CourtCase courtCase = new CourtCase();
        CaseCriteria caseCriteria = new CaseCriteria();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());
        lenient().when(caseRepository.getApplications(any(), any())).thenReturn(List.of(caseCriteria));

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingStatutes() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("categ1");
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        CaseCriteria caseCriteria = new CaseCriteria();
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());
        lenient().when(caseRepository.getApplications(any(), any())).thenReturn(List.of(caseCriteria));

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingLitigantsNotThrowWhenWhenDeleteAction() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("categ1");
        courtCase.setStatutesAndSections(List.of(StatuteSection.builder().tenantId("pb").build()));
        CaseCriteria caseCriteria = new CaseCriteria();
        Workflow workflow = new Workflow();
        workflow.setAction(DELETE_DRAFT_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        lenient().when(caseRepository.getApplications(any(), any())).thenReturn(List.of(caseCriteria));
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingLitigants() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("categ1");
        courtCase.setStatutesAndSections(List.of(StatuteSection.builder().tenantId("pb").build()));
        CaseCriteria caseCriteria = new CaseCriteria();
        Workflow workflow = new Workflow();
        workflow.setAction(SUBMIT_CASE_WORKFLOW_ACTION);
        courtCase.setWorkflow(workflow);
        lenient().when(caseRepository.getApplications(any(), any())).thenReturn(List.of(caseCriteria));
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }

    @Test
    void testValidateApplicationExistence_NoExistingApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("Filing123");
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(courtCase);
        caseRequest.setRequestInfo(new RequestInfo());

        lenient().when(caseRepository.getApplications(any(), any())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(caseRequest));
    }
    @Test
    public void testValidateLitigantJoinCase_ValidIndividualIdAndDocuments() {
        litigant.setIndividualId("validId");
        litigant.setDocuments(Collections.singletonList(document));
        document.setFileStore("validFileStore");
        litigant.setTenantId("tenantId");

        when(individualService.searchIndividual(requestInfo, "validId")).thenReturn(true);
        when(fileStoreUtil.doesFileExist("tenantId", "validFileStore")).thenReturn(true);

        assertTrue(validator.canLitigantJoinCase(joinCaseRequest));
    }
    @Test
    public void testValidateLitigantJoinCase_InvalidIndividualId() {
        litigant.setIndividualId("invalidId");

        when(individualService.searchIndividual(requestInfo, "invalidId")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canLitigantJoinCase(joinCaseRequest);
        });
        assertEquals(INDIVIDUAL_NOT_FOUND, exception.getCode());
        assertEquals("Invalid complainant details", exception.getMessage());
    }
    @Test
    public void testValidateLitigantJoinCase_NullIndividualId() {

        lenient().when(individualService.searchIndividual(requestInfo, "ind_id")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canLitigantJoinCase(joinCaseRequest);
        });
        assertEquals(INDIVIDUAL_NOT_FOUND, exception.getCode());
        assertEquals("Invalid complainant details", exception.getMessage());
    }
    @Test
    public void testValidateLitigantJoinCase_InvalidDocumentFileStore() {
        litigant.setIndividualId("validId");
        litigant.setDocuments(Collections.singletonList(document));
        document.setFileStore("invalidFileStore");
        litigant.setTenantId("tenantId");

        when(individualService.searchIndividual(requestInfo, "validId")).thenReturn(true);
        when(fileStoreUtil.doesFileExist("tenantId", "invalidFileStore")).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canLitigantJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }

    @Test
    public void testValidateLitigantJoinCase_MissingDocumentFileStore() {
        litigant.setIndividualId("validId");
        litigant.setDocuments(Collections.singletonList(document));
        litigant.setTenantId("tenantId");

        when(individualService.searchIndividual(requestInfo, "validId")).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canLitigantJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }

    @Test
    public void testValidateRepresentativeJoinCase_ValidAdvocateIdAndDocuments() {
        representative.setAdvocateId("validId");
        representative.setDocuments(Collections.singletonList(document));
        document.setFileStore("validFileStore");
        representative.setTenantId("tenantId");

        when(advocateUtil.doesAdvocateExist(requestInfo, "validId")).thenReturn(true);
        when(fileStoreUtil.doesFileExist("tenantId", "validFileStore")).thenReturn(true);

        assertTrue(validator.canRepresentativeJoinCase(joinCaseRequest));
    }

    @Test
    public void testValidateRepJoinCase_InvalidAdvocateId() {
        representative.setAdvocateId("invalidId");

        when(advocateUtil.doesAdvocateExist(requestInfo, "invalidId")).thenReturn(false);


        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canRepresentativeJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_ADVOCATE_ID, exception.getCode());
    }
    @Test
    public void testValidateLitigantJoinCase_NullAdvocateId() {

        lenient().when(advocateUtil.doesAdvocateExist(requestInfo, "ind_id")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canRepresentativeJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_ADVOCATE_ID, exception.getCode());
        assertEquals("Invalid advocate details", exception.getMessage());
    }
    @Test
    public void testValidateRepJoinCase_InvalidDocumentFileStore() {
        representative.setAdvocateId("validId");
        representative.setDocuments(Collections.singletonList(document));
        document.setFileStore("invalidFileStore");
        representative.setTenantId("tenantId");

        when(advocateUtil.doesAdvocateExist(requestInfo, "validId")).thenReturn(true);
        when(fileStoreUtil.doesFileExist("tenantId", "invalidFileStore")).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canRepresentativeJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }

    @Test
    public void testValidateRepJoinCase_MissingDocumentFileStore() {
        representative.setAdvocateId("validId");
        representative.setDocuments(Collections.singletonList(document));
        representative.setTenantId("tenantId");

        when(advocateUtil.doesAdvocateExist(requestInfo, "validId")).thenReturn(true);
        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.canRepresentativeJoinCase(joinCaseRequest);
        });
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }
}

