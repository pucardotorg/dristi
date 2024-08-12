package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonFormat;
=======
>>>>>>> main
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * A task is created as part of an Order. It will always be linked to an order
 */
@Schema(description = "A task is created as part of an Order. It will always be linked to an order")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
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
=======

>>>>>>> main
    private String tenantId = null;

    @JsonProperty("orderId")
    @NotNull
<<<<<<< HEAD
=======

>>>>>>> main
    @Valid
    private UUID orderId = null;

    @JsonProperty("filingNumber")
<<<<<<< HEAD
    private String filingNumber = null;

    @JsonProperty("taskNumber")
    private String taskNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("createdDate")
    @Valid
    private Long createdDate = null;

    @JsonProperty("dateCloseBy")
    @Valid
    private Long dateCloseBy = null;

    @JsonProperty("dateClosed")
    @Valid
    private Long dateClosed = null;

    @JsonProperty("taskDescription")
=======

    private String filingNumber = null;

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("createdDate")
    @NotNull

    @Valid
    private LocalDate createdDate = null;

    @JsonProperty("dateCloseBy")

    @Valid
    private LocalDate dateCloseBy = null;

    @JsonProperty("dateClosed")

    @Valid
    private LocalDate dateClosed = null;

    @JsonProperty("taskDescription")

>>>>>>> main
    private String taskDescription = null;

    @JsonProperty("taskType")
    @NotNull
<<<<<<< HEAD
    private String taskType = null;

    @JsonProperty("taskDetails")
    private Object taskDetails = null;

    @JsonProperty("amount")
=======

    private String taskType = null;

    @JsonProperty("taskDetails")

    private Object taskDetails = null;

    @JsonProperty("amount")

>>>>>>> main
    @Valid
    private Amount amount = null;

    @JsonProperty("status")
    @NotNull
<<<<<<< HEAD
    private String status = null;

    @JsonProperty("assignedTo")
    private AssignedTo assignedTo = null;

    @JsonProperty("isActive")
=======

    private String status = null;

    @JsonProperty("assignedTo")

    private Object assignedTo = null;

    @JsonProperty("isActive")

>>>>>>> main
    private Boolean isActive = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = new ArrayList<>();

    @JsonProperty("additionalDetails")
<<<<<<< HEAD
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
=======

    private String additionalDetails = null;

    @JsonProperty("auditDetails")

>>>>>>> main
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("workflow")
<<<<<<< HEAD
=======

>>>>>>> main
    @Valid
    private Workflow workflow = null;


    public Task addDocumentsItem(Document documentsItem) {
        this.documents.add(documentsItem);
        return this;
    }

}
