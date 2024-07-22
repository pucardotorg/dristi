package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.service.AdvocateClerkService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
 class ClerkApiControllerTest {

    @Autowired
    private ClerkApiController advocateApiController;

    @MockBean
    private AdvocateClerkService advocateService;
    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testAdvocateClerkV1CreatePost() {
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        AdvocateClerk advocate = new AdvocateClerk();
        List<AdvocateClerk> advocateList = Arrays.asList(new AdvocateClerk());
        when(advocateService.registerAdvocateClerkRequest(request)).thenReturn(advocate);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateClerkResponse> responseEntity = advocateApiController.clerkV1CreatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getClerks());
    }



    @Test
     void testAdvocateClerkV1StatusSearchPost() {
        String status = "active";
        String tenantId = "tenantId";
        List<AdvocateClerk> advocateList = Arrays.asList(new AdvocateClerk());
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);
        AdvocateSimpleSearchRequest advocateSimpleSearchRequest = new AdvocateSimpleSearchRequest();
        advocateSimpleSearchRequest.setRequestInfo(requestInfo);

        when(advocateService.searchAdvocateClerkApplicationsByStatus(requestInfo, status, tenantId, 10, 0)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(null, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateClerkResponse> responseEntity = advocateApiController.clerkV1StatusSearchPost(status, tenantId, 10, 0, advocateSimpleSearchRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getClerks());
    }

    @Test
     void testAdvocateClerkV1ApplicationnumberSearchPost() {
        String applicationNumber = "app123";
        String tenantId = "tenantId";
        List<AdvocateClerk> advocateList = Arrays.asList(new AdvocateClerk());
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);
        AdvocateSimpleSearchRequest advocateSimpleSearchRequest = new AdvocateSimpleSearchRequest();
        advocateSimpleSearchRequest.setRequestInfo(requestInfo);

        when(advocateService.searchAdvocateClerkApplicationsByAppNumber(requestInfo, applicationNumber, tenantId, 10, 0)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(null, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateClerkResponse> responseEntity = advocateApiController.clerkV1ApplicationnumberSearchPost(applicationNumber, tenantId, 10, 0, advocateSimpleSearchRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getClerks());
    }

    @Test
     void testAdvocateClerkV1UpdatePost() {
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        AdvocateClerk advocate = new AdvocateClerk();
        List<AdvocateClerk> advocateList = Arrays.asList(advocate);

        when(advocateService.updateAdvocateClerk(request)).thenReturn(advocate);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateClerkResponse> responseEntity = advocateApiController.clerkV1UpdatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getClerks());
    }

    @Test
    void advocateClerkV1SearchPost_Success() {
        // Arrange
        AdvocateClerkSearchRequest body = new AdvocateClerkSearchRequest();
        RequestInfo requestInfo = new RequestInfo();
        body.setRequestInfo(requestInfo);
        List<AdvocateClerkSearchCriteria> criteria = new ArrayList<>(); // Your criteria here
        body.setCriteria(criteria);
        body.setTenantId("tenant1");
        Integer limit = 10;
        Integer offset = 0;
        ResponseInfo responseInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        // Act
        ResponseEntity<AdvocateClerkListResponse> response = advocateApiController.clerkV1SearchPost(body, limit, offset);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(criteria, response.getBody().getClerks());
    }
}
