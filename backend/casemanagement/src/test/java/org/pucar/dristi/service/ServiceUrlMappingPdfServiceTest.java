package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.util.SummonsOrderPdfUtil;
import org.pucar.dristi.web.models.PdfRequest;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceUrlMappingPdfServiceTest {

    @Mock
    private TaskSearchService taskSearchService;

    @Mock
    private SummonsOrderPdfUtil summonsOrderPdfUtil;

    @Mock
    private QrCodeImageService qrCodeImageService;

    @Mock
    private PdfSummonsOrderRequestService pdfSummonsOrderRequestService;

    @InjectMocks
    private ServiceUrlMappingPdfService serviceUrlMappingPdfService;

    private PdfRequest pdfRequestobject;

    @BeforeEach
    void setUp() {
        pdfRequestobject = new PdfRequest();
        pdfRequestobject.setReferenceCode("summons-order");
        pdfRequestobject.setReferenceId("ref123");
        pdfRequestobject.setTenantId("tenant123");
        pdfRequestobject.setRequestInfo(new RequestInfo());
    }

    @Test
    void testGetSVcUrlMappingPdf_validReferenceCode() {
        String qrCodeImage = "qrCodeImage";
        ResponseEntity<Object> taskSearchResponse = ResponseEntity.ok(new Object());
        PdfSummonsRequest pdfSummonsRequest = new PdfSummonsRequest();
        Object pdfResponse = new Object();

        when(qrCodeImageService.getQrCodeImage(any())).thenReturn(qrCodeImage);
        when(taskSearchService.getTaskSearchResponse(anyString(), anyString(), any())).thenReturn(taskSearchResponse);
        when(summonsOrderPdfUtil.fetchSummonsPdfObjectData(anyString(), any(), any())).thenReturn(pdfSummonsRequest);
        when(pdfSummonsOrderRequestService.createPdf(any(), any())).thenReturn(pdfResponse);

        Object result = serviceUrlMappingPdfService.getSVcUrlMappingPdf(pdfRequestobject);

        assertEquals(pdfResponse, result);
        verify(qrCodeImageService).getQrCodeImage(pdfRequestobject);
        verify(taskSearchService).getTaskSearchResponse("ref123", "tenant123", pdfRequestobject.getRequestInfo());
        verify(summonsOrderPdfUtil).fetchSummonsPdfObjectData(qrCodeImage, taskSearchResponse, pdfRequestobject.getRequestInfo());
        verify(pdfSummonsOrderRequestService).createPdf(pdfSummonsRequest, pdfRequestobject);
    }

    @Test
    void testGetSVcUrlMappingPdf_invalidReferenceCode() {
        pdfRequestobject.setReferenceCode("invalid-code");

        CustomException exception = assertThrows(CustomException.class, () -> {
            serviceUrlMappingPdfService.getSVcUrlMappingPdf(pdfRequestobject);
        });

        assertEquals("INVALID_REFERENCE_CODE", exception.getCode());
        assertEquals("The reference code invalid-code is not recognized.", exception.getMessage());

        verify(qrCodeImageService, never()).getQrCodeImage(any());
        verify(taskSearchService, never()).getTaskSearchResponse(anyString(), anyString(), any());
        verify(summonsOrderPdfUtil, never()).fetchSummonsPdfObjectData(anyString(), any(), any());
        verify(pdfSummonsOrderRequestService, never()).createPdf(any(), any());
    }
}
