package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> main
/**
 * ApplicationExistsRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationExistsRequest   {
<<<<<<< HEAD
        @JsonProperty("RequestInfo")
=======
        @JsonProperty("requestInfo")
>>>>>>> main

          @Valid
                private RequestInfo requestInfo = null;

<<<<<<< HEAD
        @JsonProperty("applicationList")

          @Valid
                private List<ApplicationExists> applicationExists = null;
=======
        @JsonProperty("order")

          @Valid
                private ApplicationExists order = null;
>>>>>>> main


}
