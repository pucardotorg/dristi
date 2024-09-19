package org.pucar.dristi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.WitnessPdfRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfRequestUtilTest {

    @Mock
    private Configuration configuration;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PdfRequestUtil pdfRequestUtil;

    @Test
    void testCreatePdfForWitness_Success() throws JsonProcessingException {
        // Arrange
        WitnessPdfRequest request = new WitnessPdfRequest();
        String tenantId = "test-tenant";

        when(configuration.getGeneratePdfHost()).thenReturn("http://pdf-host");
        when(configuration.getGeneratePdfUrl()).thenReturn("/generate");
        when(configuration.getWitnessPdfKey()).thenReturn("witness-key");

        ByteArrayResource mockResource = new ByteArrayResource("test-pdf".getBytes());
        ResponseEntity<ByteArrayResource> mockResponse = ResponseEntity.ok(mockResource);

        when(restTemplate.postForEntity(anyString(), any(), eq(ByteArrayResource.class)))
                .thenReturn(mockResponse);

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Act
        ByteArrayResource result = pdfRequestUtil.createPdfForWitness(request, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(mockResource, result);
        verify(restTemplate).postForEntity(anyString(), any(), eq(ByteArrayResource.class));
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    void testCreatePdfForWitness_RestClientException() throws JsonProcessingException {
        // Arrange
        WitnessPdfRequest request = new WitnessPdfRequest();
        String tenantId = "test-tenant";

        when(configuration.getGeneratePdfHost()).thenReturn("http://pdf-host");
        when(configuration.getGeneratePdfUrl()).thenReturn("/generate");
        when(configuration.getWitnessPdfKey()).thenReturn("witness-key");

        when(restTemplate.postForEntity(anyString(), any(), eq(ByteArrayResource.class)))
                .thenThrow(new RestClientException("REST client error"));

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pdfRequestUtil.createPdfForWitness(request, tenantId));

        assertEquals("PDF_UTILITY_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Error generating PDF for case"));
    }

    @Test
    void testCreatePdfForWitness_JsonProcessingException() throws JsonProcessingException {
        // Arrange
        WitnessPdfRequest request = new WitnessPdfRequest();
        String tenantId = "test-tenant";

        when(configuration.getGeneratePdfHost()).thenReturn("http://pdf-host");
        when(configuration.getGeneratePdfUrl()).thenReturn("/generate");
        when(configuration.getWitnessPdfKey()).thenReturn("witness-key");

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("JSON processing error") {});

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pdfRequestUtil.createPdfForWitness(request, tenantId));

        assertEquals("PDF_UTILITY_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Error generating PDF for case"));
    }

    @Test
    void testCreatePdfForWitness_NullResponse() throws JsonProcessingException {
        // Arrange
        WitnessPdfRequest request = new WitnessPdfRequest();
        String tenantId = "test-tenant";

        when(configuration.getGeneratePdfHost()).thenReturn("http://pdf-host");
        when(configuration.getGeneratePdfUrl()).thenReturn("/generate");
        when(configuration.getWitnessPdfKey()).thenReturn("witness-key");

        ResponseEntity<ByteArrayResource> mockResponse = ResponseEntity.ok(null);

        when(restTemplate.postForEntity(anyString(), any(), eq(ByteArrayResource.class)))
                .thenReturn(mockResponse);

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Act
        ByteArrayResource result = pdfRequestUtil.createPdfForWitness(request, tenantId);

        // Assert
        assertNull(result);
        verify(restTemplate).postForEntity(anyString(), any(), eq(ByteArrayResource.class));
        verify(objectMapper).writeValueAsString(request);
    }
}