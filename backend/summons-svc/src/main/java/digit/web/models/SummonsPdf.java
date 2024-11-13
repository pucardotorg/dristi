package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummonsPdf  {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("cnrNumber")
    private String cnrNumber;

    @JsonProperty("filingNumber")
    private String filingNumber;

    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("hearingDate")
    private String hearingDate;

    @JsonProperty("respondentName")
    private String respondentName;

    @JsonProperty("respondentAddress")
    private String respondentAddress;

    @JsonProperty("caseName")
    private String caseName;

    @JsonProperty("judgeName")
    private String judgeName;

    @JsonProperty("courtName")
    private String courtName;

    @JsonProperty("caseYear")
    private String caseYear;

    @JsonProperty("caseNumber")
    private String caseNumber;

    @JsonProperty("embeddedUrl")
    private String embeddedUrl;

    @JsonProperty("complainantName")
    private String complainantName;

    @JsonProperty("complainantAddress")
    private String complainantAddress;

    @JsonProperty("accessCode")
    private String accessCode;

    @JsonProperty("witnessName")
    private String witnessName;

    @JsonProperty("witnessAddress")
    private String witnessAddress;

    @JsonProperty("oneSuretyAmount")
    private String oneSuretyAmount;

    @JsonProperty("twoSuretyAmount")
    private String twoSuretyAmount;

    @JsonProperty("executorName")
    private String executorName;

    @JsonProperty("bailableAmount")
    private String bailableAmount;

    @JsonProperty("courtCaseNumber")
    private String courtCaseNumber;
}