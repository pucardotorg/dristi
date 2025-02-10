package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static digit.config.ServiceConstants.ERROR_FROM_CASE;

@Slf4j
@Component
public class CaseUtil {

    private final Configuration configuration;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public CaseUtil(Configuration configuration, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<CourtCase> getCaseDetails(CaseDiaryGenerateRequest generateRequest) {

        String caseSearchText = generateRequest.getDiary().getCaseNumber();
        RequestInfo requestInfo = generateRequest.getRequestInfo();

        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getCaseHost()).append(configuration.getCaseSearchPath());

        CaseCriteria caseCriteria = CaseCriteria.builder().caseSearchText(caseSearchText).build();

        CaseSearchRequest caseSearchRequest = CaseSearchRequest.builder()
                .requestInfo(requestInfo)
                .criteria(Collections.singletonList(caseCriteria))
                .build();

        Object response;
        CaseListResponse caseListResponse;

        try {
            response = restTemplate.postForObject(uri.toString(), caseSearchRequest, Map.class);
            caseListResponse = objectMapper.convertValue(response,CaseListResponse.class);
            log.info("Case response : {} ", caseListResponse);
        }
        catch (Exception e) {
            log.error("Error while fetching from case service");
            throw new CustomException(ERROR_FROM_CASE,e.getMessage());
        }

        if (caseListResponse != null && caseListResponse.getCriteria() != null && !caseListResponse.getCriteria().isEmpty()) {
            return caseListResponse.getCriteria().get(0).getResponseList();
        }
        return null;
    }

}
