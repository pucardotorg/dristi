package org.pucar.dristi.web.models.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;


@Schema(description = "Case overall status topic object")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseOverallStatus {

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("stage")
    private String stage = null;

    @JsonProperty("substage")
    private String substage = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    public CaseOverallStatus(String filingNumber, String tenantId, String stage, String substage) {
        this.filingNumber = filingNumber;
        this.tenantId = tenantId;
        this.stage = stage;
        this.substage = substage;
    }

}
