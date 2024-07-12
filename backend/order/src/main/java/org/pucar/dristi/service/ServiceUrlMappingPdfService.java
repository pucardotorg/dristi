package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pucar.dristi.util.SummonsOrderPdfUtil;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.pucar.dristi.web.models.QrCodeRequest;
import org.pucar.dristi.web.models.SummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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

    public ResponseEntity<InputStreamResource> getSVcUrlMapping(String referenceId, String refCode) throws IOException {
        String urlMapping = null;
        ResponseEntity<InputStreamResource> pdfresponse = null;
        switch (refCode) {
            case "summon":
                urlMapping = "https://dristi-dev.pucar.org/task/v1/search";
                String qrCodeImage = qrCodeImageService.getQrCodeImage(referenceId);
                ResponseEntity<Object> taskSearchResponse = taskSearchService.getTaskSearchResponse(referenceId);
                PdfSummonsRequest pdfSummonsRequest = summonsOrderPdfUtil.fetchSummonsPdfObjectData(qrCodeImage, taskSearchResponse);
                pdfresponse = pdfSummonsOrderRequestService.createNoSave(pdfSummonsRequest);
        }
        return pdfresponse;
    }
}
