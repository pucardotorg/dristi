package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A task is created as part of an Order. It will always be linked to an order
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("orderId")
    @NotNull
    @Valid
    private UUID orderId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("taskNumber")
    private String taskNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("createdDate")
    @NotNull
    @Valid
    private Long createdDate = null;

    @JsonProperty("dateCloseBy")
    @Valid
    private Long dateCloseBy = null;

    @JsonProperty("dateClosed")
    @Valid
    private Long dateClosed = null;

    @JsonProperty("taskDescription")
    private String taskDescription = null;

    @JsonProperty("taskType")
    @NotNull
    private String taskType = null;

    @JsonProperty("taskDetails")
    private TaskDetails taskDetails = null;

    @JsonProperty("status")
    @NotNull
    private String status = null;

    @JsonProperty("assignedTo")
    private Object assignedTo = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = new ArrayList<>();

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

}
