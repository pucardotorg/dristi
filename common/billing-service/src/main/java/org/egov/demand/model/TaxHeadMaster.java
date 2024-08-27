package org.egov.demand.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.egov.demand.model.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxHeadMaster {

	private String id;

	@NotNull
	private String tenantId;
	@Valid
	@NotNull
	private Category category;
	@NotNull
	private String service;
	@NotNull
	private String name;

	private String code;
	
	private List<GlCodeMaster> glCodes;

	private Boolean isDebit = false;

	private Boolean isActualDemand;
	@NotNull
	private Long validFrom;
	@NotNull
	private Long validTill;
	
	private Integer order;

	private AuditDetail auditDetail;
	

}
