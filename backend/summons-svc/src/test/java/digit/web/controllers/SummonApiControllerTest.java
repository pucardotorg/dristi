package digit.web.controllers;

import digit.service.SummonsService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummonsApiControllerTest {

    @Mock
    private SummonsService summonsService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private SummonsApiController summonsApiController;

    @Mock
    private TaskRequest taskRequest;

    @Mock
    private SummonsDeliverySearchRequest searchRequest;

    @Mock
    private UpdateSummonsRequest updateRequest;

    @Mock
    private TaskResponse taskResponse;

    @Mock
    private SummonsResponse summonsResponse;

    @Mock
    private SummonsDeliverySearchResponse searchResponse;

    @Mock
    private UpdateSummonsResponse updateResponse;

    @Mock
    private ResponseInfo responseInfo;

    @Mock
    private SummonsDelivery summonsDelivery;

    @Mock
    private ChannelMessage channelMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateSummons_Success() {
        // Arrange
        when(summonsService.generateSummonsDocument(taskRequest)).thenReturn(taskResponse);

        // Act
        ResponseEntity<TaskResponse> response = summonsApiController.generateSummons(taskRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskResponse, response.getBody());
    }

    @Test
    void sendSummons_Success() {
        // Arrange
        when(responseInfoFactory.createResponseInfoFromRequestInfo(taskRequest.getRequestInfo(), true)).thenReturn(responseInfo);
        when(summonsService.sendSummonsViaChannels(taskRequest)).thenReturn(summonsDelivery);

        // Act
        ResponseEntity<SummonsResponse> response = summonsApiController.sendSummons(taskRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(summonsDelivery, response.getBody().getSummonsDelivery());
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }

    @Test
    void getSummons_Success() {
        // Arrange
        when(responseInfoFactory.createResponseInfoFromRequestInfo(searchRequest.getRequestInfo(), true)).thenReturn(responseInfo);
        when(summonsService.getSummonsDelivery(searchRequest)).thenReturn(Collections.singletonList(summonsDelivery));

        // Act
        ResponseEntity<SummonsDeliverySearchResponse> response = summonsApiController.getSummons(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getSummonsDeliveryList().size());
        assertEquals(summonsDelivery, response.getBody().getSummonsDeliveryList().get(0));
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }

    @Test
    void updateSummonsStatus_Success() {
        // Arrange
        when(responseInfoFactory.createResponseInfoFromRequestInfo(updateRequest.getRequestInfo(), true)).thenReturn(responseInfo);
        when(summonsService.updateSummonsDeliveryStatus(updateRequest)).thenReturn(channelMessage);

        // Act
        ResponseEntity<UpdateSummonsResponse> response = summonsApiController.updateSummonsStatus(updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(channelMessage, response.getBody().getChannelMessage());
        assertEquals(responseInfo, response.getBody().getResponseInfo());
    }

    @Test
    void generateSummons_Exception() {
        // Arrange
        when(summonsService.generateSummonsDocument(taskRequest)).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summonsApiController.generateSummons(taskRequest));
    }

    @Test
    void sendSummons_Exception() {
        // Arrange
        when(summonsService.sendSummonsViaChannels(taskRequest)).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summonsApiController.sendSummons(taskRequest));
    }

    @Test
    void getSummons_Exception() {
        // Arrange
        when(summonsService.getSummonsDelivery(searchRequest)).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summonsApiController.getSummons(searchRequest));
    }

    @Test
    void updateSummonsStatus_Exception() {
        // Arrange
        when(summonsService.updateSummonsDeliveryStatus(updateRequest)).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summonsApiController.updateSummonsStatus(updateRequest));
    }
}
