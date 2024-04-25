package org.pucar.web.models;

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
 * AdvocateSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateSearchRequest {
	@JsonProperty("RequestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("criteria")
	@Valid
	private List<AdvocateSearchCriteria> criteria = null;

	@JsonProperty("status")
	private List<String> status = null;

	@JsonProperty("applicationNumber")
	private String applicationNumber = null;

	public AdvocateSearchRequest addCriteriaItem(AdvocateSearchCriteria criteriaItem) {
		if (this.criteria == null) {
			this.criteria = new ArrayList<>();
		}
		this.criteria.add(criteriaItem);
		return this;
	}

}
