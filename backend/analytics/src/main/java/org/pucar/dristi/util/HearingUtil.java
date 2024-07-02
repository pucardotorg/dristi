package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.APPLICATION_NUMBER;
import static org.pucar.dristi.config.ServiceConstants.CNR_NUMBER;
import static org.pucar.dristi.config.ServiceConstants.HEARING_ID;
import static org.pucar.dristi.config.ServiceConstants.TENANT_ID;
import static org.pucar.dristi.config.ServiceConstants.HEARING_PATH;

@Slf4j
@Component
public class HearingUtil {

	private final Configuration config;
	private final ServiceRequestRepository repository;
	private final Util util;

	@Autowired
	public HearingUtil(Configuration config, ServiceRequestRepository repository, Util util) {
		this.config = config;
		this.repository = repository;
		this.util = util;
	}

	public Object getHearing(JSONObject request, String applicationNumber, String cnrNumber, String hearingId, String tenantId) {
		StringBuilder url = getSearchURLWithParams(applicationNumber, cnrNumber, hearingId, tenantId);
		log.info("Inside HearingUtil getHearing :: URL: {}", url);

		try {
			String response = repository.fetchResult(url, request);
			log.info("Inside HearingUtil getHearing :: Response: {}", response);

			JSONArray hearings = util.constructArray(response, HEARING_PATH);
			return hearings.length() > 0 ? hearings.get(0) : null;
		} catch (Exception e) {
			log.error("Error while fetching or processing the hearing response", e);
			throw new RuntimeException("Error while fetching or processing the hearing response", e);
		}
	}

	private StringBuilder getSearchURLWithParams(String applicationNumber, String cnrNumber, String hearingId, String tenantId) {
		StringBuilder url = new StringBuilder(config.getHearingHost());
		url.append(config.getHearingSearchPath());
		url.append(APPLICATION_NUMBER);
		if (applicationNumber != null) {
			url.append(applicationNumber);
		}
		url.append(CNR_NUMBER);
		if (cnrNumber != null) {
			url.append(cnrNumber);
		}
		url.append(HEARING_ID);
		url.append(hearingId);
		url.append(TENANT_ID);
		url.append(tenantId);
		return url;
	}
}
