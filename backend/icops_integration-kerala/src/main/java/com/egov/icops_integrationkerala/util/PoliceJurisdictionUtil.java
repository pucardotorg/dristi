package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import com.egov.icops_integrationkerala.model.Location;
import com.egov.icops_integrationkerala.model.LocationBasedJurisdiction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PoliceJurisdictionUtil {

    private final RestTemplate restTemplate;

    private final IcopsConfiguration config;

    private final ObjectMapper objectMapper;

    @Autowired
    public PoliceJurisdictionUtil(RestTemplate restTemplate, IcopsConfiguration config, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public LocationBasedJurisdiction getLocationBasedJurisdiction(AuthResponse authResponse, Location location) throws Exception {
        String icopsUrl = config.getIcopsUrl() + config.getLocationBasedJurisdiction();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authResponse.getAccessToken());
        HttpEntity<Location> requestEntity = new HttpEntity<>(location, headers);

        try {
            log.info("Request Headers: {}", headers);
            log.info("Request Body: {}", objectMapper.writeValueAsString(location));
            // Send the request and get the response
            ResponseEntity<Object> responseEntity =
                    restTemplate.postForEntity(icopsUrl, requestEntity, Object.class);
            // Print the response body and status code
            log.info("Status Code: {}", responseEntity.getStatusCode());
            log.info("Response Body: {}", responseEntity.getBody());
            return objectMapper.convertValue(responseEntity.getBody(), LocationBasedJurisdiction.class);
        } catch (RestClientException | JsonProcessingException e) {
            log.error("Error occurred when getting location jurisdiction ", e);
            throw new CustomException("ICOPS_LOCATION_JURISDICTION_ERROR","Error occurred when getting location jurisdiction");
        }
    }
}
