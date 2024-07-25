package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the case file. This is an aggregation of all the entities across the case lifecycle.
 */
@Schema(description = "Representation of the case file. This is an aggregation of all the entities across the case lifecycle.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseFile {
	@JsonProperty("case")
	@NotNull

	@Valid
	private CourtCase courtCase = null;

	@JsonProperty("hearings")
	@Valid
	private List<Hearing> hearings = null;

	@JsonProperty("witnesses")
	@Valid
	private List<Witness> witnesses = null;

	@JsonProperty("orders")
	@Valid
	private List<OrderTasks> orders = null;

	@JsonProperty("applications")
	@Valid
	private List<Application> applications = null;

	@JsonProperty("evidence")
	@Valid
	private List<Artifact> evidence = null;


	public CaseFile addHearingsItem(Hearing hearingsItem) {
		if (this.hearings == null) {
			this.hearings = new ArrayList<>();
		}
		this.hearings.add(hearingsItem);
		return this;
	}

	public CaseFile addWitnessesItem(Witness witnessesItem) {
		if (this.witnesses == null) {
			this.witnesses = new ArrayList<>();
		}
		this.witnesses.add(witnessesItem);
		return this;
	}

	public CaseFile addOrdersItem(OrderTasks ordersItem) {
		if (this.orders == null) {
			this.orders = new ArrayList<>();
		}
		this.orders.add(ordersItem);
		return this;
	}

	public CaseFile addApplicationsItem(Application applicationsItem) {
		if (this.applications == null) {
			this.applications = new ArrayList<>();
		}
		this.applications.add(applicationsItem);
		return this;
	}

	public CaseFile addEvidenceItem(Artifact evidenceItem) {
		if (this.evidence == null) {
			this.evidence = new ArrayList<>();
		}
		this.evidence.add(evidenceItem);
		return this;
	}

}
