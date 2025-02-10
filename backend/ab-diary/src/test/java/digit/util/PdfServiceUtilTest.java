package digit.util;

import digit.config.Configuration;
import digit.web.models.CaseDiaryRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfServiceUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @InjectMocks
    private PdfServiceUtil pdfServiceUtil;

    @Test
    void generatePdfFromPdfService_Success() {
        // Given
        String tenantId = "tenant1";
        String pdfTemplateKey = "templateKey1";
        String pdfServiceHost = "http://localhost";
        String pdfServiceEndpoint = "/pdf-service";

        when(config.getPdfServiceHost()).thenReturn(pdfServiceHost);
        when(config.getPdfServiceEndpoint()).thenReturn(pdfServiceEndpoint);

        ByteArrayResource byteArrayResource = new ByteArrayResource("PDF Content".getBytes());
        ResponseEntity<ByteArrayResource> responseEntity = new ResponseEntity<>(byteArrayResource, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ByteArrayResource.class)))
                .thenReturn(responseEntity);

        // When
        ByteArrayResource result = pdfServiceUtil.generatePdfFromPdfService(new CaseDiaryRequest(), tenantId, pdfTemplateKey);

        // Then
        assertNotNull(result);
        assertArrayEquals("PDF Content".getBytes(), result.getByteArray());
    }

    @Test
    void generatePdfFromPdfService_NullResponse() {
        // Given
        CaseDiaryRequest caseDiaryRequest = new CaseDiaryRequest();
        String tenantId = "tenant1";
        String pdfTemplateKey = "templateKey1";
        String pdfServiceHost = "http://localhost";
        String pdfServiceEndpoint = "/pdf-service";

        when(config.getPdfServiceHost()).thenReturn(pdfServiceHost);
        when(config.getPdfServiceEndpoint()).thenReturn(pdfServiceEndpoint);

        ResponseEntity<ByteArrayResource> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ByteArrayResource.class)))
                .thenReturn(responseEntity);

        // When
        ByteArrayResource result = pdfServiceUtil.generatePdfFromPdfService(caseDiaryRequest, tenantId, pdfTemplateKey);

        // Then
        assertNull(result);
    }

    @Test
    void generatePdfFromPdfService_Exception() {
        // Given
        CaseDiaryRequest caseDiaryRequest = new CaseDiaryRequest();
        String tenantId = "tenant1";
        String pdfTemplateKey = "templateKey1";
        String pdfServiceHost = "http://localhost";
        String pdfServiceEndpoint = "/pdf-service";

        when(config.getPdfServiceHost()).thenReturn(pdfServiceHost);
        when(config.getPdfServiceEndpoint()).thenReturn(pdfServiceEndpoint);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ByteArrayResource.class)))
                .thenThrow(new RuntimeException("Error during REST call"));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            pdfServiceUtil.generatePdfFromPdfService(caseDiaryRequest, tenantId, pdfTemplateKey);
        });

        assertEquals("CL_PDF_APP_ERROR", exception.getCode());
        assertEquals("Error getting response from Pdf Service", exception.getMessage());
    }
}
