package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EFilingParam {

    @JsonProperty("applicationFee")
    private Double applicationFee;

    @JsonProperty("courtFee")
    private Double courtFee;

    @JsonProperty("vakalathnamaFee")
    private Double vakalathnamaFee;

    @JsonProperty("advocateWelfareFund")
    private Double advocateWelfareFund;

    @JsonProperty("advocateClerkWelfareFund")
    private Double advocateClerkWelfareFund;

    @JsonProperty("petitionFeePercentage")
    private Double petitionFeePercentage;

    @JsonProperty("defaultPetitionFee")
    private Double defaultPetitionFee;
}
