package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


/**
 * HearingExists
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HearingExists   {
        @JsonProperty("hearingId")

        @Valid
        private String hearingId = null;

        @JsonProperty("filingNumber")

        private String filingNumber = null;

        @JsonProperty("cnrNumber")

        private String cnrNumber = null;

        @JsonProperty("applicationNumber")

        private String applicationNumber = null;

        @JsonProperty("exists")

        private Boolean exists = null;
}
