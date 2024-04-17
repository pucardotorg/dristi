//package org.pucar.web.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.egov.common.contract.request.RequestInfo;
//import org.egov.common.contract.response.ResponseInfo;
//import org.junit.jupiter.api.BeforeEach;
//import org.pucar.service.AdvocateService;
//import org.pucar.util.ResponseInfoFactory;
//import org.pucar.web.models.Advocate;
//import org.pucar.web.models.AdvocateRequest;
//import org.pucar.web.models.AdvocateResponse;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.junit.jupiter.api.Test;
//import org.pucar.TestConfiguration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for AdvocateApiController
//*/
//@WebMvcTest(AdvocateApiController.class)
//@Import(TestConfiguration.class)
//public class AdvocateApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AdvocateService advocateRegistrationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ResponseInfoFactory responseInfoFactory;
//
//    private AdvocateRequest advocateRequest;
//    private AdvocateResponse advocateResponse;
//
//    @BeforeEach
//    public void setup() {
//        RequestInfo requestInfo = new RequestInfo();
//        requestInfo.setApiId("apiId");
//
//        ResponseInfo responseInfo = new ResponseInfo();
//        responseInfo.setApiId("apiId");
//
//        List<Advocate> advocateList = new ArrayList<>();
//        Advocate advocate = new Advocate();
//        advocate.setId(UUID.randomUUID());
//        advocate.setTenantId("tenantId");
//        advocateList.add(advocate);
//
//        advocateRequest = new AdvocateRequest();
//        advocateRequest = AdvocateRequest.builder()
//                .advocates(advocateList)
//                .requestInfo(requestInfo)
//                .build();
//        advocateResponse = new AdvocateResponse();
//        advocateResponse = AdvocateResponse.builder()
//                .advocates(advocateList)
//                .responseInfo(responseInfo)
//                .build();
//    }
//    @Test
//    public void advocateV1CreatePostSuccess() throws Exception {
//        // Mock the behavior of your service layer
//        given(advocateRegistrationService.createAdvocate(any(AdvocateRequest.class))).willReturn(advocateRequest.getAdvocates());
//        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(objectMapper.writeValueAsString(advocateRequest))) // objectMapper to convert request object to JSON
//                .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                .andExpect(content().json(objectMapper.writeValueAsString(advocateResponse)));
//    }
//
//    @Test
//    public void advocateV1CreatePostFailure() throws Exception {
//        // Mock the behavior of your service layer
//        given(advocateRegistrationService.createAdvocate(any(AdvocateRequest.class))).willReturn(advocateRequest.getAdvocates());
//        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(advocateRequest)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void advocateV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void advocateV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void advocateV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void advocateV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
////
////import static org.junit.Assert.assertEquals;
////import static org.junit.Assert.assertNotNull;
////import static org.mockito.Mockito.any;
////import static org.mockito.Mockito.doThrow;
////import static org.mockito.Mockito.mock;
////import static org.mockito.Mockito.when;
////
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.List;
////
////import org.egov.common.contract.response.ResponseInfo;
////import org.junit.Test;
////import org.mockito.Mockito;
////import org.pucar.service.AdvocateService;
////import org.pucar.util.ResponseInfoFactory;
////import org.pucar.web.controllers.AdvocateApiController;
////import org.pucar.web.models.Advocate;
////import org.pucar.web.models.AdvocateResponse;
////import org.pucar.web.models.AdvocateSearchRequest;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.validation.Valid;
////
////public class AdvocateApiControllerTest {
////
////    @Test
////    public void testAdvocateV1SearchPost_Success() throws IOException {
////        // Mock AdvocateService
////        AdvocateService mockAdvocateService = mock(AdvocateService.class);
////        List<Advocate> expectedAdvocates = new ArrayList<>();
////        expectedAdvocates.add(new Advocate());
////        AdvocateResponse expectedResponse = new AdvocateResponse(expectedAdvocates, new ResponseInfo());
////        Mockito.when(mockAdvocateService.searchAdvocate(Mockito.any(), Mockito.any())).thenReturn(expectedAdvocates);
////
////        // Create AdvocateApiController with mocked dependency
////        AdvocateApiController controller = new AdvocateApiController(new ObjectMapper(), mock(HttpServletRequest.class));
////        controller.advocateService = mockAdvocateService;
////
////        // Create AdvocateSearchRequest object
////        AdvocateSearchRequest request = new AdvocateSearchRequest();
////        request.setRequestInfo(new ResponseInfo());
////
////        // Set request headers to simulate JSON acceptance
////        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
////        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
////        controller.request = mockRequest;
////
////        // Call the API method
////        ResponseEntity<AdvocateResponse> response = controller.advocateV1SearchPost(request);
////
////        // Verify response status and content
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        AdvocateResponse actualResponse = response.getBody();
////        assertNotNull(actualResponse);
////        assertEquals(expectedAdvocates, actualResponse.getAdvocates());
////        assertNotNull(actualResponse.getResponseInfo());
////    }
////
////    @Test
////    public void testAdvocateV1SearchPost_Exception() throws IOException {
////        // Mock AdvocateService
////        AdvocateService mockAdvocateService = mock(AdvocateService.class);
////        doThrow(new RuntimeException()).when(mockAdvocateService).searchAdvocates(any(AdvocateSearchRequest.class));
////
////        // Create AdvocateApiController with mocked dependency
////        AdvocateApiController controller = new AdvocateApiController(new ObjectMapper(), mock(HttpServletRequest.class));
////        controller.advocateService = mockAdvocateService;
////
////        // Create AdvocateSearchRequest object
////        AdvocateSearchRequest request = new AdvocateSearchRequest();
////        request.setRequestInfo(new ResponseInfo());
////
////        // Set request headers to simulate JSON acceptance
////        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
////        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
////        controller.request = mockRequest;
////
////        // Call the API method
////        ResponseEntity<AdvocateResponse> response = controller.advocateV1SearchPost(request);
////
////        // Verify response status
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////    }
////
////    @Test
////    public void testAdvocateV1SearchPost_Not Implemented() throws IOException {
////        // Create AdvocateApiController with mocked dependencies
////        AdvocateApiController controller = new AdvocateApiController(new ObjectMapper(), mock(HttpServletRequest.class));
////
////        // Create AdvocateSearchRequest object
////        AdvocateSearchRequest request = new AdvocateSearchRequest();
////        request.setRequestInfo(new ResponseInfo());
////
////        // Set request headers to simulate non-JSON acceptance
////        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
////        when(mockRequest.getHeader("Accept")).thenReturn("text/plain");
////        controller.request = mockRequest;
////
////        // Call the API method
////        ResponseEntity<AdvocateResponse> response = controller.advocateV1SearchPost(request);
////
////        // Verify response status
////        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
////    }
////}
