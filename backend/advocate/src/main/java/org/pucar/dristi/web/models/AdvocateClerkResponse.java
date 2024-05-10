package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * AdvocateClerkResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkResponse {
	@JsonProperty("responseInfo")

	@Valid
	private ResponseInfo responseInfo = null;

	@JsonProperty("clerks")
	@Valid
	private List<AdvocateClerk> clerks = null;

	@JsonProperty("pagination")

	@Valid
	private Pagination pagination = null;

	public AdvocateClerkResponse addClerksItem(AdvocateClerk clerksItem) {
		if (this.clerks == null) {
			this.clerks = new ArrayList<>();
		}
		this.clerks.add(clerksItem);
		return this;
	}

}
