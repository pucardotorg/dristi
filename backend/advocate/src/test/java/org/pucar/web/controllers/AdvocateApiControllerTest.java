package org.pucar.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.service.AdvocateService;
import org.pucar.util.ResponseInfoFactory;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateResponse;
import org.pucar.web.models.AdvocateSearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvocateApiControllerTest {

    @InjectMocks
    private AdvocateApiController controller;

    @Mock
    private AdvocateService advocateService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Test
    public void testAdvocateV1CreatePost_Success() {
        // Mock AdvocateService response
        List<Advocate> expectedAdvocates = Collections.singletonList(new Advocate());
        when(advocateService.createAdvocate(any(AdvocateRequest.class)))
                .thenReturn(expectedAdvocates);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock AdvocateRequest
        AdvocateRequest requestBody = new AdvocateRequest();
        requestBody.setRequestInfo(new RequestInfo());

        controller.setMockInjects(advocateService, responseInfoFactory);
        // Perform POST request
        ResponseEntity<AdvocateResponse> response = controller.advocateV1CreatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedAdvocates, actualResponse.getAdvocates());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testAdvocateV1SearchPost_Success() {
        // Mock AdvocateService response
        List<Advocate> expectedAdvocates = Collections.singletonList(new Advocate());
        when(advocateService.searchAdvocate(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedAdvocates);
        controller.setMockInjects(advocateService, responseInfoFactory);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock AdvocateSearchRequest
        AdvocateSearchRequest requestBody = new AdvocateSearchRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<AdvocateResponse> response = controller.advocateV1SearchPost(requestBody,1,1);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedAdvocates, actualResponse.getAdvocates());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testAdvocateV1UpdatePost_Success() throws Exception {
        // Mock AdvocateService response
        List<Advocate> expectedAdvocates = Collections.singletonList(new Advocate());
        when(advocateService.updateAdvocate(any(AdvocateRequest.class)))
                .thenReturn(expectedAdvocates);
        controller.setMockInjects(advocateService, responseInfoFactory);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock AdvocateRequest
        AdvocateRequest requestBody = new AdvocateRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<AdvocateResponse> response = controller.advocateV1UpdatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedAdvocates, actualResponse.getAdvocates());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testAdvocateV1CreatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        AdvocateRequest requestBody = new AdvocateRequest();  // Missing required fields

        // Expected validation error
        when(advocateService.createAdvocate(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        controller.setMockInjects(advocateService, responseInfoFactory);

        // Perform POST request
        try{
            controller.advocateV1CreatePost(requestBody);
        }
        catch (Exception e){
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Invalid request", e.getMessage());
        }
    }
    @Test
    public void testAdvocateV1CreatePost_EmptyList() throws Exception {
        // Mock service to return empty list
        List<Advocate> emptyList = Collections.emptyList();
        when(advocateService.createAdvocate(any(AdvocateRequest.class))).thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        AdvocateRequest requestBody = new AdvocateRequest();
        requestBody.setRequestInfo(new RequestInfo());

        controller.setMockInjects(advocateService, responseInfoFactory);

        // Perform POST request
        ResponseEntity<AdvocateResponse> response = controller.advocateV1CreatePost(requestBody);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(emptyList, actualResponse.getAdvocates());
    }
    @Test
    public void testAdvocateV1SearchPost_InvalidRequest() throws Exception {
        // Prepare invalid request
        AdvocateSearchRequest requestBody = new AdvocateSearchRequest();  // Missing required fields

        // Expected validation error
        when(advocateService.searchAdvocate(any(), any(), any(), any(), any(), any(), any())).thenThrow(new IllegalArgumentException("Invalid request"));

        controller.setMockInjects(advocateService, responseInfoFactory);

        // Perform POST request
        try {
            controller.advocateV1SearchPost(requestBody,1,1);
        }
        catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Invalid request", e.getMessage());
        }
    }

    @Test
    public void testAdvocateV1SearchPost_EmptyList() throws Exception {
        // Mock service to return empty list
        List<Advocate> emptyList = Collections.emptyList();
        when(advocateService.searchAdvocate(any(), any(), any(), any(), any(), any(), any())).thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        AdvocateSearchRequest requestBody = new AdvocateSearchRequest();
        requestBody.setRequestInfo(new RequestInfo());

        controller.setMockInjects(advocateService, responseInfoFactory);

        // Perform POST request
        ResponseEntity<AdvocateResponse> response = controller.advocateV1SearchPost(requestBody,1,1);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(emptyList, actualResponse.getAdvocates());
    }

    @Test
    public void testAdvocateV1UpdatePost_InvalidRequest() throws Exception {
        // Prepare invalid request
        AdvocateRequest requestBody = new AdvocateRequest();  // Missing required fields

        // Expected validation error
        when(advocateService.updateAdvocate(requestBody)).thenThrow(new IllegalArgumentException("Invalid request"));

        controller.setMockInjects(advocateService, responseInfoFactory);

        // Perform POST request
        try {
            controller.advocateV1UpdatePost(requestBody);
        }
        catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Invalid request", e.getMessage());
        }
    }
}
