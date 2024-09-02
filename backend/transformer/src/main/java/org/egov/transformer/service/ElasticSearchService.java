package org.egov.transformer.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.repository.ElasticSearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    private final ElasticSearchRepository elasticSearchRepository;
    private final TransformerProperties properties;
    private final ObjectMapper objectMapper;

    @Autowired
    public ElasticSearchService(ElasticSearchRepository elasticSearchRepository, TransformerProperties properties, ObjectMapper objectMapper) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public LinkedHashMap<String, Object> getDocumentByField(String index, String fieldName, String value) throws IOException {

        Query query = QueryBuilders.match().field(fieldName).query(value).build()._toQuery();
        LinkedHashMap<String, Object> sourceMap = elasticSearchRepository.getElasticResponse(index, query);
        logger.info(objectMapper.writeValueAsString(sourceMap));
        return sourceMap;
    }
}
