package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.PendingTask;
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

    private final IndexerUtils indexerUtils;
    public PendingTaskUtil(Configuration config, RestTemplate restTemplate, ObjectMapper objectMapper, IndexerUtils indexerUtils) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.indexerUtils = indexerUtils;
    }

    public JsonNode callPendingTask(String filingNumber) {
        String url = config.getEsHostUrl() + config.getPendingTaskIndexEndpoint() + config.getPendingTaskSearchPath();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", getESEncodedCredentials());

        String query = getEsQuery(filingNumber);

        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
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
                "            \"Data.filingNumber.keyword\":" + "\"" + filingNumber + "\"" +
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

    public void updatePendingTask(List<JsonNode> pendingTasks) throws Exception {

        String url = config.getEsHostUrl() + config.getBulkPath();
        for(JsonNode task: pendingTasks) {
            PendingTask pendingTask = objectMapper.convertValue( task.get("_source").get("Data"), PendingTask.class);
            String requestBody = indexerUtils.buildPayload(pendingTask);
            indexerUtils.esPostManual(url, requestBody);
        }
    }
}
