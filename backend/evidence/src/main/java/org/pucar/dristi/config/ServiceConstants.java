package org.pucar.dristi.config;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {
	private ServiceConstants() {
	}
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
	public static final String COMMENT_ADD_ERR = "COMMENT_ADD_ERR";
	public static final String COMPLAINANT = "COMPLAINANT";
	public static final String ACCUSED = "ACCUSED";
	public static final String COURT = "COURT";

	public static final String PARSING_ERROR = "PARSING ERROR";
	public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";
	public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
	public static final String THE_BUSINESS_SERVICE = "The businessService ";
	public static final String NOT_FOUND = " is not found";
	public static final String TENANTID = "?tenantId=";
	public static final String BUSINESS_SERVICES = "&businessServices=";
	public static final String EVIDENCE_CREATE_EXCEPTION = "ADVOCATE_CREATE_EXCEPTION";
	public static final String WORKFLOW_SERVICE_EXCEPTION = "WORKFLOW_SERVICE_EXCEPTION";
	public static final String ENRICHMENT_EXCEPTION = "ENRICHMENT_EXCEPTION";
	public static final String EVIDENCE_SEARCH_QUERY_EXCEPTION = "EVIDENCE_SEARCH_QUERY_EXCEPTION";
	public static final String DOCUMENT_SEARCH_QUERY_EXCEPTION = "DOCUMENT_SEARCH_QUERY_EXCEPTION";
	public static final String COMMENT_SEARCH_QUERY_EXCEPTION = "COMMENT_SEARCH_QUERY_EXCEPTION";
	public static final String MDMS_DATA_NOT_FOUND = "MDMS_DATA_NOT_FOUND";
	public static final String PUBLISHED_STATE = "PUBLISHED";
	public static final String ABATED_STATE = "ABATED";
	public static final String ARTIFACT_ID_NAME = "artifact.artifact_number";
	public static final String AFFIDAVIT = "AFFIDAVIT";
	public static final String DOCUMENTARY = "DOCUMENTARY";
	public static final String DEPOSITION = "DEPOSITION";
	public static final String CASE_EXCEPTION = "CASE_EXCEPTION";
	public static final String ORDER_EXCEPTION = "ORDER_EXCEPTION";
	public static final String APPLICATION_EXCEPTION = "APPLICATION_EXCEPTION";
	public static final String HEARING_EXCEPTION = "HEARING_EXCEPTION";
	public static final String EVIDENCE_UPDATE_EXCEPTION= "EVIDENCE_UPDATE_EXCEPTION";
	public static final String EVIDENCE_SEARCH_EXCEPTION= "EVIDENCE_SEARCH_EXCEPTION";
	public static final String ERROR_WHILE_FETCHING_FROM_CASE = "ERROR_WHILE_FETCHING_FROM_CASE";
	public static final String ERROR_WHILE_FETCHING_FROM_APPLICATION_SERVICE = "ERROR_WHILE_FETCHING_FROM_APPLICATION_SERVICE";
	public static final String ERROR_WHILE_FETCHING_FROM_ORDER = "ERROR_WHILE_FETCHING_FROM_ORDER_SERVICE";
	public static final String ERROR_WHILE_FETCHING_FROM_HEARING = "ERROR_WHILE_FETCHING_FROM_HEARING";

}
