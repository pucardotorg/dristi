package org.egov.eTreasury.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ETreasuryUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public ETreasuryUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> callConnectionService(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();

        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypeList);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.postForEntity(url, requestEntity, responseType);
    }

    public ResponseEntity<Object> callAuthService(String clientId, String clientSecret, String payload, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        httpHeaders.add("clientId", clientId);
        httpHeaders.add("clientSecret", clientSecret);

        HttpEntity<String> httpEntity = new HttpEntity<>(payload, httpHeaders);
        return restTemplate.postForEntity(url, httpEntity, Object.class);
    }

    public <T> ResponseEntity<T> callService(String inputHeaders, String inputBody, String url, Class<T> responseType, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(mediaType));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("input_headers", inputHeaders);
        body.add("input_data", inputBody);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> callRefundService(String clientId, String authToken, String payload, String url,  Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("clientId", clientId);
        headers.add("authToken", authToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        return restTemplate.postForEntity(url, requestEntity, responseType);
    }
}
