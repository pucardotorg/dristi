package org.pucar.dristi.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.pucar.dristi.web.models.SummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SummonsOrderPdfUtil {

    private static final String CASE_DETAILS="caseDetails";
    private static final String RESPONDENT_DETAILS="respondentDetails";
    private static final String SUMMON_DETAILS="summonDetails";

    private final ObjectMapper objectMapper;

    @Autowired
    public SummonsOrderPdfUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PdfSummonsRequest fetchSummonsPdfObjectData(String qrCodeImage, ResponseEntity<Object> taskSearchResponseObject, RequestInfo requestInfo) {
        String courtName=null;
        String summonId=null;
        String respondentName=null;
        String summonIssueDate=null;
        String address=null;
        String caseCharge=null;
        String judgeName=null;
        String hearingDate=null;
        try{
            String responseBodyString = objectMapper.writeValueAsString(taskSearchResponseObject.getBody());
            log.info("Response from the task search: " + responseBodyString);
            String taskDetailsString = JsonPath.parse(responseBodyString).read("$.list[0].taskDetails", String.class);
            // Parse the taskDetails string as JSON
            JsonNode taskDetailsJson = objectMapper.readTree(taskDetailsString);
            // Extract specific fields from taskDetails JSON
            courtName = taskDetailsJson.path(CASE_DETAILS).path("courtName").asText();
            summonId = taskDetailsJson.path(SUMMON_DETAILS).path("summonId").asText();
            respondentName = taskDetailsJson.path(RESPONDENT_DETAILS).path("name").asText();
            summonIssueDate = taskDetailsJson.path(SUMMON_DETAILS).path("issueDate").asText();
            address = taskDetailsJson.path(RESPONDENT_DETAILS).path("address").asText();
            caseCharge = taskDetailsJson.path(CASE_DETAILS).path("caseCharge").asText();
            judgeName = taskDetailsJson.path(CASE_DETAILS).path("judgeName").asText();
            hearingDate = taskDetailsJson.path(CASE_DETAILS).path("hearingDate").asText();
        }
        catch(Exception e){
            throw new CustomException("JSON_PARSING_ERROR","error while parsing the task response to create pdf object");
        }


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

        return PdfSummonsRequest.builder()
                .requestInfo(requestInfo)
                .taskSummon(List.of(summonsRequest))
                .build();
    }
}
