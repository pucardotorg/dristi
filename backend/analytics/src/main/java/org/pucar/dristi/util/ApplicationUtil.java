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
public class ApplicationUtil {

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;



	public Object getApplication(JSONObject request, String tenantId, String applicationNumber) {
		StringBuilder url = getSearchURLWithParams();
		log.info("Inside Application util getApplication :: url: " + url);

		request.put("tenantId", tenantId);
		JSONObject criteria = new JSONObject();
		criteria.put("applicationNumber",applicationNumber);
		criteria.put("tenantId",tenantId);
		request.put("criteria",criteria);
		log.info("Inside Application util getApplication :: request: " + request);
		String response = repository.fetchResult(url, request);
		log.info("Inside Application util getApplication :: response: " + response);
		JSONArray applications = null;
		try{
			applications = util.constructArray(response, APPLICATION_PATH);
			log.info("Inside Application util getApplication :: applications: " + applications.toString());
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return applications.get(0);
	}

	private StringBuilder getSearchURLWithParams() {
		StringBuilder url = new StringBuilder(config.getApplicationHost());
		url.append(config.getApplicationSearchPath());
		return url;
	}
}
