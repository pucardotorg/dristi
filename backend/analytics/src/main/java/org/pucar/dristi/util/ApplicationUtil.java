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
public class ApplicationUtil {

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;



	public Object getApplication(JSONObject request, String tenantId, String applicationNumber) {
		StringBuilder url = getSearchURLWithParams(applicationNumber);
		request.put("tenantId", tenantId);
		String response = repository.fetchResult(url, request);
		JSONArray applications = null;
		try{
			applications = util.constructArray(response, APPLICATION_PATH);
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return applications.get(0);
	}

	private StringBuilder getSearchURLWithParams(String applicationNumber) {
		StringBuilder url = new StringBuilder(config.getApplicationHost());
		url.append(config.getApplicationSearchPath());
		url.append(APPLICATION_NUMBER);
		url.append(applicationNumber);
		return url;
	}
}
