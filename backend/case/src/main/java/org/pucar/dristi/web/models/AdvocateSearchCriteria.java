package org.pucar.dristi.web.models;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdvocateSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateSearchCriteria {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("barRegistrationNumber")
	private String barRegistrationNumber = null;

	@JsonProperty("applicationNumber")
	private String applicationNumber = null;

	@JsonProperty("individualId")
	private String individualId = null;

	@JsonProperty("responseList")
	@Valid
	private List<Advocate> responseList = null;

}
