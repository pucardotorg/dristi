package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.enums.Classification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostalHub {

    @JsonProperty("hubId")
    private String hubId;

    @JsonProperty("pincode")
    @Size(min = 6, max = 6, message = "pincode must be 6 digit")
    @NotNull(message = "pincode cannot be null")
    @NotBlank(message = "pincode cannot be null")
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    private String pincode;

    @JsonProperty("name")
    private String name;

    @JsonProperty("classification")
    @NotNull
    private Classification classification;

    @JsonProperty("tenantId")
    @NotNull(message = "tenantId cannot be null")
    @NotBlank(message = "tenantId cannot be null")
    private String tenantId = null;

    @JsonProperty("rowVersion")
    private Integer rowVersion = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}
