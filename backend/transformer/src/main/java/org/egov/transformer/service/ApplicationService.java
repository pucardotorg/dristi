package org.egov.transformer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.egov.transformer.config.ServiceConstants;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Application;
import org.egov.transformer.models.ApplicationData;
import org.egov.transformer.models.ApplicationRequest;
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
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);


    private final ElasticSearchService elasticSearchService;
    private final TransformerProperties properties;
    private final TransformerProducer producer;
    private final ObjectMapper objectMapper;


    @Autowired
    public ApplicationService(ElasticSearchService elasticSearchService, TransformerProperties properties, TransformerProducer producer, ObjectMapper objectMapper) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
        this.producer = producer;
        this.objectMapper = objectMapper;


    }

    private ApplicationData fetchApplication(String fieldValue) throws IOException {
        LinkedHashMap<String, Object> sourceMap = elasticSearchService.getDocumentByField(ServiceConstants.APPLICATION_INDEX, ServiceConstants.APPLICATION_NUMBER, fieldValue);
        if (null == sourceMap || null == sourceMap.get("Data")) {
            log.error("No application data found for {}", fieldValue);
            throw new CustomException("APPLICATION_SEARCH_EMPTY", ServiceConstants.APPLICATION_SEARCH_EMPTY);
        }

        return objectMapper.convertValue(sourceMap.get("Data"), ApplicationData.class);

    }

    public void updateApplication(Order order, String applicationNumber) {

        try {

            ApplicationData applicationData = fetchApplication(applicationNumber);
            Application application = applicationData.getApplicationDetails();

            application.setOrderDetails(order);
            application.setAuditDetails(applicationData.getAuditDetails());

            ApplicationRequest applicationRequest = new ApplicationRequest();
            applicationRequest.setApplication(application);

            producer.push(properties.getUpdateApplicationOrderTopic(), applicationRequest);

        } catch (Exception e) {
            log.error("error executing application search query", e);
            throw new CustomException("ERROR_APPLICATION_SEARCH", ServiceConstants.ERROR_APPLICATION_SEARCH);
        }
    }


}
