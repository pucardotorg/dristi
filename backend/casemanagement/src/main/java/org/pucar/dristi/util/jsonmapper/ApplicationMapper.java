package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.json.JSONObject;
import org.pucar.dristi.web.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {


	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public ApplicationMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Application getApplication(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("Data");
		if (dataObject == null) {
			return null;
		}

		Application application = jsonMapperUtil.map(dataObject.optJSONObject("applicationDetails"), Application.class);

		if (application != null) {
			application.setAuditDetails(jsonMapperUtil.map(dataObject.optJSONObject("auditDetails"), AuditDetails.class));
		}

		return application;
	}
}
