package org.pucar.dristi.web.models;

<<<<<<< HEAD
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
>>>>>>> main

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
<<<<<<< HEAD
	@JsonProperty("caseId")
	private String caseId = null;

	@JsonProperty("courtCaseNumber")
	private String courtCaseNumber = null;

	@JsonProperty("cnrNumber")
	private String cnrNumber = null;

	@JsonProperty("filingNumber")
	private String filingNumber = null;

	@JsonProperty("exists")
=======
	@JsonProperty("courtCaseNumber")

	private String courtCaseNumber = null;

	@JsonProperty("cnrNumber")

	private String cnrNumber = null;

	@JsonProperty("filingNumber")

	private String filingNumber = null;

	@JsonProperty("exists")

>>>>>>> main
	private Boolean exists = null;

}
