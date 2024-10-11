package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class CaseUtil {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final Configuration configs;

    @Autowired
    public CaseUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
    }

    public Boolean fetchCaseDetails(CaseExistsRequest caseExistsRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getCaseHost()).append(configs.getCaseExistsPath());

        Object response = new HashMap<>();
        CaseExistsResponse caseExistsResponse = new CaseExistsResponse();
        try {
            response = restTemplate.postForObject(uri.toString(), caseExistsRequest, Map.class);
            caseExistsResponse = mapper.convertValue(response, CaseExistsResponse.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_CASE, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_CASE, e.getMessage());

        }
        return caseExistsResponse.getCriteria().get(0).getExists();
    }

    public JsonNode searchCaseDetails(CaseSearchRequest caseSearchRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getCaseHost()).append(configs.getCaseSearchPath());

        try {
            Object response = restTemplate.postForObject(uri.toString(), caseSearchRequest, Map.class);
            if (response == null) {
                throw new CustomException(ERROR_WHILE_FETCHING_FROM_CASE, "Received null response from case search");
            }

            JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(response));
            JsonNode criteria = jsonNode.get("criteria");
            if (criteria == null || criteria.size() == 0 || !criteria.get(0).has("responseList")) {
                throw new CustomException(ERROR_WHILE_FETCHING_FROM_CASE, "Invalid response structure");
            }

            JsonNode caseList = criteria.get(0).get("responseList");
            if (caseList.size() == 0) {
                return null; // or throw an exception, depending on your requirements
            }

            // Returning the first item. Consider returning the whole list if needed.
            return caseList.get(0);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_CASE, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_CASE, e.getMessage());
        }
    }
}