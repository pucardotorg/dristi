package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ElasticSearchRepository;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.MdmsV2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class CaseBundleIndexBuilderService {

    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final CaseBundleService caseBundleService;
    private final MdmsV2Util mdmsV2Util;
    private final ServiceRequestRepository serviceRequestRepository;
    private final ElasticSearchRepository esRepository;

    @Value("classpath:CaseBundleDefault.json")
    private Resource caseDataResource;

    @Autowired
    public CaseBundleIndexBuilderService(Configuration configuration, ObjectMapper objectMapper, CaseBundleService caseBundleService, MdmsV2Util mdmsV2Util,
                                         ServiceRequestRepository serviceRequestRepository, ElasticSearchRepository esRepository) {

        this.configuration = configuration;
        this.objectMapper = objectMapper;
        this.caseBundleService=caseBundleService;
        this.mdmsV2Util=mdmsV2Util;
        this.serviceRequestRepository=serviceRequestRepository;
        this.esRepository=esRepository;
    }

    public Boolean isValidState(String moduleName, String businessService, String state,String tenantID,RequestInfo requestInfo){

        Map<String, String> filters = new HashMap<>();
        filters.put("state", state);
        filters.put("moduleName",moduleName);
        filters.put("businessservice",businessService);

        List<Mdms> mdmsData = mdmsV2Util.fetchMdmsV2Data(requestInfo,tenantID,null,null,configuration.getStateMasterSchema(),true,filters);

        if(mdmsData.isEmpty()){
            return false;
        }

        return true;
    }

    public String getCaseNumber(RequestInfo requestInfo, String filingNumber, String tenantId) {
        String caseId=null;
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setTenantId(tenantId);
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setFilingNumber(filingNumber);
        caseCriteria.setDefaultFields(false);
        List<CaseCriteria> caseList = new ArrayList<>();
        caseList.add(caseCriteria);
        caseSearchRequest.setCriteria(caseList);
        caseSearchRequest.setRequestInfo(requestInfo);

        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getCaseHost()).append(configuration.getCaseSearchUrl());

        Object response = null;
        try {
            response = serviceRequestRepository.fetchResult(uri, caseSearchRequest);
        } catch (Exception e) {
            log.error("Error while fetching case data from service request repository", e);
        }


        try {
            Map<String, Object> responseMap = (Map<String, Object>) response;
            List<Map<String, Object>> criteriaList = (List<Map<String, Object>>) responseMap.get("criteria");

            if (criteriaList == null || criteriaList.isEmpty()) {
                log.error(CASE_ERROR_MESSAGE);
                throw new CustomException(CASE_NOT_FOUND, CASE_ERROR_MESSAGE);
            }

            Map<String, Object> criteria = criteriaList.get(0);

            List<Map<String,Object>> responseList  = (List<Map<String,Object>>) criteria.get("responseList");

            if (responseList != null) {
                caseId = responseList.get(0).get("id").toString();
            }
            else {
                log.error(CASE_ERROR_MESSAGE);
                throw new CustomException(CASE_NOT_FOUND, CASE_ERROR_MESSAGE);
            }

        } catch (Exception e) {
            log.error("Error processing case response", e);
        }

        return caseId;
    }

    @KafkaListener(topics = {"${casemanagement.kafka.workflow.transition.topic}"})
    public void listen(final HashMap<String, Object> record) {
        List<Map<String, Object>> processInstances = (List<Map<String, Object>>) record.get("ProcessInstances");
        String moduleName =null ;
        String businessService=null ;
        String businessId =null;
        String tenantId=null ;
        String stateName=null;
        RequestInfo requestInfo = objectMapper.convertValue(record.get("RequestInfo"), RequestInfo.class);

        if (processInstances != null && !processInstances.isEmpty()) {
            Map<String, Object> processInstance = processInstances.get(0);
            moduleName = (String) processInstance.get("moduleName");
            businessService = (String) processInstance.get("businessService");
            businessId = (String) processInstance.get("businessId");
            tenantId = (String) processInstance.get("tenantId");
            Map<String, Object> state = (Map<String, Object>) processInstance.get("state");
            stateName = (String) state.get("state");
        }

        Boolean isValid = isValidState(moduleName, businessService, stateName, tenantId,requestInfo);
        if(isValid){
            String caseID = getCaseNumber(requestInfo,businessId,tenantId);
            if(caseID!=null){
                String uri = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + configuration.getSearchPath();
                String request = String.format(ES_IDS_QUERY, caseID);
                String response =null;

                try {
                    response = esRepository.fetchDocuments(uri, request);
                } catch (Exception e) {
                    log.error("Error while fetching documents from ElasticSearch", e);

                }

                try {
                    JsonNode rootNode = objectMapper.readTree(response);
                    JsonNode hitsNode = rootNode.path("hits").path("hits");

                    if (hitsNode.isArray() && hitsNode.isEmpty()) {
                        log.error("not able to get data from es for given case ID");
                    } else {
                        JsonNode indexJson = hitsNode.get(0).path("_source");
                        ProcessCaseBundlePdfRequest processCaseBundlePdfRequest = new ProcessCaseBundlePdfRequest();
                        processCaseBundlePdfRequest.setRequestInfo(requestInfo);
                        processCaseBundlePdfRequest.setCaseId(caseID);
                        processCaseBundlePdfRequest.setIndex(indexJson);
                        processCaseBundlePdfRequest.setState(stateName);
                        processCaseBundlePdfRequest.setTenantId(tenantId);
                        StringBuilder url = new StringBuilder();
                        url.append(configuration.getCaseBundlePdfHost()).append(configuration.getProcessCaseBundlePdfPath());

                        Object pdfResponse =null;
                        try {
                            pdfResponse = serviceRequestRepository.fetchResult(url, processCaseBundlePdfRequest);
                        } catch (Exception e) {
                            log.error("Error generating PDF", e);
                        }

                        Map<String, Object> pdfResponseMap = objectMapper.convertValue(pdfResponse, Map.class);
                        Map<String, Object> indexMap = (Map<String, Object>) pdfResponseMap.get("index");
                        JsonNode updateIndexJson = objectMapper.valueToTree(indexMap);

                        String esUpdateUrl = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + "/_update/" + caseID;
                        String esRequest;
                        try {
                            esRequest = String.format(ES_UPDATE_QUERY, objectMapper.writeValueAsString(updateIndexJson));
                            esRepository.fetchDocuments(esUpdateUrl, esRequest);
                        } catch (IOException e) {
                            log.error("Error updating ElasticSearch index with new data", e);
                        }

                    }
                }catch(Exception e){
                    log.error("Not able to parse json body");
                }

            }
            else{
                log.error("No case present for processing");
            }

        }


    }

    @KafkaListener(topics = {"${casemanagement.kafka.case.application.topic}"})
    public void listenCaseApplication(final HashMap<String, Object> record) throws IOException {

        String caseId = ((LinkedHashMap<String, Object>) record.get("cases")).get("id").toString();

        String tenantId = ((LinkedHashMap<String, Object>) record.get("cases")).get("tenantId").toString();

        JsonNode caseData = objectMapper.readTree(caseDataResource.getInputStream());

        if (caseData.has("caseID") && caseData instanceof ObjectNode) {
            ((ObjectNode) caseData).put("caseID", caseId);
        }

        if (caseData.has("tenantId") && caseData instanceof ObjectNode) {
            ((ObjectNode) caseData).put("tenantId", tenantId);
        }

        String esUpdateUrl = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + "/_doc/" + caseId;
        String esRequest;
        try {
            esRequest = String.format(objectMapper.writeValueAsString(caseData));
            esRepository.fetchDocuments(esUpdateUrl, esRequest);
        } catch (IOException e) {
            log.error("Error enriching  ElasticSearch index with new data to initiate index", e);
        }


    }

}
