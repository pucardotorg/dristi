package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "This field will contain list of people required and present for the hearing along with also the lawyers that represented the case for this hearing")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendee {
	@JsonProperty("name")

	private String name = null;

	@JsonProperty("individualId")

	private String individualId = null;

	@JsonProperty("type")

	private String type = null;

	@JsonProperty("associatedWith")

	private String associatedWith = null;

	@JsonProperty("wasPresent")

	private Boolean wasPresent = null;

}
