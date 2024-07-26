package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Holds the statute ID and the corresponding section &amp; subsections
 * applicable to the case.
 */
@Schema(description = "Holds the statute ID and the corresponding section & subsections applicable to the case. ")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatuteSection {
	@JsonProperty("id")
	@Valid
	private UUID id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 2, max = 64)
	private String tenantId = null;

	@JsonProperty("statute")
	private String statute = null;

	@JsonProperty("sections")
	private List<String> sections = null;

	@JsonProperty("subsections")
	private List<String> subsections = null;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@JsonProperty("auditDetails")
	@Valid
	private AuditDetails auditdetails = null;

	@JsonProperty("strSections")
	private String strSections = null;

	@JsonProperty("strSubsections")
	private String strSubsections = null;

}
