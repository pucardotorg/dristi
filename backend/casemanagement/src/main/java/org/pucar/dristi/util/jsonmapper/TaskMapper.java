package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.json.JSONObject;
import org.pucar.dristi.web.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public TaskMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Task getTask(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("Data");
		if (dataObject == null) {
			return null;
		}

		Task task = jsonMapperUtil.map(dataObject.optJSONObject("taskDetails"), Task.class);

		if (task != null) {
			task.setAuditDetails(jsonMapperUtil.map(dataObject.optJSONObject("taskDetails"), AuditDetails.class));
		}
		return task;
	}
}
