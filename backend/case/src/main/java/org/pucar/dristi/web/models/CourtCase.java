package org.pucar.dristi.web.models;

<<<<<<< HEAD
=======
import java.time.LocalDate;
>>>>>>> main
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
=======
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;

>>>>>>> main
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * Case registry
 */
@Schema(description = "Case registry")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourtCase {
<<<<<<< HEAD

	@JsonProperty("id")
=======
	@JsonProperty("id")

>>>>>>> main
	@Valid
	private UUID id = null;

	@JsonProperty("tenantId")
	@NotNull
<<<<<<< HEAD
	//@Size(min = 2, max = 64)
	private String tenantId = null;

	@JsonProperty("resolutionMechanism")
	//@Size(min = 2, max = 128)
	private String resolutionMechanism = null;

	@JsonProperty("caseTitle")
	//@Size(min = 2, max = 512)
	private String caseTitle = null;

	@JsonProperty("isActive")
	private Boolean isActive = true;

	@JsonProperty("caseDescription")
	//@Size(min = 2, max = 10000)
	private String caseDescription = null;

	@JsonProperty("filingNumber")
	//@Size(min = 2, max = 64)
	private String filingNumber = null;

	@JsonProperty("courtCaseNumber")
	//@Size(min=10,max=24)
	private String courtCaseNumber = null;

	@JsonProperty("caseNumber")

	//@Size(min = 2, max = 32)
	private String caseNumber = null;

	@JsonProperty("cnrNumber")
	//@Size(min = 2, max = 32)
	private String cnrNumber = null;

	@JsonProperty("accessCode")
	private String accessCode = null;

	@JsonProperty("outcome")
	private String outcome = null;

	@JsonProperty("courtId")
	//@Size(min = 2, max = 64)
	private String courtId = null;

	@JsonProperty("benchId")
	//@Size(min = 2, max = 64)
=======

	@Size(min = 2, max = 64)
	private String tenantId = null;

	@JsonProperty("resolutionMechanism")

	@Size(min = 2, max = 128)
	private String resolutionMechanism = null;

	@JsonProperty("caseTitle")

	@Size(min = 2, max = 512)
	private String caseTitle = null;

	@JsonProperty("caseDescription")

	@Size(min = 2, max = 10000)
	private String caseDescription = null;

	@JsonProperty("filingNumber")

	@Size(min = 2, max = 64)
	private String filingNumber = null;

	@JsonProperty("courtCaseNumber")

	@Size(min=10,max=24)
	private String courCaseNumber = null;
	@JsonProperty("caseNumber")

	@Size(min = 2, max = 32)
	private String caseNumber = null;

	@JsonProperty("accessCode")

	private String accessCode = null;

	@JsonProperty("courtId")

	@Size(min = 2, max = 64)
	private String courtId = null;

	@JsonProperty("benchId")

	@Size(min = 2, max = 64)
>>>>>>> main
	private String benchId = null;

	@JsonProperty("linkedCases")
	@Valid
	private List<LinkedCase> linkedCases = new ArrayList<>();

	@JsonProperty("filingDate")
<<<<<<< HEAD
	//@NotNull
	@Valid
	private Long filingDate = null;

	@JsonProperty("registrationDate")
	private Long registrationDate = null;

	@JsonProperty("judgementDate")
	private Long judgementDate = null;

	@JsonProperty("caseDetails")
	private Object caseDetails = null;

	@JsonProperty("caseCategory")
	//@NotNull
	//@Size(min = 2, max = 64)
	private String caseCategory = null;

	@JsonProperty("judgeId")
	private String judgeId = null;

	@JsonProperty("stage")
	private String stage = null;

	@JsonProperty("substage")
	private String substage = null;

	@JsonProperty("natureOfPleading")
	//@Size(min = 2, max = 64)
	private String natureOfPleading = null;

	@JsonProperty("statutesAndSections")
	//@NotNull
=======
	@NotNull

	@Valid
	private LocalDate filingDate = null;

	@JsonProperty("registrationDate")

	private String registrationDate = null;

	@JsonProperty("caseDetails")

	private Object caseDetails = null;

	@JsonProperty("caseCategory")
	@NotNull

	@Size(min = 2, max = 64)
	private String caseCategory = null;

	@JsonProperty("natureOfPleading")

	@Size(min = 2, max = 64)
	private String natureOfPleading = null;

	@JsonProperty("statutesAndSections")
	@NotNull
>>>>>>> main
	@Valid
	private List<StatuteSection> statutesAndSections = new ArrayList<>();

	@JsonProperty("litigants")
<<<<<<< HEAD
	//@NotNull
	@Valid
	//@Size(min = 2) //FIX
=======
	@NotNull
	@Valid
	@Size(min = 2)
>>>>>>> main
	private List<Party> litigants = new ArrayList<>();

	@JsonProperty("representatives")
	@Valid
	private List<AdvocateMapping> representatives = new ArrayList<>();

	@JsonProperty("status")
<<<<<<< HEAD
=======

>>>>>>> main
	private String status = null;

	@JsonProperty("documents")
	@Valid
	private List<Document> documents = new ArrayList<>();

	@JsonProperty("remarks")
<<<<<<< HEAD
	private String remarks = null;

	@JsonProperty("workflow")
=======

	private String remarks = null;

	@JsonProperty("workflow")

>>>>>>> main
	@Valid
	private Workflow workflow = null;

	@JsonProperty("additionalDetails")
<<<<<<< HEAD
	private Object additionalDetails = null;

	@JsonProperty("auditDetails")
	@Valid
	private AuditDetails auditdetails = null;

=======

	private Object additionalDetails = null;

	@JsonProperty("auditdetails")

	@Valid
	private AuditDetails auditdetails = null;

	public CourtCase addLinkedCasesItem(LinkedCase linkedCasesItem) {
		this.linkedCases.add(linkedCasesItem);
		return this;
	}

	public CourtCase addStatutesAndSectionsItem(StatuteSection statutesAndSectionsItem) {
		this.statutesAndSections.add(statutesAndSectionsItem);
		return this;
	}

	public CourtCase addLitigantsItem(Party litigantsItem) {
		this.litigants.add(litigantsItem);
		return this;
	}

	public CourtCase addRepresentativesItem(AdvocateMapping representativesItem) {
		this.representatives.add(representativesItem);
		return this;
	}

	public CourtCase addDocumentsItem(Document documentsItem) {
		this.documents.add(documentsItem);
		return this;
	}

>>>>>>> main
}
