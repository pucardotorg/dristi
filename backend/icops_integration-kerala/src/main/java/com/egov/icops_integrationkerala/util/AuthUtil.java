package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AuthUtil {

    private RestTemplate restTemplate;

    private final IcopsConfiguration config;

    @Autowired
    public AuthUtil(RestTemplate restTemplate, IcopsConfiguration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public AuthResponse authenticateAndGetToken() throws Exception {
        log.info("Getting Auth Token from Icop");
        // Define the URL for authentication
        String authUrl = config.getIcopsUrl() + config.getAuthEndpoint();

        // Set up the request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", config.getClientId());
        requestBody.add("client_secret", config.getClientSecret());
        requestBody.add("grant_type", config.getGrantType());

        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HTTP entity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<AuthResponse> responseEntity = restTemplate.postForEntity(authUrl, requestEntity, AuthResponse.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Error occurred at authentication ", e);
            throw new Exception("Error occurred when authenticating ICops");
        }
    }
}
