package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Party
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T13:54:45.904122+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Party   {
        @JsonProperty("id")

        @Size(min=36,max=36)         private String id = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(min=2,max=64)         private String tenantId = null;

        @JsonProperty("caseId")
          @NotNull

        @Size(min=2,max=36)         private String caseId = null;

        @JsonProperty("partyCategory")
          @NotNull

                private String partyCategory = null;

        @JsonProperty("organisationID")

                private String organisationID = null;

        @JsonProperty("individualId")

                private String individualId = null;

        @JsonProperty("partyType")

                private String partyType = null;

        @JsonProperty("representationType")

                private String representationType = null;

        @JsonProperty("representativeId")

                private List<String> representativeId = null;

        @JsonProperty("isActive")

                private Boolean isActive = true;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;


        public Party addRepresentativeIdItem(String representativeIdItem) {
            if (this.representativeId == null) {
            this.representativeId = new ArrayList<>();
            }
        this.representativeId.add(representativeIdItem);
        return this;
        }

        public Party addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

}
