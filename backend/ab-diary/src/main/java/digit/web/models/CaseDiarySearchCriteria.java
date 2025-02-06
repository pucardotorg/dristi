package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.annotation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * CaseDiarySearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-15T12:45:29.792404900+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@OneOf(message = "One of caseId or date must be provided")
public class CaseDiarySearchCriteria {
    @JsonProperty("tenantId")
    @NotNull

    private String tenantId = null;

    @JsonProperty("date")

    private Long date = null;

    @JsonProperty("caseId")

    private String caseId = null;

    @JsonProperty("diaryType")

    private String diaryType = null;

    @JsonProperty("judgeId")
    @NotNull

    private String judgeId = null;


}
