package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

/**
 * CaseCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseCriteria {

	@JsonProperty("caseId")
	private String caseId = null;

	@JsonProperty("cnrNumber")
	private String cnrNumber = null;

	@JsonProperty("filingNumber")
	private String filingNumber = null;

	@JsonProperty("courtCaseNumber")
	private String courtCaseNumber = null;

	@JsonProperty("filingFromDate")
	@Valid
	private LocalDate filingFromDate = null;

	@JsonProperty("filingToDate")
	@Valid
	private LocalDate filingToDate = null;

	@JsonProperty("registrationFromDate")
	@Valid
	private LocalDate registrationFromDate = null;

	@JsonProperty("registrationToDate")
	@Valid
	private LocalDate registrationToDate = null;

}
