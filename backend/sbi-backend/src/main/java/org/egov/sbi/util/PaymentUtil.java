package org.egov.sbi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class PaymentUtil {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentUtil(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void callSbiGateway(Map<String, String> transactionMap) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referrer", "https://dristi-kerala-dev.pucar.org/digit-ui/citizen/home/sbi-epost-payment");
        headers.set("Origin", "https://dristi-kerala-dev.pucar.org/digit-ui/citizen/home/sbi-epost-payment");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("merchIdVal", transactionMap.get("merchantId"));
        formData.add("EncryptTrans", transactionMap.get("encryptedString"));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<Object> responseEntity = restTemplate.exchange(transactionMap.get("transactionUrl"), HttpMethod.POST, requestEntity, Object.class);

            HttpHeaders responseHeaders = responseEntity.getHeaders();
            Object responseBody = responseEntity.getBody();

            log.info("Response Status Code: {}", responseEntity.getStatusCode());
            log.info("Response Headers: {}", responseHeaders);
            log.info("Response Body: {}", objectMapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            log.error("Error while calling SBI Gateway: ", e);
        }
    }
}
