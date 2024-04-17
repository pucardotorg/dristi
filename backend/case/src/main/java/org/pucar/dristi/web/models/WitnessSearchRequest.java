package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WitnessSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WitnessSearchRequest {
	@JsonProperty("requestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("searchCriteria")
	@Valid
	private List<WitnessSearchCriteria> searchCriteria = null;

	public WitnessSearchRequest addSearchCriteriaItem(WitnessSearchCriteria searchCriteriaItem) {
		if (this.searchCriteria == null) {
			this.searchCriteria = new ArrayList<>();
		}
		this.searchCriteria.add(searchCriteriaItem);
		return this;
	}

}
