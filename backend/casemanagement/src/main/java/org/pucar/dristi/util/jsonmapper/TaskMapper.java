package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
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
		JSONObject taskDetails = jsonObject.optJSONObject("Data").optJSONObject("taskDetails");
		JSONObject currentProcessInstance = jsonObject.optJSONObject("Data").optJSONObject("currentProcessInstance");

		Task task = jsonMapperUtil.map(taskDetails, Task.class);

		if (task != null) {
			task.setAuditDetails(jsonMapperUtil.map(taskDetails, AuditDetails.class));
			task.setWorkflow(jsonMapperUtil.map(currentProcessInstance, Workflow.class));
		}
		return task;
	}
}
