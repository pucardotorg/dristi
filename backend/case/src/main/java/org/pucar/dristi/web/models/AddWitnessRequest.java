package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;


@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddWitnessRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("caseFilingNumber")
    @NotNull
    private String caseFilingNumber = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
