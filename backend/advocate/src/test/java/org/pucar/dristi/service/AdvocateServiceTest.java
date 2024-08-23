package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.validators.AdvocateRegistrationValidator;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ADVOCATE_CREATE_EXCEPTION;

 class AdvocateServiceTest {

    @InjectMocks
    private AdvocateService advocateService;

    @Mock
    private AdvocateRegistrationValidator validator;

    @Mock
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateRepository advocateRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateAdvocateSuccess() {
        AdvocateRequest request = mock(AdvocateRequest.class);
        Advocate advocate = mock(Advocate.class);
        when(request.getAdvocate()).thenReturn(advocate);

        doNothing().when(validator).validateAdvocateRegistration(request);
        doNothing().when(enrichmentUtil).enrichAdvocateRegistration(request);
        doNothing().when(workflowService).updateWorkflowStatus(request);
        when(config.getAdvocateCreateTopic()).thenReturn("advocateCreateTopic");
        doNothing().when(producer).push(anyString(), any());

        Advocate result = advocateService.createAdvocate(request);

        assertEquals(advocate, result);
        verify(validator).validateAdvocateRegistration(request);
        verify(enrichmentUtil).enrichAdvocateRegistration(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("advocateCreateTopic", request);
    }

    @Test
     void testCreateAdvocateCustomException() {
        AdvocateRequest request = mock(AdvocateRequest.class);
        CustomException customException = new CustomException("Error", "Error message");

        doThrow(customException).when(validator).validateAdvocateRegistration(request);

        CustomException exception = assertThrows(CustomException.class, () -> advocateService.createAdvocate(request));

        assertEquals("Error", exception.getCode());
        assertEquals("Error message", exception.getMessage());
    }

    @Test
     void testCreateAdvocateException() {
        AdvocateRequest request = mock(AdvocateRequest.class);

        doThrow(new RuntimeException("Runtime exception")).when(validator).validateAdvocateRegistration(request);

        CustomException exception = assertThrows(CustomException.class, () -> advocateService.createAdvocate(request));

        assertEquals(ADVOCATE_CREATE_EXCEPTION, exception.getCode());
        assertEquals("Runtime exception", exception.getMessage());
    }

    @Test
     void testSearchAdvocate() {
        RequestInfo requestInfo = mock(RequestInfo.class);
        List<AdvocateSearchCriteria> criteria = new ArrayList<>();
        AdvocateSearchCriteria searchCriteria = mock(AdvocateSearchCriteria.class);
        criteria.add(searchCriteria);

        // Mock the repository call to return a specific value
        when(advocateRepository.getAdvocates(criteria, "tenantId", 10, 0)).thenReturn(new ArrayList<>());

        // Call the method under test
        advocateService.searchAdvocate(requestInfo, criteria, "tenantId", null, null);

        // Verify the repository interaction
        verify(advocateRepository).getAdvocates(criteria, "tenantId", 10, 0);

        // Additional assertions can be added here if needed
    }


    @Test
     void testSearchAdvocateCustomException() {
        RequestInfo requestInfo = mock(RequestInfo.class);
        List<AdvocateSearchCriteria> criteria = new ArrayList<>();
        criteria.add(mock(AdvocateSearchCriteria.class));
        CustomException customException = new CustomException("Error", "Error message");

        doThrow(customException).when(advocateRepository).getAdvocates(criteria, "tenantId", 10, 0);

        CustomException exception = assertThrows(CustomException.class, () -> advocateService.searchAdvocate(requestInfo, criteria, "tenantId", null, null));

        assertEquals("Error", exception.getCode());
        assertEquals("Error message", exception.getMessage());
    }

    @Test
     void testSearchAdvocateByStatus() {
        RequestInfo requestInfo = mock(RequestInfo.class);
        List<Advocate> advocates = new ArrayList<>();
        advocates.add(mock(Advocate.class));

        when(advocateRepository.getListApplicationsByStatus("status", "tenantId", 10, 0)).thenReturn(advocates);

        List<Advocate> result = advocateService.searchAdvocateByStatus(requestInfo, "status", "tenantId", null, null);

        assertEquals(advocates, result);
        verify(advocateRepository).getListApplicationsByStatus("status", "tenantId", 10, 0);
    }

    @Test
     void testSearchAdvocateByApplicationNumber() {
        RequestInfo requestInfo = mock(RequestInfo.class);
        List<Advocate> advocates = new ArrayList<>();
        advocates.add(mock(Advocate.class));

        when(advocateRepository.getListApplicationsByApplicationNumber("appNumber", "tenantId", 10, 0)).thenReturn(advocates);

        List<Advocate> result = advocateService.searchAdvocateByApplicationNumber(requestInfo, "appNumber", "tenantId", null, null);

        assertEquals(advocates, result);
        verify(advocateRepository).getListApplicationsByApplicationNumber("appNumber", "tenantId", 10, 0);
    }

    @Test
     void testUpdateAdvocateSuccess() {
        AdvocateRequest request = mock(AdvocateRequest.class);
        Advocate advocate = mock(Advocate.class);
        when(request.getAdvocate()).thenReturn(advocate);

        Advocate existingAdvocate = advocate; // Use the same advocate mock instance
        when(validator.validateApplicationExistence(advocate)).thenReturn(existingAdvocate);

        doNothing().when(enrichmentUtil).enrichAdvocateApplicationUponUpdate(request);
        doNothing().when(workflowService).updateWorkflowStatus(request);
        when(config.getAdvocateUpdateTopic()).thenReturn("advocateUpdateTopic");
        doNothing().when(producer).push(anyString(), any());

        Advocate result = advocateService.updateAdvocate(request);

        assertEquals(existingAdvocate, result); // Both should refer to the same mock instance
        verify(validator).validateApplicationExistence(advocate);
        verify(enrichmentUtil).enrichAdvocateApplicationUponUpdate(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("advocateUpdateTopic", request);
    }


    @Test
     void testUpdateAdvocateCustomException() {
        AdvocateRequest request = mock(AdvocateRequest.class);
        Advocate advocate = mock(Advocate.class);
        when(request.getAdvocate()).thenReturn(advocate);

        CustomException customException = new CustomException("Error", "Error message");
        doThrow(customException).when(validator).validateApplicationExistence(advocate);

        CustomException exception = assertThrows(CustomException.class, () -> advocateService.updateAdvocate(request));

        assertEquals("VALIDATION_ERROR", exception.getCode());
        assertEquals("Error validating existing application: Error message", exception.getMessage());
    }

    @Test
     void testUpdateAdvocateException() {
        AdvocateRequest request = mock(AdvocateRequest.class);
        Advocate advocate = mock(Advocate.class);
        when(request.getAdvocate()).thenReturn(advocate);

        doThrow(new RuntimeException("Runtime exception")).when(validator).validateApplicationExistence(advocate);

        CustomException exception = assertThrows(CustomException.class, () -> advocateService.updateAdvocate(request));

        assertEquals("VALIDATION_ERROR", exception.getCode());
        assertEquals("Error validating existing application: Runtime exception", exception.getMessage());
    }
}
