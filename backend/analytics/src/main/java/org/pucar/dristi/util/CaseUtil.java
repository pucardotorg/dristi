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

	@Autowired
	private Configuration config;

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private Util util;



	public Object getCase(JSONObject request, String tenantId, String cnrNumber, String filingNumber) {
		StringBuilder url = getSearchURLWithParams();
		request.put("tenantId", tenantId);

		JSONArray criteriaArray = new JSONArray();
		JSONObject criteria = new JSONObject();
		if (cnrNumber != null) {
			criteria.put("cnrNumber", cnrNumber);
		}
		if (filingNumber != null) {
			criteria.put("filingNumber", filingNumber);
		}
		criteriaArray.put(criteria);

		request.put("criteria", criteriaArray);
		String response = repository.fetchResult(url, request);
		JSONArray cases = null;
		try{
			cases = util.constructArray(response, CASE_PATH);
		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return cases.get(0);
	}

	public Object getCase(JSONObject request, String tenantId, String cnrNumber, String filingNumber,String caseId) {
		StringBuilder url = getSearchURLWithParams();
		log.info("Inside Case util getCase with caseId :: url: " + url);

		request.put("tenantId", tenantId);

		JSONArray criteriaArray = new JSONArray();
		JSONObject criteria = new JSONObject();
		if (cnrNumber != null) {
			criteria.put("cnrNumber", cnrNumber);
		}
		if (filingNumber != null) {
			criteria.put("filingNumber", filingNumber);
		}
		if (filingNumber != null) {
			criteria.put("caseId", caseId);
		}
		criteriaArray.put(criteria);

		request.put("criteria", criteriaArray);
		log.info("Inside Case util getCase with caseId :: criteria: " + criteriaArray);
		String response = repository.fetchResult(url, request);
		log.info("Inside Case util getCase with caseId :: response: " + response);

		JSONArray cases = null;
		try{
			cases = util.constructArray(response, CASE_PATH);
			log.info("Inside Case util getCase with caseId :: cases: " + cases.toString());

		} catch (Exception e){
			log.error("Error while building from case response", e);
		}

		return cases.get(0);
	}


	private StringBuilder getSearchURLWithParams() {
		StringBuilder url = new StringBuilder(config.getCaseHost());
		url.append(config.getCaseSearchPath());
		return url;
	}
}
