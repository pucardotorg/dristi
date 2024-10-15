package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummary {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("caseTitle")
    private String caseTitle;

    @JsonProperty("filingDate")
    private Long filingDate;

    @JsonProperty("statutesAndSections")
    private String statutesAndSections;

    @JsonProperty("stage")
    private String stage;

    @JsonProperty("subStage")
    private String subStage;

    @JsonProperty("outcome")
    private String outcome;

    @JsonProperty("litigants")
    private List<PartySummary> litigants;

    @JsonProperty("representatives")
    private List<RepresentativeSummary> representatives;

    @JsonProperty("judge")
    private Judge judge;
}
