package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.WitnessPdfRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.pucar.dristi.config.ServiceConstants.PDF_UTILITY_EXCEPTION;


@Component
@Slf4j
public class PdfRequestUtil {

    private final Configuration configuration;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String FILE_STORE_ID_KEY = "fileStoreId";
    private static final String FILES_KEY = "files";
    private static final String DOCUMENT_TYPE_PDF = "application/pdf";

    @Autowired
    public PdfRequestUtil(Configuration configuration, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ByteArrayResource createPdfForWitness(WitnessPdfRequest request, String tenantId) {
        try {
            StringBuilder requestUrl = new StringBuilder();
            requestUrl.append(configuration.getGeneratePdfHost()).append(configuration.getGeneratePdfUrl()).append("?key=")
                    .append(configuration.getWitnessPdfKey()).append("&tenantId=").append(tenantId);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            log.info("Witness Pdf Request ; {}", objectMapper.writeValueAsString(request));
            HttpEntity<WitnessPdfRequest> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<ByteArrayResource> responseEntity = restTemplate.postForEntity(requestUrl.toString(),
                    requestEntity, ByteArrayResource.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error generating PDF for case {}: {}", request, e.getMessage());
            throw new CustomException(PDF_UTILITY_EXCEPTION, "Error generating PDF for case: " + e.getMessage());
        }
    }
}
