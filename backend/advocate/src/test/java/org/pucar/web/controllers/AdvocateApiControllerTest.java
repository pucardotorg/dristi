//package org.pucar.web.controllers;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import org.egov.common.contract.request.RequestInfo;
//import org.egov.common.contract.response.ResponseInfo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.pucar.service.AdvocateService;
//import org.pucar.util.ResponseInfoFactory;
//import org.pucar.web.models.Advocate;
//import org.pucar.web.models.AdvocateRequest;
//import org.pucar.web.models.AdvocateResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//public class AdvocateApiControllerTest {
//
//    @Mock
//    private MockMvc mockMvc;
//
//    @Mock
//    private AdvocateService advocateService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private ResponseInfoFactory responseInfoFactory;
//
//    @Mock
//    private AdvocateRequest advocateRequest;
//
//    @Mock
//    private AdvocateResponse advocateResponse;
//
//    @InjectMocks
//    private AdvocateApiController advocateApiController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(advocateApiController).build();
//
//
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
//        advocateRequest = AdvocateRequest.builder().advocates(advocateList).requestInfo(requestInfo).build();
//        advocateResponse = new AdvocateResponse();
//        advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();
//    }
//
//    @Test
//    public void advocateV1CreatePostSuccess() throws Exception {
//
//        // Mock the behavior of your service layer
//        given(advocateService.createAdvocate(any(AdvocateRequest.class))).willReturn(advocateRequest.getAdvocates());
//        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
//                        .content(new ObjectMapper().writeValueAsString(advocateRequest))) // objectMapper to convert request object to JSON
//                        .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
//                        .andExpect(content().json(objectMapper.writeValueAsString(advocateResponse)));
//    }
//
//    @Test
//    public void advocateV1CreatePostFailure() throws Exception {
//        // Mock the behavior of your service layer
//        given(advocateService.createAdvocate(any(AdvocateRequest.class))).willReturn(advocateRequest.getAdvocates());
//        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());
//
//        // Perform the request with the required Accept header
//        mockMvc.perform(post("/advocate/v1/_create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(advocateRequest)))
//                        .andExpect(status().isBadRequest());
//    }
//}
