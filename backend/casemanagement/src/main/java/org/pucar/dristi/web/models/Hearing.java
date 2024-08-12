package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

>>>>>>> main
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
=======
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> main

/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "a case can have multiple hearings. this represents one of the many hearings related to the case")
@Validated
<<<<<<< HEAD
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
=======
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
>>>>>>> main
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
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

    @JsonProperty("startTime")

    @Valid
    private Long startTime = null;

    @JsonProperty("endTime")

    @Valid
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
=======
public class Hearing   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

                private String tenantId = null;

        @JsonProperty("filingNumber")

                private List<String> filingNumber = null;

        @JsonProperty("cnrNumbers")

                private List<String> cnrNumbers = null;

        @JsonProperty("applicationNumbers")

                private List<String> applicationNumbers = null;

        @JsonProperty("hearingType")
          @NotNull

                private String hearingType = null;

        @JsonProperty("status")
          @NotNull

                private Boolean status = null;

        @JsonProperty("startTime")

          @Valid
                private LocalDate startTime = null;

        @JsonProperty("endTime")

          @Valid
                private LocalDate endTime = null;

        @JsonProperty("presidedBy")

                private Object presidedBy = null;

        @JsonProperty("attendees")

                private List<Object> attendees = null;

        @JsonProperty("transcript")

                private List<String> transcript = null;

        @JsonProperty("vcLink")

                private String vcLink = null;

        @JsonProperty("isActive")

                private Boolean isActive = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("additionalDetails")

                private String additionalDetails = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("workflow")

          @Valid
                private Workflow workflow = null;

        @JsonProperty("notes")

                private String notes = null;


        public Hearing addFilingNumberItem(String filingNumberItem) {
            if (this.filingNumber == null) {
            this.filingNumber = new ArrayList<>();
            }
        this.filingNumber.add(filingNumberItem);
        return this;
        }

        public Hearing addCnrNumbersItem(String cnrNumbersItem) {
            if (this.cnrNumbers == null) {
            this.cnrNumbers = new ArrayList<>();
            }
        this.cnrNumbers.add(cnrNumbersItem);
        return this;
        }

        public Hearing addApplicationNumbersItem(String applicationNumbersItem) {
            if (this.applicationNumbers == null) {
            this.applicationNumbers = new ArrayList<>();
            }
        this.applicationNumbers.add(applicationNumbersItem);
        return this;
        }

        public Hearing addAttendeesItem(Object attendeesItem) {
            if (this.attendees == null) {
            this.attendees = new ArrayList<>();
            }
        this.attendees.add(attendeesItem);
        return this;
        }

        public Hearing addTranscriptItem(String transcriptItem) {
            if (this.transcript == null) {
            this.transcript = new ArrayList<>();
            }
        this.transcript.add(transcriptItem);
        return this;
        }

        public Hearing addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }
>>>>>>> main

}
