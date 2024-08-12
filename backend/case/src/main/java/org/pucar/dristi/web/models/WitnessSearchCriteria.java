package org.pucar.dristi.web.models;

<<<<<<< HEAD
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

=======
import com.fasterxml.jackson.annotation.JsonProperty;
>>>>>>> main
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.validation.annotation.Validated;
>>>>>>> main

/**
 * WitnessSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-19T15:42:53.131831400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class WitnessSearchCriteria {

    @JsonProperty("caseId")
    private String caseId = null;

    @JsonProperty("individualId")
    private String individualId = null;

    @JsonProperty("includeInactive")
    private Boolean includeInactive = false;
=======
public class WitnessSearchCriteria   {
        @JsonProperty("caseId")

                private String caseId = null;

        @JsonProperty("individualId")

                private String individualId = null;

        @JsonProperty("includeInactive")

                private Boolean includeInactive = false;
>>>>>>> main


}
