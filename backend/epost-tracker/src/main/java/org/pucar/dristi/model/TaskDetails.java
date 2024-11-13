package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
public class TaskDetails {

    @JsonProperty("summonDetails")
    private SummonsDetails summonDetails = null;

    @JsonProperty("caseDetails")
    private CaseDetails caseDetails = null;

    @JsonProperty("respondentDetails")
    private RespondentDetails respondentDetails = null;

    @JsonProperty("deliveryChannel")
    private DeliveryChannel deliveryChannel = null;
}
