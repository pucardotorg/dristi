package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestInfoGenerator {

    private final ObjectMapper objectMapper;

    private final IcopsConfiguration configs;

    private final RestTemplate restTemplate;

    @Autowired
    public RequestInfoGenerator(ObjectMapper objectMapper, IcopsConfiguration configs, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.configs = configs;
        this.restTemplate = restTemplate;
    }

    private static final String AUTH_HEADER = "Basic " + Base64.getEncoder()
            .encodeToString("egov-user-client:".getBytes());

    public RequestInfo generateSystemRequestInfo() {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("username", configs.getUsername());
            formData.put("password", configs.getPassword());
            formData.put("tenantId", "kl");
            formData.put("userType", "EMPLOYEE");
            formData.put("scope", "read");
            formData.put("grant_type", "password");

            // Convert formData to URL-encoded string
            StringBuilder formBody = new StringBuilder();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (!formBody.isEmpty()) {
                    formBody.append("&");
                }
                formBody.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
            }

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-cache");
            headers.set("Connection", "keep-alive");
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Authorization", AUTH_HEADER);

            // Set up the request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(formBody.toString(), headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    configs.getUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String responseBody = responseEntity.getBody();
            log.info("Response data: {}", responseBody);

            // Parse response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(responseBody);

            String accessToken = jsonResponse.get("access_token").asText();
            JsonNode userInfo = jsonResponse.get("UserRequest");
            User user = objectMapper.treeToValue(userInfo, User.class);

            Map<String, Object> requestInfo = new HashMap<>();
            requestInfo.put("apiId", "Rainmaker");
            requestInfo.put("authToken", accessToken);
            requestInfo.put("userInfo", user);

            return mapper.convertValue(requestInfo, RequestInfo.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
