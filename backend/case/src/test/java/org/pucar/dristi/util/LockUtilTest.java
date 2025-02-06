package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LockUtilTest {


    @Mock
    private Configuration configuration;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LockUtil lockUtil;

    private RequestInfo requestInfo;
    private String uniqueId = "12345";
    private String tenantId = "tenant-1";

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
    }

    @Test
    void testIsLockPresent_whenLockExists() throws Exception {
        // Mock Configuration properties
        when(configuration.getLockSvcHost()).thenReturn("http://mock-service");
        when(configuration.getLockEndPoint()).thenReturn("/lock");

        // Mock the repository response
        Object mockResponse = new Object(); // Replace with appropriate mock response
        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);

        // Mock JSON handling
        String jsonResponse = "{ \"Lock\": { \"id\": \"lock-123\" ,\"isLocked\":true} }";
        when(objectMapper.writeValueAsString(any())).thenReturn(jsonResponse);
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);

        // Execute method
        boolean result = lockUtil.isLockPresent(requestInfo, uniqueId, tenantId);

        // Verify
        assertTrue(result);
        verify(repository, times(1)).fetchResult(any(), any());
    }

    @Test
    void testIsLockPresent_whenLockDoesNotExist() throws Exception {
        // Mock Configuration properties
        when(configuration.getLockSvcHost()).thenReturn("http://mock-service");
        when(configuration.getLockEndPoint()).thenReturn("/lock");

        // Mock repository response
        Object mockResponse = new Object();
        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);

        // Mock JSON handling
        String jsonResponse = "{ \"Lock\": { \"id\": \"lock-123\" ,\"isLocked\":false} }";
        when(objectMapper.writeValueAsString(any())).thenReturn(jsonResponse);
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        when(objectMapper.readTree(jsonResponse)).thenReturn(jsonNode);

        // Execute method
        boolean result = lockUtil.isLockPresent(requestInfo, uniqueId, tenantId);

        // Verify
        assertFalse(result);
        verify(repository, times(1)).fetchResult(any(), any());
    }
}
