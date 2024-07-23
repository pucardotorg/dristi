package org.pucar.dristi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.CASE_ADMIT_STATUS;
import static org.pucar.dristi.config.ServiceConstants.VALIDATION_ERR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.EncryptionDecryptionUtil;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.JoinCaseRequest;
import org.pucar.dristi.web.models.JoinCaseResponse;
import org.pucar.dristi.web.models.Party;

@ExtendWith(MockitoExtension.class)
public class CaseServiceTest {

    @Mock
    private CaseRegistrationValidator validator;
    @Mock
    private CaseRegistrationEnrichment enrichmentUtil;
    @Mock
    private CaseRepository caseRepository;
    @Mock
    private WorkflowService workflowService;
    @Mock
    private Configuration config;
    @Mock
    private Producer producer;
    @Mock
    private EncryptionDecryptionUtil encryptionDecryptionUtil;

    @InjectMocks
    private CaseService caseService;

    private CaseRequest caseRequest;
    private RequestInfo requestInfo;
    private User userInfo;

    private JoinCaseRequest joinCaseRequest;
    private CourtCase courtCase;
    private CourtCase caseObj;
    private AuditDetails auditDetails;

    private CaseSearchRequest caseSearchRequest;

    private CaseExistsRequest caseExistsRequest;



    @BeforeEach
    void setup() {
        caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());
        caseSearchRequest = new CaseSearchRequest();
        caseExistsRequest = new CaseExistsRequest();
        requestInfo = new RequestInfo();
        userInfo = new User();
        userInfo.setUuid("user-uuid");
        userInfo.setType("employee");
        Role role = new Role();
        role.setName("employee");
        userInfo.setRoles(Collections.singletonList(role));
        requestInfo.setUserInfo(userInfo);

        // Initialize mocks and create necessary objects for the tests
        joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setAdditionalDetails("form-data");
        courtCase = new CourtCase();
        caseObj = new CourtCase();
        auditDetails = AuditDetails.builder()
                .createdBy("user-uuid")
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy("user-uuid")
                .lastModifiedTime(System.currentTimeMillis())
                .build();
    }
    @Test
    void testCreateCase() {
        // Set up mock responses
        doNothing().when(validator).validateCaseRegistration(any());
        doNothing().when(enrichmentUtil).enrichCaseRegistrationOnCreate(any());
        doNothing().when(workflowService).updateWorkflowStatus(any());
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(caseRequest.getCases());
        doNothing().when(producer).push(any(), any()); // Stubbing to accept any arguments

        // Call the method under test
        CourtCase result = caseService.createCase(caseRequest);

        // Assert and verify
        assertNotNull(result);
        verify(validator, times(1)).validateCaseRegistration(any());
        verify(enrichmentUtil, times(1)).enrichCaseRegistrationOnCreate(any());
        verify(workflowService, times(1)).updateWorkflowStatus(any());
        verify(producer, times(1)).push(any(), any()); // Verifying the method was called with any arguments
    }

    @Test
    public void testVerifyJoinCaseRequest_CaseDoesNotExist() {
        // Mock the CaseRepository to return an empty list
        when(caseRepository.getApplications(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(new CaseCriteria()));
        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });

    }

    @Test
    public void testVerifyJoinCaseRequest_AccessCodeNotGenerated() {
        // Setup a sample CourtCase object access code as null
        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        AdvocateMapping representative = new AdvocateMapping();
        representative.setAdvocateId("existingAdv");
        CourtCase courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
        courtCase.setFilingNumber(joinCaseRequest.getCaseFilingNumber());
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setRepresentatives(Collections.singletonList(representative));
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        lenient().when(caseRepository.getApplications(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));
        assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });
    }

    @Test
    public void testVerifyJoinCaseRequest_LitigantAlreadyExists() {
        // Setup a Litigant that is already part of the case
        Party litigant = new Party();
        litigant.setIndividualId("existingLitigant");
        CourtCase courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setLitigants(Collections.singletonList(litigant));
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getApplications(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));

        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setLitigant(litigant);

        when(validator.canLitigantJoinCase(joinCaseRequest)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });

        assertEquals(VALIDATION_ERR, exception.getCode());
        assertEquals("Litigant is already a part of the given case", exception.getMessage());
    }

    @Test
    public void testVerifyJoinCaseRequestInvalidAccessCode() {
        String filingNumber = "filing-number";
        joinCaseRequest.setCaseFilingNumber(filingNumber);
        joinCaseRequest.setAccessCode("access-code");

        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(filingNumber).build();
        List<CaseCriteria> existingApplications = Collections.singletonList(caseCriteria);

        Party party = Party.builder().individualId("individual-id").partyType(ServiceConstants.COMPLAINANT_PRIMARY).isActive(true).auditDetails(new AuditDetails()).build();
        AdvocateMapping advocateMapping = AdvocateMapping.builder().representing(Collections.singletonList(party)).isActive(true).auditDetails(new AuditDetails()).build();
        courtCase.setRepresentatives(Collections.singletonList(advocateMapping));

        caseCriteria.setResponseList(Collections.singletonList(courtCase));


        when(caseRepository.getApplications(anyList(), any())).thenReturn(existingApplications);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        joinCaseRequest.setRequestInfo(requestInfo);

        CustomException exception = assertThrows(CustomException.class, () -> caseService.verifyJoinCaseRequest(joinCaseRequest));

        assertEquals("VALIDATION_EXCEPTION", exception.getCode());
        assertEquals("Access code not generated", exception.getMessage());
    }

    @Test
    public void testVerifyJoinCaseRequest_RepresentativesAlreadyExists() {
        Party litigant = new Party();
        litigant.setIndividualId("existingLitigant");
        litigant.setPartyType("primary");

        AdvocateMapping representative = new AdvocateMapping();
        representative.setAdvocateId("existingAdv");
        representative.setRepresenting(Collections.singletonList(litigant));
        CourtCase courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setRepresentatives(Collections.singletonList(representative));

        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getApplications(anyList(),any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));

        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setRepresentative(representative);
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });

        assertEquals("Advocate is already a part of the given case", exception.getMessage());
    }

    @Test
    void testVerifyJoinCaseRequest_DisableExistingRepresenting() {
        // Prepare data for the test
        String filingNumber = "filing-number";
        joinCaseRequest.setCaseFilingNumber(filingNumber);
        joinCaseRequest.setAccessCode("access-code");

        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(filingNumber).build();

        Party party = Party.builder().individualId("111").partyType(ServiceConstants.COMPLAINANT_PRIMARY).isActive(true).auditDetails(new AuditDetails()).build();
        AdvocateMapping advocateMapping = AdvocateMapping.builder().advocateId("222").representing(Collections.singletonList(party)).isActive(true).auditDetails(new AuditDetails()).build();
        courtCase.setRepresentatives(Collections.singletonList(advocateMapping));
        courtCase.setAccessCode("access-code");
        courtCase.setId(UUID.randomUUID());

        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        List<CaseCriteria> existingApplications = Collections.singletonList(caseCriteria);

        when(caseRepository.getApplications(anyList(), any())).thenReturn(existingApplications);
        when(config.getUpdateRepresentativeJoinCaseTopic()).thenReturn("update-topic");
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        joinCaseRequest.setRequestInfo(requestInfo);
        Party party1 = Party.builder().individualId("111").partyType(ServiceConstants.COMPLAINANT_PRIMARY).isActive(true).auditDetails(new AuditDetails()).build();
        AdvocateMapping advocateMapping2 = AdvocateMapping.builder().advocateId("333").representing(Collections.singletonList(party1)).isActive(true).auditDetails(new AuditDetails()).build();
        joinCaseRequest.setRepresentative(advocateMapping2);

        // Call the method
        JoinCaseResponse response = caseService.verifyJoinCaseRequest(joinCaseRequest);

        // Verify the results
        assertNotNull(response);
    }

    @Test
    public void testVerifyJoinCaseRequest_Success() {
        Party litigant = new Party();
        litigant.setIndividualId("newLitigant");
        AdvocateMapping advocate = new AdvocateMapping();
        advocate.setAdvocateId("newAdvocate");
        CourtCase courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getApplications(anyList(),any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));
        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setLitigant(litigant);
        joinCaseRequest.setAdditionalDetails("form-data");

        joinCaseRequest.setRepresentative(advocate);
        when(validator.canLitigantJoinCase(joinCaseRequest)).thenReturn(true);
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);

        JoinCaseResponse response = caseService.verifyJoinCaseRequest(joinCaseRequest);
        assertEquals("validAccessCode", response.getJoinCaseRequest().getAccessCode());
        assertEquals("12345", response.getJoinCaseRequest().getCaseFilingNumber());
        assertEquals(litigant, response.getJoinCaseRequest().getLitigant());
        assertEquals(advocate, response.getJoinCaseRequest().getRepresentative());
    }

    @Test
    public void testVerifyRepresentativesAndJoinCase_throwsException() {
        // Given
        String filingNumber = "123";
        joinCaseRequest.setCaseFilingNumber(filingNumber);
        joinCaseRequest.setAccessCode("validAccessCode");
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setUuid("user-uuid");
        requestInfo.setUserInfo(user);
        joinCaseRequest.setRequestInfo(requestInfo);

        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        List<CaseCriteria> existingApplications = List.of(CaseCriteria.builder().responseList(List.of(courtCase)).build());

        when(caseRepository.getApplications(anyList(), any())).thenReturn(existingApplications);

        AdvocateMapping representative = new AdvocateMapping();
        representative.setAdvocateId("advocate-1");
        Party party = new Party();
        party.setIndividualId("individual-1");
        representative.setRepresenting(Collections.singletonList(party));
        joinCaseRequest.setRepresentative(representative);

        AdvocateMapping advocateMapping = new AdvocateMapping();
        advocateMapping.setAdvocateId("ed14dc62-6162-4e29-8b4b-51e8cd25646c");
        Party party1 = new Party();
        party.setIndividualId("111");
        advocateMapping.setRepresenting(Collections.singletonList(party1));
        courtCase.setRepresentatives(Collections.singletonList(advocateMapping));

        // When
       CustomException exception = assertThrows(CustomException.class, ()->caseService.verifyJoinCaseRequest(joinCaseRequest));
       assertEquals("Invalid request for joining a case",exception.getMessage());
    }


    @Test
    void testSearchCases() {
        // Set up mock responses
        List<CaseCriteria> mockCases = new ArrayList<>(); // Assume filled with test data
        when(caseRepository.getApplications(any(), any())).thenReturn(mockCases);

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getApplications(any(), any());
    }

    @Test
    void testSearchCases2() {
        // Set up mock responses
        List<CourtCase> mockCases = new ArrayList<>(); // Assume filled with test data

        when(caseRepository.getApplications(any(), any())).thenReturn(List.of(CaseCriteria.builder().filingNumber("filNo").courtCaseNumber("123").build()));

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getApplications(any(), any());
    }

    @Test
    void testSearchCases_CustomException() {
        when(caseRepository.getApplications(any(), any())).thenThrow(CustomException.class);

        assertThrows(CustomException.class, () -> caseService.searchCases(caseSearchRequest));
    }

    @Test
    void testSearchCases_Exception() {
        when(caseRepository.getApplications(any(), any())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> caseService.searchCases(caseSearchRequest));
    }

    @Test
    void testRegisterCaseRequest_CustomException() {
        // Setup
        doThrow(new CustomException("VALIDATION", "Validation failed")).when(validator).validateCaseRegistration(any(CaseRequest.class));

        // Execute & Assert
        assertThrows(CustomException.class, () -> {
            caseService.createCase(caseRequest);
        });
    }

    @Test
    void testRegisterCaseRequest_Exception() {
        // Setup
        doThrow(new RuntimeException()).when(validator).validateCaseRegistration(any(CaseRequest.class));

        // Execute & Assert
        assertThrows(Exception.class, () -> {
            caseService.createCase(caseRequest);
        });
    }

    @Test
    void testUpdateCase_Success() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Mock case-indexer.yml CourtCase object with required fields
        caseRequest.setCases(courtCase);

        when(validator.validateApplicationExistence(any(CaseRequest.class))).thenReturn(true);
        doNothing().when(enrichmentUtil).enrichCaseApplicationUponUpdate(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(courtCase);

        doNothing().when(producer).push(anyString(), any(CaseRequest.class));
        when(config.getCaseUpdateTopic()).thenReturn("case-update-topic");

        // Execute
        CourtCase results = caseService.updateCase(caseRequest);

        // Assert
        assertNotNull(results);
        verify(producer).push(eq("case-update-topic"), any(CaseRequest.class));
    }

    @Test
    void testUpdateCase_Validation_ExistenceException() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Assume the necessary properties are set
        caseRequest.setCases(courtCase);

        when(validator.validateApplicationExistence(any(CaseRequest.class))).thenReturn(false);

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testUpdateCase_Validation_CustomException() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Assume the necessary properties are set
        caseRequest.setCases(courtCase);

        when(validator.validateApplicationExistence(any(CaseRequest.class))).thenThrow(new CustomException("VALIDATION", "Case does not exist"));

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testUpdateCase_Validation_Exception() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Assume the necessary properties are set
        caseRequest.setCases(courtCase);

        when(validator.validateApplicationExistence(any(CaseRequest.class))).thenThrow(new RuntimeException());

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testExistCases_Success() {
        // Setup
        CaseExists caseExists = new CaseExists();
        caseExists.setCnrNumber("12345");
        caseExists.setFilingNumber("67890");

        caseExistsRequest.setCriteria(List.of(caseExists));

        when(caseRepository.checkCaseExists(any())).thenReturn(List.of(caseExists));

        // Execute
        List<CaseExists> results = caseService.existCases(caseExistsRequest);

        // Assert
        assertNotNull(results);
    }

    @Test
    void testExistCases_NoCasesExist() {
        // Setup
        CaseExists caseExists = new CaseExists();
        caseExists.setCnrNumber("12345");
        caseExists.setFilingNumber("67890");

        caseExistsRequest.setCriteria(List.of(caseExists));

        when(caseRepository.checkCaseExists(any())).thenReturn(new ArrayList<>());

        // Execute
        List<CaseExists> results = caseService.existCases(caseExistsRequest);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty() || !results.get(0).getExists());
    }

    @Test
    void testExistCases_CustomException() {
        // Setup
        when(caseRepository.checkCaseExists(any())).thenThrow(new CustomException("Error code", "Error msg"));

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.existCases(caseExistsRequest));
    }

    @Test
    void testExistCases_Exception() {
        // Setup
        when(caseRepository.checkCaseExists(any())).thenThrow(new RuntimeException());

        // Execute & Assert
        assertThrows(Exception.class, () -> caseService.existCases(caseExistsRequest));
    }


    @Test
    void testRegisterCaseRequest_ValidInput() {
        CaseRequest caseRequest = new CaseRequest(); // Assume CaseRequest is suitably instantiated
        CourtCase cases = new CourtCase(); // Mock court case list
        caseRequest.setCases(cases);
        doNothing().when(validator).validateCaseRegistration(any(CaseRequest.class));
        doNothing().when(enrichmentUtil).enrichCaseRegistrationOnCreate(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(cases);

        CourtCase result = caseService.createCase(caseRequest);

        assertNotNull(result);
        assertEquals(cases, result);

    }

    @Test
    void testSearchCases_EmptyResult() {
        CaseSearchRequest searchRequest = new CaseSearchRequest(); // Setup search request
        when(caseRepository.getApplications(any(), any())).thenReturn(Arrays.asList());

        caseService.searchCases(searchRequest);
    }

}
