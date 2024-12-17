package org.pucar.dristi.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.json.JSONObject;
import org.pucar.dristi.config.ServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.EXTERNAL_SERVICE_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.SEARCHER_SERVICE_EXCEPTION;

@Repository
@Slf4j
public class ServiceRequestRepository {

	private final ObjectMapper mapper;

	private final RestTemplate restTemplate;

	@Autowired
	public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
		this.mapper = mapper;
		this.restTemplate = restTemplate;
    }


	public String fetchResult(StringBuilder uri, JSONObject request) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

			// Send HTTP POST request and receive response as JSON object
			response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, String.class).getBody();
		} catch (HttpClientErrorException e) {
			log.error(ServiceConstants.EXTERNAL_SERVICE_EXCEPTION, e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error(ServiceConstants.SEARCHER_SERVICE_EXCEPTION, uri, request, e);
		}

		return response;
	}

	public Object fetchResult(StringBuilder uri, Object request) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		try {
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		} catch (HttpClientErrorException e) {
			log.error(EXTERNAL_SERVICE_EXCEPTION + " URI: {}", uri, e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error(SEARCHER_SERVICE_EXCEPTION, e);
		}

		return response;
	}
}