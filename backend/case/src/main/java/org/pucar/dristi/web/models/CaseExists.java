package org.pucar.dristi.web.models;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CaseExists
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseExists {
	@JsonProperty("caseId")
	private String caseId = null;

	@JsonProperty("courtCaseNumber")
	private String courtCaseNumber = null;

	@JsonProperty("cnrNumber")
	private String cnrNumber = null;

	@JsonProperty("filingNumber")
	private String filingNumber = null;

	@JsonProperty("exists")
	private Boolean exists = null;

}
