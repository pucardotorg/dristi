package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.CASE_PATH;

@Slf4j
@Component
public class CaseUtil {

	private final Configuration config;
	private final ServiceRequestRepository repository;
	private final Util util;

	@Autowired
	public CaseUtil(Configuration config, ServiceRequestRepository repository, Util util) {
		this.config = config;
		this.repository = repository;
		this.util = util;
	}

	public Object getCase(JSONObject request, String tenantId, String cnrNumber, String filingNumber, String caseId) {
		StringBuilder url = getSearchURLWithParams();
		log.info("Inside CaseUtil getCaseInternal :: URL: {}", url);

		request.put("tenantId", tenantId);
		JSONArray criteriaArray = new JSONArray();
		JSONObject criteria = new JSONObject();

		if (cnrNumber != null) {
			criteria.put("cnrNumber", cnrNumber);
		}
		if (filingNumber != null) {
			criteria.put("filingNumber", filingNumber);
		}
		if (caseId != null) {
			criteria.put("caseId", caseId);
		}
		criteriaArray.put(criteria);
		request.put("criteria", criteriaArray);

		log.info("Inside CaseUtil getCaseInternal :: Criteria: {}", criteriaArray);

		try {
			String response = repository.fetchResult(url, request);
			log.info("Inside CaseUtil getCaseInternal :: Response: {}", response);
			JSONArray cases = util.constructArray(response, CASE_PATH);
			return cases.length() > 0 ? cases.get(0) : null;
		} catch (Exception e) {
			log.error("Error while processing case response", e);
			throw new RuntimeException("Error while processing case response", e);
		}
	}

	private StringBuilder getSearchURLWithParams() {
		return new StringBuilder(config.getCaseHost())
				.append(config.getCaseSearchPath());
	}
}
