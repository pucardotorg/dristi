package org.pucar.dristi.web.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Case registry
 */
@Schema(description = "Case registry")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourtCase   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(min=2,max=64)         private String tenantId = null;

        @JsonProperty("resolutionMechanism")

        @Size(min=2,max=128)         private String resolutionMechanism = null;

        @JsonProperty("caseTitle")

        @Size(min=2,max=512)         private String caseTitle = null;

        @JsonProperty("caseDescription")

        @Size(min=2,max=10000)         private String caseDescription = null;

        @JsonProperty("filingNumber")

        @Size(min=2,max=64)         private String filingNumber = null;

        @JsonProperty("courCaseNumber")

        @Size(min=10,max=24)         private String courCaseNumber = null;

        @JsonProperty("cnrNumber")

        @Size(min=2,max=32)         private String cnrNumber = null;

        @JsonProperty("accessCode")

                private String accessCode = null;

        @JsonProperty("courtId")

        @Size(min=2,max=64)         private String courtId = null;

        @JsonProperty("benchId")

        @Size(min=2,max=64)         private String benchId = null;

        @JsonProperty("linkedCases")
          @Valid
                private List<LinkedCase> linkedCases = null;

        @JsonProperty("filingDate")
          @NotNull

          @Valid
                private LocalDate filingDate = null;

        @JsonProperty("registrationDate")

                private String registrationDate = null;

        @JsonProperty("caseDetails")

                private Object caseDetails = null;

        @JsonProperty("caseCategory")
          @NotNull

        @Size(min=2,max=64)         private String caseCategory = null;

        @JsonProperty("natureOfPleading")

        @Size(min=2,max=64)         private String natureOfPleading = null;

        @JsonProperty("statutesAndSections")
          @NotNull
          @Valid
                private List<StatuteSection> statutesAndSections = new ArrayList<>();

        @JsonProperty("litigants")
          @NotNull
          @Valid
        @Size(min=2)         private List<Party> litigants = new ArrayList<>();

        @JsonProperty("representatives")
          @Valid
                private List<AdvocateMapping> representatives = null;

        @JsonProperty("status")

                private String status = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("remarks")

                private String remarks = null;

        @JsonProperty("workflow")

          @Valid
                private Workflow workflow = null;

        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;

        @JsonProperty("auditdetails")

          @Valid
                private AuditDetails auditdetails = null;


        public CourtCase addLinkedCasesItem(LinkedCase linkedCasesItem) {
            if (this.linkedCases == null) {
            this.linkedCases = new ArrayList<>();
            }
        this.linkedCases.add(linkedCasesItem);
        return this;
        }

        public CourtCase addStatutesAndSectionsItem(StatuteSection statutesAndSectionsItem) {
        this.statutesAndSections.add(statutesAndSectionsItem);
        return this;
        }

        public CourtCase addLitigantsItem(Party litigantsItem) {
        this.litigants.add(litigantsItem);
        return this;
        }

        public CourtCase addRepresentativesItem(AdvocateMapping representativesItem) {
            if (this.representatives == null) {
            this.representatives = new ArrayList<>();
            }
        this.representatives.add(representativesItem);
        return this;
        }

        public CourtCase addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

}
