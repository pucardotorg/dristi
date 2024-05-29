package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * AdvocateClerkSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkSearchCriteria {
	@JsonProperty("id")

	private String id = null;

	@JsonProperty("applicationNumber")

	private String applicationNumber = null;

	@JsonProperty("stateRegnNumber")
	private String stateRegnNumber = null;

	@JsonProperty("individualId")

	private String individualId = null;

}
