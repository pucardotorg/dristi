package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.TaskSearchRequest;
import org.pucar.dristi.web.models.VcEntityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskSearchService {

    private final RestTemplate restTemplate;

    private final Configuration configuration;

    @Autowired
    public TaskSearchService(RestTemplate restTemplate,Configuration configuration) {
        this.restTemplate = restTemplate;
        this.configuration=configuration;
    }

    public ResponseEntity<Object> getTaskSearchResponse(String referenceId, String tenantId,RequestInfo requestInfo) {
        StringBuilder searchTaskUrl= new StringBuilder();
        searchTaskUrl.append(configuration.getTaskSearchHost()).append(configuration.getTaskSearchPath());
        HttpHeaders headers = new HttpHeaders();

        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");

        requestInfo.setAuthToken(requestInfo.getAuthToken());
        VcEntityCriteria criteria= VcEntityCriteria.builder()
                .id(referenceId)
                .build();

        TaskSearchRequest taskSearchRequest= TaskSearchRequest.builder()
                .requestInfo(requestInfo)
                .tenantId(tenantId)
                .criteria(criteria)
                .build();

        HttpEntity<TaskSearchRequest> entity = new HttpEntity<>(taskSearchRequest, headers);
        ResponseEntity<Object> response=null;

        try{
            response = restTemplate.exchange(searchTaskUrl.toString(), HttpMethod.POST, entity, Object.class);
        }
        catch (Exception e){
            throw new CustomException("TASK_SEARCH_ERR","error while fetching the task details:"+ e.getMessage());
        }
        return response;
    }
}
