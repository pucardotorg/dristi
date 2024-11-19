package com.pucar.drishti.web.controllers;


import com.pucar.drishti.config.Configuration;
import com.pucar.drishti.service.InterceptorService;
import jakarta.annotation.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;


@Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-07-02T12:37:46.343081666+05:30[Asia/Kolkata]")
@RestController
@RequestMapping("")
@Slf4j
public class InterceptorApiController {

    private final InterceptorService service;
    private final Configuration configs;

    @Autowired
    public InterceptorApiController(InterceptorService service, Configuration configs) {
        this.service = service;
        this.configs = configs;
    }


    @GetMapping("/v1/redirect")
    public ResponseEntity<HttpHeaders> redirectHandler(@RequestParam("result") String result, @RequestParam("filestoreId") String filestoreId, @RequestParam("userType") String userType) {
        log.info("api=/v1/redirect, result = IN_PROGRESS result = {}, filestoreId = {}, userType = {}", result, filestoreId, userType);
        log.info("redirecting through get method");

        // Construct the final redirect URL
        String redirectUri = configs.getRedirectUrl() + userType + "/dristi";
        redirectUri += "?result=" + result + "&filestoreId=" + filestoreId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUri));
        log.info("redirectUri {}", redirectUri);
        log.info("api=/v1/redirect, result = SUCCESS result = {}, filestoreId = {}, userType = {}", result, filestoreId, userType);
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }


    @PostMapping("/v1/_intercept")
    public ModelAndView eSignV1Interceptor(@RequestParam("eSignResponse") String response, @RequestParam("espTxnID") String espId) {
        log.info("api=/v1/_intercept, result = IN_PROGRESS eSignResponse = {}, espTxnID = {}", response, espId);

        String filestoreId = "";
        String result = "error";

        int firstHyphenIndex = espId.indexOf("-");
        int secondHyphenIndex = espId.indexOf("-", firstHyphenIndex + 1);
        log.info("calculating tenantId,pageModule,fileStore id");
        String tenantId = espId.substring(0, firstHyphenIndex);
        String pageModule = espId.substring(firstHyphenIndex + 1, secondHyphenIndex);
        String txnId = espId.substring(secondHyphenIndex + 1);
        log.info("tenantId {} ,pageModule {} , txnId {}", tenantId, pageModule, txnId);
        try {
            log.info("sending response to sign doc");
            filestoreId = service.process(response, espId, tenantId, txnId);
            result = "success";
            log.info("successfully sign doc");
        } catch (Exception e) {
            log.error("Error Occured While signing the doc");

        }

        log.info("generating uri to redirect");

        String userType;
        if (pageModule.equals("en")) {
            userType = "employee";
        } else if (pageModule.equals("ci")) {
            userType = "citizen";
        } else {
            throw new RuntimeException("Invalid pageModule: " + pageModule);
        }

        // Redirect to the GET handler with parameters
        ModelAndView modelAndView = new ModelAndView("redirect:/v1/redirect");
        modelAndView.addObject("result", result);
        modelAndView.addObject("filestoreId", filestoreId);
        modelAndView.addObject("userType", userType);
        log.info("api=/v1/_intercept, result = SUCCESS eSignResponse = {}, espTxnID = {}", response, espId);
        return modelAndView;

    }

}
