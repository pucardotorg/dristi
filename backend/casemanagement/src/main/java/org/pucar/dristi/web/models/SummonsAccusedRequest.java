package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummonsAccusedRequest {

    @JsonProperty("taskNumber")
    private String taskNumber;

    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("respondentName")
    private String respondentName;

    @JsonProperty("respondentAddress")
    private String respondentAddress;

    @JsonProperty("caseCharge")
    private String caseCharge;

    @JsonProperty("caseNumber")
    private String caseNumber;

    @JsonProperty("caseYear")
    private String caseYear;

    @JsonProperty("judgeName")
    private String judgeName;

    @JsonProperty("caseName")
    private String caseName;

    @JsonProperty("courtName")
    private String courtName;

    @JsonProperty("hearingDate")
    private String hearingDate;

    @JsonProperty("embeddedUrl")
    private String embeddedUrl;
}
