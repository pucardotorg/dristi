package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class ElasticSearchRepository {

	private final Configuration configuration;
	private final RestTemplate restTemplate;

	@Autowired
	public ElasticSearchRepository(RestTemplate restTemplate, Configuration configuration) {
		this.restTemplate = restTemplate;
		this.configuration = configuration;
	}

	public String getESEncodedCredentials() {
		String credentials = configuration.getEsUsername() + ":" + configuration.getEsPassword();
		return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
	}

	public String fetchDocuments(String uri, String request) {
		try {
			log.info("Query: {}", request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", getESEncodedCredentials());
			HttpEntity<String> entity = new HttpEntity<>(request, headers);

			return restTemplate.postForEntity(uri, entity, String.class).getBody();
		} catch (ResourceAccessException e) {
			log.error("ES is DOWN: {}", e.getMessage(), e);
			throw new CustomException("ES_DOWN", e.getMessage());
		} catch (HttpClientErrorException e) {
			log.error("Client error occurred while querying the ES documents: {}", e.getMessage(), e);
			throw new CustomException("CLIENT_ERROR", e.getMessage());
		} catch (HttpServerErrorException e) {
			log.error("Server error occurred while querying the ES documents: {}", e.getMessage(), e);
			throw new CustomException("SERVER_ERROR", e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected exception occurred while querying the ES documents: {}", e.getMessage(), e);
			throw new CustomException("QUERY_EXEC_ERROR", "Error while executing query: " + e.getMessage());
		}
	}
}
