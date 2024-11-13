package org.pucar.dristi.util;

import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.web.model.OcrRequest;
import org.pucar.dristi.web.model.OcrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private final RestTemplate restTemplate;

    @Autowired
    public Util(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<OcrResponse> callOCR(String url, Resource resource, OcrRequest ocrRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", resource);

        if (ocrRequest.getKeywords() != null) {
            body.add(ServiceConstants.OCR_REQUEST_PARAMETER_WORDS_CHECK_LIST, ocrRequest.getKeywords());
        }
        if (ocrRequest.getDistanceCutoff() != null) {
            body.add(ServiceConstants.OCR_REQUEST_PARAMETER_DISTANCE_CUTOFF, ocrRequest.getDistanceCutoff());
        }
        if (ocrRequest.getDocumentType() != null) {
            body.add(ServiceConstants.OCR_REQUEST_PARAMETER_DOCUMENT_TYPE, ocrRequest.getDocumentType().toLowerCase());
        }
        if (ocrRequest.getExtractData() != null) {
            body.add(ServiceConstants.OCR_REQUEST_PARAMETER_EXTRACT_DATA, ocrRequest.getExtractData());
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                OcrResponse.class
        );
    }
}
