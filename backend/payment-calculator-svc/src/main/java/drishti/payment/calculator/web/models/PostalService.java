package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import digit.models.coremodels.AuditDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * PostalPinService
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostalService {

    @JsonProperty("postalServiceId")
    private String postalServiceId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("pincode")
    @Valid
    private String pincode = null;

    @JsonProperty("rowVersion")
    private Integer rowVersion = null;

    @JsonProperty("postalHubId")
    private String postalHubId;

    @JsonProperty("distanceKM")
    private Double distanceKM;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;


}
