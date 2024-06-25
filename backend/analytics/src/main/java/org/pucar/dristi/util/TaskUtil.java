package org.pucar.dristi.util;

import com.jayway.jsonpath.JsonPath;
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


	public Object getTask(JSONObject request, String orderId, String cnrNumber, String tenantId, String taskNumber) {
		StringBuilder url = getSearchURLWithParams(orderId, cnrNumber, tenantId, taskNumber);
		String response = repository.fetchResult(url, request);
		JSONArray tasks = null;
		try{
			tasks = util.constructArray(response, ARTIFACT_PATH);
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return tasks.get(0);
	}

	private StringBuilder getSearchURLWithParams(String orderId, String cnrNumber, String tenantId, String taskNumber) {
		StringBuilder url = new StringBuilder(config.getTaskHost());
		url.append(config.getTaskSearchPath());
		url.append(ORDER_ID);
		url.append(orderId);
		url.append(CNR_NUMBER);
		url.append(cnrNumber);
		url.append(TENANT_ID);
		url.append(tenantId);
		url.append(TASK_NUMBER);
		url.append(taskNumber);
		return url;
	}
}
