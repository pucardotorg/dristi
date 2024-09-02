package org.egov.transformer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks
 */
//@Schema(description = "An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:13:43.389623100+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("applicationNumber")
    private List<String> applicationNumber = new ArrayList<>();

    @JsonProperty("hearingNumber")
    @Valid
    private String hearingNumber = null;

    @JsonProperty("orderNumber")
    @Size(min = 24, max = 256)
    private String orderNumber = null;

    @JsonProperty("linkedOrderNumber")
    @Size(min = 24, max = 256)
    private String linkedOrderNumber = null;

    @JsonProperty("createdDate")
    @NotNull
    @Valid
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate = null;

    @JsonProperty("issuedBy")
    private IssuedBy issuedBy = null;

    @JsonProperty("orderType")
    @NotNull
    @Valid
    private String orderType = null;

    @JsonProperty("orderCategory")
    private String orderCategory = null;

    @JsonProperty("status")
    @NotNull
    private String status = null;

    @JsonProperty("comments")
    private String comments = null;

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

    public Order addApplicationIdsItem(String applicationNumbersItem) {
        this.applicationNumber.add(applicationNumbersItem);
        return this;
    }

    public Order addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

}
