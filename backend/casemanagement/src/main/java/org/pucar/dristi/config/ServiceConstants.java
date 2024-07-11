package org.pucar.dristi.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    //ElasticSearch
    public static final String ES_TERM_QUERY = "{\"size\":10000,\"query\":{\"term\":{\"%s\":\"%s\"}},\"sort\":[{\"%s\":{\"order\":\"%s\"}}]}";

    public static final String ROOT_PATH = "$.";
    public static final String CASE_BASE_PATH = "Data.caseDetails";
    public static final String HEARING_BASE_PATH = "Data.hearing";
    public static final String WITNESS_BASE_PATH = "Data.witnessDetails";
    public static final String ORDER_BASE_PATH = "Data.orderDetails";
    public static final String TASK_BASE_PATH = "Data.taskDetails";
    public static final String APPLICATION_BASE_PATH = "Data.applicationDetails";
    public static final String ARTIFACT_BASE_PATH = "Data.artifactDetails";

    public static final String FILING_NUMBER = ".filingNumber.keyword";
    public static final String ORDER_ID = ".orderId.keyword";
    public static final String CREATED_TIME = ".createdTime";
    public static final String CREATED_TIME_NESTED = "auditDetails.createdTime";
    public static final String CREATED_TIME_ABSOLUTE = "Data.auditDetails.createdTime";

    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    //JSON Path
    public static final String ES_HITS_PATH = "$.hits.hits.*._source";
}
