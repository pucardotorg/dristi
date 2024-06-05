package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
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
 * Holds the statute ID and the corresponding section &amp; subsections applicable to the case. 
 */
@Schema(description = "Holds the statute ID and the corresponding section & subsections applicable to the case. ")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatuteSection   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(min=2,max=64)         private String tenantId = null;

        @JsonProperty("statute")

                private String statute = null;

        @JsonProperty("sections")

                private List<String> sections = null;

        @JsonProperty("subsections")

                private List<String> subsections = null;

        @JsonProperty("additionalDetails")

                private String additionalDetails = null;

        @JsonProperty("auditdetails")

          @Valid
                private AuditDetails auditdetails = null;


        public StatuteSection addSectionsItem(String sectionsItem) {
            if (this.sections == null) {
            this.sections = new ArrayList<>();
            }
        this.sections.add(sectionsItem);
        return this;
        }

        public StatuteSection addSubsectionsItem(String subsectionsItem) {
            if (this.subsections == null) {
            this.subsections = new ArrayList<>();
            }
        this.subsections.add(subsectionsItem);
        return this;
        }

}
