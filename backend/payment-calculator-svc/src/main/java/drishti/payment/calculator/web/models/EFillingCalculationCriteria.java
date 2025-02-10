package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EFillingCalculationCriteria {

    @JsonProperty("checkAmount")
    @NotNull(message = "Check amount cannot be null")
    private Double checkAmount;

    @JsonProperty("numberOfApplication")
    private Integer numberOfApplication;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("filingNumber")
    @NotNull(message = "Filing number cannot be null")
    private String filingNumber = null;

    @JsonProperty("delayCondonation")
    private Long delayCondonation = 0L;

    @JsonProperty("isDelayCondonation")
    private Boolean isDelayCondonation = false;

}
