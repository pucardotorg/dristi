package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceUrlEntityRequestService {

    private final ObjectMapper objectMapper;
    private final OrderSearchService orderSearchService;
    private final TaskSearchService taskSearchService;

    @Autowired
    public ServiceUrlEntityRequestService(ObjectMapper objectMapper,
                     OrderSearchService orderSearchService,
                     TaskSearchService taskSearchService) {
        this.objectMapper = objectMapper;
        this.orderSearchService = orderSearchService;
        this.taskSearchService = taskSearchService;
    }

    public CredentialRequest getEntityDetails(String signDate, VcCredentialRequest vcCredentialRequest) {
        String referenceId= vcCredentialRequest.getReferenceId();
        String tenantId= vcCredentialRequest.getTenantId();
        RequestInfo requestInfo= vcCredentialRequest.getRequestInfo();
        ResponseEntity<Object> response= taskSearchService.getTaskSearchResponse(referenceId,tenantId,requestInfo);
        String taskDetailsString=null;
        String orderId=null;
        String courtName=null;
        String summonId= null;
        String respondentName=null;
        String summonIssueDate=null;
        try{
            String responseBodyString = objectMapper.writeValueAsString(response.getBody());
            log.info("Response from the task search: " + responseBodyString);
            Object taskDetails = JsonPath.parse(responseBodyString).read("$.list[0].taskDetails");
            taskDetailsString = objectMapper.writeValueAsString(taskDetails);
            orderId = JsonPath.parse(responseBodyString).read("$.list[0].orderId", String.class);


        // Parse the taskDetails string as JSON
        JsonNode taskDetailsJson = objectMapper.readTree(taskDetailsString);

        courtName = taskDetailsJson.path("caseDetails").path("courtName").asText();
        summonId = taskDetailsJson.path("summonDetails").path("summonId").asText();
        respondentName = taskDetailsJson.path("respondentDetails").path("name").asText();
        summonIssueDate = taskDetailsJson.path("summonDetails").path("issueDate").asText();
        }
        catch(Exception e){
            throw new CustomException("JSON_PARSING_ERROR","error while parsing task search response"+e.getMessage());
        }
        String cnrNumber = orderSearchService.searchOrder(orderId,tenantId,requestInfo);


        return CredentialRequest.builder()
                .cnrNumber(cnrNumber)
                .courtName(courtName)
                .summonsIssueDate(summonIssueDate)
                .summonId(summonId)
                .orderPdfSignature(signDate)
                .respondentName(respondentName)
                .id(referenceId)
                .module(vcCredentialRequest.getModuleName())
                .document(referenceId)
                .build();

    }
}