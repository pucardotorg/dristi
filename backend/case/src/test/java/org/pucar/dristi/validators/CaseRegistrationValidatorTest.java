package org.pucar.dristi.validators;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import net.minidev.json.JSONArray;
import org.apache.kafka.common.protocol.types.Field;
import org.checkerframework.checker.units.qual.C;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CaseRegistrationValidatorTest {

    @Mock
    private IndividualService individualService;
    @Mock
    private CaseService caseService;

    @Mock
    private Configuration config;

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

    @BeforeEach
    void setUp() {
        // Setup done before each test
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
        lenient().when(fileStoreUtil.fileStore("pg", "123")).thenReturn(true);
        lenient().when(advocateUtil.fetchAdvocateDetails(requestInfo, "123")).thenReturn(true);

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
        List<StatuteSection> statuteSectionList = new ArrayList<>();
        statuteSectionList.add(StatuteSection.builder().tenantId("pb").build());
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
        List<StatuteSection> statuteSectionList = new ArrayList<>();
        statuteSectionList.add(StatuteSection.builder().tenantId("pb").build());
        List<Party> litigantList = new ArrayList<>();
        litigantList.add(Party.builder().tenantId("pb").build());
        request.setCases(courtCase);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateApplicationExistence_Exception() {
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

        when(caseRepository.getApplications(any())).thenReturn(List.of(caseCriteria));
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

//    @Test
//    void testValidateApplicationExistence_ExistingApplication() {
//        CourtCase courtCase = new CourtCase();
//        courtCase.setId(UUID.randomUUID());
//        courtCase.setTenantId("pg");
//        courtCase.setCaseCategory("civil");
//        Document document = new Document();
//        document.setFileStore("123");
//        courtCase.setDocuments(List.of(document));
//        AdvocateMapping advocateMapping = new AdvocateMapping();
//        advocateMapping.setAdvocateId("123");
//        courtCase.setRepresentatives(List.of(advocateMapping));
//        Party party = new Party();
//        party.setIndividualId("123");
//        courtCase.setLitigants(List.of(party));
//        LinkedCase linkedCase = LinkedCase.builder().id(courtCase.getId()).caseNumber("caseNumber").build();
//        courtCase.setLinkedCases(List.of(linkedCase));
//        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
//        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
//
//        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
//        mdmsRes.put("case", new HashMap<>());
//        List<String> masterList = new ArrayList<>();
//        masterList.add("ComplainantType");
//        masterList.add("CaseCategory");
//        masterList.add("PaymentMode");
//        masterList.add("ResolutionMechanism");
//
//        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
//        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
//        CaseCriteria caseCriteria = new CaseCriteria();
//        caseCriteriaList.add(caseCriteria);
//        caseSearchRequest.setRequestInfo(new RequestInfo());
//        caseSearchRequest.setCriteria(caseCriteriaList);
//
//        when(mdmsUtil.fetchMdmsData(new RequestInfo(),"pg","case", masterList)).thenReturn(mdmsRes);
//
//        when(individualService.searchIndividual(new RequestInfo(), "123")).thenReturn(true);
//        when(fileStoreUtil.fileStore("pg","123")).thenReturn(true);
//        when(advocateUtil.fetchAdvocateDetails(new RequestInfo(), "123")).thenReturn(true);
//
//        when(caseRepository.getApplications(any())).thenReturn(caseCriteriaList);
//
//        Boolean result = validator.validateApplicationExistence(courtCase, new RequestInfo());
//        assertTrue(result);
//    }

    @Test
    void testValidateApplicationExistence_ExistingApplication_MDMSDataInvalid() {
        CourtCase courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
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
        courtCase.setLitigants(List.of(party));
        LinkedCase linkedCase = LinkedCase.builder().id(courtCase.getId()).caseNumber("caseNumber").build();
        courtCase.setLinkedCases(List.of(linkedCase));
        courtCase.setStatutesAndSections(List.of(new StatuteSection()));
        courtCase.setFilingDate(LocalDate.parse("2021-12-12"));
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsRes.put("case", new HashMap<>());

        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        List<CaseCriteria> caseCriteriaList = new ArrayList<>();
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(linkedCase.getId().toString());
        caseCriteriaList.add(caseCriteria);
        caseSearchRequest.setRequestInfo(new RequestInfo());
        caseSearchRequest.setCriteria(caseCriteriaList);

        when(caseRepository.getApplications(any())).thenReturn(caseCriteriaList);
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingTenantId() {
        CourtCase courtCase = new CourtCase();

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingFilingDate() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingCaseCategory() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());

        when(caseRepository.getApplications(any())).thenReturn(List.of(new CaseCriteria()));

        assertThrows(Exception.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingStatutes() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("categ1");

        when(caseRepository.getApplications(any())).thenReturn(List.of(new CaseCriteria()));

        assertThrows(Exception.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplicationMissingLitigants() {
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("pg");
        courtCase.setFilingDate(LocalDate.now());
        courtCase.setCaseCategory("categ1");
        courtCase.setStatutesAndSections(List.of(StatuteSection.builder().tenantId("pb").build()));

        when(caseRepository.getApplications(any())).thenReturn(List.of(new CaseCriteria()));

        assertThrows(Exception.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }

    @Test
    void testValidateApplicationExistence_NoExistingApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("Filing123");

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase, new RequestInfo()));
    }
}

