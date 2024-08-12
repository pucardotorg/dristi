package org.pucar.dristi.web.models;

<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;

>>>>>>> main
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CaseRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseRequest {
<<<<<<< HEAD

	@JsonProperty("RequestInfo")
=======
	@JsonProperty("requestInfo")

>>>>>>> main
	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("cases")
	@Valid
<<<<<<< HEAD
	private CourtCase cases = null;
=======
	private List<CourtCase> cases = new ArrayList<>();

	public CaseRequest addCasesItem(CourtCase casesItem) {
		this.cases.add(casesItem);
		return this;
	}
>>>>>>> main

}
