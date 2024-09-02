package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EFillingCalculationResponse {

    @JsonProperty("ResponseInfo")

    @Valid
    private ResponseInfo responseInfo = null;

    private String caseId;

    @JsonProperty("Calculation")
    @Valid
    private List<Calculation> calculation = null;


    private List<BreakDown> breakDowns;
}
