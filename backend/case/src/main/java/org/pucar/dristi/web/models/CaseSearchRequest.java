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
 * CaseSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSearchRequest {
	@JsonProperty("requestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("criteria")
	@Valid
	private List<CaseCriteria> criteria = null;

	public CaseSearchRequest addCriteriaItem(CaseCriteria criteriaItem) {
		if (this.criteria == null) {
			this.criteria = new ArrayList<>();
		}
		this.criteria.add(criteriaItem);
		return this;
	}

}
