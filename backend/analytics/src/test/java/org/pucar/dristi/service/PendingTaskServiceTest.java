package org.pucar.dristi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.Pending_Task_Exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.service.PendingTaskService;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.PendingTaskUtil;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskRequest;
import org.egov.tracer.model.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PendingTaskServiceTest {

    @InjectMocks
    private PendingTaskService pendingTaskService;

    @Mock
    private Configuration config;

    @Mock
    private IndexerUtils indexerUtils;

    @Mock
    private PendingTaskUtil pendingTaskUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePendingTask_Success() throws Exception {
        // Arrange
        PendingTaskRequest request = new PendingTaskRequest();
        PendingTask task = new PendingTask();
        request.setPendingTask(task);

        String bulkRequest = "bulkRequest";
        when(indexerUtils.buildPayload(task)).thenReturn(bulkRequest);
        doNothing().when(indexerUtils).esPostManual(anyString(), anyString());
        when(config.getEsHostUrl()).thenReturn("http://localhost:9200");
        when(config.getBulkPath()).thenReturn("/_bulk");

        // Act
        PendingTask result = pendingTaskService.createPendingTask(request);

        // Assert
        assertEquals(task, result);
        verify(indexerUtils, times(1)).buildPayload(task);
        verify(indexerUtils, times(1)).esPostManual("http://localhost:9200/_bulk", bulkRequest);
    }

    @Test
    void testCreatePendingTask_CustomException() throws Exception {
        // Arrange
        PendingTaskRequest request = new PendingTaskRequest();
        when(indexerUtils.buildPayload(any())).thenThrow(new CustomException("Custom Exception","Test Message"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> pendingTaskService.createPendingTask(request));

        assertEquals("Test Message", thrown.getMessage());
        verify(indexerUtils, times(1)).buildPayload(any());
        verify(indexerUtils, times(0)).esPostManual(anyString(), anyString());
    }

    @Test
    void testCreatePendingTask_GeneralException() throws Exception {
        // Arrange
        PendingTaskRequest request = new PendingTaskRequest();
        when(indexerUtils.buildPayload(any())).thenThrow(new RuntimeException("General Exception"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> pendingTaskService.createPendingTask(request));

        assertEquals(Pending_Task_Exception, thrown.getCode());
        assertEquals("General Exception", thrown.getMessage());
        verify(indexerUtils, times(1)).buildPayload(any());
        verify(indexerUtils, times(0)).esPostManual(anyString(), anyString());
    }
}
