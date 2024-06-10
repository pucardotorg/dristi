package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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
		AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
		advocateSearchCriteria.setId(advocateId);
		List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
		criteriaList.add(advocateSearchCriteria);
		advocateSearchRequest.setCriteria(criteriaList);

		Object response = new HashMap<>();
		AdvocateListResponse advocateResponse = new AdvocateListResponse();
		try {
			response = restTemplate.postForObject(uri.toString(), advocateSearchRequest, Map.class);
			advocateResponse = mapper.convertValue(response, AdvocateListResponse.class);
			log.info("Advocate response :: {}", advocateResponse);
		} catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_ADVOCATE, e);
			throw new CustomException(ERROR_WHILE_FETCHING_FROM_ADVOCATE, e.getMessage());
		}

		List<Advocate> list = advocateResponse.getAdvocates().get(0).getResponseList().stream().filter(Advocate::getIsActive).toList();

		return !list.isEmpty();
	}
}