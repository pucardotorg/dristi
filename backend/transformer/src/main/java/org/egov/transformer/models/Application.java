package org.egov.transformer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Application
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @JsonProperty("id")

    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull

    private String tenantId = null;

    @JsonProperty("caseId")
    @NotNull
    private String caseId = null;

    @JsonProperty("filingNumber")

    private String filingNumber = null;

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("referenceId")

    @Valid
    private UUID referenceId = null;

    @NotNull
    private Long createdDate = null;

    @JsonProperty("createdBy")

    @Valid
    private UUID createdBy = null;

    @JsonProperty("onBehalfOf")
    @Valid
    private List<UUID> onBehalfOf = null;

    @JsonProperty("applicationType")
    @NotNull
    @Valid
    private String applicationType = null;

    @JsonProperty("applicationNumber")

    @Size(min = 24, max = 48)
    private String applicationNumber = null;

    @JsonProperty("issuedBy")

    private IssuedBy issuedBy = null;

    @JsonProperty("status")
    @NotNull

    private String status = null;

    @JsonProperty("comment")
    private List<Comment> comment = new ArrayList<>();

    @JsonProperty("isActive")
    @NotNull

    private Boolean isActive = null;

    @JsonProperty("statuteSection")

    @Valid
    private StatuteSection statuteSection = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;

    @JsonProperty("auditDetails")

    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("workflow")

    @Valid
    private Workflow workflow = null;

    @JsonProperty("orderDetails")
    private Order orderDetails;


    public Application addOnBehalfOfItem(UUID onBehalfOfItem) {
        if (this.onBehalfOf == null) {
            this.onBehalfOf = new ArrayList<>();
        }
        this.onBehalfOf.add(onBehalfOfItem);
        return this;
    }

    public Application addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

    public boolean isResponseRequired() {
        if (additionalDetails instanceof Map) {
            Map<String, Object> detailsMap = (Map<String, Object>) additionalDetails;
            if (detailsMap.containsKey("isResponseRequired")) {
                return (boolean) detailsMap.get("isResponseRequired");
            }
        }
        return false;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        String formattedDate = "";
        if (null != this.createdDate) {
            if (this.createdDate > 0) {
                formattedDate = Instant.ofEpochMilli(this.createdDate)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }
        return formattedDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String date) throws ParseException {
        try {
            this.createdDate = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.createdDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(date).getTime();
        }
    }

}