package org.pucar.dristi.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.function.numeric.Sum;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.service.PDFSummonsOrderService;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.pucar.dristi.web.models.SummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SummonsOrderPdfUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public PdfSummonsRequest fetchSummonsPdfObjectData(String qrCodeImage,ResponseEntity<Object> taskSearchResponseObject) throws JsonProcessingException {

        String responseBodyString = objectMapper.writeValueAsString(taskSearchResponseObject.getBody());
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


        String requestInfoJson = "{\n" +
                "  \"apiId\": \"Rainmaker\",\n" +
                "  \"ver\": \".01\",\n" +
                "  \"action\": \"_get\",\n" +
                "  \"did\": \"1\",\n" +
                "  \"key\": \"\",\n" +
                "  \"msgId\": \"20170310130900|en_IN\",\n" +
                "  \"authToken\": \"2c21ee1f-2b26-47bf-8ce8-39cc88b0ca3f\",\n" +
                "  \"userInfo\": {}\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Object requestInfo = null;
            requestInfo = objectMapper.readValue(requestInfoJson, Object.class);


        SummonsRequest summonsRequest= SummonsRequest.builder()
                .summonId(summonId)
                .courtName(courtName)
                .name(respondentName)
                .issueDate(summonIssueDate)
                .address(address)
                .caseCharge(caseCharge)
                .judgeName(judgeName)
                .hearingDate(hearingDate)
                .embeddedUrl(qrCodeImage)
                .build();

        PdfSummonsRequest pdfSummonsRequest= PdfSummonsRequest.builder()
                .requestInfo(requestInfo)
                .TaskSummon(List.of(summonsRequest))
                .build();

        System.out.println("pbject request for pdf serviceis "+ objectMapper.writeValueAsString(pdfSummonsRequest));

        return pdfSummonsRequest;
    }
}
