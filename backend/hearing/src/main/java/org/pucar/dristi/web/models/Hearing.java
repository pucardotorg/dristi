package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
<<<<<<< HEAD
import jakarta.validation.constraints.Size;
=======
>>>>>>> main
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
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "a case can have multiple hearings. this represents one of the many hearings related to the case")
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

<<<<<<< HEAD
    @JsonProperty("hearingId")
    @Size(min = 2, max = 64)
    @Valid
    private String hearingId = null;

=======
>>>>>>> main
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
<<<<<<< HEAD
// Hearing workflow state,
    private String status = null;
=======

    private Boolean status = null;
>>>>>>> main

    @JsonProperty("startTime")

    @Valid
<<<<<<< HEAD
    private Long startTime = null;
=======
    private LocalDate startTime = null;
>>>>>>> main

    @JsonProperty("endTime")

    @Valid
<<<<<<< HEAD
    private Long endTime = null;

    @JsonProperty("presidedBy")

    private PresidedBy presidedBy = null;

    @JsonProperty("attendees")

    private List<Attendee> attendees = new ArrayList<>();
=======
    private LocalDate endTime = null;

    @JsonProperty("attendees")

    private List<Object> attendees = new ArrayList<>();
>>>>>>> main

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

<<<<<<< HEAD
    private Object additionalDetails = null;
=======
    private String additionalDetails = null;
>>>>>>> main

    @JsonProperty("auditDetails")

    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("workflow")

    @Valid
    private Workflow workflow = null;

    @JsonProperty("notes")

    private String notes = null;


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

<<<<<<< HEAD
    public Hearing addAttendeesItem(Attendee attendeesItem) {
=======
    public Hearing addAttendeesItem(Object attendeesItem) {
>>>>>>> main
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

}
