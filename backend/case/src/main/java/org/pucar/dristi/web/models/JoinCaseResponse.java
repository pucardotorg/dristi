package org.pucar.dristi.web.models;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinCaseResponse {
    @JsonProperty("ResponseInfo")
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("accessCode")
    @NotNull
    private String accessCode = null;

    @JsonProperty("caseFilingNumber")
    @NotNull
    private String caseFilingNumber = null;

    @JsonProperty("representative")
    private AdvocateMapping representative = null;

    @JsonProperty("litigant")
    private Party litigant = null;
}
