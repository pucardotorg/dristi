package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;


@Slf4j
@Component
public class PendingTaskUtil {

    private final Configuration config;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;
    public PendingTaskUtil(Configuration config, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode callPendingTask(String filingNumber) {
        String url = config.getEsHostUrl() + config.getPendingTaskIndexEndpoint() + config.getPendingTaskSearchPath();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", getESEncodedCredentials());

        String query = getEsQuery(filingNumber);

        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_PENDING_TASK, e);
            throw new CustomException(ERROR_WHILE_FETCHING_PENDING_TASK, e.getMessage());
        }
    }

    public String getESEncodedCredentials() {
        String credentials = config.getEsUsername() + ":" + config.getEsPassword();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
    private String getEsQuery(String filingNumber) {
        return "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"Data.filingNumber\": \"" + filingNumber + "\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"Data.isCompleted\": false\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    public void updatePendingTask(List<JsonNode> pendingTasks) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", getESEncodedCredentials());

        String url = config.getEsHostUrl() + config.getPendingTaskIndexEndpoint() + config.getBulkPath();
        for(JsonNode task: pendingTasks) {
            String requestBody = buildRequestBody(task);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            try {
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            } catch (Exception e) {
                log.error(ERROR_WHILE_FETCHING_PENDING_TASK, e);
                throw new CustomException(ERROR_WHILE_FETCHING_PENDING_TASK, e.getMessage());
            }
        }
    }

    private String buildRequestBody(JsonNode task){
        String taskId = task.get("referenceId").asText();
        StringBuilder bulkRequestBody = new StringBuilder();
        bulkRequestBody.append("{ \"create\": { \"_index\": \"pending-tasks-index\", \"_id\": \"").append(taskId).append("\" } }\n");
        bulkRequestBody.append("{ \"doc\": ").append(task).append(" }\n");
        return bulkRequestBody.toString();
    }
}
