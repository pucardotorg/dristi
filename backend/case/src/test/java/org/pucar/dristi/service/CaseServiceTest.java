package org.pucar.dristi.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @InjectMocks
    private CaseService caseService;

    private CaseRequest caseRequest;

    private CaseSearchRequest caseSearchRequest;

    private CaseExistsRequest caseExistsRequest;

    @BeforeEach
    void setup() {
        caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());
        caseSearchRequest = new CaseSearchRequest();
        caseExistsRequest = new CaseExistsRequest();
    }
    @Test
    void testCreateCase() {
        // Set up mock responses
        doNothing().when(validator).validateCaseRegistration(any());
        doNothing().when(enrichmentUtil).enrichCaseRegistrationOnCreate(any());
        doNothing().when(workflowService).updateWorkflowStatus(any());
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
    void testSearchCases() {
        // Set up mock responses
        List<CaseCriteria> mockCases = new ArrayList<>(); // Assume filled with test data
        when(caseRepository.getApplications(any())).thenReturn(mockCases);

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getApplications(any());
    }

    @Test
    void testSearchCases2() {
        // Set up mock responses
        List<CourtCase> mockCases = new ArrayList<>(); // Assume filled with test data

        when(caseRepository.getApplications(any())).thenReturn(List.of(CaseCriteria.builder().filingNumber("filNo").courtCaseNumber("123").build()));

        // Call the method under test
        caseService.searchCases(caseSearchRequest);

        verify(caseRepository, times(1)).getApplications(any());
    }

    @Test
    void testSearchCases_CustomException() {
        when(caseRepository.getApplications(any())).thenThrow(CustomException.class);

        assertThrows(CustomException.class, () -> caseService.searchCases(caseSearchRequest));
    }

    @Test
    void testSearchCases_Exception() {
        when(caseRepository.getApplications(any())).thenThrow(new RuntimeException());

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


        CourtCase result = caseService.createCase(caseRequest);

        assertNotNull(result);
        assertEquals(cases, result);

    }

    @Test
    void testSearchCases_EmptyResult() {
        CaseSearchRequest searchRequest = new CaseSearchRequest(); // Setup search request
        when(caseRepository.getApplications(any())).thenReturn(Arrays.asList());

        caseService.searchCases(searchRequest);
    }

}
