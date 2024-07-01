package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class TaskUtil {

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;


	public Object getTask(JSONObject request, String tenantId, String taskNumber) {
		StringBuilder url = getSearchURLWithParams(tenantId, taskNumber);
		log.info("Inside Task util getTask :: url: " + url);
		String response = repository.fetchResult(url, request);
		log.info("Inside Task util getTask :: response: " + response);

		JSONArray tasks = null;
		try{
			tasks = util.constructArray(response, TASK_PATH);
			log.info("Inside Task util getTask:: tasks: " + tasks.toString());

		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return tasks.get(0);
	}

	private StringBuilder getSearchURLWithParams(String tenantId, String taskNumber) {
		StringBuilder url = new StringBuilder(config.getTaskHost());
		url.append(config.getTaskSearchPath());
		url.append(TENANT_ID);
		url.append(tenantId);
		url.append(TASK_NUMBER);
		url.append(taskNumber);
		return url;
	}
}
