package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Representation of the case summary.
 */
@Schema(description = "Representation of the case summary.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummary   {
        @JsonProperty("resolutionMechanism")

        @Size(min=2,max=128)         private String resolutionMechanism = null;

        @JsonProperty("caseTitle")
          @NotNull

        @Size(min=2,max=512)         private String caseTitle = null;

        @JsonProperty("caseDescription")
          @NotNull

        @Size(min=2,max=10000)         private String caseDescription = null;

        @JsonProperty("filingNumber")
          @NotNull

        @Size(min=2,max=64)         private String filingNumber = null;

        @JsonProperty("courCaseNumber")

        @Size(min=10,max=24)         private String courCaseNumber = null;

        @JsonProperty("cnrNumber")
          @NotNull

        @Size(min=2,max=32)         private String cnrNumber = null;

        @JsonProperty("filingDate")

          @Valid
                private LocalDate filingDate = null;

        @JsonProperty("registrationDate")
          @NotNull

                private String registrationDate = null;

        @JsonProperty("caseDetails")
          @NotNull

                private Object caseDetails = null;

        @JsonProperty("caseCategory")
          @NotNull

        @Size(min=2,max=64)         private String caseCategory = null;

        @JsonProperty("statutesAndSections")
          @NotNull
          @Valid
                private List<StatuteSection> statutesAndSections = new ArrayList<>();

        @JsonProperty("status")
          @NotNull

                private String status = null;

        @JsonProperty("remarks")

                private String remarks = null;

        @JsonProperty("judgement")

          @Valid
                private Order judgement = null;


        public CaseSummary addStatutesAndSectionsItem(StatuteSection statutesAndSectionsItem) {
        this.statutesAndSections.add(statutesAndSectionsItem);
        return this;
        }

}
