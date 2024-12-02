package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.AuditDetails;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseBundleRepository;
import org.pucar.dristi.repository.ElasticSearchRepository;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.egov.tracer.model.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.ES_BULK_QUERY;

import java.io.IOException;

@Service
@Slf4j
public class CaseBundleService {

    private final ElasticSearchRepository esRepository;
    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final ServiceRequestRepository serviceRequestRepository;
    private final CaseBundleRepository caseBundleRepository;
    private final Producer producer;

    @Autowired
    public CaseBundleService(ElasticSearchRepository esRepository, Configuration configuration, ObjectMapper objectMapper, ServiceRequestRepository serviceRequestRepository,
                             CaseBundleRepository caseBundleRepository,Producer producer) {
        this.esRepository = esRepository;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
        this.serviceRequestRepository = serviceRequestRepository;
        this.caseBundleRepository = caseBundleRepository;
        this.producer = producer;
    }

    public CaseNumberResponse getCaseNumber(RequestInfo requestInfo, String caseId, String tenantId) {
        CaseNumberResponse caseNumberResponse = new CaseNumberResponse();
        String caseNumber;
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setTenantId(tenantId);
        CaseCriteria caseCriteria = new CaseCriteria();
        caseCriteria.setCaseId(caseId);
        caseCriteria.setDefaultFields(false);
        List<CaseCriteria> caseList = new ArrayList<>();
        caseList.add(caseCriteria);
        caseSearchRequest.setCriteria(caseList);
        caseSearchRequest.setRequestInfo(requestInfo);

        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getCaseHost()).append(configuration.getCaseSearchUrl());

        Object response;
        try {
            response = serviceRequestRepository.fetchResult(uri, caseSearchRequest);
        } catch (Exception e) {
            log.error("Error while fetching case data from service request repository", e);
            throw new CustomException("FETCH_RESULT_ERROR", "Error while fetching case data from service request repository");
        }

        caseNumberResponse.setCaseResponse(response);

        try {
            Map<String, Object> responseMap = (Map<String, Object>) response;
            List<Map<String, Object>> criteriaList = (List<Map<String, Object>>) responseMap.get("criteria");

            if (criteriaList == null || criteriaList.isEmpty()) {
                throw new CustomException("CRITERIA_NOT_FOUND", "Criteria not found in response");
            }

            Map<String, Object> criteria = criteriaList.get(0);
            List<Map<String, Object>> responseList = (List<Map<String, Object>>) criteria.get("responseList");

            if (responseList == null || responseList.isEmpty()) {
                throw new CustomException("NO_RESPONSE_LIST", "No response list found in the criteria");
            }

            Map<String, Object> caseData = responseList.get(0);
            String courtCaseNumber = (String) caseData.get("courtCaseNumber");
            String cmpNumber = (String) caseData.get("cmpNumber");
            String filingNumber = (String) caseData.get("filingNumber");

            if (courtCaseNumber != null) {
                caseNumber = courtCaseNumber;
            } else if (cmpNumber != null) {
                caseNumber = cmpNumber;
            } else if (filingNumber != null) {
                caseNumber = filingNumber;
            } else {
                throw new CustomException("CASE_NUMBER_NOT_FOUND", "No valid case number found in the response");
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing case response", e);
            throw new CustomException("PROCESSING_ERROR", "Error processing case response");
        }

        caseNumberResponse.setCaseNumber(caseNumber);

        return caseNumberResponse;
    }

    public String getCaseBundle(CaseBundleRequest caseBundleRequest) {
        CaseBundleTracker caseBundleTracker = new CaseBundleTracker();
        caseBundleTracker.setStartTime(System.currentTimeMillis());
        caseBundleTracker.setId(UUID.randomUUID().toString());
        AuditDetails auditDetails = AuditDetails.builder().createdBy(caseBundleRequest.getRequestInfo().getUserInfo().getUuid())
                .createdTime(System.currentTimeMillis())
                .lastModifiedBy(caseBundleRequest.getRequestInfo().getUserInfo().getUuid())
                .lastModifiedTime(System.currentTimeMillis())
                .build();
        caseBundleTracker.setAuditDetails(auditDetails);

        String tenantId = caseBundleRequest.getTenantId();
        String caseId = caseBundleRequest.getCaseId();
        String fileStoreId = null;

        log.info("Retrieving documents from index: {} with id: {}", configuration.getCaseBundleIndex(), caseId);
        String uri = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + configuration.getSearchPath();
        String request = String.format(ES_IDS_QUERY, caseId);
        String response;

        try {
            response = esRepository.fetchDocuments(uri, request);
        } catch (Exception e) {
            log.error("Error while fetching documents from ElasticSearch", e);
            throw new CustomException("ES_FETCH_ERROR", "Error while fetching documents from ElasticSearch");
        }

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode hitsNode = rootNode.path("hits").path("hits");

            if (hitsNode.isArray() && hitsNode.isEmpty()) {
                throw new CustomException("CASE_NOT_FOUND", "Unable to find case with given caseId");
            } else {
                JsonNode indexJson = hitsNode.get(0).path("_source");
                long contentLastModified = indexJson.path("contentLastModified").asLong();
                long pdfCreatedDate = indexJson.path("pdfCreatedDate").asLong();

                if (contentLastModified <= pdfCreatedDate) {
                    log.info("No content update. Reusing existing PDF bundle.");
                    fileStoreId = indexJson.path("fileStoreId").asText();
                    return fileStoreId;
                } else {
                    CaseNumberResponse responseCaseNumber = getCaseNumber(caseBundleRequest.getRequestInfo(), caseId, tenantId);
                    CaseBundlePdfRequest caseBundlePdfRequest = new CaseBundlePdfRequest();
                    caseBundlePdfRequest.setRequestInfo(caseBundleRequest.getRequestInfo());
                    caseBundlePdfRequest.setIndex(indexJson);
                    caseBundlePdfRequest.setCaseNumber(responseCaseNumber.getCaseNumber());
                    caseBundlePdfRequest.setCaseObject(responseCaseNumber.getCaseResponse());
                    caseBundlePdfRequest.setTenantId(tenantId);

                    StringBuilder url = new StringBuilder();
                    url.append(configuration.getCaseBundlePdfHost()).append(configuration.getCaseBundlePdfPath());

                    Object pdfResponse;
                    try {
                        pdfResponse = serviceRequestRepository.fetchResult(url, caseBundlePdfRequest);
                    } catch (Exception e) {
                        log.error("Error generating PDF", e);
                        throw new CustomException("PDF_GENERATION_ERROR", "Error generating PDF");
                    }

                    Map<String, Object> pdfResponseMap = objectMapper.convertValue(pdfResponse, Map.class);
                    Map<String, Object> indexMap = (Map<String, Object>) pdfResponseMap.get("index");
                    JsonNode updateIndexJson = objectMapper.valueToTree(indexMap);
                    fileStoreId = (String) indexMap.get("fileStoreId");
                    Integer pageCount = (Integer) pdfResponseMap.get("pageCount");
                    caseBundleTracker.setPageCount(pageCount);

                    String esUpdateUrl = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + "/_update/" + caseId;
                    String esRequest;
                    try {
                        esRequest = String.format(ES_UPDATE_QUERY, objectMapper.writeValueAsString(updateIndexJson));
                        esRepository.fetchDocuments(esUpdateUrl, esRequest);
                    } catch (IOException e) {
                        log.error("Error updating ElasticSearch index with new data", e);
                        throw new CustomException("ES_UPDATE_ERROR", "Error updating ElasticSearch index with new data");
                    }
                }
            }
        } catch (CustomException e) {
            throw e;
        } catch (IOException e) {
            log.error("Error parsing JSON response", e);
            throw new CustomException("JSON_PARSE_ERROR", "Error parsing JSON response");
        } catch (Exception e) {
            log.error("Unexpected error while processing case bundle", e);
            throw new CustomException("UNKNOWN_ERROR", "Unexpected error while processing case bundle");
        }

        caseBundleTracker.setEndTime(System.currentTimeMillis());
        caseBundleRepository.insertCaseTracker(caseBundleTracker);

        return fileStoreId;
    }

    public Boolean getBulkCaseBundle(CaseBundleBulkRequest caseBundleBulkRequest){
        BulkCaseBundleTracker bulkCaseBundleTracker = new BulkCaseBundleTracker();
        bulkCaseBundleTracker.setStartTime(System.currentTimeMillis());
        bulkCaseBundleTracker.setId(UUID.randomUUID().toString());
        RequestInfo requestInfo = caseBundleBulkRequest.getRequestInfo();
        String tenantId = caseBundleBulkRequest.getTenantId();

        log.info("Retrieving documents from index: {}", configuration.getCaseBundleIndex());
        String uri = configuration.getEsHostUrl() + configuration.getCaseBundleIndex() + configuration.getSearchPath();
        String request = String.format(ES_BULK_QUERY);
        String response;

        try {
            response = esRepository.fetchDocuments(uri, request);
        } catch (Exception e) {
            log.error("Error while fetching documents from ElasticSearch", e);
            throw new CustomException("ES_FETCH_ERROR", "Error while fetching documents from ElasticSearch");
        }


        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode hitsNode = rootNode.path("hits").path("hits");

            if (hitsNode.isArray() && hitsNode.isEmpty()) {
                throw new CustomException("CASE_NOT_FOUND", "No case available to update");
            } else {
                bulkCaseBundleTracker.setCaseCount(hitsNode.size());
                for (JsonNode hit : hitsNode) {
                    JsonNode indexJson = hit.path("_source");
                    String caseId = indexJson.path("caseID").asText();
                    CaseBundleRequest caseBundleRequest = new CaseBundleRequest();
                    caseBundleRequest.setRequestInfo(requestInfo);
                    caseBundleRequest.setTenantId(tenantId);
                    caseBundleRequest.setCaseId(caseId);
                    producer.push(configuration.getBundleCreateTopic(),caseBundleRequest);
                }

            }
        } catch (CustomException e) {
            throw e;
        } catch (IOException e) {
            log.error("Error parsing JSON response", e);
            throw new CustomException("JSON_PARSE_ERROR", "Error parsing JSON response");
        } catch (Exception e) {
            log.error("Unexpected error while processing case bundle", e);
            throw new CustomException("UNKNOWN_ERROR", "Unexpected error while processing case bundle");
        }

        bulkCaseBundleTracker.setEndTime(System.currentTimeMillis());
        caseBundleRepository.insertBulkCaseTracker(bulkCaseBundleTracker);
        return true;
    }
}

