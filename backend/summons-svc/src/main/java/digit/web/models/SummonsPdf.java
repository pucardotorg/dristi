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
}