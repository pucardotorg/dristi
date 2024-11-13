package org.egov.eTreasury.util;

import static org.egov.eTreasury.config.ServiceConstants.PDFSERVICE_UTILITY_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.model.TreasuryPaymentRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class PdfServiceUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaymentConfiguration config;

    @InjectMocks
    private PdfServiceUtil pdfServiceUtil;

    @Test
    public void testGeneratePdfFromPdfService_Success() {
        // Arrange
        TreasuryPaymentRequest pdfRequest = new TreasuryPaymentRequest();
        ByteArrayResource byteArrayResource = new ByteArrayResource("pdf data".getBytes());

        when(config.getPdfServiceHost()).thenReturn("http://pdf-service-host");
        when(config.getPdfServiceEndpoint()).thenReturn("/generatePdf");
        when(config.getEgovStateTenantId()).thenReturn("tenantId");
        when(config.getPdfTemplateKey()).thenReturn("templateKey");

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(byteArrayResource));

        // Act
        ByteArrayResource result = pdfServiceUtil.generatePdfFromPdfService(pdfRequest);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGeneratePdfFromPdfService_Exception() {
        // Arrange
        TreasuryPaymentRequest pdfRequest = new TreasuryPaymentRequest();

        when(config.getPdfServiceHost()).thenReturn("http://pdf-service-host");
        when(config.getPdfServiceEndpoint()).thenReturn("/generatePdf");
        when(config.getEgovStateTenantId()).thenReturn("tenantId");
        when(config.getPdfTemplateKey()).thenReturn("templateKey");

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        CustomException thrownException = assertThrows(CustomException.class, () -> {
            pdfServiceUtil.generatePdfFromPdfService(pdfRequest);
        });

        assertNotNull(thrownException);
        assertEquals(PDFSERVICE_UTILITY_EXCEPTION, thrownException.getCode());
        assertEquals("Error getting response from Pdf Service", thrownException.getMessage());
    }
}

