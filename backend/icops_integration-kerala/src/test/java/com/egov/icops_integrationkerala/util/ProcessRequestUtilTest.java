package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import com.egov.icops_integrationkerala.model.ChannelMessage;
import com.egov.icops_integrationkerala.model.ProcessRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProcessRequestUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IcopsConfiguration config;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuthResponse authResponse;

    @Mock
    private ProcessRequest processRequest;

    @InjectMocks
    private ProcessRequestUtil processRequestUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void callProcessRequest_Success() throws Exception {
        // Arrange
        authResponse.setAccessToken("testToken");

        ChannelMessage expectedChannelMessage = new ChannelMessage();
        expectedChannelMessage.setAcknowledgementStatus("SUCCESS");

        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getProcessRequestEndPoint()).thenReturn("/process");
        when(restTemplate.postForEntity(
                eq("http://icops.com/process"),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        when(objectMapper.convertValue(any(), eq(ChannelMessage.class))).thenReturn(expectedChannelMessage);

        // Act
        ChannelMessage result = processRequestUtil.callProcessRequest(authResponse, processRequest);

        // Assert
        assertNotNull(result);
        assertEquals("SUCCESS", result.getAcknowledgementStatus());
        verify(objectMapper, times(1)).writeValueAsString(processRequest);
    }

    @Test
    void callProcessRequest_RestClientException() throws Exception {
        // Arrange
        authResponse.setAccessToken("testToken");

        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getProcessRequestEndPoint()).thenReturn("/process");
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenThrow(new RestClientException("Test exception"));

        // Act
        ChannelMessage result = processRequestUtil.callProcessRequest(authResponse, processRequest);

        // Assert
        assertNotNull(result);
        assertEquals("FAILURE", result.getAcknowledgementStatus());
        assertEquals("Failed to connect to ICOPS", result.getFailureMsg());
    }
}