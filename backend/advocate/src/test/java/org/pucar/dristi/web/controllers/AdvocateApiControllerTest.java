package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.service.AdvocateService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdvocateApiControllerTest {

    @Autowired
    private AdvocateApiController advocateApiController;

    @MockBean
    private AdvocateService advocateService;
    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdvocateV1CreatePost() {
        AdvocateRequest request = new AdvocateRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        Advocate advocate = new Advocate();
        List<Advocate> advocateList = Arrays.asList(advocate);

        when(advocateService.createAdvocate(request)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateResponse> responseEntity = advocateApiController.advocateV1CreatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getAdvocates());
    }



    @Test
    public void testAdvocateV1StatusSearchPost() {
        String status = "active";
        String tenantId = "tenantId";
        List<Advocate> advocateList = Arrays.asList(new Advocate());

        when(advocateService.searchAdvocateByStatus(status, tenantId, 10, 0)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(null, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateResponse> responseEntity = advocateApiController.advocateV1StatusSearchPost(status, tenantId, 10, 0);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getAdvocates());
    }

    @Test
    public void testAdvocateV1ApplicationnumberSearchPost() {
        String applicationNumber = "app123";
        String tenantId = "tenantId";
        List<Advocate> advocateList = Arrays.asList(new Advocate());

        when(advocateService.searchAdvocateByApplicationNumber(applicationNumber, tenantId, 10, 0)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(null, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateResponse> responseEntity = advocateApiController.advocateV1ApplicationnumberSearchPost(applicationNumber, tenantId, 10, 0);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getAdvocates());
    }

    @Test
    public void testAdvocateV1UpdatePost() {
        AdvocateRequest request = new AdvocateRequest();
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        Advocate advocate = new Advocate();
        List<Advocate> advocateList = Arrays.asList(advocate);

        when(advocateService.updateAdvocate(request)).thenReturn(advocateList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(new ResponseInfo());

        ResponseEntity<AdvocateResponse> responseEntity = advocateApiController.advocateV1UpdatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(advocateList, responseEntity.getBody().getAdvocates());
    }
}
