package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class HearingUtil {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final Configuration configs;

    @Autowired
    public HearingUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
    }

    public Boolean fetchHearingDetails(HearingExistsRequest hearingExistsRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getHearingHost()).append(configs.getHearingExistsPath());

        Object response = new HashMap<>();
        HearingExistsResponse hearingExistsResponse = new HearingExistsResponse();
        try {
            response = restTemplate.postForObject(uri.toString(), hearingExistsRequest, Map.class);
            hearingExistsResponse = mapper.convertValue(response, HearingExistsResponse.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_HEARING, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_HEARING, e.getMessage());
        }
        return hearingExistsResponse.getOrder().getExists();
    }
}