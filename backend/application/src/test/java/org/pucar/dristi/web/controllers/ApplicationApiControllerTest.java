package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.ApplicationService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
* API tests for ApplicationApiController
*/
@ExtendWith(MockitoExtension.class)
class ApplicationApiControllerTest {
    @Mock
    private ApplicationService applicationService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private ApplicationApiController controller;
//
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplicationsV1CreatePost_Success() {
        Application expectedApplication = new Application();
        when(applicationService.createApplication(any(ApplicationRequest.class)))
                .thenReturn(expectedApplication);

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        ApplicationRequest requestBody = new ApplicationRequest();
        requestBody.setRequestInfo(new RequestInfo());

        ResponseEntity<ApplicationResponse> response = controller.applicationV1CreatePost(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedApplication, actualResponse.getApplication());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testApplicationV1SearchPost_Success() {
        // Arrange
        List<Application> expectedApplication = Collections.singletonList(new Application());
        when(applicationService.searchApplications(any(ApplicationSearchRequest.class)))
                .thenReturn(expectedApplication);
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true)))
                .thenReturn(expectedResponseInfo);
        ApplicationSearchRequest requestInfoBody = new ApplicationSearchRequest();
        requestInfoBody.setCriteria(new ApplicationCriteria());
        requestInfoBody.setRequestInfo(new RequestInfo());

        // Act
        ResponseEntity<ApplicationListResponse> response = controller.applicationV1SearchPost(requestInfoBody);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedApplication, actualResponse.getApplicationList());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }
    @Test
    public void testArtifactsV1UpdatePost_Success() {
        Application expectedApplication = new Application();
        when(applicationService.updateApplication(any(ApplicationRequest.class),any(Boolean.class)))
                .thenReturn(expectedApplication);

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        ApplicationRequest requestBody = new ApplicationRequest();
        requestBody.setRequestInfo(new RequestInfo());


        ResponseEntity<ApplicationResponse> response = controller.applicationV1UpdatePost(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedApplication, actualResponse.getApplication());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }
    @Test
    public void testApplicationV1CreatePost_InvalidRequest() {
        ApplicationRequest requestBody = new ApplicationRequest();  // Missing required fields

        when(applicationService.createApplication(any(ApplicationRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.applicationV1CreatePost(requestBody);
        });

        assertEquals("Invalid request", exception.getMessage());
    }

    @Test
    public void testApplicationV1CreatePost_EmptyList() {
        Application nullApplication = null;
        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(nullApplication);

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        ApplicationRequest requestBody = new ApplicationRequest();
        requestBody.setRequestInfo(new RequestInfo());

        ResponseEntity<ApplicationResponse> response = controller.applicationV1CreatePost(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertNull(actualResponse.getApplication());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1SearchPost_InvalidRequest() {

        when(applicationService.searchApplications(any(ApplicationSearchRequest.class))).thenThrow(new CustomException("Invalid request", "The request parameters did not meet the expected format."));

        ApplicationSearchRequest requestInfoBody = new ApplicationSearchRequest();
        requestInfoBody.setCriteria(new ApplicationCriteria());
        requestInfoBody.setRequestInfo(new RequestInfo());

        Exception exception = assertThrows(CustomException.class, () -> {
            controller.applicationV1SearchPost(requestInfoBody);
        });

        assertEquals("The request parameters did not meet the expected format.", exception.getMessage());
    }

    @Test
    public void testApplicationV1SearchPost_EmptyList() {
        List<Application> emptyList = Collections.emptyList();
        when(applicationService.searchApplications(any(ApplicationSearchRequest.class)))
                .thenReturn(emptyList);

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any()))
                .thenReturn(expectedResponseInfo);

        ApplicationSearchRequest requestInfoBody = new ApplicationSearchRequest();
        requestInfoBody.setCriteria(new ApplicationCriteria());
        requestInfoBody.setRequestInfo(new RequestInfo());

        ResponseEntity<ApplicationListResponse> response = controller.applicationV1SearchPost(requestInfoBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationListResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(emptyList, actualResponse.getApplicationList());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }
//
    @Test
    public void testArtifactsV1UpdatePost_InvalidRequest() {
        ApplicationRequest requestBody = new ApplicationRequest();  // Missing required fields
        when(applicationService.updateApplication(any(ApplicationRequest.class),any(Boolean.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.applicationV1UpdatePost(requestBody);
        });

        assertEquals("Invalid request", exception.getMessage());
    }

    @Test
    public void applicationV1ExistsPostSuccess() throws Exception {
        List<ApplicationExists> expectedApplicationExists = Collections.singletonList(new ApplicationExists());
        when(applicationService.existsApplication(any(ApplicationExistsRequest.class)))
                .thenReturn(expectedApplicationExists);

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any()))
                .thenReturn(expectedResponseInfo);

        ApplicationExistsRequest request = new ApplicationExistsRequest();
        request.setRequestInfo(new RequestInfo());
        ResponseEntity<ApplicationExistsResponse> response = controller.applicationV1ExistsPost(request);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationExistsResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedApplicationExists, actualResponse.getApplicationExists());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void applicationV1ExistsPost_InvalidRequest() throws Exception {
        ApplicationExistsRequest requestBody = new ApplicationExistsRequest();
        when(applicationService.existsApplication(any(ApplicationExistsRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.applicationV1ExistsPost(requestBody);
        });

        // Verify exception message
        assertEquals("Invalid request", exception.getMessage());
    }

    @Test
    void applicationV1AddCommentPost_Success() {
        ApplicationAddCommentRequest requestBody = new ApplicationAddCommentRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setApplicationAddComment(new ApplicationAddComment());

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(expectedResponseInfo);

        ApplicationAddCommentResponse expectedResponse = ApplicationAddCommentResponse.builder()
                .applicationAddComment(requestBody.getApplicationAddComment())
                .responseInfo(expectedResponseInfo)
                .build();

        ResponseEntity<ApplicationAddCommentResponse> response = controller.applicationV1AddCommentPost(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApplicationAddCommentResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getApplicationAddComment(), actualResponse.getApplicationAddComment());
        assertEquals(expectedResponse.getResponseInfo(), actualResponse.getResponseInfo());
    }

    @Test
    void applicationV1AddCommentPost_InvalidRequest() {
        ApplicationAddCommentRequest requestBody = new ApplicationAddCommentRequest();  // Missing required fields

        Mockito.doThrow(new IllegalArgumentException("Invalid request")).when(applicationService).addComments(any(ApplicationAddCommentRequest.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.applicationV1AddCommentPost(requestBody);
        });

        assertEquals("Invalid request", exception.getMessage());
    }

}
