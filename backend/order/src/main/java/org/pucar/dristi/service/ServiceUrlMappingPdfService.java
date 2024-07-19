package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.util.SummonsOrderPdfUtil;
import org.pucar.dristi.web.models.PdfRequest;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ServiceUrlMappingPdfService {

    @Autowired
    private TaskSearchService taskSearchService;

    @Autowired
    private SummonsOrderPdfUtil summonsOrderPdfUtil;

    @Autowired
    private QrCodeImageService qrCodeImageService;

    @Autowired
    private PdfSummonsOrderRequestService pdfSummonsOrderRequestService;

    public Object getSVcUrlMappingPdf(PdfRequest pdfRequestobject) {
        String referenceId=pdfRequestobject.getReferenceId();
        String refCode= pdfRequestobject.getReferenceCode();
        String tenantId= pdfRequestobject.getTenantId();
        RequestInfo requestInfo= pdfRequestobject.getRequestInfo();
        Object pdfresponse = null;
        switch (refCode) {
            case "summons-order":
                String qrCodeImage = qrCodeImageService.getQrCodeImage(pdfRequestobject);
                ResponseEntity<Object> taskSearchResponse = taskSearchService.getTaskSearchResponse(referenceId,tenantId,requestInfo);
                PdfSummonsRequest pdfSummonsRequest = summonsOrderPdfUtil.fetchSummonsPdfObjectData(qrCodeImage, taskSearchResponse, requestInfo);
                pdfresponse = pdfSummonsOrderRequestService.createPdf(pdfSummonsRequest,pdfRequestobject);
        }
        return pdfresponse;
    }
}
