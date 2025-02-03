package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Schema
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDiaryDocument {
    @JsonProperty("id")
    @Valid
    private UUID id;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("fileStoreId")
    private String fileStoreId;

    @JsonProperty("documentUid")
    private String documentUid;

    @JsonProperty("documentName")
    private String documentName;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("caseDiaryId")
    private String caseDiaryId;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("additionalDetails")
    private Object additionalDetails;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails;

}
