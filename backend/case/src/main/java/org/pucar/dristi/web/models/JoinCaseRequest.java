package org.pucar.dristi.web.models;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.models.AuditDetails;

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
public class JoinCaseRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

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

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
