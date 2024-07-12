package org.pucar.dristi.service;

import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;

@Service
public class PdfSummonsOrderRequestService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<InputStreamResource> createNoSave(PdfSummonsRequest requestObject) {
        String url = "http://localhost:9000/pdf-service/v1/_createnosave?key=summons-order&tenantId=pb";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<PdfSummonsRequest> requestEntity = new HttpEntity<>(requestObject, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBody());
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=document.pdf");
            responseHeaders.setContentLength(response.getBody().length);
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }
}
