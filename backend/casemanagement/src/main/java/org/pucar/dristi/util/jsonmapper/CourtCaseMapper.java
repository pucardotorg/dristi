package org.pucar.dristi.util.jsonmapper;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.json.JSONObject;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CourtCaseMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public CourtCaseMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public CourtCase getCourtCase(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("Data");
		if (dataObject == null) {
			return null;
		}

		CourtCase courtCase = jsonMapperUtil.map(dataObject.optJSONObject("caseDetails"), CourtCase.class);

		if (courtCase != null) {
			courtCase.setAuditdetails(jsonMapperUtil.map(dataObject.optJSONObject("caseDetails"), AuditDetails.class));
		}

		return courtCase;
	}
}
