package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.AdvocateResponse;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.pucar.dristi.web.models.AdvocateSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class AdvocateUtil {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private Configuration configs;

	public Boolean fetchAdvocateDetails(RequestInfo requestInfo, String advocateId) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getAdvocateHost()).append(configs.getAdvocatePath());

		AdvocateSearchRequest advocateSearchRequest = new AdvocateSearchRequest();
		advocateSearchRequest.setRequestInfo(requestInfo);
		advocateSearchRequest.setStatus(List.of(INWORKFLOW_STATUS));
		AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
		advocateSearchCriteria.setId(advocateId);
		List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
		criteriaList.add(advocateSearchCriteria);
		advocateSearchRequest.setCriteria(criteriaList);

		Object response = new HashMap<>();
		AdvocateResponse advocateResponse = new AdvocateResponse();
		try {
			response = restTemplate.postForObject(uri.toString(), advocateSearchRequest, Map.class);
			advocateResponse = mapper.convertValue(response, AdvocateResponse.class);
		} catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_ADVOCATE, e);
		}

		return !advocateResponse.getAdvocates().isEmpty();
	}
}