package org.pucar.dristi.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

    public static final String IDGEN_ERROR = "IDGEN ERROR";
    public static final String NO_IDS_FOUND_ERROR = "No ids returned from idgen Service";

    public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    public static final String URL = "url";
    public static final String URL_SHORTENING_ERROR_CODE = "URL_SHORTENING_ERROR";
    public static final String URL_SHORTENING_ERROR_MESSAGE = "Unable to shorten url: ";

    public static final String DOB_FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String DOB_FORMAT_D_M_Y = "dd/MM/yyyy";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_CODE = "IllegalArgumentException";
    public static final String OBJECTMAPPER_UNABLE_TO_CONVERT = "ObjectMapper not able to convertValue in userCall";
    public static final String DOB_FORMAT_D_M_Y_H_M_S = "dd-MM-yyyy HH:mm:ss";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String DOB = "dob";
    public static final String PWD_EXPIRY_DATE = "pwdExpiryDate";
    public static final String INVALID_DATE_FORMAT_CODE = "INVALID_DATE_FORMAT";
    public static final String INVALID_DATE_FORMAT_MESSAGE = "Failed to parse date format in user";
    public static final String CITIZEN_UPPER = "CITIZEN";
    public static final String CITIZEN_LOWER = "Citizen";
    public static final String USER = "user";

    public static final String PARSING_ERROR = "PARSING ERROR";
    public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";
    public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
    public static final String THE_BUSINESS_SERVICE = "The businessService ";
    public static final String NOT_FOUND = " is not found";
    public static final String TENANTID = "?tenantId=";
    public static final String BUSINESS_SERVICES = "&businessServices=";
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/secure-temp/";

    public static final String FILE_STORE_MAPPER_KEY="referenceid_filestore_mapper";
    public static final String JSON_PARSING_ERR = "JSON_PARSING_ERR";

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

    public static final String TIME_ZONE = "Asia/Kolkata";

}
