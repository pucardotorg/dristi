package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * AdvocateClerkSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkSearchRequest {
	@JsonProperty("RequestInfo")
	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("criteria")
	@Valid
	private List<AdvocateClerkSearchCriteria> criteria = new ArrayList<>();

	public AdvocateClerkSearchRequest addCriteriaItem(AdvocateClerkSearchCriteria criteriaItem) {
		this.criteria.add(criteriaItem);
		return this;
	}

}
