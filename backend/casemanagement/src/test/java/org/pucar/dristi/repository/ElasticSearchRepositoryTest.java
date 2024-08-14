package org.pucar.dristi.repository;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElasticSearchRepositoryTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private Configuration configuration;

	@InjectMocks
	private ElasticSearchRepository elasticSearchRepository;

	private String username = "user";
	private String password = "pass";
	private String uri = "http://localhost:9200/_search";
	private String request = "{\"query\": {\"match_all\": {}}}";
	private String response = "{\"hits\": {\"total\": 1}}";

	@BeforeEach
	void setUp() {
		when(configuration.getEsUsername()).thenReturn(username);
		when(configuration.getEsPassword()).thenReturn(password);
	}

	@Test
	void testGetESEncodedCredentials() {
		String expectedCredentials = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
		assertEquals(expectedCredentials, elasticSearchRepository.getESEncodedCredentials());
	}

	@Test
	void testFetchDocumentsSuccess() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", elasticSearchRepository.getESEncodedCredentials());
		HttpEntity<String> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		when(restTemplate.postForEntity(eq(uri), eq(entity), eq(String.class))).thenReturn(responseEntity);

		String result = elasticSearchRepository.fetchDocuments(uri, request);
		assertEquals(response, result);
	}

	@Test
	void testFetchDocumentsResourceAccessException() {
		when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
				.thenThrow(new ResourceAccessException("ES is DOWN"));

		CustomException exception = assertThrows(CustomException.class, () -> {
			elasticSearchRepository.fetchDocuments(uri, request);
		});

		assertEquals("ES_DOWN", exception.getCode());
	}

	@Test
	void testFetchDocumentsHttpClientErrorException() {
		when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

		CustomException exception = assertThrows(CustomException.class, () -> {
			elasticSearchRepository.fetchDocuments(uri, request);
		});

		assertEquals("CLIENT_ERROR", exception.getCode());
	}

	@Test
	void testFetchDocumentsHttpServerErrorException() {
		when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
				.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

		CustomException exception = assertThrows(CustomException.class, () -> {
			elasticSearchRepository.fetchDocuments(uri, request);
		});

		assertEquals("SERVER_ERROR", exception.getCode());
	}

	@Test
	void testFetchDocumentsException() {
		when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
				.thenThrow(new RuntimeException("Unexpected error"));

		CustomException exception = assertThrows(CustomException.class, () -> {
			elasticSearchRepository.fetchDocuments(uri, request);
		});

		assertEquals("QUERY_EXEC_ERROR", exception.getCode());
	}
}
