package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummonsRequest {
    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("caseCharge")
    private String caseCharge;

    @JsonProperty("judgeName")
    private String judgeName;

    @JsonProperty("courtName")
    private String courtName;

    @JsonProperty("hearingDate")
    private String hearingDate;

    @JsonProperty("summonId")
    private String summonId;

    @JsonProperty("embeddedUrl")
    private String embeddedUrl;
}
