package org.pucar.dristi.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

	private ServiceConstants() {
	}

	public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
	public static final String SEARCHER_SERVICE_EXCEPTION = "Error while fetching result from URL: {} with request: {}";
	public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";


	//Consumer
	public static final String ES_INDEX_HEADER_FORMAT = "{\"index\":{\"_index\":\"%s\",\"_id\":\"%s\"}}\n";
	public static final String ES_INDEX_DOCUMENT_FORMAT = "{\"Data\": {\"id\":\"%s\",\"name\":\"%s\",\"entityType\":\"%s\",\"referenceId\":\"%s\",\"status\":\"%s\",\"assignedTo\":%s,\"assignedRole\":%s,\"cnrNumber\":\"%s\",\"filingNumber\":\"%s\",\"isCompleted\":%b,\"stateSla\":%d,\"businessServiceSla\":%d,\"additionalDetails\":%s}}\n";
	public static final String TENANTID_MDC_STRING = "TENANTID";


	//JSON path
	public static final String PROCESS_INSTANCE_PATH = "$.ProcessInstances.*";
	public static final String REQUEST_INFO_PATH = "$.RequestInfo";
	public static final String ID_PATH = "$.id";
	public static final String BUSINESS_SERVICE_PATH = "$.businessService";
	public static final String BUSINESS_ID_PATH = "$.businessId";
	public static final String STATE_PATH = "$.state.state";
	public static final String TENANT_ID_PATH = "$.tenantId";
	public static final String ACTION_PATH = "$.action";
	public static final String ASSIGNES_PATH = "$.assignes.*";
	public static final String ASSIGNED_ROLE_PATH = "$.state.actions.*.roles.*";
	public static final String STATE_SLA_PATH = "$.stateSla";
	public static final String BUSINESS_SERVICE_SLA_PATH = "$.businesssServiceSla";

	public static final String HEARING_PATH = "$.HearingList.*";
	public static final String HEARING_TYPE_PATH = "$.hearingType";
	public static final String CASE_PATH = "$.criteria.*.responseList[0]";
	public static final String ARTIFACT_PATH = "$.artifacts.*";
	public static final String TASK_PATH = "$.list.*";
	public static final String ORDER_PATH = "$.list.*";
	public static final String APPLICATION_PATH = "$.applicationList.*";
	public static final String CNR_NUMBERS_PATH = "$.cnrNumbers";
	public static final String CNR_NUMBER_PATH = "$.cnrNumber";
	public static final String FILING_NUMBER_PATH = "$.filingNumber";
	public static final String ERRORS_PATH = "$.errors";
	public static final String ORDER_TYPE_PATH = "$.orderType";
	public static final String ORDER_FINDINGS_PATH = "$.additionalDetails.formdata.findings.code";



	public static final String RES_MSG_ID = "uief87324";
	public static final String SUCCESSFUL = "successful";
	public static final String FAILED = "failed";

	public static final String ILLEGAL_ARGUMENT_EXCEPTION_CODE = "IllegalArgumentException";

	public static final String PARSING_ERROR = "PARSING ERROR";

	public static final String PENDING_TASK_EXCEPTION = "Pending Task Exception";
	public static final String REQUEST_INFO = "RequestInfo";
	public static final String CUSTOM_EXCEPTION = "Custom Exception";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CNR_NUMBER = "cnrNumber";
	public static final String FILING_NUMBER = "filingNumber";


}
