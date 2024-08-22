package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LinkedCase
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedCase {
	@JsonProperty("id")
	@Valid
	private UUID id = null;

	@JsonProperty("relationshipType")
	private String relationshipType = null;

	@JsonProperty("caseNumber")
	private String caseNumber = null;

	@JsonProperty("referenceUri")
	private String referenceUri = null;

	@JsonProperty("isActive")
	private Boolean isActive = null;

	@JsonProperty("documents")
	@Valid
	private List<Document> documents = null;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@JsonProperty("auditdetails")
	@Valid
	private AuditDetails auditdetails = null;

}
