package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.UUID;

/**
 * CaseDiarySearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-15T12:45:29.792404900+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDiaryExistCriteria {

    @JsonProperty("id")
    @NotNull

    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull

    private String tenantId = null;

}
