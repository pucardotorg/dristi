package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * the PDF for the case diary. Mostly will be storing the signed PDF, but there is a provision to store unsigned PDF also
 */
@Schema(description = "the PDF for the case diary. Mostly will be storing the signed PDF, but there is a provision to store unsigned PDF also")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-15T12:45:29.792404900+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDiary {
    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull(message = "tenant id cannot be null")
    private String tenantId = null;

    @JsonProperty("caseNumber")
    private String caseNumber = null;

    @JsonProperty("diaryDate")
    @NotNull(message = "Diary Date cannot be null")
    private Long diaryDate = null;

    @JsonProperty("diaryType")
    @NotNull(message = "Diary type cannot be null")
    private String diaryType = null;

    @JsonProperty("judgeId")
    @NotNull(message = "JudgeId cannot be null")
    private String judgeId = null;

    @JsonProperty("documents")
    @Valid
    private List<CaseDiaryDocument> documents = null;

    @JsonProperty("caseDiaryEntries")
    private List<CaseDiaryEntry> caseDiaryEntries = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("date")
    private String date = null;

    @JsonProperty("workflow")
    @Valid
    private Workflow workflow = null;

    @JsonProperty("status")
    private String status = null;


    public CaseDiary addDocumentsItem(CaseDiaryDocument documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

}
