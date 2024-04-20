package org.pucar.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ClerkApiController clerkApiController;

    @Mock
    private AdvocateClerkSearchRequest advocateClerkSearchRequest;

    @BeforeEach
    public void setup() {
        when(request.getHeader("Accept")).thenReturn("application/json");
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
        advocateClerkSearchRequest.setCriteria(Arrays.asList(advocateClerkSearchCriteria));
    }

//    @Test
//    public void advocateClerkV1CreatePostSuccess() throws Exception {
//
//        // Mock the behavior of your service layer
//        when(advocateClerkService.registerAdvocateRequest(any(AdvocateClerkRequest.class))).thenReturn(advocateClerkRequest.getClerks());
//        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).thenReturn(advocateClerkResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/clerk/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(new ObjectMapper().writeValueAsString(advocateClerkRequest))) // objectMapper to convert request object to JSON
//                        .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                        .andExpect(content().json(new ObjectMapper().writeValueAsString(advocateClerkRequest)));
//    }

    @Test
    public void advocateClerkV1CreatePostFailure() throws Exception {
        // Perform the request with the required Accept header
        mockMvc.perform(post("/clerk/v1/_create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(advocateClerkRequest)))
                .andExpect(status().isInternalServerError());
    }

//    @Test
//    public void advocateClerkV1SearchPostSuccess() throws Exception {
//
//        // Mock the behavior of your service layer
//        when(advocateClerkService.searchAdvocateApplications(any(RequestInfo.class),anyList())).thenReturn(advocateClerkRequest.getClerks());
//        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).thenReturn(advocateClerkResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/clerk/v1/_search")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(new ObjectMapper().writeValueAsString(advocateClerkSearchRequest))) // objectMapper to convert request object to JSON
//                .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(advocateClerkSearchRequest)));
//    }

    @Test
    public void advocateClerkV1SearchPostFailure() throws Exception {
        // Perform the request with the required Accept header
        mockMvc.perform(post("/clerk/v1/_search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(advocateClerkSearchRequest)))
                .andExpect(status().isBadRequest());
    }

}
