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
public class SpeedPostConfigParams {

    @JsonProperty("pageWeight")
    private Double pageWeight;

    @JsonProperty("weightUnit")
    private String weightUnit;

    @JsonProperty("printingFeePerPage")
    private Double printingFeePerPage;

    @JsonProperty("businessFee")
    private Double businessFee;

    @JsonProperty("envelopeChargeIncludingGST")
    private Double envelopeChargeIncludingGst;

    @JsonProperty("GSTPercentage")
    private Double gstPercentage;

    @JsonProperty("courtFee")
    private Double courtFee;

    @JsonProperty("applicationFee")
    private Double applicationFee;

    @JsonProperty("speedPost")
    private SpeedPost speedPost;
}