package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.json.JSONObject;
import org.pucar.dristi.web.models.Witness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WitnessMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public WitnessMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Witness getWitness(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("Data");
		if (dataObject == null) {
			return null;
		}

		Witness witness = jsonMapperUtil.map(dataObject.optJSONObject("witnessDetails"), Witness.class);

		if (witness != null) {
			witness.setAuditDetails(jsonMapperUtil.map(dataObject.optJSONObject("witnessDetails"), AuditDetails.class));
		}
		return witness;
	}
}
