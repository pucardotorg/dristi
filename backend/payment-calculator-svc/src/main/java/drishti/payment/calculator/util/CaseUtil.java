package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.web.models.CaseCriteria;
import drishti.payment.calculator.web.models.CaseSearchRequest;
import drishti.payment.calculator.web.models.EFillingCalculationCriteria;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static drishti.payment.calculator.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_CASE;
import static drishti.payment.calculator.config.ServiceConstants.FLOW_JAC;

@Component
@Slf4j
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


    public Map<String, List<JsonNode>> getAdvocateForLitigant(RequestInfo requestInfo, String filingNumber, String tenantId) {
        CaseCriteria criteria = CaseCriteria.builder()
                .filingNumber(filingNumber)
                .defaultFields(false)
                .build();
        CaseSearchRequest searchRequest = CaseSearchRequest.builder()
                .requestInfo(requestInfo)
                .tenantId(tenantId)
                .flow(FLOW_JAC)
                .criteria(Collections.singletonList(criteria)).build();

        JsonNode caseNode = searchCaseDetails(searchRequest);

        JsonNode representatives = caseNode.get("representatives");
        JsonNode litigants = caseNode.get("litigants");

        // Extract litigant IDs from the JSON node
        Set<String> litigantIds = Optional.ofNullable(litigants)
                .map(litigant -> StreamSupport.stream(litigant.spliterator(), false)
                        .map(litigantNode -> litigantNode.get("individualId"))
                        .filter(Objects::nonNull)
                        .map(JsonNode::asText)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());


        // Process representatives and map them to their respective litigants
        Map<String, List<JsonNode>> representativesMap =Optional.ofNullable(representatives)
                .map(repNodes -> StreamSupport.stream(repNodes.spliterator(), false))
                .orElse(Stream.empty()) // Handle null representatives safely
                .filter(repNode -> repNode.has("isActive") && repNode.get("isActive").asBoolean()) // Filter only active reps
                .flatMap(repNode -> {
                    JsonNode representing = repNode.get("representing");
                    if (representing == null || !representing.isArray()) return Stream.empty(); // Handle null or non-array case

                    return StreamSupport.stream(representing.spliterator(), false)
                            .map(repLitigantNode -> repLitigantNode.get("individualId"))
                            .filter(Objects::nonNull)
                            .map(JsonNode::asText)
                            .filter(litigantIds::contains) // Ensure the litigant exists in the litigants list
                            .map(litigantId -> new AbstractMap.SimpleEntry<>(litigantId, repNode)); // Pair litigant ID with repNode
                })
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        return representativesMap;

    }

}
