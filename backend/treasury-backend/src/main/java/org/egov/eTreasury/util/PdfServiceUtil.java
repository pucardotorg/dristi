package org.egov.eTreasury.util;


import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.model.TreasuryPaymentRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.egov.eTreasury.config.ServiceConstants.PDFSERVICE_UTILITY_EXCEPTION;

@Component
@Slf4j
public class PdfServiceUtil {

    private final RestTemplate restTemplate;

    private final PaymentConfiguration config;

    @Autowired
    public PdfServiceUtil(RestTemplate restTemplate, PaymentConfiguration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public ByteArrayResource generatePdfFromPdfService(TreasuryPaymentRequest pdfRequest) {
        try {
            StringBuilder uri = new StringBuilder();
            uri.append(config.getPdfServiceHost())
                    .append(config.getPdfServiceEndpoint())
                    .append("?tenantId=").append(config.getEgovStateTenantId()).append("&key=").append(config.getPdfTemplateKey());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TreasuryPaymentRequest> requestEntity = new HttpEntity<>(pdfRequest, headers);

            ResponseEntity<ByteArrayResource> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, ByteArrayResource.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error getting response from Pdf Service", e);
            throw new CustomException(PDFSERVICE_UTILITY_EXCEPTION, "Error getting response from Pdf Service");
        }
    }
}
