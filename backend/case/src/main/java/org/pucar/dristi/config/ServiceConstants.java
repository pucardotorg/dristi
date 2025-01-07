package org.pucar.dristi.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServiceConstants {
    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";
    public static final String IDGEN_ERROR = "IDGEN ERROR";
    public static final String CMP = "CMP";
    public static final String ST = "ST";
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
    public static final String RESPONDENT_PRIMARY = "respondent.primary";
    public static final String COMPLAINANT_PRIMARY = "complainant.primary";
    public static final String CASE_CREATE_EXCEPTION = "CASE_CREATE_EXCEPTION";
    public static final String PARSING_ERROR = "PARSING ERROR";
    public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";
    public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
    public static final String THE_BUSINESS_SERVICE = "The businessService ";
    public static final String NOT_FOUND = " is not found";
    public static final String TENANTID = "?tenantId=";
    public static final String BUSINESS_SERVICES = "&businessServices=";
    public static final String INDIVIDUAL_NOT_FOUND = "INDIVIDUAL_NOT_FOUND";
    public static final String ERROR_WHILE_FETCHING_FROM_ADVOCATE = "ERROR_WHILE_FETCHING_FROM_ADVOCATE";
    public static final String ERROR_WHILE_CREATING_DEMAND_FOR_CASE = "ERROR_WHILE_CREATING_DEMAND_FOR_CASE";
    public static final String CREATE_CASE_ERR = "CREATE_CASE_ERR";
    public static final String SEARCH_CASE_ERR = "CASE_NOT_FOUND";
    public static final String UPDATE_CASE_ERR = "UPDATE_CASE_ERR";
    public static final String EDIT_CASE_ERR = "EDIT_CASE_ERR";
    public static final String ADD_WITNESS_TO_CASE_ERR = "ADD_WITNESS_TO_CASE_ERR";
    public static final String CREATE_WITNESS_ERR = "CREATE_WITNESS_ERR";
    public static final String SEARCH_WITNESS_ERR = "WITNESS_NOT_FOUND";
    public static final String UPDATE_WITNESS_ERR = "UPDATE_WITNESS_ERR";
    public static final String VALIDATION_ERR = "VALIDATION_EXCEPTION";
    public static final String JOIN_CASE_ERR = "JOIN_CASE_ERR";
    public static final String ENRICHMENT_EXCEPTION = "ENRICHMENT_EXCEPTION";
    public static final String WORKFLOW_SERVICE_EXCEPTION = "WORKFLOW_SERVICE_EXCEPTION";
    public static final String INDIVIDUAL_SERVICE_EXCEPTION = "INDIVIDUAL_SERVICE_EXCEPTION";
    public static final String CASE_PDF_SERVICE_EXCEPTION = "CASE_PDF_SERVICE_EXCEPTION";
    public static final String ROW_MAPPER_EXCEPTION = "ROW_MAPPER_EXCEPTION";
    public static final String WITNESS_SEARCH_QUERY_EXCEPTION = "WITNESS_SEARCH_QUERY_EXCEPTION";
    public static final String CASE_SEARCH_QUERY_EXCEPTION = "CASE_SEARCH_QUERY_EXCEPTION";
    public static final String LINKED_CASE_SEARCH_QUERY_EXCEPTION = "LINKED_CASE_SEARCH_QUERY_EXCEPTION";
    public static final String LITIGANT_SEARCH_QUERY_EXCEPTION = "LITIGANT_SEARCH_QUERY_EXCEPTION";
    public static final String STATUTE_SECTION_SEARCH_QUERY_EXCEPTION = "STATUTE_SECTION_SEARCH_QUERY_EXCEPTION";
    public static final String REPRESENTATIVES_SEARCH_QUERY_EXCEPTION = "REPRESENTATIVES_SEARCH_QUERY_EXCEPTION";
    public static final String REPRESENTING_SEARCH_QUERY_EXCEPTION = "REPRESENTING_SEARCH_QUERY_EXCEPTION";
    public static final String DOCUMENT_SEARCH_QUERY_EXCEPTION = "DOCUMENT_SEARCH_QUERY_EXCEPTION";
    public static final String CASE_EXIST_ERR = "CASE_EXIST_EXCEPTION";
    public static final String MDMS_DATA_NOT_FOUND = "MDMS_DATA_NOT_FOUND";
    public static final String INVALID_ADVOCATE_ID = "INVALID_ADVOCATE_ID";
    public static final String INVALID_CASE_ID = "INVALID_CASE_ID";
    public static final String INVALID_CASE = "INVALID_CASE";
    public static final String INVALID_FILESTORE_ID = "INVALID_FILESTORE_ID";
    public static final String REGISTERED_STATUS = "REGISTERED";
    public static final String INWORKFLOW_STATUS = "INWORKFLOW";
    public static final String INVALID_LINKEDCASE_ID = "INVALID_LINKEDCASE_ID";
    public static final String PENDING_ADMISSION_HEARING_STATUS = "PENDING_ADMISSION_HEARING";
    public static final String INDIVIDUAL_UTILITY_EXCEPTION = "INDIVIDUAL_UTILITY_EXCEPTION";
    public static final String CASE_PDF_UTILITY_EXCEPTION = "CASE_PDF_UTILITY_EXCEPTION";
    public static final String FILE_STORE_UTILITY_EXCEPTION = "FILE_STORE_UTILITY_EXCEPTION";
    public static final String SAVE_DRAFT_CASE_WORKFLOW_ACTION = "SAVE_DRAFT";
    public static final String SUBMIT_CASE_WORKFLOW_ACTION = "SUBMIT_CASE";
    public static final String SUBMIT_CASE_ADVOCATE_WORKFLOW_ACTION = "SUBMIT_CASE_ADVOCATE";
    public static final String DELETE_DRAFT_WORKFLOW_ACTION = "DELETE_DRAFT";
    public static final Long TAX_PERIOD_FROM = 1680287400000l;
    public static final Long TAX_PERIOD_TO = 1711909799000l;
    public static final BigDecimal TAX_AMOUNT = BigDecimal.valueOf(2000.00);
    public static final String TAX_HEADMASTER_CODE = "CASE_ADVANCE_CARRYFORWARD";
    public static final String CREATE_DEMAND_STATUS = "PAYMENT_PENDING";
    public static final String CASE_ADMIT_STATUS = "CASE_ADMITTED";
    public static final int ACCESSCODE_LENGTH = 6;
    public static final String STATE = "KL";
    public static final String DISTRICT = "JL";
    public static final String ESTABLISHMENT_CODE = "01";
    //error logs constants
    public static final String JOIN_CASE_INVALID_REQUEST = "Invalid request for joining a case";
    public static final String INVALID_COMPLAINANT_DETAILS = "Invalid complainant details";
    public static final String INVALID_DOCUMENT_DETAILS = "Invalid document details";
    public static final String INVALID_ADVOCATE_DETAILS = "Invalid advocate details";
    public static final String ENCRYPTION_SERVICE_ERROR = "Error occurred while calling Encryption Service";
    public static final String PURPOSE = "purpose";
    // Roles required for decryption
    public static final String JUDGE_ROLE = "JUDGE_ROLE";
    public static final String BENCH_CLERK = "BENCH_CLERK";
    public static final String FSO_ROLE = "FSO_ROLE";
    public static final String BENCH_CLERK_ROLE = "BENCHCLERK_ROLE";
    public static final String NYAY_MITRA_ROLE = "NYAY_MITRA_ROLE";
    public static final String INTERNAL_MICROSERVICE_ROLE = "INTERNAL_MICROSERVICE_ROLE";
    public static final String ADVOCATE_ROLE = "ADVOCATE_ROLE";
    public static final String EMPLOYEE = "EMPLOYEE";
    //	Models for encryption decryption in MDMS
    public static final String COURT_CASE_ENCRYPT = "CourtCase";
    public static final String CASE_DECRYPT_SELF = "CaseDecryptSelf";
    public static final String CASE_DECRYPT_OTHER = "CaseDecryptOther";
    public static final String PAYMENT_PENDING = "PENDING_PAYMENT";

    public static final String PENDING_E_SIGN = "PENDING_E-SIGN";


    public static final String MAKE_PAYMENT = "MAKE_PAYMENT";

    public static final String UNDER_SCRUTINY = "UNDER_SCRUTINY";

    public static final String CASE_PAYMENT_COMPLETED = "CASE_PAYMENT_COMPLETED";

    public static final String PENDING_REGISTRATION = "PENDING_REGISTRATION";

    public static final String FSO_VALIDATED = "FSO_VALIDATED";
    public static final String CASE_SUBMITTED = "CASE_SUBMITTED";
    public static final String ESIGN_PENDING = "ESIGN_PENDING";

    public static final String CASE_REASSIGNED = "CASE_REASSIGNED";

    public static final String FSO_SEND_BACK = "FSO_SEND_BACK";

    public static final String JUDGE_SEND_BACK = "JUDGE_SEND_BACK";

    public static final String PENDING_ADMISSION_HEARING = "PENDING_ADMISSION_HEARING";


    public static final String CASE_REGISTERED = "CASE_REGISTERED";


    public static final String ADVOCATE_ESIGN_PENDING = "ADVOCATE_ESIGN_PENDING";

    public static final String PENDING_SIGN = "PENDING_SIGN";
    public static final String CASE_SUBMISSION = "CASE_SUBMISSION";
    public static final String CASE_FILED = "CASE_FILED";
    public static final String SCRUTINY_COMPLETE_CASE_REGISTERED = "SCRUTINY_COMPLETE_CASE_REGISTERED";
    public static final String EFILING_ERRORS = "EFILING_ERRORS";
    public static final String ERRORS_IDENTIFIED_CASE_FILE = "ERRORS_IDENTIFIED_CASE_FILE";
    public static final String ADMISSION_HEARING_SCHEDULED = "ADMISSION_HEARING_SCHEDULED";
    public static final String CASE_ADMITTED = "CASE_ADMITTED";
    public static final String PENDING_RESPONSE = "PENDING_RESPONSE";
    public static final String HEARING_REJECTED = "HEARING_REJECTED";
    public static final String HIGH_COURT_LOCALIZATION_CODE = "HIGH_COURT_KERALA";
    public static final String NOTIFICATION_ENG_LOCALE_CODE = "en_IN";
    public static final String NOTIFICATION_MODULE_CODE = "notification";
    public static final String NOTIFICATION_LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";
    public static final String NOTIFICATION_LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";
    public static final String CASE_SUMMARY_SEARCH_QUERY_EXCEPTION = "CASE_SUMMARY_SEARCH_QUERY_EXCEPTION";
    public static final String JUDGE_ID = "JUDGE_ID";

    public static final String FLOW_JAC = "flow_jac";

    public static final String ADVOCATE_CASE_JOIN = "ADVOCATE_CASE_JOIN";

    public static final String ADVOCATE_NAME = "advocateName";

    private ServiceConstants() {
    }
}
