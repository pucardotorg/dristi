package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IndividualSearchRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class IndividualUtilTest {

    @InjectMocks
    private IndividualUtil individualUtil;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration configs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIndividualCallSuccess() {
        IndividualSearchRequest request = new IndividualSearchRequest();
        StringBuilder uri = new StringBuilder("http://localhost:8080");

        JsonObject jsonResponse = new JsonObject();
        JsonArray individualArray = new JsonArray();
        JsonObject individualObject = new JsonObject();
        individualObject.addProperty("individualId", "12345");
        individualArray.add(individualObject);
        jsonResponse.add("Individual", individualArray);

        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(jsonResponse);

        Boolean result = individualUtil.individualCall(request, uri);

        assertTrue(result);
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testIndividualCallEmptyResponse() {
        IndividualSearchRequest request = new IndividualSearchRequest();
        StringBuilder uri = new StringBuilder("http://localhost:8080");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.add("Individual", new JsonArray());

        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(jsonResponse);

        Boolean result = individualUtil.individualCall(request, uri);

        assertFalse(result);
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testIndividualCallCustomException() {
        IndividualSearchRequest request = new IndividualSearchRequest();
        StringBuilder uri = new StringBuilder("http://localhost:8080");

        when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new CustomException("CODE", "Custom Exception"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            individualUtil.individualCall(request, uri);
        });

        assertTrue(thrown.getMessage().contains("Custom Exception"));
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testIndividualCallGenericException() {
        IndividualSearchRequest request = new IndividualSearchRequest();
        StringBuilder uri = new StringBuilder("http://localhost:8080");

        when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new RuntimeException("Error"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            individualUtil.individualCall(request, uri);
        });

        assertTrue(thrown.getMessage().contains("Exception in individual utility service: Error"));
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }
}
