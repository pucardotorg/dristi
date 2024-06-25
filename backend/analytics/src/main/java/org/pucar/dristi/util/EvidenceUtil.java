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
public class EvidenceUtil {

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;


	public Object getEvidence(JSONObject request, String tenantId, String artifactNumber) {
		StringBuilder url = getSearchURLWithParams();
		request.put("tenantId", tenantId);

		JSONObject criteria = new JSONObject();
		if (artifactNumber != null) {
			criteria.put("artifactNumber", artifactNumber);
		}
		request.put("criteria", criteria);

		String response = repository.fetchResult(url, request);
		JSONArray artifacts = null;
		try{
			artifacts = util.constructArray(response, ARTIFACT_PATH);
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return artifacts.get(0);
	}

	private StringBuilder getSearchURLWithParams() {
		StringBuilder url = new StringBuilder(config.getEvidenceHost());
		url.append(config.getEvidenceSearchPath());
		return url;
	}
}
