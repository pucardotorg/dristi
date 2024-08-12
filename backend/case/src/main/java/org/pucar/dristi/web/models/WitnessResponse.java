package org.pucar.dristi.web.models;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

=======
import com.fasterxml.jackson.annotation.JsonProperty;
>>>>>>> main
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
>>>>>>> main

/**
 * WitnessResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-19T15:42:53.131831400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WitnessResponse   {
<<<<<<< HEAD
        @JsonProperty("RequestInfo")
=======
        @JsonProperty("requestInfo")
>>>>>>> main

          @Valid
                private ResponseInfo requestInfo = null;

        @JsonProperty("witnesses")
          @Valid
                private List<Witness> witnesses = null;


<<<<<<< HEAD
=======
        public WitnessResponse addWitnessesItem(Witness witnessesItem) {
            if (this.witnesses == null) {
            this.witnesses = new ArrayList<>();
            }
        this.witnesses.add(witnessesItem);
        return this;
        }

>>>>>>> main
}
