package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class EvidenceStatusUpdateService {

    private final ObjectMapper objectMapper;
    private final EvidenceService evidenceService;
    private final EvidenceEnrichment evidenceEnrichment;
    private final Producer producer;
    private final Configuration config;

    @Autowired
    public EvidenceStatusUpdateService(ObjectMapper objectMapper, EvidenceService evidenceService, EvidenceEnrichment evidenceEnrichment, Producer producer, Configuration config) {
        this.objectMapper = objectMapper;
        this.evidenceService = evidenceService;
        this.evidenceEnrichment = evidenceEnrichment;
        this.producer = producer;
        this.config = config;
    }

    public void updateEvidenceStatus(Map<String, Object> record) {
        log.info("method: updateEvidenceStatus , result: InProgress");

        try {
            String application = objectMapper.writeValueAsString(record);
            LinkedHashMap<String, Object> requestInfoMap = JsonPath.read(application, REQUEST_INFO_PATH);
            RequestInfo requestInfo = objectMapper.convertValue(requestInfoMap, RequestInfo.class);
            String applicationNumber = JsonPath.read(application, APPLICATION_NUMBER_PATH);
            String status = JsonPath.read(application, APPLICATION_STATUS_PATH);

            EvidenceSearchCriteria evidenceSearchCriteria = EvidenceSearchCriteria.builder().applicationNumber(applicationNumber).build();
            List<Artifact> artifacts = evidenceService.searchEvidence(requestInfo, evidenceSearchCriteria, null);

            log.info("updating evidence for application number: {} , status : {} , no of evidence: {}", applicationNumber, status, artifacts.size());
            for (Artifact artifact : artifacts) {

                artifact.setStatus(status);

                EvidenceRequest evidenceRequest = new EvidenceRequest();
                evidenceRequest.setRequestInfo(requestInfo);
                evidenceRequest.setArtifact(artifact);

                evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);
                producer.push(config.getUpdateEvidenceWithoutWorkflowKafkaTopic(), evidenceRequest);

            }
            log.info("successfully updated evidence for application number: {} , status : {} , no of evidence: {}", applicationNumber, status, artifacts.size());

        } catch (JsonProcessingException e) {
            log.error("Error occurred while processing json, method: updateEvidenceStatus , result: Failure");
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("Error occurred while updating evidence, method: updateEvidenceStatus , result: Failure");
            log.error("Error: {}", e.toString());

        }
        log.info("method: updateEvidenceStatus , result: Success");

    }
}
