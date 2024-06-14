package org.pucar.dristi.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.pucar.dristi.config.ServiceConstants.*;

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
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private ApplicationExistsRequest applicationExistsRequest;


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
        when(validator.validateApplicationExistence(any(), any())).thenReturn(true);
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

        assertEquals("Error occurred while updating application: Validation failed", exception.getMessage());
        verify(validator).validateApplicationExistence(requestInfo, application);
        verify(enrichmentUtil, never()).enrichApplicationUponUpdate(applicationRequest);
        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void testUpdateApplication_generalFailure() {
        // Arrange
        when(applicationRequest.getApplication()).thenReturn(application);
        when(applicationRequest.getRequestInfo()).thenReturn(requestInfo);
        when(validator.validateApplicationExistence(any(), any())).thenReturn(true);
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

    @Test
    public void testSearchApplication_success() {
        List<Application> applicationList = new ArrayList<>();
        Application mockApplication = new Application();
        mockApplication.setId(UUID.randomUUID());
        applicationList.add(mockApplication);

        when(applicationRepository.getApplications(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(applicationList);

        List<Application> result = applicationService.searchApplications("id", "filingNum", "cnrNum", "tenant", "status", null, null, null, new RequestInfoBody());

        assertNotNull(result);
        verify(applicationRepository, times(1)).getApplications("id", "filingNum", "cnrNum", "tenant", "status", null, null);
    }

    @Test
    public void testSearchApplications_NoResults() {
        // Arrange
        when(applicationRepository.getApplications(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<Application> result = applicationService.searchApplications("id", "filingNumber", "cnrNumber", "tenantId", "status", 10, 0, "sortBy", new RequestInfoBody());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(applicationRepository, times(1)).getApplications(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }


    @Test
    void testSearchApplicationHandleException() {
        String id = "testId";
        String tenantId = "testTenantId";
        String filingNumber = "filingNumber";
        String cnrNumber = "cnrNumber";
        String status = "status";

        when(applicationRepository.getApplications(id, filingNumber, cnrNumber, tenantId, status, null, null)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                applicationService.searchApplications(id, filingNumber, cnrNumber, tenantId, status, null, null, null, new RequestInfoBody()));

        assertEquals(APPLICATION_SEARCH_ERR, exception.getCode());
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testExistsApplication_Success() {
        List<ApplicationExists> expectedResponse = Collections.singletonList(new ApplicationExists("filingNum", "cnr", "app123", true));
        when(applicationRepository.checkApplicationExists(applicationExistsRequest.getApplicationExists()))
                .thenReturn(expectedResponse);

        List<ApplicationExists> actualResponse = applicationService.existsApplication(applicationExistsRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(applicationRepository).checkApplicationExists(applicationExistsRequest.getApplicationExists());
    }

    @Test
    void testExistsApplication_CustomException() {
        when(applicationRepository.checkApplicationExists(applicationExistsRequest.getApplicationExists()))
                .thenThrow(new RuntimeException("Database error"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            applicationService.existsApplication(applicationExistsRequest);
        });

        assertEquals("Database error", thrown.getMessage());
        verify(applicationRepository).checkApplicationExists(applicationExistsRequest.getApplicationExists());
    }
}
