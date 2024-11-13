package org.pucar.dristi.util;


import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.pucar.dristi.config.ServiceConstants.CASE_PDF_UTILITY_EXCEPTION;

@Component
@Slf4j
public class CasePdfUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public CasePdfUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ByteArrayResource generateCasePdf(CaseRequest caseRequest, StringBuilder uri) {
        log.info("Starting PDF generation for case filing Number: {}", caseRequest.getCases().getFilingNumber());
        try {
            HttpEntity<CaseRequest> requestEntity = new HttpEntity<>(caseRequest);

            ResponseEntity<ByteArrayResource> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, ByteArrayResource.class);

            ByteArrayResource pdfResource = responseEntity.getBody();
            if (pdfResource == null) {
                log.warn("No PDF data received for case filing Number: {}", caseRequest.getCases().getFilingNumber());
                throw new CustomException(CASE_PDF_UTILITY_EXCEPTION, "Failed to generate PDF, no data received.");
            }

            log.info("PDF generation successful for case filing Number: {}", caseRequest.getCases().getFilingNumber());
            return pdfResource;
        } catch (Exception e) {
            log.error("Error generating PDF for case filing Number {}", caseRequest.getCases().getFilingNumber(), e);
            throw new CustomException(CASE_PDF_UTILITY_EXCEPTION, "Error generating PDF for case: " + e.getMessage());
        }
    }
}
