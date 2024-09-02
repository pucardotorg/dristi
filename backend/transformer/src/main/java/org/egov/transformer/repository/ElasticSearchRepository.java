package org.egov.transformer.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import org.egov.transformer.config.DashboardReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Repository
@Slf4j
public class ElasticSearchRepository {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public ElasticSearchRepository(ElasticsearchClient elasticsearchClient, DashboardReportProperties properties){
        this.elasticsearchClient = elasticsearchClient;
    }

    public LinkedHashMap<String, Object>  getElasticResponse(String index, Query query) throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(index)
                .query(query)
        );
        SearchResponse<Object> response = elasticsearchClient.search(searchRequest, (Type) Object.class);

        return ((LinkedHashMap<String, Object>) response.hits().hits().get(0).source());
    }

}
