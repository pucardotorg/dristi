package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-24T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPaymentRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("Criteria")
    @Valid
    private List<TaskPaymentCriteria> calculationCriteria = null;


    public TaskPaymentRequest addCalculationCriteriaItem(TaskPaymentCriteria calculationCriteriaItem) {
        if (this.calculationCriteria == null) {
            this.calculationCriteria = new ArrayList<>();
        }
        this.calculationCriteria.add(calculationCriteriaItem);
        return this;
    }
}