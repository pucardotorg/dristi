package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * Summon
 */
@Validated
@Data
@Builder
public class SummonsDetails {

    @JsonProperty("summonId")
    private String summonId = null;

    @JsonProperty("issueDate")
    private Long issueDate;

    @JsonProperty("docType")
    private String docType;

    @JsonProperty("docSubType")
    private String docSubType;

    @JsonProperty("partyType")
    private String partyType;
}
