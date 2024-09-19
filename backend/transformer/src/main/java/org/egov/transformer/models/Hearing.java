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
import java.util.UUID;

/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hearing {
    @JsonProperty("id")

    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull

    private String tenantId = null;

    @JsonProperty("hearingId")
    @Size(min = 2, max = 64)
    @Valid
    private String hearingId = null;

    @JsonProperty("filingNumber")

    private List<String> filingNumber = new ArrayList<>();

    @JsonProperty("cnrNumbers")

    private List<String> cnrNumbers = new ArrayList<>();

    @JsonProperty("applicationNumbers")

    private List<String> applicationNumbers = new ArrayList<>();

    @JsonProperty("hearingType")
    @NotNull

    private String hearingType = null;

    @JsonProperty("status")
    @NotNull
// Hearing workflow state,
    private String status = null;

    private Long startTime = null;

    private Long endTime = null;

    @JsonProperty("presidedBy")

    private PresidedBy presidedBy = null;

    @JsonProperty("attendees")

    private List<Attendee> attendees = new ArrayList<>();

    @JsonProperty("transcript")

    private List<String> transcript = new ArrayList<>();

    @JsonProperty("vcLink")

    private String vcLink = null;

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

    @JsonProperty("notes")

    private String notes = null;

    private Long filingDate = null;

    private Long registrationDate = null;

    @JsonProperty("stage")
    private String stage = null;

    @JsonProperty("substage")
    private String substage = null;

    public Hearing addFilingNumberItem(String filingNumberItem) {
        this.filingNumber.add(filingNumberItem);
        return this;
    }

    public Hearing addCnrNumbersItem(String cnrNumbersItem) {
        this.cnrNumbers.add(cnrNumbersItem);
        return this;
    }

    public Hearing addApplicationNumbersItem(String applicationNumbersItem) {
        this.applicationNumbers.add(applicationNumbersItem);
        return this;
    }

    public Hearing addAttendeesItem(Attendee attendeesItem) {
        if (this.attendees == null) {
            this.attendees = new ArrayList<>();
        }
        this.attendees.add(attendeesItem);
        return this;
    }

    public Hearing addTranscriptItem(String transcriptItem) {
        this.transcript.add(transcriptItem);
        return this;
    }

    public Hearing addDocumentsItem(Document documentsItem) {
        this.documents.add(documentsItem);
        return this;
    }

    public String getFormattedDateTime(Long dateTime, String pattern) {
        String formattedDateTime = "";
        if (null != dateTime) {
            if (dateTime > 0) {
                formattedDateTime = Instant.ofEpochMilli(dateTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern(pattern));
            }
        }
        return formattedDateTime;
    }

    @JsonProperty("startTime")
    public String getStartTime() {
        return getFormattedDateTime(this.startTime, "dd/MM/yyyy HH:mm");
    }

    @JsonProperty("endTime")
    public String getEndTime() {
        return getFormattedDateTime(this.endTime, "dd/MM/yyyy HH:mm");
    }

    @JsonProperty("filingDate")
    public String getFilingDate() {
        return getFormattedDateTime(this.filingDate, "dd/MM/yyyy");
    }

    @JsonProperty("registrationDate")
    public String getRegistrationDate() {
        return getFormattedDateTime(this.registrationDate, "dd/MM/yyyy");
    }


    public Long convertDateToLong(String dateTime, String pattern) throws ParseException {
        return new java.text.SimpleDateFormat(pattern).parse(dateTime).getTime();
    }

    @JsonProperty("startTime")
    public void setStartTime(String dateTime) throws ParseException {
        try {
            this.startTime = Long.parseLong(dateTime);
        } catch (NumberFormatException e) {
            this.startTime = convertDateToLong(dateTime, "dd/MM/yyyy HH:mm");
        }
    }

    @JsonProperty("endTime")
    public void setEndTime(String dateTime) throws ParseException {
        try {
            this.endTime = Long.parseLong(dateTime);
        } catch (NumberFormatException e) {
            this.endTime = convertDateToLong(dateTime, "dd/MM/yyyy HH:mm");
        }
    }


    @JsonProperty("filingDate")
    public void setFilingDate(String date) throws ParseException {
        try {
            this.filingDate = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.filingDate = convertDateToLong(date, "dd/MM/yyyy");
        }
    }

    @JsonProperty("registrationDate")
    public void setRegistrationDate(String date) throws ParseException {
        try {
            this.registrationDate = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.registrationDate = convertDateToLong(date, "dd/MM/yyyy");
        }
    }

    @JsonProperty("filingDate")
    public void setFilingDate(Long date) {
        this.filingDate = date;
    }

    @JsonProperty("registrationDate")
    public void setRegistrationDate(Long date) {
        this.registrationDate = date;
    }
}
