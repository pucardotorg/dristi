package com.egov.icops_integrationkerala.controller;

import com.egov.icops_integrationkerala.model.*;
import com.egov.icops_integrationkerala.service.IcopsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IcopsControllerTest {

    @Mock
    private IcopsService icopsService;

    @InjectMocks
    private IcopsController icopsController;

    @Mock
    private TaskRequest taskRequest;

    @Mock
    private AuthResponse authResponse;

    @Mock
    private IcopsProcessReport icopsProcessReport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendPRRequest_Success() throws Exception {
        // Arrange
        ChannelMessage channelMessage = new ChannelMessage();
        when(icopsService.sendRequestToIcops(taskRequest)).thenReturn(channelMessage);

        // Act
        ResponseEntity<ProcessResponse> response = icopsController.sendPRRequest(taskRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(channelMessage, response.getBody().getChannelMessage());
    }

    @Test
    void getAuthToken_Success() throws Exception {
        // Arrange
        String serviceName = "testService";
        String serviceKy = "testKey";
        String authType = "testType";
        when(icopsService.generateAuthToken(serviceName, serviceKy, authType)).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = icopsController.getAuthToken(serviceName, serviceKy, authType);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void getProcessReport_Success() {
        // Arrange
        ChannelMessage channelMessage = new ChannelMessage();
        when(icopsService.processPoliceReport(icopsProcessReport)).thenReturn(channelMessage);

        // Act
        ResponseEntity<ChannelMessage> response = icopsController.getProcessReport(icopsProcessReport);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(channelMessage, response.getBody());
    }

    @Test
    void sendPRRequest_Exception() throws Exception {
        // Arrange
        when(icopsService.sendRequestToIcops(taskRequest)).thenThrow(new Exception("Test exception"));

        // Act & Assert
        assertThrows(Exception.class, () -> icopsController.sendPRRequest(taskRequest));
    }

    @Test
    void getAuthToken_Exception() throws Exception {
        // Arrange
        String serviceName = "testService";
        String serviceKy = "testKey";
        String authType = "testType";
        when(icopsService.generateAuthToken(serviceName, serviceKy, authType)).thenThrow(new Exception("Test exception"));

        // Act & Assert
        assertThrows(Exception.class, () -> icopsController.getAuthToken(serviceName, serviceKy, authType));
    }
}