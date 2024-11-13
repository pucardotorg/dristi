package com.egov.icops_integrationkerala.controller;

import com.egov.icops_integrationkerala.model.*;
import com.egov.icops_integrationkerala.service.IcopsService;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@Slf4j
public class IcopsController {

    private final IcopsService icopsService;

    @Autowired
    public IcopsController(IcopsService icopsService) {
        this.icopsService = icopsService;
    }

    @PostMapping("/v1/integrations/iCops/_sendRequest")
    public ResponseEntity<ProcessResponse> sendPRRequest(@RequestBody TaskRequest taskRequest) throws Exception {
        log.info("api = /v1/_sendRequest , Status = IN-PROGRESS");
        ChannelMessage response = icopsService.sendRequestToIcops(taskRequest);
        ResponseInfo responseInfo = ResponseInfo.builder().build();
        ProcessResponse iCopsResponse = ProcessResponse.builder()
                .responseInfo(responseInfo).channelMessage(response).build();
        log.info("api = /v1/_sendRequest , Status = SUCCESS");
        return new ResponseEntity<>(iCopsResponse, HttpStatus.CREATED);
    }

    @PostMapping("/v1/integrations/iCops/_getAuthToken")
    public ResponseEntity<AuthResponse> getAuthToken(@RequestParam("service_name") String serviceName,
                                                     @RequestParam("service_ky") String serviceKy,
                                                     @RequestParam("auth_type") String authType) throws Exception {
        log.info("api = /getAuthToken , Status = IN-PROGRESS");
        AuthResponse authResponse = icopsService.generateAuthToken(serviceName, serviceKy, authType);
        log.info("api = /getAuthToken , Status = SUCCESS");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/integrations/iCops/_getProcessReport")
    public ResponseEntity<ChannelMessage> getProcessReport(@RequestBody IcopsProcessReport icopsProcessReport) {
        log.info("api = /getProcessReport , Status = IN-PROGRESS");
        ChannelMessage response = icopsService.processPoliceReport(icopsProcessReport);
        log.info("api = /getProcessReport , Status = SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/integrations/iCops/_getLocationBasedJurisdiction")
    public ResponseEntity<LocationBasedJurisdictionResponse> getLocationBasedJurisdiction(@RequestBody LocationRequest locationRequest) throws Exception {
        log.info("api = /_getLocationBasedJurisdiction , Status = IN-PROGRESS");
        LocationBasedJurisdiction locationBasedJurisdiction = icopsService.getLocationBasedJurisdiction(locationRequest);
        ResponseInfo responseInfo = ResponseInfo.builder().build();
        LocationBasedJurisdictionResponse response = LocationBasedJurisdictionResponse.builder()
                .responseInfo(responseInfo)
                .locationBasedJurisdiction(locationBasedJurisdiction).build();
        log.info("api = /_getLocationBasedJurisdiction , Status = SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
