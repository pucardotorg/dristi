package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.PendingTaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.controllers.PendingTaskApiController;
import org.pucar.dristi.web.models.*;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskRequest;
import org.pucar.dristi.web.models.PendingTaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PendingTaskApiControllerTest {

    @InjectMocks
    private PendingTaskApiController controller;

    @Mock
    private PendingTaskService pendingTaskService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPendingTaskV1CreatePost_Success() {
        // Mock PendingTaskService response
        org.pucar.dristi.web.models.PendingTask expectedTask = new org.pucar.dristi.web.models.PendingTask();
        when(pendingTaskService.createPendingTask(any(org.pucar.dristi.web.models.PendingTaskRequest.class)))
                .thenReturn(expectedTask);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean()))
                .thenReturn(expectedResponseInfo);

        // Create mock PendingTaskRequest
        org.pucar.dristi.web.models.PendingTaskRequest requestBody = new org.pucar.dristi.web.models.PendingTaskRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<org.pucar.dristi.web.models.PendingTaskResponse> response = controller.pendingTaskV1CreatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        org.pucar.dristi.web.models.PendingTaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedTask, actualResponse.getPendingTask());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    void testPendingTaskV1CreatePost_InvalidRequest() {
        // Prepare invalid request
        org.pucar.dristi.web.models.PendingTaskRequest requestBody = new org.pucar.dristi.web.models.PendingTaskRequest();  // Missing required fields

        // Expected validation error
        when(pendingTaskService.createPendingTask(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        try {
            controller.pendingTaskV1CreatePost(requestBody);
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    void testPendingTaskV1CreatePost_EmptyList() {
        // Mock service to return an empty list
        when(pendingTaskService.createPendingTask(any(org.pucar.dristi.web.models.PendingTaskRequest.class))).thenReturn(new org.pucar.dristi.web.models.PendingTask());

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean()))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        org.pucar.dristi.web.models.PendingTaskRequest requestBody = new org.pucar.dristi.web.models.PendingTaskRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<org.pucar.dristi.web.models.PendingTaskResponse> response = controller.pendingTaskV1CreatePost(requestBody);

        // Verify OK status and response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        org.pucar.dristi.web.models.PendingTaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(new org.pucar.dristi.web.models.PendingTask(), actualResponse.getPendingTask());
    }

    @Test
    void testPendingTaskV1CreatePost_Neutral() {
        // Create a neutral request with some optional fields missing
        org.pucar.dristi.web.models.PendingTaskRequest neutralPendingTaskRequest = new org.pucar.dristi.web.models.PendingTaskRequest();
        neutralPendingTaskRequest.setRequestInfo(new RequestInfo());
        // Add more fields if necessary, leaving some optional fields empty

        // Mock PendingTaskService response
        org.pucar.dristi.web.models.PendingTask expectedTask = new PendingTask();
        when(pendingTaskService.createPendingTask(any(PendingTaskRequest.class)))
                .thenReturn(expectedTask);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean()))
                .thenReturn(expectedResponseInfo);

        // Perform POST request
        ResponseEntity<org.pucar.dristi.web.models.PendingTaskResponse> response = controller.pendingTaskV1CreatePost(neutralPendingTaskRequest);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PendingTaskResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedTask, actualResponse.getPendingTask());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }
}
