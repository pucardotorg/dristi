package org.pucar.dristi.config;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {
	// Private constructor to prevent instantiation
	private ServiceConstants() {
	}
	public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
	public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

	public static final String IDGEN_ERROR = "IDGEN ERROR";
	public static final String NO_IDS_FOUND_ERROR = "No ids returned from idgen Service";

	public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

	public static final String RES_MSG_ID = "200";
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
	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String CITIZEN_LOWER = "Citizen";
	public static final String USER = "user";

	public static final String PARSING_ERROR = "PARSING ERROR";
	public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";
	public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
	public static final String THE_BUSINESS_SERVICE = "The businessService ";
	public static final String NOT_FOUND = " is not found";
	public static final String TENANTID = "?tenantId=";
	public static final String BUSINESS_SERVICES = "&businessServices=";
	public static final String APPLICATION_ACTIVE_STATUS = "ACTIVE";
	public static final String INDIVIDUAL_SERVICE_EXCEPTION = "INDIVIDUAL_SERVICE_EXCEPTION";
	public static final String VALIDATION_EXCEPTION = "VALIDATION_ERROR";
	public static final String ENRICHMENT_EXCEPTION = "ENRICHMENT_EXCEPTION";
	public static final String WORKFLOW_SERVICE_EXCEPTION = "WORKFLOW_SERVICE_EXCEPTION";
	public static final String ADVOCATE_CREATE_EXCEPTION = "ADVOCATE_CREATE_EXCEPTION";
	public static final String ADVOCATE_SEARCH_EXCEPTION = "ADVOCATE_SEARCH_EXCEPTION";
	public static final String ADVOCATE_UPDATE_EXCEPTION = "ADVOCATE_UPDATE_EXCEPTION";
	public static final String ADVOCATE_SEARCH_QUERY_EXCEPTION = "ADVOCATE_SEARCH_QUERY_EXCEPTION";
	public static final String ADVOCATE_CLERK_CREATE_EXCEPTION = "ADVOCATE_CLERK_CREATE_EXCEPTION";
	public static final String ADVOCATE_CLERK_SEARCH_EXCEPTION = "ADVOCATE_CLERK_SEARCH_EXCEPTION";
	public static final String ADVOCATE_CLERK_UPDATE_EXCEPTION = "ADVOCATE_CLERK_UPDATE_EXCEPTION";
	public static final String ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION = "ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION";
	public static final String DOCUMENT_SEARCH_QUERY_EXCEPTION = "DOCUMENT_SEARCH_QUERY_EXCEPTION";
	public static final String RESPONSE_INFO_FACTORY_EXCEPTION = "RESPONSE_INFO_FACTORY_EXCEPTION";
	public static final String INDIVIDUAL_NOT_FOUND = "INDIVIDUAL_NOT_FOUND";
	public static final String QUERY_EXECUTION_FAILED = "QUERY_EXECUTION_FAILED";
	public static final String ROW_MAPPER_EXCEPTION = "ROW_MAPPER_EXCEPTION";
	public static final String INDIVIDUAL_UTILITY_EXCEPTION = "INDIVIDUAL_UTILITY_EXCEPTION";
	public static final String TEST_EXCEPTION = "TEST_EXCEPTION";
	public static final String FINAL_QUERY = "Final query :: {}";
	public static final String FINAL_QUERY_DOCUMENT = "Final query Document :: {}";
	public static final String FETCH_ADVOCATE_CLERK_EXCEPTION = "Error while fetching advocate clerk application list :: {}";
	public static final String FETCH_ADVOCATE_EXCEPTION = "Error while fetching advocate application list :: {}";
	public static final String ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION = "Error while building advocate clerk search query :: {}";
	public static final String FETCH_SEARCH_RESULT_EXCEPTION = "Error while fetching to search results :: {}";
	public static final String ADVOCATE_SEARCH_QUERY_BUILD_EXCEPTION = "Error while building advocate search query :: {}";
	public static final String ADVOCATE_LIST_QUERY = "Final advocate list query :: {}";
	public static final String DOCUMENT_LIST_QUERY = "Final document query :: {}";
	public static final String AND = " AND ";

	public static final String ACTIVE = "ACTIVE";

	public static final String ADVOCATE_REGISTERED = "ADVOCATE_REGISTERED";

	public static final String NOTIFICATION_ENG_LOCALE_CODE = "en_IN";
	public static final String NOTIFICATION_MODULE_CODE = "notification";
	public static final String NOTIFICATION_LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";
	public static final String NOTIFICATION_LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";
}
