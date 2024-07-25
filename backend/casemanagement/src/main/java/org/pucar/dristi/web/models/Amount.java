package org.pucar.dristi.web.models;

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

/**
 * amount in form of fees or penalty
 */
@Schema(description = "amount in form of fees or penalty")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
