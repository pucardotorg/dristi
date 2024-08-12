package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

/**
 * ApplicationResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse   {
<<<<<<< HEAD
        @JsonProperty("ResponseInfo")
=======
        @JsonProperty("responseInfo")
>>>>>>> main

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("application")

          @Valid
                private Application application = null;


}
