package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.web.models.TaskSearchRequest;
import org.pucar.dristi.web.models.VcEntityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskSearchService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Object> getTaskSearchResponse(String referenceId) throws JsonProcessingException {
        String url = "https://dristi-dev.pucar.org/task/v1/search";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Origin", "http://localhost:3000");
        headers.set("Referer", "http://localhost:3000/digit-ui/employee/dristi/registration-requests/details/CLERK-2024-04-29-000123?individualId=IND-2024-04-29-000144&isAction=true");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        headers.set("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Linux\"");

        RequestInfo requestInfo= new RequestInfo();
        requestInfo.setAuthToken("94fca590-d0ed-4ec5-8157-47815b15a47b");

        VcEntityCriteria criteria= VcEntityCriteria.builder()
                .id(referenceId)
                .build();

        TaskSearchRequest taskSearchRequest= TaskSearchRequest.builder()
                .requestInfo(requestInfo)
                .tenantId("pg")
                .criteria(criteria)
                .build();

        HttpEntity<TaskSearchRequest> entity = new HttpEntity<>(taskSearchRequest, headers);

        String entityString = objectMapper.writeValueAsString(entity);
        System.out.println("task search entity as String: " + entityString);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        return response;
    }
}
