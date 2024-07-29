package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
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
		JSONObject dataObject = jsonObject.optJSONObject("Data");
		if (dataObject == null) {
			return null;
		}

		Artifact artifact = jsonMapperUtil.map(dataObject.optJSONObject("artifactDetails"), Artifact.class);

		if (artifact != null) {
			artifact.setAuditdetails(jsonMapperUtil.map(dataObject.optJSONObject("auditDetails"), AuditDetails.class));
		}

		return artifact;
	}
}
