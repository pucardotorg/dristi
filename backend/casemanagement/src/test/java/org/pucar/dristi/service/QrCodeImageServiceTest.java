package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.PdfRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeImageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private QrCodeImageService qrCodeImageService;

    @Mock
    private ResponseEntity<String> responseEntity;

    @Value("${egov.credential.host}")
    private String credentialHost;

    @Value("${egov.credential.url}")
    private String credentialUrl;

    @BeforeEach
    void setUp() {
        lenient().when(configuration.getCredentialHost()).thenReturn("http://localhost:8080");
        lenient().when(configuration.getCredentialUrl()).thenReturn("/credential/url");
    }

    @Test
    void testGetQrCodeImageSuccess() {
        PdfRequest pdfRequestObject = new PdfRequest();
        pdfRequestObject.setReferenceId("123");
        pdfRequestObject.setRequestInfo(new RequestInfo());
        pdfRequestObject.setModuleName("moduleName");

        String mockResponseBody = "<html><body><img src='http://example.com/qrcode.png'></body></html>";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponseBody, HttpStatus.OK));

        String qrImage = qrCodeImageService.getQrCodeImage(pdfRequestObject);

        assertNotNull(qrImage);
        assertEquals("http://example.com/qrcode.png", qrImage);

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testGetQrCodeImageThrowsCustomException() {
        PdfRequest pdfRequestObject = new PdfRequest();
        pdfRequestObject.setReferenceId("123");
        pdfRequestObject.setRequestInfo(new RequestInfo());
        pdfRequestObject.setModuleName("moduleName");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("RestTemplate Exception"));

        CustomException exception = assertThrows(CustomException.class, () ->
                qrCodeImageService.getQrCodeImage(pdfRequestObject)
        );

        assertEquals("QR_CODE_RESPONSE_ERROR", exception.getCode());
        assertTrue(exception.getMessage().contains("there was an issue fetching the qr code"));

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testExtractImageFromResponseSuccess() {
        String mockResponseBody = "<html><body><img src='http://example.com/qrcode.png'></body></html>";

        String qrImage = qrCodeImageService.extractImageFromResponse(mockResponseBody);

        assertNotNull(qrImage);
        assertEquals("http://example.com/qrcode.png", qrImage);
    }

    @Test
    void testExtractImageFromResponseThrowsCustomException() {
        String invalidResponseBody = "<html><body><img></body></html>"; // Missing src attribute

        CustomException exception = assertThrows(CustomException.class, () ->
                qrCodeImageService.extractImageFromResponse(invalidResponseBody)
        );

        assertEquals("ERROR_PARSING_QRCODE_RESPONSE", exception.getCode());
        assertTrue(exception.getMessage().contains("there was error fetching image from the qr response"));
    }
}
