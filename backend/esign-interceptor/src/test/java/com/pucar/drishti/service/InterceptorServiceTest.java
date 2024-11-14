package com.pucar.drishti.service;

import com.pucar.drishti.config.Configuration;
import com.pucar.drishti.repository.ServiceRequestRepository;
import com.pucar.drishti.web.models.SignDocRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InterceptorServiceTest {


    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration configuration;

    private InterceptorService interceptorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptorService = new InterceptorService(serviceRequestRepository, configuration);
    }

    @Test
    void testProcess() {
        // Arrange
        String response = "testResponse";
        String espId = "testEspId";
        String tenantId = "testTenantId";
        String fileStoreId = "testFileStoreId";
        String token = "testToken";
        String signedFileStoreId = "signedFileStoreId";

        // Mock configuration
        when(configuration.getESignHost()).thenReturn("http://esign-host");
        when(configuration.getESignEndPoint()).thenReturn("/esign-endpoint");
        when(configuration.getOathHost()).thenReturn("http://oath-host");
        when(configuration.getOathEndPoint()).thenReturn("/oath-endpoint");
        when(configuration.getUserName()).thenReturn("testUser");
        when(configuration.getPassword()).thenReturn("testPassword");
        when(configuration.getTenantId()).thenReturn("testTenantId");
        when(configuration.getUserType()).thenReturn("testUserType");
        when(configuration.getScope()).thenReturn("testScope");
        when(configuration.getGrantType()).thenReturn("testGrantType");

        // Mock OAuth response
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(createMockOAuthResponse(token));

        // Mock eSign response
        when(serviceRequestRepository.callESign(any(), any())).thenReturn(signedFileStoreId);

        // Act
        String result = interceptorService.process(response, espId, tenantId, fileStoreId);

        // Assert
        assertEquals(signedFileStoreId, result);
        verify(serviceRequestRepository).fetchResult(any(), any()); // Verify OAuth call
        verify(serviceRequestRepository).callESign(any(), any(SignDocRequest.class));
    }

    private Object createMockOAuthResponse(String token) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("access_token", token);
        return response;
    }
}