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
public class HearingUtil {

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;


	public Object getHearing(JSONObject request, String applicationNumber, String cnrNumber, String hearingId, String tenantId) {
		StringBuilder url = getSearchURLWithParams(applicationNumber, cnrNumber, hearingId, tenantId);
		log.info("Inside hearing util getHearing :: url: " + url);
		String response = repository.fetchResult(url, request);
		log.info("Inside hearing util getHearing:: response: " + response);
		JSONArray hearings = null;
		try {
			hearings = util.constructArray(response, HEARING_PATH);
			log.info("Inside hearing util getHearing :: hearings: " + hearings.toString());
		} catch (Exception e) {
			log.error("Error while building from hearing response", e);
		}

		return hearings.get(0);
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
