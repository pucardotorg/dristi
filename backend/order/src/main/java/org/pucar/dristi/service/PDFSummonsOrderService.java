package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFSummonsOrderService {

    @Autowired
    private ServiceUrlMappingPdfService serviceUrlMappingPdfService;

    @Autowired
    private QrCodeImageService qrCodeImageService;

    public ResponseEntity<InputStreamResource> getPdfService(String referenceId, String referenceCode) throws IOException {

        return serviceUrlMappingPdfService.getSVcUrlMapping(referenceId,referenceCode);

    }

}
