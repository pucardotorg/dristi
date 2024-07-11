package org.pucar.dristi.util.jsonmapper;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
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
		JSONObject caseDetails = jsonObject.optJSONObject("Data").optJSONObject("caseDetails");
		JSONObject currentProcessInstance = jsonObject.optJSONObject("Data").optJSONObject("currentProcessInstance");

		CourtCase courtCase = jsonMapperUtil.map(caseDetails, CourtCase.class);

		if (courtCase != null) {
			courtCase.setWorkflow(jsonMapperUtil.map(currentProcessInstance, Workflow.class));
			courtCase.setAuditdetails(jsonMapperUtil.map(caseDetails, AuditDetails.class));
		}

		return courtCase;
	}
}
