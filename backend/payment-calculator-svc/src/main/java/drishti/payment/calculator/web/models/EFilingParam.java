package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EFilingParam {

    @JsonProperty("applicationFee")
    private Double applicationFee;

    @JsonProperty("vakalathnamaFee")
    private Double vakalathnamaFee;

    @JsonProperty("advocateWelfareFund")
    private Double advocateWelfareFund;

    @JsonProperty("advocateClerkWelfareFund")
    private Double advocateClerkWelfareFund;

    @JsonProperty("delayCondonationPeriod")
    private Long delayCondonationPeriod;

    @JsonProperty("delayCondonationFee")
    private Double delayCondonationFee;

    @JsonProperty("petitionFee")
    private Map<String, Range> petitionFee;

}
