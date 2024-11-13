package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.SummonsOrderPdfUtil;
import org.pucar.dristi.web.models.PdfRequest;
import org.pucar.dristi.web.models.PdfSummonsAccusedRequest;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServiceUrlMappingPdfService {

    private final TaskSearchService taskSearchService;
    private final SummonsOrderPdfUtil summonsOrderPdfUtil;
    private final QrCodeImageService qrCodeImageService;
    private final PdfSummonsOrderRequestService pdfSummonsOrderRequestService;

    @Autowired
    public ServiceUrlMappingPdfService(TaskSearchService taskSearchService,
                     SummonsOrderPdfUtil summonsOrderPdfUtil,
                     QrCodeImageService qrCodeImageService,
                     PdfSummonsOrderRequestService pdfSummonsOrderRequestService) {
        this.taskSearchService = taskSearchService;
        this.summonsOrderPdfUtil = summonsOrderPdfUtil;
        this.qrCodeImageService = qrCodeImageService;
        this.pdfSummonsOrderRequestService = pdfSummonsOrderRequestService;
    }

    public Object getSVcUrlMappingPdf(PdfRequest pdfRequestobject) {
        String referenceId = pdfRequestobject.getReferenceId();
        String refCode = pdfRequestobject.getReferenceCode();
        String tenantId = pdfRequestobject.getTenantId();
        RequestInfo requestInfo = pdfRequestobject.getRequestInfo();
        Object pdfresponse = null;

        if ("summons-order".equals(refCode)) {
            String qrCodeImage = qrCodeImageService.getQrCodeImage(pdfRequestobject);
            ResponseEntity<Object> taskSearchResponse = taskSearchService.getTaskSearchResponse(referenceId, tenantId, requestInfo);
            PdfSummonsRequest pdfSummonsRequest = summonsOrderPdfUtil.fetchSummonsPdfObjectData(qrCodeImage, taskSearchResponse, requestInfo);
            pdfresponse = pdfSummonsOrderRequestService.createPdf(pdfSummonsRequest, pdfRequestobject);
        } else if ("summons-accused-qr".equals(refCode)) {
            String qrCodeImage = qrCodeImageService.getQrCodeImage(pdfRequestobject);
            ResponseEntity<Object> taskSearchResponse = taskSearchService.getTaskSearchResponse(referenceId, tenantId, requestInfo);
            PdfSummonsAccusedRequest pdfSummonsAccusedRequest = summonsOrderPdfUtil.fetchSummonsAccusedPdfObjectData(qrCodeImage, taskSearchResponse, requestInfo);
            pdfresponse = pdfSummonsOrderRequestService.createPdf(pdfSummonsAccusedRequest, pdfRequestobject);
        } else {
            throw new CustomException("INVALID_REFERENCE_CODE", "The reference code " + refCode + " is not recognized.");
        }

        return pdfresponse;
    }
}
