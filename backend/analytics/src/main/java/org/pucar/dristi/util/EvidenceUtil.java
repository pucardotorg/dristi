package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.ARTIFACT_PATH;
import static org.pucar.dristi.config.ServiceConstants.PARSING_ERROR;

@Slf4j
@Component
public class EvidenceUtil {

	private final Configuration config;
	private final ServiceRequestRepository repository;
	private final Util util;

	@Autowired
	public EvidenceUtil(Configuration config, ServiceRequestRepository repository, Util util) {
		this.config = config;
		this.repository = repository;
		this.util = util;
	}

	public Object getEvidence(JSONObject request, String tenantId, String artifactNumber) {
		StringBuilder url = getSearchURLWithParams();
		log.info("Inside EvidenceUtil getEvidence :: URL: {}", url);

		request.put("tenantId", tenantId);

		JSONObject criteria = new JSONObject();
		if (artifactNumber != null) {
			criteria.put("artifactNumber", artifactNumber);
		}
		request.put("criteria", criteria);

		try {
			String response = repository.fetchResult(url, request);
			log.info("Inside EvidenceUtil getEvidence :: Response: {}", response);

			JSONArray artifacts = util.constructArray(response, ARTIFACT_PATH);
			return artifacts.length() > 0 ? artifacts.get(0) : null;
		} catch (Exception e) {
			log.error("Error while fetching or processing the evidence response", e);
			throw new CustomException(PARSING_ERROR,"Error while processing evidence response :: " + e.getMessage());
		}
	}

	private StringBuilder getSearchURLWithParams() {
		return new StringBuilder(config.getEvidenceHost())
				.append(config.getEvidenceSearchPath());
	}
}
