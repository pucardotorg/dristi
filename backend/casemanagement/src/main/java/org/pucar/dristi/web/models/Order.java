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
 * An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks
 */
@Schema(description = "An order is created as an outcome of an hearing or based on an application. Order will contain a set of tasks")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

                private String tenantId = null;

        @JsonProperty("filingNumber")

                private String filingNumber = null;

        @JsonProperty("cnrNumber")

                private String cnrNumber = null;

        @JsonProperty("applicationNumber")

                private List<String> applicationNumber = null;

        @JsonProperty("hearingNumber")

          @Valid
                private UUID hearingNumber = null;

        @JsonProperty("orderNumber")

        @Size(min=24,max=256)         private String orderNumber = null;

        @JsonProperty("createdDate")
          @NotNull

          @Valid
                private LocalDate createdDate = null;

        @JsonProperty("issuedBy")

                private Object issuedBy = null;

        @JsonProperty("orderType")
          @NotNull
          @Valid
                private List<UUID> orderType = new ArrayList<>();

        @JsonProperty("orderCategory")

                private String orderCategory = null;

        @JsonProperty("status")
          @NotNull

                private String status = null;

        @JsonProperty("comments")

                private String comments = null;

        @JsonProperty("isActive")
          @NotNull

                private Boolean isActive = null;

        @JsonProperty("statuteSection")

          @Valid
                private StatuteSection statuteSection = null;

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


        public Order addApplicationNumberItem(String applicationNumberItem) {
            if (this.applicationNumber == null) {
            this.applicationNumber = new ArrayList<>();
            }
        this.applicationNumber.add(applicationNumberItem);
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

}
