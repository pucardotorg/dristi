package org.egov.transformer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A task is created as part of an Order. It will always be linked to an order
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
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


    @NotNull
    private Long createdDate = null;

    private Long dateCloseBy = null;

    private Long dateClosed = null;

    @JsonProperty("taskDescription")
    private String taskDescription = null;

    @JsonProperty("taskType")
    @NotNull
    private String taskType = null;

    @JsonProperty("taskDetails")
    private Object taskDetails = null;

    @JsonProperty("amount")
    @Valid
    private Amount amount = null;

    @JsonProperty("status")
    @NotNull
    private String status = null;

    @JsonProperty("assignedTo")
    private AssignedTo assignedTo = null;

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

    @JsonProperty("workflow")
    @Valid
    private Workflow workflow = null;


    public Task addDocumentsItem(Document documentsItem) {
        this.documents.add(documentsItem);
        return this;
    }

    public String getFormattedDateTime(Long dateTime, String pattern) {
        String formattedDateTime = "";
        if (null != dateTime) {
            if (dateTime > 0) {
                formattedDateTime = Instant.ofEpochMilli(dateTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern(pattern));
            }
        }
        return formattedDateTime;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return getFormattedDateTime(this.createdDate, "dd/MM/yyyy");
    }

    @JsonProperty("dateCloseBy")
    public String getDateCloseBy() {
        return getFormattedDateTime(this.dateCloseBy, "dd/MM/yyyy");
    }

    @JsonProperty("dateClosed")
    public String getDateClosed() {
        return getFormattedDateTime(this.dateClosed, "dd/MM/yyyy");
    }

    public Long convertDateToLong(String dateTime, String pattern) throws ParseException {
        return new java.text.SimpleDateFormat(pattern).parse(dateTime).getTime();
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String date) throws ParseException {
        try {
            this.createdDate = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.createdDate = convertDateToLong(date, "dd/MM/yyyy");
        }
    }

    @JsonProperty("dateCloseBy")
    public void setDateCloseBy(String date) throws ParseException {
        try {
            this.dateCloseBy = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.dateCloseBy = convertDateToLong(date, "dd/MM/yyyy");
        }
    }

    @JsonProperty("dateClosed")
    public void setDateClosed(String date) throws ParseException {
        try {
            this.dateClosed = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.dateClosed = convertDateToLong(date, "dd/MM/yyyy");
        }
    }
}
