package org.pucar.dristi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.model.*;
import org.pucar.dristi.service.EPostService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EPostControllerTest {

    @Mock
    private EPostService ePostService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private TaskRequest taskRequest;

    @Mock
    private ChannelMessage channelMessage;

    @InjectMocks
    private EPostController ePostController;

    @Test
    void testSendEPost() throws JsonProcessingException {
        // Arrange

        ResponseInfo responseInfo = new ResponseInfo();
        ChannelResponse channelResponse = ChannelResponse.builder()
                .channelMessage(channelMessage)
                .responseInfo(responseInfo)
                .build();

        when(ePostService.sendEPost(taskRequest)).thenReturn(channelMessage);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), anyBoolean())).thenReturn(responseInfo);

        // Act
        ResponseEntity<ChannelResponse> responseEntity = ePostController.sendEPost(taskRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(channelResponse, responseEntity.getBody());
    }

    @Test
    void testGetEPost() {
        // Arrange
        EPostTrackerSearchRequest request = new EPostTrackerSearchRequest();
        EPostResponse ePostResponse = new EPostResponse();
        ResponseInfo responseInfo = new ResponseInfo();
        ePostResponse.setResponseInfo(responseInfo);

        when(ePostService.getEPost(request, 10, 0)).thenReturn(ePostResponse);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), anyBoolean())).thenReturn(responseInfo);

        // Act
        ResponseEntity<EPostResponse> responseEntity = ePostController.getEPost(request, 10, 0);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ePostResponse, responseEntity.getBody());
    }

    @Test
    void testUpdateEPost() {
        // Arrange
        EPostRequest ePostRequest = new EPostRequest();
        EPostTracker ePostTracker = new EPostTracker();
        ResponseInfo responseInfo = new ResponseInfo();
        EPostResponse ePostResponse = EPostResponse.builder()
                .ePostTrackers(Collections.singletonList(ePostTracker))
                .responseInfo(responseInfo)
                .build();

        when(ePostService.updateEPost(ePostRequest)).thenReturn(ePostTracker);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), anyBoolean())).thenReturn(responseInfo);

        // Act
        ResponseEntity<EPostResponse> responseEntity = ePostController.updateEPost(ePostRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ePostResponse, responseEntity.getBody());
    }
}
