package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
=======
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
>>>>>>> main

/**
 * amount in form of fees or penalty
 */
@Schema(description = "amount in form of fees or penalty")
@Validated
<<<<<<< HEAD
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
=======
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
>>>>>>> main
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class Amount {

	@JsonProperty("id")
	@Valid
	private UUID id = null;

	@JsonProperty("amount")
	@NotNull
	private String amount = null;

	@JsonProperty("type")
	@NotNull
	private String type = null;

	@JsonProperty("paymentRefNumber")
	private String paymentRefNumber = null;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@JsonProperty("status")
	@NotNull
	private String status = null;
=======
public class Amount   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("amount")
          @NotNull

                private String amount = null;

        @JsonProperty("type")
          @NotNull

                private String type = null;

        @JsonProperty("paymentRefNumber")

                private String paymentRefNumber = null;

        @JsonProperty("additionalDetails")

                private String additionalDetails = null;

        @JsonProperty("status")
          @NotNull

                private String status = null;

>>>>>>> main

}
