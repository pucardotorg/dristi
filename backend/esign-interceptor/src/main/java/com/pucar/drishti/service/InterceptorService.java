package com.pucar.drishti.service;


import com.pucar.drishti.config.Configuration;
import com.pucar.drishti.repository.ServiceRequestRepository;
import com.pucar.drishti.web.models.SignDocParameter;
import com.pucar.drishti.web.models.SignDocRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;

import static com.pucar.drishti.config.ServiceConstants.AUTHORIZATION;

@Service
@Slf4j
public class InterceptorService {

    private final ServiceRequestRepository restCall;
    private final Configuration configs;

    @Autowired
    public InterceptorService(ServiceRequestRepository restCall, Configuration configs) {
        this.restCall = restCall;
        this.configs = configs;
    }

    public String process(String response, String espId, String tenantId, String txnId) {
        log.info("operation = process, result = IN_PROGRESS, response = {}, espId = {} , tenantId = {} , fileStoreId = {}", response, espId, tenantId, txnId);
        log.info("generating token for created user");
        log.info(espId);
        String token = oAuthForDristi();
        log.info("validating by calling filestore id");
        SignDocRequest request = getSignDocRequest(token, response, txnId, tenantId);
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getESignHost()).append(configs.getESignEndPoint());
        Object result = restCall.callESign(uri, request);

        log.info("signed fileStore id {} :", result.toString());
        log.info("operation = process, result = SUCCESS, response = {}, espId = {} , tenantId = {} , fileStoreId = {}", response, espId, tenantId, txnId);
        return result.toString();

    }

    private SignDocRequest getSignDocRequest(String token, String response, String txnId, String tenantId) {

        log.info("operation = getSignDocRequest, result = IN_PROGRESS, token = {} , response = {} , txnId = {} , tenantId = {}", token, response, txnId, tenantId);
        RequestInfo requestInfo = RequestInfo.builder().authToken(token).build();  //fixme: update user for this

        SignDocParameter parameter = SignDocParameter.builder()
                .txnId(txnId).response(response).tenantId(tenantId).build();

        log.info("operation = getSignDocRequest, result = SUCCESS, token = {} , response = {} , fileStoreId = {} , tenantId = {}", token, response, txnId, tenantId);
        return SignDocRequest.builder().requestInfo(requestInfo).eSignParameter(parameter).build();


    }

    private String oAuthForDristi() {
        log.info("operation = oAuthForDristi, result = IN_PROGRESS");

        StringBuilder uri = new StringBuilder();
        uri.append(configs.getOathHost()).append(configs.getOathEndPoint());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", configs.getUserName());
        map.add("password", configs.getPassword());
        map.add("tenantId", configs.getTenantId());
        map.add("userType", configs.getUserType());
        map.add("scope", configs.getScope());
        map.add("grant_type", configs.getGrantType());


        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache");
        headers.setConnection("keep-alive");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", AUTHORIZATION);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        Object response = restCall.fetchResult(uri, request);
        log.info("operation = oAuthForDristi, result = SUCCESS");
        return ((LinkedHashMap) response).get("access_token").toString();
    }
}