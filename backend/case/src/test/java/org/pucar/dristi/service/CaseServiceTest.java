package org.pucar.dristi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.EncryptionDecryptionUtil;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.web.client.RestTemplate;

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

    @Mock
    private CacheService cacheService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CaseService caseService;

    private CaseRequest caseRequest;
    private RequestInfo requestInfo;
    private User userInfo;

    private JoinCaseRequest joinCaseRequest;
    private CourtCase courtCase;

    private CaseSearchRequest caseSearchRequest;

    private CaseExistsRequest caseExistsRequest;

    private CaseCriteria caseCriteria;

    @BeforeEach
    void setup() {
        caseRequest = new CaseRequest();
        courtCase = new CourtCase();
        courtCase.setId(UUID.randomUUID());
        caseRequest.setCases(courtCase);
        caseSearchRequest = new CaseSearchRequest();
        caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId("case-id");
        caseSearchRequest.setCriteria(Collections.singletonList(caseCriteria));
        caseExistsRequest = new CaseExistsRequest();
        requestInfo = new RequestInfo();
        userInfo = new User();
        userInfo.setUuid("user-uuid");
        userInfo.setType("employee");
        userInfo.setTenantId("tenant-id");
        Role role = new Role();
        role.setName("employee");
        userInfo.setRoles(Collections.singletonList(role));
        requestInfo.setUserInfo(userInfo);
        caseSearchRequest.setRequestInfo(requestInfo);
        // Initialize mocks and create necessary objects for the tests
        joinCaseRequest = new JoinCaseRequest();
        joinCaseRequest.setAdditionalDetails("form-data");
        courtCase = new CourtCase();
        objectMapper = new ObjectMapper();
        caseService = new CaseService(validator,enrichmentUtil,caseRepository,workflowService,config,producer,new BillingUtil(new RestTemplate(),config),encryptionDecryptionUtil,objectMapper,cacheService);
    }
    @Test
    void testCreateCase() {
        // Set up mock responses
        doNothing().when(validator).validateCaseRegistration(any());
        doNothing().when(enrichmentUtil).enrichCaseRegistrationOnCreate(any());
        doNothing().when(workflowService).updateWorkflowStatus(any());
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(caseRequest.getCases());
        when(encryptionDecryptionUtil.decryptObject(any(),any(),any(),any())).thenReturn(caseRequest.getCases());
        doNothing().when(producer).push(any(), any()); // Stubbing to accept any arguments
        doNothing().when(cacheService).save(anyString(), any());
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
        when(caseRepository.getCases(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(new CaseCriteria()));
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });

    }

    @Test
    public void testVerifyJoinCaseRequest_AccessCodeNotGenerated() {
        // Setup a sample CourtCase object access code as null
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        AdvocateMapping representative = new AdvocateMapping();
        representative.setAdvocateId("existingAdv");
        courtCase.setId(UUID.randomUUID());
        courtCase.setFilingNumber(joinCaseRequest.getCaseFilingNumber());
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setRepresentatives(Collections.singletonList(representative));
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        lenient().when(caseRepository.getCases(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));
        assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });
    }

    @Test
    public void testVerifyJoinCaseRequest_LitigantAlreadyExists() {
        // Setup a Litigant that is already part of the case
        Party litigant = new Party();
        litigant.setIndividualId("existingLitigant");
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setLitigants(Collections.singletonList(litigant));

        when(encryptionDecryptionUtil.decryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class), any(RequestInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getCases(anyList(), any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));

        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setLitigant(litigant);

        when(validator.canLitigantJoinCase(joinCaseRequest)).thenReturn(true);
        when(config.getCaseDecryptSelf()).thenReturn("CaseDecryptSelf");

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


        when(caseRepository.getCases(anyList(), any())).thenReturn(existingApplications);

        requestInfo.setUserInfo(new User());
        joinCaseRequest.setRequestInfo(requestInfo);

        when(encryptionDecryptionUtil.decryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class), any(RequestInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(config.getCaseDecryptSelf()).thenReturn("CaseDecryptSelf");
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
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        courtCase.setRepresentatives(Collections.singletonList(representative));

        when(encryptionDecryptionUtil.decryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class), any(RequestInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getCases(anyList(),any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));

        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setRepresentative(representative);
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);
        when(config.getCaseDecryptSelf()).thenReturn("CaseDecryptSelf");

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.verifyJoinCaseRequest(joinCaseRequest);
        });

        assertEquals("Advocate is already a part of the given case", exception.getMessage());
    }

    @Test
    void testVerifyJoinCaseRequest_DisableExistingRepresenting() throws JsonProcessingException {
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

        when(caseRepository.getCases(anyList(), any())).thenReturn(existingApplications);
        when(config.getUpdateRepresentativeJoinCaseTopic()).thenReturn("update-topic");
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);

        requestInfo.setUserInfo(new User());
        joinCaseRequest.setRequestInfo(requestInfo);
        Party party1 = Party.builder().individualId("111").partyType(ServiceConstants.COMPLAINANT_PRIMARY).isActive(true).auditDetails(new AuditDetails()).build();
        AdvocateMapping advocateMapping2 = AdvocateMapping.builder().advocateId("333").representing(Collections.singletonList(party1)).isActive(true).auditDetails(new AuditDetails()).build();
        joinCaseRequest.setRepresentative(advocateMapping2);

        when(encryptionDecryptionUtil.decryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class), any(RequestInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(encryptionDecryptionUtil.encryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String additionalDetails1Json = """
        {
            "advocateDetails": {
                "advocateName": "John Doe",
                "advocateId": "ADV-2024-01"
            },
            "respondentDetails": {
                "formdata": [
                    {
                        "data": {
                            "respondentLastName": "Doe",
                            "respondentFirstName": "John",
                            "respondentMiddleName": "M",
                            "respondentVerification": {
                                "individualDetails": {
                                    "individualId": "IND-2024-08-21-002193",
                                    "document": null
                                }
                            }
                        }
                    }
                ]
            }
        }
        """;

        String additionalDetails2Json = """
        {
            "caseId": "CASE-2024-01",
            "advocateDetails": {
                "advocateName": "Jane Smith",
                "advocateId": "ADV-2024-02"
            },
            "respondentDetails": {
                "formdata": [
                    {
                        "data": {
                            "respondentVerification": {
                                "individualDetails": {
                                    "individualId": "IND-2024-08-21-002193",
                                    "document": "SomeDocument"
                                }
                            }
                        }
                    }
                ]
            }
        }
        """;
        Object additionalDetails1 = objectMapper.readValue(additionalDetails1Json, Object.class);
        Object additionalDetails2 = objectMapper.readValue(additionalDetails2Json, Object.class);
        courtCase.setAdditionalDetails(additionalDetails1);
        joinCaseRequest.setAdditionalDetails(additionalDetails2);
        when(config.getCaseDecryptSelf()).thenReturn("CaseDecryptSelf");
        when(config.getCourtCaseEncrypt()).thenReturn("CourtCase");

        // Call the method
        JoinCaseResponse response = caseService.verifyJoinCaseRequest(joinCaseRequest);

        // Verify the results
        assertNotNull(response);
    }

    @Test
    public void testVerifyJoinCaseRequest_Success() throws JsonProcessingException {
        Party litigant = new Party();
        litigant.setIndividualId("newLitigant");
        AdvocateMapping advocate = new AdvocateMapping();
        advocate.setAdvocateId("newAdvocate");
        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        courtCase.setStatus(CASE_ADMIT_STATUS);
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setResponseList(Collections.singletonList(courtCase));
        when(caseRepository.getCases(anyList(),any(RequestInfo.class))).thenReturn(Collections.singletonList(caseCriteria));
        joinCaseRequest.setRequestInfo(requestInfo);
        joinCaseRequest.setCaseFilingNumber("12345");
        joinCaseRequest.setAccessCode("validAccessCode");
        joinCaseRequest.setLitigant(litigant);
        joinCaseRequest.setAdditionalDetails("form-data");
        String additionalDetails1Json = """
        {
            "advocateDetails": {
                "advocateName": "John Doe",
                "advocateId": "ADV-2024-01"
            },
            "respondentDetails": {
                "formdata": [
                    {
                        "data": {
                            "respondentLastName": "Doe",
                            "respondentFirstName": "John",
                            "respondentMiddleName": "M",
                            "respondentVerification": {
                                "individualDetails": {
                                    "individualId": "IND-2024-08-21-002193",
                                    "document": null
                                }
                            }
                        }
                    }
                ]
            }
        }
        """;

        String additionalDetails2Json = """
        {
            "caseId": "CASE-2024-01",
            "advocateDetails": {
                "advocateName": "Jane Smith",
                "advocateId": "ADV-2024-02"
            },
            "respondentDetails": {
                "formdata": [
                    {
                        "data": {
                            "respondentVerification": {
                                "individualDetails": {
                                    "individualId": "IND-2024-08-21-002193",
                                    "document": "SomeDocument"
                                }
                            }
                        }
                    }
                ]
            }
        }
        """;
        Object additionalDetails1 = objectMapper.readValue(additionalDetails1Json, Object.class);
        Object additionalDetails2 = objectMapper.readValue(additionalDetails2Json, Object.class);
        courtCase.setAdditionalDetails(additionalDetails1);
        joinCaseRequest.setAdditionalDetails(additionalDetails2);
        when(config.getCaseDecryptSelf()).thenReturn("CaseDecryptSelf");
        when(config.getCourtCaseEncrypt()).thenReturn("CourtCase");

        joinCaseRequest.setRepresentative(advocate);
        when(validator.canLitigantJoinCase(joinCaseRequest)).thenReturn(true);
        when(validator.canRepresentativeJoinCase(joinCaseRequest)).thenReturn(true);

        when(encryptionDecryptionUtil.decryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class), any(RequestInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(encryptionDecryptionUtil.encryptObject(any(CourtCase.class), any(String.class), eq(CourtCase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

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
        User user = new User();
        user.setUuid("user-uuid");
        requestInfo.setUserInfo(user);
        joinCaseRequest.setRequestInfo(requestInfo);

        courtCase.setId(UUID.randomUUID());
        courtCase.setAccessCode("validAccessCode");
        List<CaseCriteria> existingApplications = List.of(CaseCriteria.builder().responseList(List.of(courtCase)).build());

        when(caseRepository.getCases(anyList(), any())).thenReturn(existingApplications);

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
        when(caseRepository.getCases(any(), any())).thenReturn(mockCases);

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getCases(any(), any());
    }

    @Test
    void testSearchCases2() {

        List<CaseCriteria> mockCases = new ArrayList<>();
        caseCriteria.setResponseList(new ArrayList<>());
        mockCases.add(caseCriteria);
        // Set up mock responses
        when(caseRepository.getCases(any(), any())).thenReturn(mockCases);

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getCases(any(), any());
    }

    @Test
    void testSearchCases_CustomException() {
        when(caseRepository.getCases(any(), any())).thenThrow(CustomException.class);

        assertThrows(CustomException.class, () -> caseService.searchCases(caseSearchRequest));
    }

    @Test
    void testSearchCases_Exception() {
        when(caseRepository.getCases(any(), any())).thenThrow(new RuntimeException());

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
        courtCase.setId(UUID.randomUUID());
        caseRequest.setCases(courtCase);

        when(validator.validateUpdateRequest(any(CaseRequest.class))).thenReturn(true);
        doNothing().when(enrichmentUtil).enrichCaseApplicationUponUpdate(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(courtCase);

        doNothing().when(producer).push(anyString(), any(CaseRequest.class));
        doNothing().when(cacheService).save(anyString(), any());
        when(config.getCaseUpdateTopic()).thenReturn("case-update-topic");

        when(encryptionDecryptionUtil.decryptObject(any(),any(),eq(CourtCase.class),any())).thenReturn(courtCase);

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

        when(validator.validateUpdateRequest(any(CaseRequest.class))).thenReturn(false);

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testUpdateCase_Validation_CustomException() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Assume the necessary properties are set
        caseRequest.setCases(courtCase);

        when(validator.validateUpdateRequest(any(CaseRequest.class))).thenThrow(new CustomException("VALIDATION", "Case does not exist"));

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testUpdateCase_Validation_Exception() {
        // Setup
        caseRequest.setCases(courtCase);

        when(validator.validateUpdateRequest(any(CaseRequest.class))).thenThrow(new RuntimeException());

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
        cases.setId(UUID.randomUUID());
        caseRequest.setCases(cases);
        doNothing().when(validator).validateCaseRegistration(any(CaseRequest.class));
        doNothing().when(enrichmentUtil).enrichCaseRegistrationOnCreate(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        when(encryptionDecryptionUtil.encryptObject(any(),any(),any())).thenReturn(cases);
        when(encryptionDecryptionUtil.decryptObject(any(),any(),any(),any())).thenReturn(cases);
        CourtCase result = caseService.createCase(caseRequest);

        assertNotNull(result);
        assertEquals(cases, result);

    }

    @Test
    void testSearchCases_EmptyResult() {
        CaseSearchRequest searchRequest = new CaseSearchRequest(); // Setup search request
        when(caseRepository.getCases(any(), any())).thenReturn(Arrays.asList());

        caseService.searchCases(searchRequest);
    }

    @Test
    public void testAddWitness_Success() {
        AddWitnessRequest addWitnessRequest = new AddWitnessRequest();
        addWitnessRequest.setCaseFilingNumber("CASE123");
        addWitnessRequest.setAdditionalDetails("details");
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setType("EMPLOYEE");
        Role role = new Role();
        role.setName("EMPLOYEE");
        user.setRoles(Collections.singletonList(role));
        requestInfo.setUserInfo(user);
        addWitnessRequest.setRequestInfo(requestInfo);

        CaseExists caseExists = new CaseExists();
        caseExists.setExists(true);
        List<CaseExists> caseExistsList = Collections.singletonList(caseExists);

        CourtCase caseObj = CourtCase.builder()
                .filingNumber(addWitnessRequest.getCaseFilingNumber())
                .additionalDetails(addWitnessRequest.getAdditionalDetails())
                .build();

        when(caseRepository.checkCaseExists(anyList())).thenReturn(caseExistsList);
        when(config.getAdditionalJoinCaseTopic()).thenReturn("topic");
        AddWitnessResponse response = caseService.addWitness(addWitnessRequest);

        verify(producer, times(1)).push(eq("topic"), eq(addWitnessRequest));
        assertEquals(addWitnessRequest, response.getAddWitnessRequest());
        assertEquals(addWitnessRequest.getAdditionalDetails(), response.getAddWitnessRequest().getAdditionalDetails());
    }

    @Test
    public void testAddWitness_CaseNotFound() {
        AddWitnessRequest addWitnessRequest = new AddWitnessRequest();
        addWitnessRequest.setCaseFilingNumber("CASE123");
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setType("EMPLOYEE");
        requestInfo.setUserInfo(user);
        addWitnessRequest.setRequestInfo(requestInfo);

        CaseExists caseExists = new CaseExists();
        caseExists.setExists(false);
        List<CaseExists> caseExistsList = Collections.singletonList(caseExists);

        when(caseRepository.checkCaseExists(anyList())).thenReturn(caseExistsList);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.addWitness(addWitnessRequest);
        });

        assertEquals("INVALID_CASE", exception.getCode());
        assertEquals("No case found for the given filling Number", exception.getMessage());
    }

    @Test
    public void testAddWitness_InvalidUser() {
        AddWitnessRequest addWitnessRequest = new AddWitnessRequest();
        addWitnessRequest.setCaseFilingNumber("CASE123");
        addWitnessRequest.setAdditionalDetails("data");
        User user = new User();
        user.setType("CITIZEN");
        requestInfo.setUserInfo(user);
        addWitnessRequest.setRequestInfo(requestInfo);

        CaseExists caseExists = new CaseExists();
        caseExists.setExists(true);
        List<CaseExists> caseExistsList = Collections.singletonList(caseExists);

        when(caseRepository.checkCaseExists(anyList())).thenReturn(caseExistsList);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.addWitness(addWitnessRequest);
        });

        assertEquals("VALIDATION_EXCEPTION", exception.getCode());
        assertEquals("Not a valid user to add witness details", exception.getMessage());
    }

    @Test
    public void testAddWitness_AdditionalDetailsRequired() {
        AddWitnessRequest addWitnessRequest = new AddWitnessRequest();
        addWitnessRequest.setCaseFilingNumber("CASE123");
        User user = new User();
        user.setType("EMPLOYEE");
        requestInfo.setUserInfo(user);
        addWitnessRequest.setRequestInfo(requestInfo);

        CaseExists caseExists = new CaseExists();
        caseExists.setExists(true);
        List<CaseExists> caseExistsList = Collections.singletonList(caseExists);

        when(caseRepository.checkCaseExists(anyList())).thenReturn(caseExistsList);

        CustomException exception = assertThrows(CustomException.class, () -> {
            caseService.addWitness(addWitnessRequest);
        });

        assertEquals("VALIDATION_EXCEPTION", exception.getCode());
        assertEquals("Additional details are required", exception.getMessage());
    }

    @Test
    public void testSearchRedisCache_CaseFound() throws JsonProcessingException {
        caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId("case-id");
        String expectedId = "tenant-id:case-id";
        CourtCase expectedCourtCase = new CourtCase();
        when(cacheService.findById(expectedId)).thenReturn(expectedCourtCase);

        CourtCase result = caseService.searchRedisCache(requestInfo, caseCriteria);

        assertNotNull(result);
        assertEquals(expectedCourtCase, result);
    }

    @Test
    public void testSearchRedisCache_CaseNotFound() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("123");


        when(cacheService.findById(anyString())).thenReturn(null);

        CourtCase result = caseService.searchRedisCache(requestInfo, criteria);

        assertNull(result);
    }

//    @Test
//    public void testSearchRedisCache_JsonProcessingException() throws JsonProcessingException {
//        CaseCriteria criteria = new CaseCriteria();
//        criteria.setCaseId("123");
//
//        CourtCase cachedValue = new CourtCase();
//
//        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
//        when(cacheService.findById(anyString())).thenReturn(cachedValue);
//        when(objectMapperMock.writeValueAsString(cachedValue)).thenThrow(new JsonProcessingException("Error") {});
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            caseService.searchRedisCache(requestInfo, criteria);
//        });
//
//        assertEquals("Error", exception.getMessage());
//    }

    @Test
    void saveInRedisCache_withValidCaseCriteriaAndCourtCase_savesToCache() {
        List<CourtCase> responseList = new ArrayList<>();
        courtCase.setId(UUID.randomUUID());
        responseList.add(courtCase);
        caseCriteria.setResponseList(responseList);

        List<CaseCriteria> casesList = new ArrayList<>();
        casesList.add(caseCriteria);

        doNothing().when(cacheService).save(anyString(), any());

        caseService.saveInRedisCache(casesList, requestInfo);
    }

    @Test
    void saveInRedisCache_withEmptyCasesList_doesNothing() {
        List<CaseCriteria> emptyList = new ArrayList<>();
        RequestInfo requestInfo = mock(RequestInfo.class);

        caseService.saveInRedisCache(emptyList, requestInfo);

        verifyNoInteractions(cacheService);
    }
}
