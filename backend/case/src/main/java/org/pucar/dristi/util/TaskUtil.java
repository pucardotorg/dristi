package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.task.TaskRequest;
import org.pucar.dristi.web.models.task.TaskResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TaskUtil {

    private final RestTemplate restTemplate;

    private final Configuration config;

    public TaskUtil(RestTemplate restTemplate, Configuration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public TaskResponse callCreateTask(TaskRequest taskRequest) {
         try {
            StringBuilder uri = new StringBuilder();
            uri.append(config.getTaskServiceHost()).append(config.getTaskServiceCreateEndpoint());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(taskRequest, headers);

            ResponseEntity<TaskResponse> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, TaskResponse.class);
            log.info("Response of create task :: {}",requestEntity.getBody());

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error getting response from Task Service", e);
            throw new CustomException("TASK_CREATE_ERROR", "Error getting response from task Service");
        }
    }
}
