package digit.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static digit.config.ServiceConstants.EXTERNAL_SERVICE_EXCEPTION;
import static digit.config.ServiceConstants.SEARCHER_SERVICE_EXCEPTION;

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
    public Object postMethod(StringBuilder uri, Object requestObject) {
        Object response = null;
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            log.debug("Sending POST request to URL: {}", uri.toString());
            log.debug("Request body: {}", requestObject);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri.toString(), HttpMethod.POST, requestEntity, String.class);

            response = responseEntity.getBody();

            log.debug("Response: {}", response);
        } catch (Exception e) {
            log.error("Service call exception in POST method", e);
        }

        return response; // Return the response string
    }


    public Object fetchResult(StringBuilder uri, Object request) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object response = null;
        try {
            response = restTemplate.postForObject(uri.toString(), request, Map.class);
        } catch (HttpClientErrorException e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(SEARCHER_SERVICE_EXCEPTION, e);
        }

        return response;
    }
}