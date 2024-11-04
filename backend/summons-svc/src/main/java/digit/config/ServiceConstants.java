package digit.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

    public static final String IDGEN_ERROR = "IDGEN ERROR";
    public static final String NO_IDS_FOUND_ERROR = "No ids returned from idgen Service";

    public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

    public static final String ERROR_WHILE_FETCHING_FROM_CASE = "ERROR_WHILE_FETCHING_FROM_CASE";

    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    public static final String PAYMENT_CALCULATOR_ERROR = "PAYMENT_CALCULATOR_ERROR";

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

    public static final String BAIL = "BAIL";
    public static final String SUMMON = "SUMMONS";
    public static final String WARRANT = "WARRANT";
    public static final String NOTICE = "NOTICE";
    public static final String ACCUSED = "ACCUSED";
    public static final String WITNESS = "WITNESS";
    public static final String BAILABLE = "BAILABLE";
    public static final String NON_BAILABLE = "NON_BAILABLE";

    public static final String PAYMENTMASTERCODE = "PaymentMasterCode";
    public static final String PAYMENTTYPE = "paymentType";
    public static final String FILTER_PAYMENT_TYPE_DELIVERY_CHANNEL = "$[?(@.deliveryChannel == '%s')]";

    public static final String PENDING_PAYMENT = "PENDING_PAYMENT";
    public static final String ISSUESUMMON = "ISSUE_SUMMON";

    public static final String GENERATE_TASK_DOCUMENT = "GENERATE_TASK_DOCUMENT";
    public static final String SEND_TASK_DOCUMENT = "SEND_TASK_DOCUMENT";
    public static final String SIGNED_TASK_DOCUMENT = "SIGNED_TASK_DOCUMENT";

    public static final String COORDINATE_NOT_FOUND = "COORDINATE_NOT_FOUND";

    public static final String LOCATION_NOT_FOUND = "LOCATION_NOT_FOUND";
    public static final String FILE_CATEGORY = "FILE_CATEGORY";

}
