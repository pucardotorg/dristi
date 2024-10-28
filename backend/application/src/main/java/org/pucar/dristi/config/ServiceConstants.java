package org.pucar.dristi.config;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class ServiceConstants {

    private ServiceConstants() {
    }

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";
    public static final String COMPLETED = "COMPLETED";
    public static final String REJECTED = "REJECTED";

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
    public static final String CREATE_APPLICATION_ERR = "CREATE_APPLICATION_ERR";
    public static final String UPDATE_APPLICATION_ERR = "UPDATE_APPLICATION_ERR";
    public static final String APPLICATION_SEARCH_ERR = "APPLICATION_SEARCH_ERR";
    public static final String APPLICATION_SEARCH_QUERY_EXCEPTION = "APPLICATION_SEARCH_QUERY_EXCEPTION";
    public static final String DOCUMENT_SEARCH_QUERY_EXCEPTION = "DOCUMENT_SEARCH_QUERY_EXCEPTION";
    public static final String STATUTE_SEARCH_QUERY_EXCEPTION = "STATUTE_SEARCH_QUERY_EXCEPTION";
    public static final String ROW_MAPPER_EXCEPTION = "ROW_MAPPER_EXCEPTION";
    public static final String DOCUMENT_ROW_MAPPER_EXCEPTION = "DOCUMENT_ROW_MAPPER_EXCEPTION";
    public static final String STATUTE_ROW_MAPPER_EXCEPTION = "STATUTE_ROW_MAPPER_EXCEPTION";
    public static final String APPLICATION_EXIST_EXCEPTION = "APPLICATION_EXIST_EXCEPTION";
    public static final String ERROR_WHILE_FETCHING_FROM_CASE = "ERROR_WHILE_FETCHING_FROM_CASE";
    public static final String ENRICHMENT_EXCEPTION = "ENRICHMENT_EXCEPTION";
    public static final String ERROR_WHILE_CREATING_DEMAND_FOR_CASE = "ERROR_WHILE_CREATING_DEMAND_FOR_APPLICATION";
    public static final Long TAX_PERIOD_FROM = 1680287400000l;
    public static final Long TAX_PERIOD_TO = 1711909799000l;
    public static final BigDecimal TAX_AMOUNT = BigDecimal.valueOf(2000.00);
    public static final String CREATE_DEMAND_STATUS = "PENDINGPAYMENT";

    public static final String TAX_HEADMASTER_CODE = "APPLICATION_ADVANCE_CARRYFORWARD";
    public static final String VALIDATION_ERR = "VALIDATION_ERR";
    public static final String WORKFLOW_SERVICE_EXCEPTION = "WORKFLOW_SERVICE_EXCEPTION";
    public static final String JSON_PARSE_ERROR = "JSON_PARSE_ERROR";
    public static final String ERROR_WHILE_FETCHING_FROM_ORDER = "ERROR_WHILE_FETCHING_FROM_ORDER_SERVICE";
    public static final String ORDER_EXCEPTION = "ORDER_EXCEPTION";
    public static final String COMMENT_ADD_ERR = "COMMENT_ADD_ERR";
    public static final String INVALID_FILESTORE_ID = "INVALID_FILESTORE_ID";
    public static final String INVALID_DOCUMENT_DETAILS = "Invalid document details";
    public static final String DELAY_CONDONATION = "DELAY_CONDONATION";

}
