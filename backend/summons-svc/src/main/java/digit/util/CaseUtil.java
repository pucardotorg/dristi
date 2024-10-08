package digit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.web.models.CaseSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static digit.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_CASE;

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

    public JsonNode searchCaseDetails(CaseSearchRequest caseSearchRequest) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getCaseHost()).append(configs.getCaseSearchPath());

        Object response = new HashMap<>();
        try {
            response = restTemplate.postForObject(uri.toString(), caseSearchRequest, Map.class);
            JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(response));
            JsonNode caseList = jsonNode.get("criteria").get(0).get("responseList");
            return caseList.get(0);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_CASE, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_CASE, e.getMessage());
        }
    }
}