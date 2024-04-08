package org.pucar.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.pucar.service.AdvocateService;
import org.pucar.util.ResponseInfoFactory;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateResponse;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.pucar.TestConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for AdvocateApiController
*/
@WebMvcTest(AdvocateApiController.class)
@Import(TestConfiguration.class)
public class AdvocateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvocateService advocateService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    private AdvocateRequest advocateRequest;
    private AdvocateResponse advocateResponse;

    @BeforeEach
    public void setup() {
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
        advocateRequest = AdvocateRequest.builder()
                .advocates(advocateList)
                .requestInfo(requestInfo)
                .build();
        advocateResponse = new AdvocateResponse();
        advocateResponse = AdvocateResponse.builder()
                .advocates(advocateList)
                .responseInfo(responseInfo)
                .build();
    }
    @Test
    public void advocateV1CreatePostSuccess() throws Exception {
        // Mock the behavior of your service layer
        given(advocateService.registerAdvocateRequest(any(AdvocateRequest.class))).willReturn(advocateResponse.getAdvocates());
        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());

        // Perform the request with the required Accept header
        mockMvc.perform(post("/advocate/v1/_create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // Explicitly state that the client accepts JSON responses
                        .content(objectMapper.writeValueAsString(advocateRequest))) // objectMapper to convert request object to JSON
                .andExpect(status().isOk()) // Assert that the response status is HttpStatus.OK
                .andExpect(content().json(objectMapper.writeValueAsString(advocateResponse)));
    }

    @Test
    public void advocateV1CreatePostFailure() throws Exception {
        // Mock the behavior of your service layer
        given(advocateService.registerAdvocateRequest(any(AdvocateRequest.class))).willReturn(advocateResponse.getAdvocates());
        given(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true))).willReturn(advocateResponse.getResponseInfo());

        // Perform the request with the required Accept header
        mockMvc.perform(post("/advocate/v1/_create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advocateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void advocateV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void advocateV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void advocateV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void advocateV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
