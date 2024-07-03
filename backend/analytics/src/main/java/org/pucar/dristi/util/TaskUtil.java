package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.TASK_PATH;
import static org.pucar.dristi.config.ServiceConstants.TENANT_ID;
import static org.pucar.dristi.config.ServiceConstants.TASK_NUMBER;

@Slf4j
@Component
public class TaskUtil {

	private final Configuration config;
	private final ServiceRequestRepository repository;
	private final Util util;

	@Autowired
	public TaskUtil(Configuration config, ServiceRequestRepository repository, Util util) {
		this.config = config;
		this.repository = repository;
		this.util = util;
	}

	public Object getTask(JSONObject request, String tenantId, String taskNumber) {
		StringBuilder url = getSearchURLWithParams(tenantId, taskNumber);
		log.info("Inside TaskUtil getTask :: URL: {}", url);

		try {
			String response = repository.fetchResult(url, request);
			log.info("Inside TaskUtil getTask :: Response: {}", response);

			JSONArray tasks = util.constructArray(response, TASK_PATH);
			return tasks.length() > 0 ? tasks.get(0) : null;
		} catch (Exception e) {
			log.error("Error while fetching or processing the task response", e);
			throw new RuntimeException("Error while fetching or processing the task response", e);
		}
	}

	private StringBuilder getSearchURLWithParams(String tenantId, String taskNumber) {
		StringBuilder url = new StringBuilder(config.getTaskHost());
		url.append(config.getTaskSearchPath());
		url.append(TASK_NUMBER).append(taskNumber);
		url.append(TENANT_ID).append(tenantId);
		return url;
	}
}
