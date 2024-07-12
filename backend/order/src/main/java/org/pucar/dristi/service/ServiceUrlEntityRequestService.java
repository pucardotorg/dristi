package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class ServiceUrlEntityRequestService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderSearchService orderSearchService;

    @Autowired
    private Producer producer;

    @Autowired
    private QrCodeImageService qrCodeImageService;

    @Autowired
    private TaskSearchService taskSearchService;

    public void getEntityDetails(Calendar signDate, String serviceUrl, String referenceId) throws JsonProcessingException {
        ResponseEntity<Object> response= taskSearchService.getTaskSearchResponse(referenceId);
        String responseBodyString = objectMapper.writeValueAsString(response.getBody());
        System.out.println("Response from the task search: " + responseBodyString);
        String taskDetailsString = JsonPath.parse(responseBodyString).read("$.list[0].taskDetails", String.class);
        String orderId = JsonPath.parse(responseBodyString).read("$.list[0].orderId", String.class);

        // Parse the taskDetails string as JSON
        JsonNode taskDetailsJson = objectMapper.readTree(taskDetailsString);

        // Extract specific fields from taskDetails JSON
        String courtName = taskDetailsJson.path("caseDetails").path("courtName").asText();
        String summonId = taskDetailsJson.path("summonDetails").path("summonId").asText();
        String respondentName = taskDetailsJson.path("respondentDetails").path("name").asText();
        String summonIssueDate = taskDetailsJson.path("summonDetails").path("issueDate").asText();
        String address = taskDetailsJson.path("respondentDetails").path("address").asText();
        String caseCharge = taskDetailsJson.path("caseDetails").path("caseCharge").asText();
        String judgeName = taskDetailsJson.path("caseDetails").path("judgeName").asText();
        String hearingDate = taskDetailsJson.path("caseDetails").path("hearingDate").asText();



        System.out.println("court name"+ courtName);
        System.out.println("summonId"+ summonId);
        System.out.println("respondant name"+ respondentName);
        System.out.println("summon issue date"+ summonIssueDate);

        String cnrNumber = orderSearchService.searchOrder(orderId);

        CredentialRequest credentialRequest= CredentialRequest.builder()
                .cnrNumber(cnrNumber)
                .courtName(courtName)
                .summonsIssueDate(summonIssueDate)
                .summonId(summonId)
                .orderPdfSignature(signDate)
                .respondentName(respondentName)
                .id(referenceId)
                .module("Pucar.SummonsOrder")
                .build();

        String credentialRequestString = objectMapper.writeValueAsString(credentialRequest);
        System.out.println("task search entity as String: " + credentialRequestString);

        producer.push("create-vc",credentialRequest);
        //qrCodeImageService.getQrImage("Pucar.SummonsOrder", referenceId);

    }
}