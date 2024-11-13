package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummonCalculationCriteria {

    @JsonProperty("channelId")
    @NotNull(message = " channelId cannot be null")
    @NotBlank(message = " channelId cannot be null")
    private String channelId;

    @JsonProperty("receiverPincode")
    private String receiverPincode;

    @JsonProperty("tenantId")
    @NotNull
    @NotBlank
    private String tenantId = null;

    @JsonProperty("summonId")
    private String summonId;


}
