package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.TaskRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.WorkflowUtil;
import org.pucar.dristi.validators.TaskRegistrationValidator;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskExists;
import org.pucar.dristi.web.models.TaskExistsRequest;
import org.pucar.dristi.web.models.TaskRequest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRegistrationValidator validator;

    @Mock
    private TaskRegistrationEnrichment enrichmentUtil;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkflowUtil workflowUtil;

    @Mock
    private Configuration config;

    @Mock
    private Producer producer;

    @InjectMocks
    private TaskService taskService;

    private TaskRequest taskRequest;
    private Task task;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTenantId("tenant-id");
        task.setCnrNumber("cnr-number");
        task.setTaskNumber("task-number");
        task.setTaskType("doc");

        requestInfo = RequestInfo.builder().build();

        taskRequest = new TaskRequest();
        taskRequest.setTask(task);
        taskRequest.setRequestInfo(requestInfo);
    }

    @Test
    void testCreateTaskSuccess() {
        when(config.getTaskBusinessServiceName()).thenReturn("task-business-service");
        when(config.getTaskBusinessName()).thenReturn("task-business-name");
        when(config.getTaskCreateTopic()).thenReturn("task-create-topic");

        doNothing().when(validator).validateTaskRegistration(any(TaskRequest.class));
        doNothing().when(enrichmentUtil).enrichTaskRegistration(any(TaskRequest.class));

        Task result = taskService.createTask(taskRequest);

        assertEquals(task, result);
        verify(validator, times(1)).validateTaskRegistration(any(TaskRequest.class));
        verify(enrichmentUtil, times(1)).enrichTaskRegistration(any(TaskRequest.class));
        verify(workflowUtil, times(1)).updateWorkflowStatus(any(RequestInfo.class), anyString(), anyString(), anyString(), any(), anyString());
        verify(producer, times(1)).push(anyString(), any(TaskRequest.class));
    }

    @Test
    void testCreateTaskThrowsException() {
        doThrow(new CustomException(CREATE_TASK_ERR, "Error")).when(validator).validateTaskRegistration(any(TaskRequest.class));

        CustomException exception = assertThrows(CustomException.class, () -> taskService.createTask(taskRequest));

        assertEquals(CREATE_TASK_ERR, exception.getCode());
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void testSearchTaskSuccess() {
        when(taskRepository.getApplications(anyString(), anyString(), anyString(), any(UUID.class), anyString(),anyString())).thenReturn(Collections.singletonList(task));

        List<Task> result = taskService.searchTask("id", "tenant-id", "status", UUID.randomUUID(), "cnr-number", "task-number",requestInfo);

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).getApplications(anyString(), anyString(), anyString(), any(UUID.class), anyString(),anyString());
    }

    @Test
    void testSearchTaskThrowsException() {
        when(taskRepository.getApplications(anyString(), anyString(), anyString(), any(UUID.class), anyString(),anyString())).thenThrow(new CustomException(SEARCH_TASK_ERR, "Error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskService.searchTask("id", "tenant-id", "status", UUID.randomUUID(), "cnr-number", "task-number",requestInfo));

        assertEquals(SEARCH_TASK_ERR, exception.getCode());
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void testUpdateTaskSuccess() {
        when(validator.validateApplicationExistence(any(Task.class), any(RequestInfo.class))).thenReturn(true);
        when(config.getTaskBusinessServiceName()).thenReturn("task-business-service");
        when(config.getTaskBusinessName()).thenReturn("task-business-name");
        when(config.getTaskUpdateTopic()).thenReturn("task-update-topic");

        doNothing().when(enrichmentUtil).enrichCaseApplicationUponUpdate(any(TaskRequest.class));

        Task result = taskService.updateTask(taskRequest);

        assertEquals(task, result);
        verify(validator, times(1)).validateApplicationExistence(any(Task.class), any(RequestInfo.class));
        verify(enrichmentUtil, times(1)).enrichCaseApplicationUponUpdate(any(TaskRequest.class));
        verify(workflowUtil, times(1)).updateWorkflowStatus(any(RequestInfo.class), anyString(), anyString(), anyString(), any(), anyString());
        verify(producer, times(1)).push(anyString(), any(TaskRequest.class));
    }

    @Test
    void testUpdateTaskThrowsException() {
        when(validator.validateApplicationExistence(any(Task.class), any(RequestInfo.class))).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> taskService.updateTask(taskRequest));

        assertEquals(VALIDATION_ERR, exception.getCode());
        assertEquals("Task Application does not exist", exception.getMessage());
    }

    @Test
    void testExistTaskSuccess() {
        TaskExists taskExists = new TaskExists();
        when(taskRepository.checkTaskExists(any(TaskExists.class))).thenReturn(taskExists);

        TaskExistsRequest taskExistsRequest = new TaskExistsRequest();
        taskExistsRequest.setTask(new TaskExists());

        TaskExists result = taskService.existTask(taskExistsRequest);

        assertEquals(taskExists, result);
        verify(taskRepository, times(1)).checkTaskExists(any(TaskExists.class));
    }

    @Test
    void testExistTaskThrowsException() {
        when(taskRepository.checkTaskExists(any(TaskExists.class))).thenThrow(new CustomException(EXIST_TASK_ERR, "Error"));

        TaskExistsRequest taskExistsRequest = new TaskExistsRequest();
        taskExistsRequest.setTask(new TaskExists());

        CustomException exception = assertThrows(CustomException.class, () -> taskService.existTask(taskExistsRequest));

        assertEquals(EXIST_TASK_ERR, exception.getCode());
        assertEquals("Error", exception.getMessage());
    }
}
