package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.ApplicationUtil;
import org.pucar.dristi.util.Util;

@Slf4j
public class ApplicationUtilTest {

    @InjectMocks
    private ApplicationUtil applicationUtil;

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetApplication_Success() throws Exception {
        // Arrange
        JSONObject request = new JSONObject();
        String tenantId = "test-tenant";
        String applicationNumber = "12345";

        // Mock URL construction
        when(config.getApplicationHost()).thenReturn("http://localhost:8080");
        when(config.getApplicationSearchPath()).thenReturn("/search");

        // Mock repository response
        String response = "{\"applications\": [{\"id\": \"1\", \"name\": \"Test Application\"}]}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(response);

        // Mock util response
        JSONArray applications = new JSONArray();
        applications.put(new JSONObject().put("id", "1").put("name", "Test Application"));
        when(util.constructArray(anyString(), anyString())).thenReturn(applications);

        // Act
        Object result = applicationUtil.getApplication(request, tenantId, applicationNumber);

        // Assert
        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        JSONObject resultJson = (JSONObject) result;
        assertEquals("1", resultJson.getString("id"));
        assertEquals("Test Application", resultJson.getString("name"));

        // Verify interactions
        verify(config, times(1)).getApplicationHost();
        verify(config, times(1)).getApplicationSearchPath();
        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(1)).constructArray(anyString(), anyString());
    }

    @Test
    void testGetApplication_NoApplications() throws Exception {
        // Arrange
        JSONObject request = new JSONObject();
        String tenantId = "test-tenant";
        String applicationNumber = "12345";

        // Mock URL construction
        when(config.getApplicationHost()).thenReturn("http://localhost:8080");
        when(config.getApplicationSearchPath()).thenReturn("/search");

        // Mock repository response
        String response = "{\"applications\": []}";
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(response);

        // Mock util response
        JSONArray applications = new JSONArray();
        when(util.constructArray(anyString(), anyString())).thenReturn(applications);

        // Act
        Object result = applicationUtil.getApplication(request, tenantId, applicationNumber);

        // Assert
        assertNull(result);

        // Verify interactions
        verify(config, times(1)).getApplicationHost();
        verify(config, times(1)).getApplicationSearchPath();
        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(1)).constructArray(anyString(), anyString());
    }

    @Test
    void testGetApplication_RepositoryThrowsException() throws Exception {
        // Arrange
        JSONObject request = new JSONObject();
        String tenantId = "test-tenant";
        String applicationNumber = "12345";

        // Mock URL construction
        when(config.getApplicationHost()).thenReturn("http://localhost:8080");
        when(config.getApplicationSearchPath()).thenReturn("/search");

        // Mock repository to throw an exception
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class)))
                .thenThrow(new RuntimeException("Repository error"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> applicationUtil.getApplication(request, tenantId, applicationNumber));

        assertEquals("Error while processing application response", thrown.getMessage());

        // Verify interactions
        verify(config, times(1)).getApplicationHost();
        verify(config, times(1)).getApplicationSearchPath();
        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(0)).constructArray(anyString(), anyString());
    }
}
