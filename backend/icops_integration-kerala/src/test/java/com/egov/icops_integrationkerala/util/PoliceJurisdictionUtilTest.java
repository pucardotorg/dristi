package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import com.egov.icops_integrationkerala.model.Location;
import com.egov.icops_integrationkerala.model.LocationBasedJurisdiction;
import com.egov.icops_integrationkerala.model.PoliceStationDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
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

class PoliceJurisdictionUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IcopsConfiguration config;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuthResponse authResponse;

    @Mock
    private Location location;

    @Mock
    private LocationBasedJurisdiction expectedJurisdiction;

    @Mock
    private PoliceStationDetails policeStationDetails;

    @InjectMocks
    private PoliceJurisdictionUtil policeJurisdictionUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLocationBasedJurisdiction_Success() throws Exception {
        // Arrange
        authResponse.setAccessToken("testToken");

        location.setLatitude(String.valueOf(10.0));
        location.setLongitude(String.valueOf(20.0));

        expectedJurisdiction.setIncludedJurisdiction(policeStationDetails);

        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getLocationBasedJurisdiction()).thenReturn("/jurisdiction");
        when(restTemplate.postForEntity(
                eq("http://icops.com/jurisdiction"),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        when(objectMapper.convertValue(any(), eq(LocationBasedJurisdiction.class))).thenReturn(expectedJurisdiction);

        // Act
        LocationBasedJurisdiction result = policeJurisdictionUtil.getLocationBasedJurisdiction(authResponse, location);

        // Assert
        assertNotNull(result);
        verify(objectMapper, times(1)).writeValueAsString(location);
    }

    @Test
    void getLocationBasedJurisdiction_RestClientException() throws Exception {
        // Arrange
        authResponse.setAccessToken("testToken");
        location.setLatitude(String.valueOf(10.0));
        location.setLongitude(String.valueOf(20.0));

        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getLocationBasedJurisdiction()).thenReturn("/jurisdiction");
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenThrow(new RestClientException("Test exception"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> policeJurisdictionUtil.getLocationBasedJurisdiction(authResponse, location));
        assertEquals("ICOPS_LOCATION_JURISDICTION_ERROR", exception.getCode());
        assertEquals("Error occurred when getting location jurisdiction", exception.getMessage());
    }

    @Test
    void getLocationBasedJurisdiction_JsonProcessingException() throws Exception {
        // Arrange
        authResponse.setAccessToken("testToken");

        location.setLatitude(String.valueOf(10.0));
        location.setLongitude(String.valueOf(20.0));

        when(config.getIcopsUrl()).thenReturn("http://icops.com");
        when(config.getLocationBasedJurisdiction()).thenReturn("/jurisdiction");
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Test exception") {});

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> policeJurisdictionUtil.getLocationBasedJurisdiction(authResponse, location));
        assertEquals("ICOPS_LOCATION_JURISDICTION_ERROR", exception.getCode());
        assertEquals("Error occurred when getting location jurisdiction", exception.getMessage());
    }
}