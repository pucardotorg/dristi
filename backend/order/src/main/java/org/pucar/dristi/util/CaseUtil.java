package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_CASE;

@Slf4j
@Component
public class CaseUtil {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private Configuration configs;

	public Boolean fetchOrderDetails(RequestInfo requestInfo, String cnrNumber) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getCaseHost()).append(configs.getCasePath());

		CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
		caseSearchRequest.setRequestInfo(requestInfo);
		CaseCriteria caseCriteria = new CaseCriteria();
		caseCriteria.setCnrNumber(cnrNumber);
		List<CaseCriteria> criteriaList = new ArrayList<>();
		criteriaList.add(caseCriteria);
		caseSearchRequest.setCriteria(criteriaList);

		Object response = new HashMap<>();
		CaseResponse caseResponse = new CaseResponse();
		try {
			response = restTemplate.postForObject(uri.toString(), caseSearchRequest, Map.class);
			caseResponse = mapper.convertValue(response, CaseResponse.class);
		} catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_CASE, e);
		}

		return !caseResponse.getCases().isEmpty();
	}
}