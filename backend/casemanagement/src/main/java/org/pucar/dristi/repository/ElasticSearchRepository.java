package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.config.ServiceConfiguration;
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

	private final ServiceConfiguration config;
	private final RestTemplate restTemplate;

	@Autowired
	public ElasticSearchRepository(RestTemplate restTemplate, ServiceConfiguration config) {
		this.restTemplate = restTemplate;
		this.config = config;
	}

	public String getESEncodedCredentials() {
		String credentials = config.getEsUsername() + ":" + config.getEsPassword();
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
		} catch (HttpClientErrorException e) {
			log.error("Client error occurred while querying the ES documents: {}", e.getMessage(), e);
		} catch (HttpServerErrorException e) {
			log.error("Server error occurred while querying the ES documents: {}", e.getMessage(), e);
		} catch (Exception e) {
			log.error("Unexpected exception occurred while querying the ES documents: {}", e.getMessage(), e);
		}
		return null;
	}
}
