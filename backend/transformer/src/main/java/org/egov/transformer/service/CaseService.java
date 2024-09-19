package org.egov.transformer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.egov.transformer.config.ServiceConstants;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.CaseData;
import org.egov.transformer.models.CaseRequest;
import org.egov.transformer.models.CourtCase;
import org.egov.transformer.models.Order;
import org.egov.transformer.producer.TransformerProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class CaseService {

    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);


    private final ElasticSearchService elasticSearchService;
    private final TransformerProperties properties;
    private final TransformerProducer producer;
    private final ObjectMapper objectMapper;

    @Autowired
    public CaseService(ElasticSearchService elasticSearchService, TransformerProperties properties, TransformerProducer producer, ObjectMapper objectMapper) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public CourtCase fetchCase(String fieldValue) throws IOException {
        LinkedHashMap<String, Object> sourceMap = elasticSearchService.getDocumentByField(ServiceConstants.CASE_INDEX, ServiceConstants.FILING_NUMBER, fieldValue);
        if (null == sourceMap || null == sourceMap.get("Data")) {
            log.error("No case data found for {}", fieldValue);
            throw new CustomException("CASE_SEARCH_EMPTY", ServiceConstants.CASE_SEARCH_EMPTY);
        }

        CaseData data = objectMapper.convertValue(sourceMap.get("Data"), CaseData.class);
        return data.getCaseDetails();
    }

    public void updateCase(Order order) {

        try {

            CourtCase courtCase = fetchCase(order.getFilingNumber());
            if (order.getOrderType().equalsIgnoreCase(ServiceConstants.BAIL_ORDER_TYPE)) {
                courtCase.setBailOrderDetails(order);
            }
            if (order.getOrderType().equalsIgnoreCase(ServiceConstants.JUDGEMENT_ORDER_TYPE)) {
                courtCase.setJudgementOrderDetails(order);
            }

            courtCase.setAuditDetails();

            CaseRequest caseRequest = new CaseRequest();
            caseRequest.setCases(courtCase);
            producer.push(properties.getUpdateCaseOrderTopic(), caseRequest);
        } catch (Exception e) {
            log.error("error executing case search query", e);
            throw new CustomException("ERROR_CASE_SEARCH", ServiceConstants.ERROR_CASE_SEARCH);
        }
    }


}
