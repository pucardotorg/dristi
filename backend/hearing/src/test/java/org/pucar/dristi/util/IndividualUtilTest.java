package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IndividualSearchRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IndividualUtilTest {

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
    void testIndividualCallEmptyResponse() {
       IndividualSearchRequest request = new IndividualSearchRequest();
       StringBuilder uri = new StringBuilder("http://localhost:8080");
       Map<String, String> individualUserUUID = new HashMap<>();

       JsonObject jsonResponse = new JsonObject();
       jsonResponse.add("Individual", new JsonArray());

       when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(jsonResponse);

       Boolean result = individualUtil.individualCall(request, uri, individualUserUUID);

       assertFalse(result);
       verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
   }

   @Test
    void testIndividualCallCustomException() {
       IndividualSearchRequest request = new IndividualSearchRequest();
       StringBuilder uri = new StringBuilder("http://localhost:8080");
       Map<String, String> individualUserUUID = new HashMap<>();

       when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new CustomException("CODE", "Custom Exception"));

       CustomException thrown = assertThrows(CustomException.class, () -> {
           individualUtil.individualCall(request, uri, individualUserUUID);
       });

       assertTrue(thrown.getMessage().contains("Custom Exception"));
       verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
   }

   @Test
    void testIndividualCallGenericException() {
       IndividualSearchRequest request = new IndividualSearchRequest();
       StringBuilder uri = new StringBuilder("http://localhost:8080");
       Map<String, String> individualUserUUID = new HashMap<>();

       when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new RuntimeException("Error"));

       CustomException thrown = assertThrows(CustomException.class, () -> {
           individualUtil.individualCall(request, uri, individualUserUUID);
       });

       assertFalse(thrown.getMessage().contains("Exception in individual utility service: Error"));
       verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
   }

   @Test
   void testGetIndividualByIndividualIdEmptyResponse() {
       IndividualSearchRequest request = new IndividualSearchRequest();
       StringBuilder uri = new StringBuilder("http://localhost:8080");

       when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(null);

       assertNull(individualUtil.getIndividualByIndividualId(request, uri));
       verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
   }

   @Test
    void testGetIndividualByIndividualIdCustomException() {
         IndividualSearchRequest request = new IndividualSearchRequest();
         StringBuilder uri = new StringBuilder("http://localhost:8080");

         when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new CustomException("CODE", "Custom Exception"));

         CustomException thrown = assertThrows(CustomException.class, () -> {
              individualUtil.getIndividualByIndividualId(request, uri);
         });

         assertTrue(thrown.getMessage().contains("Custom Exception"));
         verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    void testGetIndividualByIndividualIdGenericException() {
         IndividualSearchRequest request = new IndividualSearchRequest();
         StringBuilder uri = new StringBuilder("http://localhost:8080");

         when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new RuntimeException("Error"));

         CustomException thrown = assertThrows(CustomException.class, () -> {
              individualUtil.getIndividualByIndividualId(request, uri);
         });

        assertTrue(thrown.getMessage().contains("Error"));
         verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }
}