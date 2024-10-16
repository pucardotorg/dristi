package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Amount;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TaskRegistrationEnrichmentTest {

    @InjectMocks
    private TaskRegistrationEnrichment taskRegistrationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a mock TaskRequest
    private TaskRequest createMockTaskRequest() {
        Task task = new Task();
        task.setAmount(new Amount());
        task.setAuditDetails(new AuditDetails());
        task.setFilingNumber("FIL-123");

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTask(task);
        taskRequest.setRequestInfo(requestInfo);

        return taskRequest;
    }

    @Test
    void testEnrichTaskRegistration_Success() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();
        String mockTenantId = "FIL123";
        String mockTaskId = "TASK123";
        String mockTaskNumber = "FIL-123" + "-" + mockTaskId;

        when(configuration.getTaskConfig()).thenReturn("taskConfigValue");
        when(configuration.getTaskFormat()).thenReturn("taskFormatValue");
        when(idgenUtil.getIdList(any(), any(),any(),any(), eq(1), eq(false)))
                .thenReturn(Collections.singletonList(mockTaskId));

        // When
        taskRegistrationEnrichment.enrichTaskRegistration(taskRequest);

        // Then
        assertNotNull(taskRequest.getTask().getAuditDetails());
        assertNotNull(taskRequest.getTask().getId());
        assertEquals(mockTaskNumber, taskRequest.getTask().getTaskNumber());
        verify(idgenUtil).getIdList(any(), eq(mockTenantId), eq("taskConfigValue"), eq("taskFormatValue"), eq(1), eq(false));
    }

    @Test
    void testEnrichCaseApplicationUponUpdate_Success() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();
        taskRequest.getTask().setAuditDetails(new AuditDetails());


        // When
        taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);

        // Then
        assertNotNull(taskRequest.getTask().getAuditDetails().getLastModifiedTime());
        assertNotNull(taskRequest.getTask().getAuditDetails().getLastModifiedBy());
    }

    @Test
    void testEnrichCaseApplicationUponUpdate_NoDocuments() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();
        taskRequest.getTask().setDocuments(null); // No documents

        // When
        taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);

        // Then
        assertNotNull(taskRequest.getTask().getAuditDetails().getLastModifiedTime());
        assertNotNull(taskRequest.getTask().getAuditDetails().getLastModifiedBy());
        // Ensure no exception is thrown when documents are null
    }

    @Test
    void testEnrichCaseApplicationUponUpdate_NonNullDocumentIds() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();
        taskRequest.getTask().getDocuments().forEach(document -> document.setId(UUID.randomUUID().toString()));

        // When
        taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);

        // Then
        // Ensure no document is updated since all documents already have an ID
        taskRequest.getTask().getDocuments().forEach(document -> assertNotNull(document.getId()));
    }

    @Test
    void testEnrichCaseApplicationUponUpdate_NullDocumentIds() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();
        taskRequest.getTask().getDocuments().forEach(document -> document.setId(null));

        // When
        taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);

        // Then
        // Ensure all documents without IDs are assigned new UUIDs
        taskRequest.getTask().getDocuments().forEach(document -> {
            assertNotNull(document.getId());
            assertEquals(document.getId(), document.getDocumentUid());
        });
    }

    @Test
    void testEnrichCaseApplicationUponUpdate_ExceptionThrown() {
        // Given
        TaskRequest taskRequest = createMockTaskRequest();

        taskRequest.setTask(null);
        // When & Then
        assertThrows(CustomException.class, () -> {
            taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);
        });
    }
}
