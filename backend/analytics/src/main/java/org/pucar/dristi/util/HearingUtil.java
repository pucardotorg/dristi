package org.pucar.dristi.util;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import static org.pucar.dristi.config.ServiceConstants.*;

import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		String response = repository.fetchResult(url, request);
		JSONArray hearings = null;
		try{
			hearings = util.constructArray(response, HEARING_PATH);
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return hearings.get(0);
	}

	private StringBuilder getSearchURLWithParams(String applicationNumber, String cnrNumber, String hearingId, String tenantId) {
		StringBuilder url = new StringBuilder(config.getHearingHost());
		url.append(config.getHearingSearchPath());
		url.append(APPLICATION_NUMBER);
		url.append(applicationNumber);
		url.append(CNR_NUMBER);
		url.append(cnrNumber);
		url.append(HEARING_ID);
		url.append(hearingId);
		url.append(TENANT_ID);
		url.append(tenantId);
		return url;
	}
}
