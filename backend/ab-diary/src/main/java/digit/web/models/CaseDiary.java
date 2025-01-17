package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
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
    @NotNull

    private String tenantId = null;

    @JsonProperty("caseNumber")

    private String caseNumber = null;

    @JsonProperty("diaryDate")
    @NotNull

    private Long diaryDate = null;

    @JsonProperty("diaryType")
    @NotNull

    private String diaryType = null;

    @JsonProperty("judgeId")
    @NotNull

    private String judgeId = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;

    @JsonProperty("auditDetails")

    @Valid
    private AuditDetails auditDetails = null;


    public CaseDiary addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

}
