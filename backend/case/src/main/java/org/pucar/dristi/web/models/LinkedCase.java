package org.pucar.dristi.web.models;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> main
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
<<<<<<< HEAD

	@JsonProperty("id")
=======
	@JsonProperty("id")

>>>>>>> main
	@Valid
	private UUID id = null;

	@JsonProperty("relationshipType")
<<<<<<< HEAD
	private String relationshipType = null;

	@JsonProperty("caseNumber")
	private String caseNumber = null;

	@JsonProperty("referenceUri")
	private String referenceUri = null;

	@JsonProperty("isActive")
=======

	private String relationshipType = null;

	@JsonProperty("caseNumber")

	private String caseNumber = null;

	@JsonProperty("referenceUri")

	private String referenceUri = null;

	@JsonProperty("isActive")

>>>>>>> main
	private Boolean isActive = null;

	@JsonProperty("documents")
	@Valid
	private List<Document> documents = null;

	@JsonProperty("additionalDetails")
<<<<<<< HEAD
	private Object additionalDetails = null;

	@JsonProperty("auditdetails")
	@Valid
	private AuditDetails auditdetails = null;

=======

	private String additionalDetails = null;

	@JsonProperty("auditdetails")

	@Valid
	private AuditDetails auditdetails = null;

	public LinkedCase addDocumentsItem(Document documentsItem) {
		if (this.documents == null) {
			this.documents = new ArrayList<>();
		}
		this.documents.add(documentsItem);
		return this;
	}
>>>>>>> main

}
