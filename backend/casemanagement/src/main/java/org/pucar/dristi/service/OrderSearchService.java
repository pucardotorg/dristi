package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.VcEntityCriteria;
import org.pucar.dristi.web.models.VcEntityOrderSearchRequest;
import org.pucar.dristi.web.models.VcOrderSearchPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OrderSearchService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderSearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${dristi.dev.order.search.host}")
    private String orderSearchHost;

    @Value("${dristi.dev.order.search.url}")
    private String orderSearchPath;


    public String searchOrder(String referenceId, String tenantId, RequestInfo requestInfo)  {
        StringBuilder orderSearchurl=new StringBuilder();
        orderSearchurl.append(orderSearchHost).append(orderSearchPath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/json;charset=UTF-8");

        requestInfo.setAuthToken(requestInfo.getAuthToken());

        VcOrderSearchPagination vcOrderSearchPagination= VcOrderSearchPagination.builder()
                .limit(1.0)
                .offSet(0.0)
                .build();

        VcEntityCriteria criteria = VcEntityCriteria.builder()
                .id(referenceId)
                .build();

        VcEntityOrderSearchRequest vcEntityOrderSearchRequest = VcEntityOrderSearchRequest.builder()
                .requestInfo(requestInfo)
                .tenantId(tenantId)
                .criteria(criteria)
                .pagination(vcOrderSearchPagination)
                .build();

        HttpEntity<VcEntityOrderSearchRequest> entity = new HttpEntity<>(vcEntityOrderSearchRequest, headers);

        ResponseEntity<Object> response=null;
        try{
            response= restTemplate.exchange(orderSearchurl.toString(), HttpMethod.POST, entity, Object.class);
        }
        catch (Exception e){
            throw new CustomException("ORDER_SEARCH_ERR","error while fetching the order details " +e.getMessage());
        }
        // Extract cnrNumber from the response


        String cnrNumber=null;
        try{
            String responseBodyString = objectMapper.writeValueAsString(response.getBody());
            log.info("Response from the order search: " + responseBodyString);
            cnrNumber = JsonPath.parse(responseBodyString).read("$.list[0].cnrNumber", String.class);
        }
        catch (Exception e){
            throw new CustomException("JSON_PARSING_ERROR","error while extracting cnr number from the order response");
        }
        log.info("CNR Number: " + cnrNumber);
        return cnrNumber;
    }
}
