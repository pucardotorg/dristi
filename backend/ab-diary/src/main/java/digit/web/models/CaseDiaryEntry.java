package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * individual diary line item i.e. business of the day. A set of these are picked up to create the final A or B diary. If items are picked up using tenant, JudgeId and date, then it forms A diary. If they are picked up using tenant, JudgeId and caseId, then it forms B diary
 */
@Schema(description = "individual diary line item i.e. business of the day. A set of these are picked up to create the final A or B diary. If items are picked up using tenant, JudgeId and date, then it forms A diary. If they are picked up using tenant, JudgeId and caseId, then it forms B diary")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-15T12:45:29.792404900+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDiaryEntry   {
        @JsonProperty("id")
          @NotNull

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

                private String tenantId = null;

        @JsonProperty("entryDate")
          @NotNull

                private Long entryDate = null;

        @JsonProperty("caseNumber")

                private String caseNumber = null;

        @JsonProperty("judgeId")
          @NotNull

                private String judgeId = null;

        @JsonProperty("businessOfDay")
          @NotNull

        @Size(max=1024)         private String businessOfDay = null;

        @JsonProperty("referenceId")

                private String referenceId = null;

        @JsonProperty("referenceType")

                private String referenceType = null;

        @JsonProperty("hearingDate")

                private Long hearingDate = null;

        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;


}
