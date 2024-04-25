package org.pucar.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.pucar.web.models.AdvocateClerk;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * AdvocateClerkRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkRequest {
	@JsonProperty("RequestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("clerks")
	@Valid
	private List<AdvocateClerk> clerks = new ArrayList<>();

	public AdvocateClerkRequest addClerksItem(AdvocateClerk clerksItem) {
		this.clerks.add(clerksItem);
		return this;
	}

}
