package org.pucar.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.service.AdvocateClerkService;
import org.pucar.util.ResponseInfoFactory;
import org.pucar.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClerkApiControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private AdvocateClerkService advocateClerkService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AdvocateClerkRequest advocateClerkRequest;

    @Mock
    private AdvocateClerkResponse advocateClerkResponse;

    @InjectMocks
    private ClerkApiController clerkApiController;

    @Mock
    private AdvocateClerkSearchRequest advocateClerkSearchRequest;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clerkApiController).build();


        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId("apiId");

        List<AdvocateClerk> advocateList = new ArrayList<>();
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setId(UUID.randomUUID());
        advocate.setTenantId("tenantId");
        advocateList.add(advocate);

        advocateClerkRequest = new AdvocateClerkRequest();
        advocateClerkRequest = AdvocateClerkRequest.builder().clerks(advocateList).requestInfo(requestInfo).build();
        advocateClerkResponse = new AdvocateClerkResponse();
        advocateClerkResponse = AdvocateClerkResponse.builder().clerks(advocateList).responseInfo(responseInfo).build();

        AdvocateClerkSearchCriteria advocateClerkSearchCriteria = new AdvocateClerkSearchCriteria();
        advocateClerkSearchCriteria.setId("id");
        advocateClerkSearchCriteria.setApplicationNumber("applicationNumber");
        advocateClerkSearchCriteria.setStateRegnNumber("1234");

        advocateClerkSearchRequest.setRequestInfo(requestInfo);
        advocateClerkSearchRequest.setCriteria(List.of(advocateClerkSearchCriteria));
    }

    @Test
    public void advocateClerkV1CreatePostSuccess() throws Exception {

        // Mock the behavior of your service layer
        when(advocateClerkService.registerAdvocateClerkRequest(any(AdvocateClerkRequest.class))).thenReturn(advocateClerkRequest.getClerks());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).thenReturn(advocateClerkResponse.getResponseInfo());

        clerkApiController.setMockInjects(advocateClerkService,responseInfoFactory);

        // Perform the request with the required Accept header
        mockMvc.perform(post("/clerk/v1/_create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
                        .content(new ObjectMapper().writeValueAsString(advocateClerkRequest))) // objectMapper to convert request object to JSON
                        .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
                        .andExpect(content().json(new ObjectMapper().writeValueAsString(advocateClerkResponse)));
    }

    @Test
    public void advocateClerkV1CreatePost_InvalidRequest() {
        // Prepare invalid request (missing required fields)
        AdvocateClerkRequest requestBody = new AdvocateClerkRequest();

        // Expected validation error
        when(advocateClerkService.registerAdvocateClerkRequest(any(AdvocateClerkRequest.class)))
                .thenThrow(new IllegalArgumentException());

        clerkApiController.setMockInjects(advocateClerkService, responseInfoFactory);

        // Perform POST request
        try {
            mockMvc.perform(post("/clerk/v1/_create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestBody)))
                    .andExpect(status().isBadRequest());  // Expect bad request status

        }
        catch (Exception e){
            assertTrue(e instanceof ServletException);
        }
    }
    @Test
    public void testClerkV1SearchPost_Success() {
        // Mock AdvocateClerkService response
        List<AdvocateClerk> expectedClerks = Collections.singletonList(new AdvocateClerk());
        when(advocateClerkService.searchAdvocateClerkApplications(any(), any(), any(), any(), any(),any(),any()))
                .thenReturn(expectedClerks);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock AdvocateClerkSearchRequest
        AdvocateClerkSearchRequest requestBody = new AdvocateClerkSearchRequest();
        requestBody.setRequestInfo(new RequestInfo());

        clerkApiController.setMockInjects(advocateClerkService, responseInfoFactory);

        // Perform POST request
        ResponseEntity<AdvocateClerkResponse> response = clerkApiController.clerkV1SearchPost(requestBody,1,1);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateClerkResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedClerks, actualResponse.getClerks());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testClerkV1SearchPost_InvalidRequest() {
        // Prepare invalid request (missing required fields)
        AdvocateClerkSearchRequest requestBody = new AdvocateClerkSearchRequest();

        // Expected validation error
        when(advocateClerkService.searchAdvocateClerkApplications(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        clerkApiController.setMockInjects(advocateClerkService, responseInfoFactory);

        // Perform POST request
        try {
            mockMvc.perform(post("/clerk/v1/_search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestBody)))
                    .andExpect(status().isBadRequest());  // Expect bad request status
        }
        catch (Exception e){
            assertTrue(e instanceof ServletException);
        }
    }
    @Test
    public void testClerkV1UpdatePost_Success() {
        // Mock AdvocateClerkService response
        List<AdvocateClerk> expectedClerks = Collections.singletonList(new AdvocateClerk());
        when(advocateClerkService.updateAdvocateClerk(any(AdvocateClerkRequest.class)))
                .thenReturn(expectedClerks);

        // Mock ResponseInfoFactory response
        ResponseInfo expectedResponseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
                .thenReturn(expectedResponseInfo);

        // Create mock AdvocateClerkRequest
        AdvocateClerkRequest requestBody = new AdvocateClerkRequest();
        requestBody.setRequestInfo(new RequestInfo());

        clerkApiController.setMockInjects(advocateClerkService, responseInfoFactory);

        // Perform POST request
        ResponseEntity<AdvocateClerkResponse> response = clerkApiController.clerkV1UpdatePost(requestBody);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdvocateClerkResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals(expectedClerks, actualResponse.getClerks());
        assertEquals(expectedResponseInfo, actualResponse.getResponseInfo());
    }

    @Test
    public void testClerkV1UpdatePost_InvalidRequest() {
        // Prepare invalid request (missing required fields)
        AdvocateClerkRequest requestBody = new AdvocateClerkRequest();

        // Expected validation error
        when(advocateClerkService.updateAdvocateClerk(any(AdvocateClerkRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        clerkApiController.setMockInjects(advocateClerkService, responseInfoFactory);

        // Perform POST request
        try{
            mockMvc.perform(post("/clerk/v1/_update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestBody)))
                    .andExpect(status().isBadRequest());  // Expect bad request status
        }
        catch (Exception e){
            assertTrue(e instanceof ServletException);
        }
    }
}
