package org.pucar.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pucar.service.AdvocateService;
import org.pucar.util.ResponseInfoFactory;
import org.pucar.web.models.*;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AdvocateApiControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private AdvocateService advocateService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AdvocateRequest advocateRequest;

    @Mock
    private AdvocateResponse advocateResponse;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AdvocateSearchRequest advocateSearchRequest;

    @InjectMocks
    private AdvocateApiController advocateApiController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(advocateApiController).build();


        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId("apiId");

        List<Advocate> advocateList = new ArrayList<>();
        Advocate advocate = new Advocate();
        advocate.setId(UUID.randomUUID());
        advocate.setTenantId("tenantId");
        advocateList.add(advocate);

        advocateRequest = new AdvocateRequest();
        advocateRequest = AdvocateRequest.builder().advocates(advocateList).requestInfo(requestInfo).build();
        advocateResponse = new AdvocateResponse();
        advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();

        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("applicationNumber");
        advocateSearchCriteria.setBarRegistrationNumber("1234");

        advocateSearchRequest.setRequestInfo(requestInfo);
        advocateSearchRequest.setCriteria(Arrays.asList(advocateSearchCriteria));

    }

//    @Test
//    public void advocateV1CreatePostSuccess() throws Exception {
//
//        // Mock the behavior of your service layer
//        when(advocateService.createAdvocate(any(AdvocateRequest.class))).thenReturn(advocateRequest.getAdvocates());
//        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).thenReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(new ObjectMapper().writeValueAsString(advocateRequest))) // objectMapper to convert request object to JSON
//                        .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                        .andExpect(content().json(new ObjectMapper().writeValueAsString(advocateRequest)));
//    }

//    @Test
//    public void advocateV1CreatePostFailure() throws Exception {
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(advocateRequest)))
//                        .andExpect(status().isInternalServerError());
//    }

//    @Test
//    public void advocateV1SearchPostSuccess() throws Exception {
//
//        // Mock the behavior of your service layer
//        when(advocateService.searchAdvocate(any(RequestInfo.class),anyList())).thenReturn(advocateRequest.getAdvocates());
//        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).thenReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_search")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(new ObjectMapper().writeValueAsString(advocateSearchRequest))) // objectMapper to convert request object to JSON
//                .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(advocateSearchRequest)));
//    }

//    @Test
//    public void advocateV1SearchPostFailure() throws Exception {
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_search")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(advocateSearchRequest)))
//                .andExpect(status().isInternalServerError());
//    }
}
