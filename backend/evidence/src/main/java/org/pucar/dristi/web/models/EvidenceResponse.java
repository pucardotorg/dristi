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
 * EvidenceResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvidenceResponse {
	@JsonProperty("ResponseInfo")

	@Valid
	private ResponseInfo responseInfo = null;

	@JsonProperty("artifact")
	@Valid
	private Artifact artifact;


	@JsonProperty("pagination")

	@Valid
	private Pagination pagination = null;

	public EvidenceResponse addArtifact(Artifact artifact) {
		this.artifact = artifact;
		return this;
	}

}
