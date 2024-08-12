package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.UUID;
=======
>>>>>>> main

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
>>>>>>> main
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Party
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Party {
	@JsonProperty("id")

<<<<<<< HEAD
	@Valid
	private UUID id = null;

	@JsonProperty("tenantId")
	//@NotNull
=======
	@Size(min = 2, max = 128)
	private String id = null;

	@JsonProperty("tenantId")
	@NotNull
>>>>>>> main

	private String tenantId = null;

	@JsonProperty("caseId")

	private String caseId = null;

	@JsonProperty("partyCategory")
<<<<<<< HEAD
	//@NotNull
=======
	@NotNull
>>>>>>> main

	private String partyCategory = null;

	@JsonProperty("organisationID")

	private String organisationID = null;

	@JsonProperty("individualId")

	private String individualId = null;

	@JsonProperty("partyType")

	private String partyType = null;

	@JsonProperty("isActive")

	private Boolean isActive = true;

	@JsonProperty("documents")
	@Valid
	private List<Document> documents = null;

	@JsonProperty("auditDetails")

	@Valid
	private AuditDetails auditDetails = null;

	@JsonProperty("additionalDetails")

	private Object additionalDetails = null;

<<<<<<< HEAD
=======
	public Party addDocumentsItem(Document documentsItem) {
		if (this.documents == null) {
			this.documents = new ArrayList<>();
		}
		this.documents.add(documentsItem);
		return this;
	}
>>>>>>> main

}
