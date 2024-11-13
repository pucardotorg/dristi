package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskApiControllerTest {

    @InjectMocks
    private TaskApiController controller;

    @Mock
    private TaskService taskService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    void setUp() {
        controller.setMockInjects(taskService, responseInfoFactory);
    }


    @Test
    void testTaskV1CreatePost_Success() {
        // Mock TaskService response
        Task task = new Task();
        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(task);

        // Mock ResponseInfoFactory response
        ResponseInfo responseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(responseInfo);

        // Create mock TaskRequest
        TaskRequest requestBody = new TaskRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setTask(task);

        // Perform POST request
        ResponseEntity<TaskResponse> response = controller.taskV1CreatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(task, actualResponse.getTask());
        assertEquals(responseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testTaskV1Exist_Success() {
        // Mock TaskService response
        TaskExists task = new TaskExists();
        when(taskService.existTask(any(TaskExistsRequest.class)))
                .thenReturn(task);

        // Mock ResponseInfoFactory response
        ResponseInfo responseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(responseInfo);

        // Create mock TaskRequest
        TaskExistsRequest requestBody = new TaskExistsRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setTask(task);

        // Perform POST request
        ResponseEntity<TaskExistsResponse> response = controller.taskV1ExistsPost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskExistsResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(task, actualResponse.getTask());
        assertEquals(responseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testTaskV1SearchPost_Success() {

        TaskSearchRequest TaskSearchRequest = new TaskSearchRequest();
        TaskSearchRequest.setCriteria(TaskCriteria.builder().build());
        TaskSearchRequest.setRequestInfo(new RequestInfo());

        // Mock TaskService response
        List<Task> expectedTasks = Collections.singletonList(new Task());
        when(taskService.searchTask(any()))
                .thenReturn(expectedTasks);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<TaskListResponse> response = controller.taskV1SearchPost(TaskSearchRequest);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testTaskV1UpdatePost_Success() throws Exception {
        // Mock TaskService response
        when(taskService.updateTask(any(TaskRequest.class)))
                .thenReturn(new Task());

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock TaskRequest
        TaskRequest requestBody = new TaskRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<TaskResponse> response = controller.taskV1UpdatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testTaskV1CreatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        TaskRequest requestBody = new TaskRequest();  // Missing required fields

        // Expected validation error
        when(taskService.createTask(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.taskV1CreatePost(requestBody);
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    void testTaskV1CreatePost_EmptyList() throws Exception {
        // Mock service to return empty list
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(new Task());

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        TaskRequest requestBody = new TaskRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<TaskResponse> response = controller.taskV1CreatePost(requestBody);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
    }

    @Test
    void testTaskV1SearchPost_InvalidRequest() throws Exception {

        // Expected validation error
        when(taskService.searchTask(any())).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.taskV1SearchPost(new TaskSearchRequest());
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    void testTaskV1SearchPost_EmptyList() throws Exception {
        TaskSearchRequest TaskSearchRequest = new TaskSearchRequest();
        TaskSearchRequest.setCriteria(TaskCriteria.builder().build());
        TaskSearchRequest.setRequestInfo(new RequestInfo());

        // Mock service to return empty list
        List<Task> emptyList = Collections.emptyList();
        when(taskService.searchTask(any())).thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<TaskListResponse> response = controller.taskV1SearchPost(TaskSearchRequest);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
    }

    @Test
    void testTaskV1UpdatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        TaskRequest requestBody = new TaskRequest();  // Missing required fields

        // Expected validation error
        when(taskService.updateTask(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.taskV1UpdatePost(requestBody);
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

}
