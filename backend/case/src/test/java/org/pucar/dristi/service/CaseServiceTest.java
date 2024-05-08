package org.pucar.dristi.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    void setup() {
        caseRequest = new CaseRequest();
        caseSearchRequest = new CaseSearchRequest();
    }
    @Test
    void testCreateCase() {
        // Set up mock responses
        doNothing().when(validator).validateCaseRegistration(any());
        doNothing().when(enrichmentUtil).enrichCaseRegistration(any());
        doNothing().when(workflowService).updateWorkflowStatus(any());
        doNothing().when(producer).push(anyString(), any());

        // Call the method under test
        List<CourtCase> result = caseService.createCase(caseRequest);

        // Assert and verify
        assertNotNull(result);
        verify(validator, times(1)).validateCaseRegistration(any());
        verify(enrichmentUtil, times(1)).enrichCaseRegistration(any());
        verify(workflowService, times(1)).updateWorkflowStatus(any());
        verify(producer, times(1)).push(anyString(), any());
    }

    @Test
    void testSearchCases() {
        // Set up mock responses
        List<CourtCase> mockCases = new ArrayList<>(); // Assume filled with test data
        when(caseRepository.getApplications(any())).thenReturn(mockCases);

        // Call the method under test
        List<CourtCase> result = caseService.searchCases(caseSearchRequest);

        // Assert and verify
        assertNotNull(result);
        verify(caseRepository, times(1)).getApplications(any());
    }

    @Test
    void testRegisterCaseRequest_Exception() {
        // Setup
        doThrow(new CustomException("VALIDATION", "Validation failed")).when(validator).validateCaseRegistration(any(CaseRequest.class));

        // Execute & Assert
        assertThrows(CustomException.class, () -> {
            caseService.createCase(caseRequest);
        });
    }

    @Test
    void testUpdateCase_Success() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Mock a CourtCase object with required fields
        caseRequest.setCases(Arrays.asList(courtCase));

        when(validator.validateApplicationExistence(any(CourtCase.class), any())).thenReturn(courtCase);
        doNothing().when(enrichmentUtil).enrichCaseApplicationUponUpdate(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        doNothing().when(producer).push(anyString(), any(CaseRequest.class));
        when(config.getCaseUpdateTopic()).thenReturn("case-update-topic");

        // Execute
        List<CourtCase> results = caseService.updateCase(caseRequest);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(producer).push(eq("case-update-topic"), any(CaseRequest.class));
    }

    @Test
    void testUpdateCase_ValidationException() {
        // Setup
        CourtCase courtCase = new CourtCase(); // Assume the necessary properties are set
        caseRequest.setCases(Arrays.asList(courtCase));

        when(validator.validateApplicationExistence(any(CourtCase.class),any())).thenThrow(new CustomException("VALIDATION", "Case does not exist"));

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.updateCase(caseRequest));
    }

    @Test
    void testExistCases_Success() {
        // Setup
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCnrNumber("12345");
        criteria.setFilingNumber("67890");

        caseSearchRequest.setCriteria(Arrays.asList(criteria));

        CourtCase courtCase = new CourtCase();
        courtCase.setCaseNumber("12345");
        courtCase.setFilingNumber("67890");
        when(caseRepository.getApplications(any())).thenReturn(Arrays.asList(courtCase));

        // Execute
        List<CaseExists> results = caseService.existCases(caseSearchRequest);

        // Assert
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getExists());
    }

    @Test
    void testExistCases_NoCasesExist() {
        // Setup
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCnrNumber("12345");
        criteria.setFilingNumber("67890");

        caseSearchRequest.setCriteria(Arrays.asList(criteria));

        when(caseRepository.getApplications(any())).thenReturn(new ArrayList<>());

        // Execute
        List<CaseExists> results = caseService.existCases(caseSearchRequest);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty() || !results.get(0).getExists());
    }

    @Test
    void testExistCases_Exception() {
        // Setup
        when(caseRepository.getApplications(any())).thenThrow(new RuntimeException("Database error"));

        // Execute & Assert
        assertThrows(CustomException.class, () -> caseService.existCases(caseSearchRequest));
    }


    @Test
    void testRegisterCaseRequest_ValidInput() {
        CaseRequest caseRequest = new CaseRequest(); // Assume CaseRequest is suitably instantiated
        List<CourtCase> cases = Arrays.asList(new CourtCase()); // Mock court case list
        caseRequest.setCases(cases);
        doNothing().when(validator).validateCaseRegistration(any(CaseRequest.class));
        doNothing().when(enrichmentUtil).enrichCaseRegistration(any(CaseRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(CaseRequest.class));
        doNothing().when(producer).push(anyString(), any(CaseRequest.class));

        List<CourtCase> result = caseService.createCase(caseRequest);

        assertNotNull(result);
        assertEquals(cases, result);
        verify(producer).push(eq("save-case-application"), eq(caseRequest));
    }

    @Test
    void testSearchCases_EmptyResult() {
        CaseSearchRequest searchRequest = new CaseSearchRequest(); // Setup search request
        when(caseRepository.getApplications(any())).thenReturn(Arrays.asList());

        List<CourtCase> results = caseService.searchCases(searchRequest);

        assertTrue(results.isEmpty());
    }

}
