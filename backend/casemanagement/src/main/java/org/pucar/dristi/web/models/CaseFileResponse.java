package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * CaseFileResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseFileResponse {
	@JsonProperty("requestInfo")

	@Valid
	private ResponseInfo responseInfo = null;

	@JsonProperty("caseFiles")
	@Valid
	private List<CaseFile> caseFiles = null;


	public CaseFileResponse addCaseFilesItem(CaseFile caseFilesItem) {
		if (this.caseFiles == null) {
			this.caseFiles = new ArrayList<>();
		}
		this.caseFiles.add(caseFilesItem);
		return this;
	}

}
