package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

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
public class Hearing   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

                private String tenantId = null;

        @JsonProperty("caseIds")
          @Valid
                private List<UUID> caseIds = null;

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


        public Hearing addCaseIdsItem(UUID caseIdsItem) {
            if (this.caseIds == null) {
            this.caseIds = new ArrayList<>();
            }
        this.caseIds.add(caseIdsItem);
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

}
