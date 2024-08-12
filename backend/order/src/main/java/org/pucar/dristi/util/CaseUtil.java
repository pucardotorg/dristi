package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CaseUtil {

	private RestTemplate restTemplate;

	private ObjectMapper mapper;

	private Configuration configs;

	@Autowired
	public CaseUtil(RestTemplate restTemplate, Configuration configs, ObjectMapper mapper) {
		this.restTemplate = restTemplate;
		this.configs = configs;
		this.mapper = mapper;
	}

	public Boolean fetchCaseDetails(RequestInfo requestInfo, String cnrNumber, String filingNumber) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getCaseHost()).append(configs.getCasePath());

		CaseExistsRequest caseExistsRequest = new CaseExistsRequest();
		caseExistsRequest.setRequestInfo(requestInfo);
		CaseExists caseCriteria = new CaseExists();
		caseCriteria.setCnrNumber(cnrNumber);
		caseCriteria.setFilingNumber(filingNumber);
		List<CaseExists> criteriaList = new ArrayList<>();
		criteriaList.add(caseCriteria);
		caseExistsRequest.setCriteria(criteriaList);

		Object response = new HashMap<>();
		CaseExistsResponse caseResponse = new CaseExistsResponse();
		try {
			response = restTemplate.postForObject(uri.toString(), caseExistsRequest, Map.class);
			caseResponse = mapper.convertValue(response, CaseExistsResponse.class);
		} catch (Exception e) {
			log.error("ERROR_WHILE_FETCHING_FROM_CASE :: {}", e.toString());
		}

		if(caseResponse.getCriteria().isEmpty())
			return false;
		return caseResponse.getCriteria().get(0).getExists();
	}
}