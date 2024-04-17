package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WitnessResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WitnessResponse {
	@JsonProperty("requestInfo")

	@Valid
	private ResponseInfo requestInfo = null;

	@JsonProperty("witnesses")
	@Valid
	private List<Witness> witnesses = null;

	public WitnessResponse addWitnessesItem(Witness witnessesItem) {
		if (this.witnesses == null) {
			this.witnesses = new ArrayList<>();
		}
		this.witnesses.add(witnessesItem);
		return this;
	}

}
