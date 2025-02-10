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
public class JoiningFor {

    @JsonProperty("litigantId")
    private String litigantId;

    @JsonProperty("isReplacingExistingAdv")
    private Boolean isReplacingExistingAdv;

    @JsonProperty("accused")
    private Boolean accused;
}
