package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.ApplicationExistsRequest;
import org.pucar.dristi.web.models.ApplicationExistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_APPLICATION_SERVICE;

@Slf4j
@Component
public class ApplicationUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final Configuration configs;

    @Autowired
    public ApplicationUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
    }

    public ApplicationExistsResponse fetchApplicationDetails(ApplicationExistsRequest applicationExistsRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getApplicationHost()).append(configs.getApplicationExistsPath());

        Object response = new HashMap<>();
        ApplicationExistsResponse applicationExistsResponse = new ApplicationExistsResponse();
        try {
            response = restTemplate.postForObject(uri.toString(), applicationExistsRequest, Map.class);
            applicationExistsResponse = mapper.convertValue(response, ApplicationExistsResponse.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_APPLICATION_SERVICE, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_APPLICATION_SERVICE, e.getMessage());

        }
        return applicationExistsResponse;
    }
}