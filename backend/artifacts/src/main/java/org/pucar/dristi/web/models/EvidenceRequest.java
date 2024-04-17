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
 * EvidenceRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:22:31.436679+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvidenceRequest {
	@JsonProperty("requestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("artifacts")
	@Valid
	private List<Artifact> artifacts = null;

	public EvidenceRequest addArtifactsItem(Artifact artifactsItem) {
		if (this.artifacts == null) {
			this.artifacts = new ArrayList<>();
		}
		this.artifacts.add(artifactsItem);
		return this;
	}

}
