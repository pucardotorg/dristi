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
<<<<<<< HEAD
	@JsonProperty("ResponseInfo")
=======
	@JsonProperty("responseInfo")
>>>>>>> main

	@Valid
	private ResponseInfo responseInfo = null;

<<<<<<< HEAD
	@JsonProperty("artifact")
	@Valid
	private Artifact artifact;

=======
	@JsonProperty("artifacts")
	@Valid
	private List<Artifact> artifacts = null;
>>>>>>> main

	@JsonProperty("pagination")

	@Valid
	private Pagination pagination = null;

<<<<<<< HEAD
	public EvidenceResponse addArtifact(Artifact artifact) {
		this.artifact = artifact;
=======
	public EvidenceResponse addArtifactsItem(Artifact artifactsItem) {
		if (this.artifacts == null) {
			this.artifacts = new ArrayList<>();
		}
		this.artifacts.add(artifactsItem);
>>>>>>> main
		return this;
	}

}
