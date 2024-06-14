package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * ApplicationExists
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationExists   {
        @JsonProperty("filingNumber")
        private String filingNumber = null;

        @JsonProperty("cnrNumber")
        private String cnrNumber = null;

        @JsonProperty("applicationNumber")
        private String applicationNumber = null;

        @JsonProperty("exists")
        private Boolean exists = null;
}
