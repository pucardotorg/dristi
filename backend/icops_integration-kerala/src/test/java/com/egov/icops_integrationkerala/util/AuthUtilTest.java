package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import org.junit.jupiter.api.Assertions;
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

class AuthUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IcopsConfiguration config;

    @Mock
    private AuthResponse mockResponse;

    @InjectMocks
    private AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateAndGetToken_Success() throws Exception {
        // Arrange
        String icopsUrl = "http://icops.com";
        String authEndpoint = "/auth";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String grantType = "client_credentials";
        mockResponse.setAccessToken("testAccessToken");
        mockResponse.setTokenType("Bearer");
        mockResponse.setExpiresIn(3600);

        when(config.getIcopsUrl()).thenReturn(icopsUrl);
        when(config.getAuthEndpoint()).thenReturn(authEndpoint);
        when(config.getClientId()).thenReturn(clientId);
        when(config.getClientSecret()).thenReturn(clientSecret);
        when(config.getGrantType()).thenReturn(grantType);

        when(restTemplate.postForEntity(
                eq(icopsUrl + authEndpoint),
                any(HttpEntity.class),
                eq(AuthResponse.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        AuthResponse result = authUtil.authenticateAndGetToken();

        // Assert
        assertNotNull(result);
        assertNull(result.getAccessToken());
        assertNull(result.getTokenType());
    }

    @Test
    void authenticateAndGetToken_RestClientException() {
        // Arrange
        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getAuthEndpoint()).thenReturn("/auth");
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(AuthResponse.class)
        )).thenThrow(new RestClientException("Test exception"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> authUtil.authenticateAndGetToken());
        assertEquals("Error occurred when authenticating ICops", exception.getMessage());
    }
}