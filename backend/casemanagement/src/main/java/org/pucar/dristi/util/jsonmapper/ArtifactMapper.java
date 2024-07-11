package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.json.JSONObject;
import org.pucar.dristi.web.models.Artifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtifactMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public ArtifactMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Artifact getArtifact(JSONObject jsonObject) {
		JSONObject artifactDetails = jsonObject.optJSONObject("Data").optJSONObject("artifactDetails");
		JSONObject auditDetails = jsonObject.optJSONObject("Data").optJSONObject("auditDetails");
		JSONObject currentProcessInstance = jsonObject.optJSONObject("Data").optJSONObject("currentProcessInstance");

		Artifact artifact = jsonMapperUtil.map(artifactDetails, Artifact.class);

		if (artifact != null) {
			artifact.setWorkflow(jsonMapperUtil.map(currentProcessInstance, Workflow.class));
			artifact.setAuditdetails(jsonMapperUtil.map(auditDetails, AuditDetails.class));
		}
		return artifact;
	}
}
