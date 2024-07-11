package org.pucar.dristi.util.jsonmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.mapper.Mapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
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
		JSONObject applicationDetails = jsonObject.optJSONObject("Data").optJSONObject("applicationDetails");
		JSONObject auditDetails = jsonObject.optJSONObject("Data").optJSONObject("auditDetails");
		JSONObject currentProcessInstance = jsonObject.optJSONObject("Data").optJSONObject("currentProcessInstance");

		Application application = jsonMapperUtil.map(applicationDetails, Application.class);

		if(application != null) {
			application.setWorkflow(jsonMapperUtil.map(currentProcessInstance, Workflow.class));
			application.setAuditDetails(jsonMapperUtil.map(auditDetails, AuditDetails.class));
		}

		return application;
	}
}
