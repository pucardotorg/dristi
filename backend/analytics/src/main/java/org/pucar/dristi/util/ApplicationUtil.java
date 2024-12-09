package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.APPLICATION_PATH;

@Slf4j
@Component
public class ApplicationUtil {

	private final Configuration config;
	private final ServiceRequestRepository repository;
	private final Util util;

	@Autowired
	public ApplicationUtil(Configuration config, ServiceRequestRepository repository, Util util) {
		this.config = config;
		this.repository = repository;
		this.util = util;
	}

	public Object getApplication(JSONObject request, String tenantId, String applicationNumber) {
		StringBuilder url = getSearchURLWithParams();
		log.info("Inside ApplicationUtil getApplication :: URL: {}", url);

		request.put("tenantId", tenantId);
		JSONObject criteria = new JSONObject();
		criteria.put("applicationNumber", applicationNumber);
		criteria.put("tenantId", tenantId);
		request.put("criteria", criteria);

		log.info("Inside ApplicationUtil getApplication :: Request: {}", request);

		try {
			String response = repository.fetchResult(url, request);
			log.info("Inside ApplicationUtil getApplication :: Response: {}", response);
			JSONArray applications = util.constructArray(response, APPLICATION_PATH);
			return applications.length() > 0 ? applications.get(0) : null;
		} catch (Exception e) {
			log.error("Error while processing application response", e);
			throw new RuntimeException("Error while processing application response", e);
		}
	}

	private StringBuilder getSearchURLWithParams() {
		return new StringBuilder(config.getApplicationHost())
				.append(config.getApplicationSearchPath());
	}
}
