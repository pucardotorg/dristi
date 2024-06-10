package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import javax.validation.Valid;

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

	public AdvocateSearchCriteria addResponseListItem(Advocate responseListItem) {
		if (this.responseList == null) {
			this.responseList = new ArrayList<>();
		}
		this.responseList.add(responseListItem);
		return this;
	}
}
