package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

<<<<<<< HEAD
import java.util.UUID;

=======
>>>>>>> main
/**
 * OrderExists
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:13:43.389623100+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderExists {
<<<<<<< HEAD

    @JsonProperty("orderId")
    private UUID orderId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("applicationNumber")
    private String applicationNumber = null;

    @JsonProperty("orderNumber")
    private String orderNumber = null;

    @JsonProperty("exists")
=======
    @JsonProperty("filingNumber")

    private String filingNumber = null;

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("applicationNumber")

    private String applicationNumber = null;

    @JsonProperty("orderNumber")

    private String orderNumber = null;

    @JsonProperty("exists")

>>>>>>> main
    private Boolean exists = null;
}
