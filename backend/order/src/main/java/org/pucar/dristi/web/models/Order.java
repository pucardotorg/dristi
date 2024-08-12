package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

<<<<<<< HEAD
=======
import java.time.LocalDate;
>>>>>>> main
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks
 */
@Schema(description = "An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:13:43.389623100+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
<<<<<<< HEAD

    @JsonProperty("id")
=======
    @JsonProperty("id")

>>>>>>> main
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
<<<<<<< HEAD
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
    @Valid
    private Long createdDate = null;

    @JsonProperty("issuedBy")
    private IssuedBy issuedBy = null;
=======

    private String tenantId = null;

    @JsonProperty("filingNumber")

    private String filingNumber = null;

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("applicationNumber")

    private List<String> applicationNumber = new ArrayList<>();

    @JsonProperty("hearingNumber")

    @Valid
    private UUID hearingNumber = null;

    @JsonProperty("orderNumber")

    @Size(min = 24, max = 256)
    private String orderNumber = null;

    @JsonProperty("createdDate")
    @NotNull

    @Valid
    private LocalDate createdDate = null;

    @JsonProperty("issuedBy")

    private Object issuedBy = null;
>>>>>>> main

    @JsonProperty("orderType")
    @NotNull
    @Valid
<<<<<<< HEAD
    private String orderType = null;

    @JsonProperty("orderCategory")
=======
    private List<UUID> orderType = new ArrayList<>();

    @JsonProperty("orderCategory")

>>>>>>> main
    private String orderCategory = null;

    @JsonProperty("status")
    @NotNull
<<<<<<< HEAD
    private String status = null;

    @JsonProperty("comments")
=======

    private String status = null;

    @JsonProperty("comments")

>>>>>>> main
    private String comments = null;

    @JsonProperty("isActive")
    @NotNull
<<<<<<< HEAD
    private Boolean isActive = null;

    @JsonProperty("statuteSection")
=======

    private Boolean isActive = null;

    @JsonProperty("statuteSection")

>>>>>>> main
    @Valid
    private StatuteSection statuteSection = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = null;

<<<<<<< HEAD
    @JsonProperty("orderDetails")
    private Object orderDetails = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
=======
    @JsonProperty("additionalDetails")

    private String additionalDetails = null;

    @JsonProperty("auditDetails")

>>>>>>> main
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("workflow")
<<<<<<< HEAD
    @Valid
    private Workflow workflow = null;

=======

    @Valid
    private Workflow workflow = null;


    public Order addApplicationIdsItem(String applicationNumbersItem) {
        this.applicationNumber.add(applicationNumbersItem);
        return this;
    }

    public Order addOrderTypeItem(UUID orderTypeItem) {
        this.orderType.add(orderTypeItem);
        return this;
    }

    public Order addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

>>>>>>> main
}
