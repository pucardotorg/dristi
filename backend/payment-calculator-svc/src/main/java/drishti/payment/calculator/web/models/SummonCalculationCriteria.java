package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

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
    @Size(min = 6, max = 6, message = "receiverPincode must be 6 digit")
    @NotNull(message = "receiverPincode cannot be null")
    @NotBlank(message = "receiverPincode cannot be null")
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    private String receiverPincode;

    @JsonProperty("tenantId")
    @NotNull
    @NotBlank
    private String tenantId = null;

    @JsonProperty("summonId")
    private String summonId;


}
