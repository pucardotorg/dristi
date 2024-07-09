package org.pucar.dristi.web.models;

import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Witness
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-19T15:42:53.131831400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Witness {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("caseId")
    @NotNull
    private String caseId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("witnessIdentifier")
    private String witnessIdentifier = null;

    @JsonProperty("individualId")
    @NotNull
    private String individualId = null;

    @JsonProperty("remarks")
    @Size(min = 10, max = 5000)
    private String remarks = null;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;

}
