package org.egov.eTreasury.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CollectionsUtil {
    
    private final RestTemplate restTemplate;

    @Autowired
    public CollectionsUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> callService(PaymentRequest request, String collectionsUrl, String paymentCreateEndPoint) {
        StringBuilder url = new StringBuilder(collectionsUrl);
        url.append(paymentCreateEndPoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url.toString(), requestEntity, String.class);

        log.info("Payment service response: {}", responseEntity);
        log.debug("Payment service response body: {}", responseEntity.getBody());

        return responseEntity;
    }
}
