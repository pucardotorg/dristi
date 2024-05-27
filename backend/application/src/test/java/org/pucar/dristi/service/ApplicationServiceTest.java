package org.pucar.dristi.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.pucar.dristi.config.ServiceConstants.CREATE_APPLICATION_ERR;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.ApplicationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.validator.ApplicationValidator;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;

class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService applicationService;

    @Mock
    private ApplicationValidator validator;

    @Mock
    private ApplicationEnrichment enrichmentUtil;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private Configuration config;

    @Mock
    private Producer producer;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private Application application;

    @Mock
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateApplication_success() {
        // Arrange
        when(applicationRequest.getApplication()).thenReturn(application);
        when(config.getApplicationCreateTopic()).thenReturn("save-application");

        // Act
        Application result = applicationService.createApplication(applicationRequest);

        // Assert
        verify(validator).validateApplication(applicationRequest);
        verify(enrichmentUtil).enrichApplication(applicationRequest);
        verify(workflowService).updateWorkflowStatus(applicationRequest);
        verify(producer).push("save-application", applicationRequest);
        assertEquals(application, result);
    }

    @Test
    void testCreateApplication_failure() {
        // Arrange
        doThrow(new RuntimeException("Validation failed")).when(validator).validateApplication(any());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicationService.createApplication(applicationRequest);
        });

        assertEquals(CREATE_APPLICATION_ERR, exception.getCode());
        assertEquals("Validation failed", exception.getMessage());
        verify(validator).validateApplication(applicationRequest);
        verify(enrichmentUtil, never()).enrichApplication(applicationRequest);
        verify(workflowService, never()).updateWorkflowStatus(applicationRequest);
        verify(producer, never()).push(anyString(), any());
    }


    @Test
    void testUpdateApplication_success() {
        // Arrange
        when(applicationRequest.getApplication()).thenReturn(application);
        when(applicationRequest.getRequestInfo()).thenReturn(requestInfo);
        when(validator.validateApplicationExistence(any(), any())).thenReturn(application);
        when(config.getApplicationUpdateTopic()).thenReturn("update-application");

        // Act
        Application result = applicationService.updateApplication(applicationRequest);

        // Assert
        verify(validator).validateApplicationExistence(requestInfo, application);
        verify(enrichmentUtil).enrichApplicationUponUpdate(applicationRequest);
        verify(producer).push("update-application", applicationRequest);
        assertEquals(application, result);
    }

    @Test
    void testUpdateApplication_validationFailure() {
        // Arrange
        when(applicationRequest.getApplication()).thenReturn(application);
        when(applicationRequest.getRequestInfo()).thenReturn(requestInfo);
        doThrow(new RuntimeException("Validation failed")).when(validator).validateApplicationExistence(any(), any());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicationService.updateApplication(applicationRequest);
        });

        assertEquals("Error validating existing application: Validation failed", exception.getMessage());
        verify(validator).validateApplicationExistence(requestInfo, application);
        verify(enrichmentUtil, never()).enrichApplicationUponUpdate(applicationRequest);
        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void testUpdateApplication_generalFailure() {
        // Arrange
        when(applicationRequest.getApplication()).thenReturn(application);
        when(applicationRequest.getRequestInfo()).thenReturn(requestInfo);
        when(validator.validateApplicationExistence(any(), any())).thenReturn(application);
        doThrow(new RuntimeException("Unexpected error")).when(enrichmentUtil).enrichApplicationUponUpdate(any());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicationService.updateApplication(applicationRequest);
        });

        assertEquals("Error occurred while updating application: Unexpected error", exception.getMessage());
        verify(validator).validateApplicationExistence(requestInfo, application);
        verify(enrichmentUtil).enrichApplicationUponUpdate(applicationRequest);
        verify(producer, never()).push(anyString(), any());
    }
}
