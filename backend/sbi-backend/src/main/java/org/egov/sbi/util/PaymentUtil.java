package org.egov.sbi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.sbi.config.PaymentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Slf4j
public class PaymentUtil {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final PaymentConfiguration configuration;

    @Autowired
    public PaymentUtil(RestTemplate restTemplate, ObjectMapper objectMapper, PaymentConfiguration configuration) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.configuration = configuration;
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

    public ResponseEntity<String> doubleVerificationRequest(Map<String, String> params) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        StringBuilder requestBody = new StringBuilder();
        params.forEach((key, value) -> {
            if (!requestBody.isEmpty()) {
                requestBody.append("&");
            }
            requestBody.append(UriComponentsBuilder.fromUriString("").queryParam(key, value).build().getQuery());
        });

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        log.info("request body: {}", requestEntity.getBody());
        return restTemplate.postForEntity(configuration.getSbiDoubleVerificationUrl(), requestEntity, String.class);
    }
}
