package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Configuration configs;

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
}