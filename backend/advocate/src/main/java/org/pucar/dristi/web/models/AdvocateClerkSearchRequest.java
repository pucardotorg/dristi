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

<<<<<<< HEAD:backend/advocate/src/main/java/org/pucar/dristi/web/models/AdvocateClerkSearchRequest.java
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("criteria")
	@Valid
	private List<AdvocateClerkSearchCriteria> criteria = new ArrayList<>();

=======
	@JsonProperty("advocates")
	@Valid
	private List<Advocate> advocates = new ArrayList<>();

	public AdvocateRequest addAdvocatesItem(Advocate advocatesItem) {
		this.advocates.add(advocatesItem);
		return this;
	}
>>>>>>> main:backend/advocate/src/main/java/org/pucar/web/models/AdvocateRequest.java

}
