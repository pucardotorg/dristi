package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * CaseSummaryResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummaryResponse {
	@JsonProperty("requestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("criteria")
	@Valid
<<<<<<<< HEAD:backend/casemanagement/src/main/java/org/pucar/dristi/web/models/CaseSummaryResponse.java
	private List<CaseSummary> criteria = null;


	public CaseSummaryResponse addCriteriaItem(CaseSummary criteriaItem) {
		if (this.criteria == null) {
			this.criteria = new ArrayList<>();
		}
========
	private List<AdvocateClerkSearchCriteria> criteria = new ArrayList<>();

	public AdvocateClerkSearchRequest addCriteriaItem(AdvocateClerkSearchCriteria criteriaItem) {
>>>>>>>> main:backend/advocate/src/main/java/org/pucar/web/models/AdvocateClerkSearchRequest.java
		this.criteria.add(criteriaItem);
		return this;
	}

}
