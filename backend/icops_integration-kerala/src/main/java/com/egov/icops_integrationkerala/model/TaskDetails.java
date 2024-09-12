package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Data
@Builder
public class TaskDetails {

    @JsonProperty("summonDetails")
    private SummonsDetails summonDetails = null;

    @JsonProperty("warrantDetails")
    private WarrantDetails warrantDetails = null;

    @JsonProperty("caseDetails")
    private CaseDetails caseDetails = null;

    @JsonProperty("respondentDetails")
    private RespondentDetails respondentDetails = null;

    @JsonProperty("deliveryChannel")
    private DeliveryChannel deliveryChannel = null;
}
