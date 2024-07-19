package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.PdfRequest;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dristi.dev.task.search.host}")
    private String taskSearchHost;

    @Value("${dristi.dev.task.search.url}")
    private String taskSearchPath;

    public ResponseEntity<Object> getTaskSearchResponse(String referenceId, String tenantId,RequestInfo requestInfo) {
        StringBuilder searchTaskUrl= new StringBuilder();
        searchTaskUrl.append(taskSearchHost).append(taskSearchPath);
        HttpHeaders headers = new HttpHeaders();

        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");
       /* headers.set("Origin", "http://localhost:3000");
        headers.set("Referer", "http://localhost:3000/digit-ui/employee/dristi/registration-requests/details/CLERK-2024-04-29-000123?individualId=IND-2024-04-29-000144&isAction=true");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        headers.set("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Linux\"");*/

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
