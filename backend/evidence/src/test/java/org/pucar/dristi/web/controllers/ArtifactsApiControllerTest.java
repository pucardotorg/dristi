<<<<<<< HEAD
package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.EvidenceService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ArtifactsApiControllerTest {

    @InjectMocks
    private ArtifactsApiController controller;

    @Mock
    private EvidenceService evidenceService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testArtifactsV1CreatePost_Success() {
        // Mock EvidenceService response
        Artifact expectedArtifact = new Artifact();
        when(evidenceService.createEvidence(any(EvidenceRequest.class)))
                .thenReturn(expectedArtifact);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock EvidenceRequest
        EvidenceRequest requestBody = new EvidenceRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<EvidenceResponse> response = controller.artifactsV1CreatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedArtifact, actualResponse.getArtifact());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1SearchPost_Success() {
        // Mock EvidenceService response
        List<Artifact> expectedArtifacts = Collections.singletonList(new Artifact());
        when(evidenceService.searchEvidence(any(RequestInfo.class), any(EvidenceSearchCriteria.class), any()))
                .thenReturn(expectedArtifacts);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock EvidenceSearchRequest
        EvidenceSearchRequest requestBody = new EvidenceSearchRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setCriteria(new EvidenceSearchCriteria());

        // Perform POST request
        ResponseEntity<EvidenceSearchResponse> response = controller.artifactsV1SearchPost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceSearchResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedArtifacts, actualResponse.getArtifacts());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1UpdatePost_Success() {
        // Mock EvidenceService response
        Artifact expectedArtifact = new Artifact();
        when(evidenceService.updateEvidence(any(EvidenceRequest.class)))
                .thenReturn(expectedArtifact);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock EvidenceRequest
        EvidenceRequest requestBody = new EvidenceRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<EvidenceResponse> response = controller.artifactsV1UpdatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedArtifact, actualResponse.getArtifact());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1CreatePost_InvalidRequest() {
        // Prepare invalid request
        EvidenceRequest requestBody = new EvidenceRequest();  // Missing required fields

        // Expected validation error
        when(evidenceService.createEvidence(any(EvidenceRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.artifactsV1CreatePost(requestBody);
        });

        // Verify exception message
        assertEquals("Invalid request", exception.getMessage());
    }

    @Test
    public void testArtifactsV1CreatePost_EmptyList() {
        // Mock service to return null
        Artifact nullArtifact = null;
        when(evidenceService.createEvidence(any(EvidenceRequest.class))).thenReturn(nullArtifact);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        EvidenceRequest requestBody = new EvidenceRequest();
        requestBody.setRequestInfo(new RequestInfo());

        // Perform POST request
        ResponseEntity<EvidenceResponse> response = controller.artifactsV1CreatePost(requestBody);

        // Verify OK status and null artifact
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertNull(actualResponse.getArtifact());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1SearchPost_InvalidRequest() {
        // Prepare invalid request with empty RequestInfo and EvidenceSearchCriteria
        EvidenceSearchRequest requestBody = new EvidenceSearchRequest();
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        requestBody.setRequestInfo(requestInfo);
        requestBody.setCriteria(criteria);

        // Expected validation error
        when(evidenceService.searchEvidence(any(RequestInfo.class), any(EvidenceSearchCriteria.class), any()))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.artifactsV1SearchPost(requestBody);
        });

        // Verify exception message
        assertEquals("Invalid request", exception.getMessage());
    }




    @Test
    public void testArtifactsV1SearchPost_EmptyList() {
        // Mock service to return empty list
        List<Artifact> emptyList = Collections.emptyList();
        when(evidenceService.searchEvidence(any(RequestInfo.class), any(EvidenceSearchCriteria.class), any()))
                .thenReturn(emptyList);

        // Mock ResponseInfoFactory
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Prepare request
        EvidenceSearchRequest requestBody = new EvidenceSearchRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setCriteria(new EvidenceSearchCriteria());

        // Perform POST request
        ResponseEntity<EvidenceSearchResponse> response = controller.artifactsV1SearchPost(requestBody);

        // Verify OK status and empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceSearchResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(emptyList, actualResponse.getArtifacts());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testArtifactsV1UpdatePost_InvalidRequest() {
        // Prepare invalid request
        EvidenceRequest requestBody = new EvidenceRequest();  // Missing required fields

        // Expected validation error
        when(evidenceService.updateEvidence(any(EvidenceRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform POST request
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.artifactsV1UpdatePost(requestBody);
        });

        // Verify exception message
        assertEquals("Invalid request", exception.getMessage());
    }

    @Test
    void applicationV1AddCommentPost_Success() {
        EvidenceAddCommentRequest requestBody = new EvidenceAddCommentRequest();
        requestBody.setRequestInfo(new RequestInfo());
        requestBody.setEvidenceAddComment(new EvidenceAddComment());

        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(expectedResponseInfo);

        EvidenceAddCommentResponse expectedResponse = EvidenceAddCommentResponse.builder()
                .evidenceAddComment(requestBody.getEvidenceAddComment())
                .responseInfo(expectedResponseInfo)
                .build();

        ResponseEntity<EvidenceAddCommentResponse> response = controller.applicationV1AddCommentPost(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        EvidenceAddCommentResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getEvidenceAddComment(), actualResponse.getEvidenceAddComment());
        assertEquals(expectedResponse.getResponseInfo(), actualResponse.getResponseInfo());
    }

    @Test
    void applicationV1AddCommentPost_InvalidRequest() {
        EvidenceAddCommentRequest requestBody = new EvidenceAddCommentRequest();  // Missing required fields

        Mockito.doThrow(new IllegalArgumentException("Invalid request")).when(evidenceService).addComments(any(EvidenceAddCommentRequest.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.applicationV1AddCommentPost(requestBody);
        });

        assertEquals("Invalid request", exception.getMessage());
    }
}
=======
//package org.pucar.dristi.web.controllers;
//
//import org.pucar.dristi.web.models.ErrorRes;
//import org.pucar.dristi.web.models.EvidenceRequest;
//import org.pucar.dristi.web.models.EvidenceResponse;
//import org.junit.Test;
//import org.junit.Ignore;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.pucar.dristi.TestConfiguration;
//
//    import java.util.ArrayList;
//    import java.util.HashMap;
//    import java.util.List;
//    import java.util.Map;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for ArtifactsApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(ArtifactsApiController.class)
//@Import(TestConfiguration.class)
//public class ArtifactsApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void artifactsV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void artifactsV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void artifactsV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void artifactsV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
>>>>>>> main
