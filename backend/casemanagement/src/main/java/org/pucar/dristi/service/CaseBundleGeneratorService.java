package org.pucar.dristi.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.CaseBundleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Component
public class CaseBundleGeneratorService {

    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final CaseBundleService caseBundleService;

    @Autowired
    public CaseBundleGeneratorService( Configuration configuration, ObjectMapper objectMapper, CaseBundleService caseBundleService) {

        this.configuration = configuration;
        this.objectMapper = objectMapper;
        this.caseBundleService=caseBundleService;
    }


    @KafkaListener(topics = {"${casemanagement.kafka.bundle.create.topic}"})
    public void listen(final HashMap<String, Object> record) {
        CaseBundleRequest caseBundleRequest = objectMapper.convertValue(record,CaseBundleRequest.class);
        caseBundleService.getCaseBundle(caseBundleRequest);
    }

}
