package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class PaymentWorkflowRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("PaymentWorkflows")
    @Size(min = 1)
    @Valid
    private List<PaymentWorkflow> paymentWorkflows;


}
