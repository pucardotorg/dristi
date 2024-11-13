package org.egov.transformer.models;

import org.egov.common.contract.models.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationData {

    @JsonProperty("applicationDetails")
    @Valid
    private Application applicationDetails = null;

    @JsonProperty("history")
    private Object history = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;


}
