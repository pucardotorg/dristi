package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import jakarta.validation.constraints.NotNull;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Bill;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Receipt {

	private String tenantId;
	
	private String id;

    private String transactionId;

	@NotNull
	@JsonProperty("Bill")
	private List<Bill> bill = new ArrayList<>();

	private AuditDetail auditDetails;

	private Long stateId;
}
