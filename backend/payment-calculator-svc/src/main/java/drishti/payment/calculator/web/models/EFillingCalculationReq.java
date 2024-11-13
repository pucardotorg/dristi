package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EFillingCalculationReq {


    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("EFillingCalculationCriteria")
    @Valid
    private List<EFillingCalculationCriteria> calculationCriteria = null;


    public EFillingCalculationReq addCalculationCriteriaItem(EFillingCalculationCriteria calculationCriteriaItem) {
        if (this.calculationCriteria == null) {
            this.calculationCriteria = new ArrayList<>();
        }
        this.calculationCriteria.add(calculationCriteriaItem);
        return this;
    }
}
