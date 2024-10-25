package digit.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import digit.config.Configuration;
import digit.web.models.Advocate;
import digit.web.models.AdvocateListResponse;
import digit.web.models.AdvocateSearchCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import digit.web.models.AdvocateSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdvocateUtil {

    private RestTemplate restTemplate;

    private ObjectMapper mapper;

    private Configuration configs;

    @Autowired
    public AdvocateUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
    }

    public Set<String> getAdvocate(RequestInfo requestInfo, List<String> advocateIds) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getAdvocateHost()).append(configs.getAdvocatePath());

        AdvocateSearchRequest advocateSearchRequest = new AdvocateSearchRequest();
        advocateSearchRequest.setRequestInfo(requestInfo);
        List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
        for(String id: advocateIds){
            AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
            advocateSearchCriteria.setId(id);
            criteriaList.add(advocateSearchCriteria);
        }
        advocateSearchRequest.setCriteria(criteriaList);
        Object response;
        AdvocateListResponse advocateResponse;
        try {
            response = restTemplate.postForObject(uri.toString(), advocateSearchRequest, Map.class);
            advocateResponse = mapper.convertValue(response, AdvocateListResponse.class);
            log.info("Advocate response :: {}", advocateResponse);
        } catch (Exception e) {
            log.error("ERROR_WHILE_FETCHING_FROM_ADVOCATE", e);
            throw new CustomException("ERROR_WHILE_FETCHING_FROM_ADVOCATE", e.getMessage());
        }
        List<Advocate> list = new ArrayList<>();

        advocateResponse.getAdvocates().forEach(advocate -> {
            List<Advocate> activeAdvocates = advocate.getResponseList().stream()
                    .filter(Advocate::getIsActive)
                    .toList();
            list.addAll(activeAdvocates);
        });


        return list.stream().map(Advocate::getIndividualId).collect(Collectors.toSet());
    }
}