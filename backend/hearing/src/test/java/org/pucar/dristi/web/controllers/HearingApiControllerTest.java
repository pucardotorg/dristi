package org.pucar.dristi.web.controllers;

import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.HearingService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingApiControllerTest {

    @Mock
    private HearingService hearingService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private HearingApiController hearingApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHearingV1CreatePost_Success() {
        HearingRequest hearingRequest = new HearingRequest();  // Set up the request
        Hearing hearing = new Hearing();  // Set up the response
        ResponseInfo responseInfo = new ResponseInfo();

        when(hearingService.createHearing(any(HearingRequest.class))).thenReturn(hearing);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(responseInfo);

        ResponseEntity<HearingResponse> response = hearingApiController.hearingV1CreatePost(hearingRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hearing, response.getBody().getHearing());
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }
    @Test
    public void testHearingV1ExistsPost_Success() {
        HearingExistsRequest hearingExistsRequest = new HearingExistsRequest();  // Set up the request
        HearingExists hearingExists = new HearingExists();  // Set up the response
        ResponseInfo responseInfo = new ResponseInfo();

        when(hearingService.isHearingExist(any(HearingExistsRequest.class))).thenReturn(hearingExists);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(responseInfo);

        ResponseEntity<HearingExistsResponse> response = hearingApiController.hearingV1ExistsPost(hearingExistsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hearingExists, response.getBody().getOrder());
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }

    @Test
    public void testHearingV1SearchPost_Success() {
        String cnrNumber = "12345";
        String applicationNumber = "67890";
        List<Hearing> hearingList = List.of(new Hearing());
        int totalCount = hearingList.size();

        when(hearingService.searchHearing(anyString(), anyString(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(hearingList);

        ResponseEntity<HearingListResponse> response = hearingApiController.hearingV1SearchPost(cnrNumber, applicationNumber, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hearingList, response.getBody().getHearingList());
        assertEquals(totalCount, response.getBody().getTotalCount());
    }

    @Test
    public void testHearingV1UpdatePost_Success() {
        HearingRequest hearingRequest = new HearingRequest();  // Set up the request
        Hearing hearing = new Hearing();  // Set up the response
        ResponseInfo responseInfo = new ResponseInfo();

        when(hearingService.updateHearing(any(HearingRequest.class))).thenReturn(hearing);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(responseInfo);

        ResponseEntity<HearingResponse> response = hearingApiController.hearingV1UpdatePost(hearingRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hearing, response.getBody().getHearing());
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }
}
