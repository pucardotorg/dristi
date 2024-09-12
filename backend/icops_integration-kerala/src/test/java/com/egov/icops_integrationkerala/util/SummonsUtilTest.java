package com.egov.icops_integrationkerala.util;

import static org.mockito.Mockito.*;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class SummonsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IcopsConfiguration config;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SummonsUtil summonsUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateSummonsDeliveryStatusSuccess() throws Exception {
        IcopsRequest request = new IcopsRequest();
        IcopsTracker icopsTracker = new IcopsTracker();
        icopsTracker.setProcessNumber("12345");
        icopsTracker.setDeliveryStatus(DeliveryStatus.DELIVERED);
        request.setIcopsTracker(icopsTracker);
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        String summonsUrl = "http://example.com/updateSummons";
        when(config.getSummonsHost()).thenReturn("http://example.com");
        when(config.getSummonsUpdateEndPoint()).thenReturn("/updateSummons");

        UpdateSummonsResponse updateSummonsResponse = new UpdateSummonsResponse();
        ChannelMessage channelMessage = new ChannelMessage();
        updateSummonsResponse.setChannelMessage(channelMessage);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(updateSummonsResponse);

        when(restTemplate.postForEntity(eq(summonsUrl), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);
        when(objectMapper.convertValue(any(), eq(UpdateSummonsResponse.class)))
                .thenReturn(updateSummonsResponse);

        ChannelMessage result = summonsUtil.updateSummonsDeliveryStatus(request);

        assertEquals(channelMessage, result);
        verify(restTemplate, times(1)).postForEntity(eq(summonsUrl), any(HttpEntity.class), eq(Object.class));
        verify(objectMapper, times(1)).convertValue(any(), eq(UpdateSummonsResponse.class));
    }

    @Test
    void testUpdateSummonsDeliveryStatusFailure() {
        IcopsRequest request = new IcopsRequest();
        IcopsTracker icopsTracker = new IcopsTracker();
        icopsTracker.setProcessNumber("12345");
        icopsTracker.setDeliveryStatus(DeliveryStatus.DELIVERED);
        request.setIcopsTracker(icopsTracker);
        RequestInfo requestInfo = new RequestInfo();
        request.setRequestInfo(requestInfo);

        String summonsUrl = "http://example.com/updateSummons";
        when(config.getSummonsHost()).thenReturn("http://example.com");
        when(config.getSummonsUpdateEndPoint()).thenReturn("/updateSummons");

        when(restTemplate.postForEntity(eq(summonsUrl), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new RestClientException("Error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            summonsUtil.updateSummonsDeliveryStatus(request);
        });

        assertEquals("SUMMONS_UPDATE_ERROR", exception.getCode());
        assertEquals("Error occurred when sending Update Summons Request", exception.getMessage());
        verify(restTemplate, times(1)).postForEntity(eq(summonsUrl), any(HttpEntity.class), eq(Object.class));
    }
}


