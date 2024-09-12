package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.egov.icops_integrationkerala.model.AuthResponse;
import com.egov.icops_integrationkerala.model.ChannelMessage;
import com.egov.icops_integrationkerala.model.ProcessRequest;
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
public class ProcessRequestUtil {

    private final RestTemplate restTemplate;

    private final IcopsConfiguration config;

    private final ObjectMapper objectMapper;

    @Autowired
    public ProcessRequestUtil(RestTemplate restTemplate, IcopsConfiguration config, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public ChannelMessage callProcessRequest(AuthResponse authResponse, ProcessRequest processRequest) throws Exception {
        String icopsUrl = config.getIcopsUrl() + config.getProcessRequestEndPoint();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authResponse.getAccessToken());
        HttpEntity<ProcessRequest> requestEntity = new HttpEntity<>(processRequest, headers);

        try {
            log.info("Request Headers: {}", headers);
            log.info("Request Body: {}", objectMapper.writeValueAsString(processRequest));
            // Send the request and get the response
            ResponseEntity<Object> responseEntity =
                    restTemplate.postForEntity(icopsUrl, requestEntity, Object.class);
            // Print the response body and status code
            log.info("Status Code: {}", responseEntity.getStatusCode());
            log.info("Response Body: {}", responseEntity.getBody());
            return objectMapper.convertValue(responseEntity.getBody(), ChannelMessage.class);
        } catch (RestClientException e) {
            log.error("Error occurred when sending Process Request ", e);

            return ChannelMessage.builder().acknowledgementStatus("FAILURE").failureMsg("Failed to connect to ICOPS").build();
        }
    }
}
