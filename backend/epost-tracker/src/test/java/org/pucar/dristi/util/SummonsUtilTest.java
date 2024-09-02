package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.EPostConfiguration;
import org.pucar.dristi.model.*;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_UPDATING_SUMMONS;
import static org.pucar.dristi.config.ServiceConstants.SUMMONS_UPDATE_ERROR;

class SummonsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EPostConfiguration config;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SummonsUtil summonsUtil;

    private EPostRequest request = new EPostRequest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestInfo requestInfo = new RequestInfo();
        EPostTracker ePostTracker = EPostTracker.builder()
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .taskNumber("TS123")
                .processNumber("PS123").build();
        request.setEPostTracker(ePostTracker);
        request.setRequestInfo(requestInfo);
    }

    @Test
    void updateSummonsDeliveryStatus_success() throws Exception {
        // Arrange
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(new Object(), HttpStatus.OK);

        when(config.getSummonsHost()).thenReturn("http://localhost");
        when(config.getSummonsUpdateEndPoint()).thenReturn("/update");
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);
        when(objectMapper.convertValue(any(), any(Class.class))).thenReturn(new Object());

        // Act
        Object response = summonsUtil.updateSummonsDeliveryStatus(request);

        // Assert
        assertNotNull(response);
    }

    @Test
    void updateSummonsDeliveryStatus_restClientException() {
        // Arrange

        when(config.getSummonsHost()).thenReturn("http://localhost");
        when(config.getSummonsUpdateEndPoint()).thenReturn("/update");
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new RestClientException("Test Exception"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () ->
                summonsUtil.updateSummonsDeliveryStatus(request));
        assertEquals(SUMMONS_UPDATE_ERROR, thrown.getCode());
        assertEquals(ERROR_WHILE_UPDATING_SUMMONS, thrown.getMessage());
    }
}